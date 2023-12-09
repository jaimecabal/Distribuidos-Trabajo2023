package distribuidos.trabajo;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.util.*;

public class AtenderPeticion implements Runnable {
    private Socket s;
    private String peticion;
    private String HOMEDIR = "./web";

    public AtenderPeticion(Socket s) {
        this.s = s;
        try {
            BufferedReader is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.peticion = is.readLine();
            System.out.println(peticion);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    //Metodo obtenido de esta respuesta de StackOverflow:
    //https://stackoverflow.com/a/35278327
    public static <Pregunta> List<Pregunta> crearTest(List<Pregunta> list, int n, Random r) {
        int length = list.size();
        if (length < n) return null;
        //We don't need to shuffle the whole list
        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(list, i, r.nextInt(i + 1));
        }
        return list.subList(length - n, length);
    }

    @Override
    public void run() {
        try {
            BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            BufferedReader brr = new BufferedReader(new InputStreamReader(s.getInputStream()));
            if (this.peticion.startsWith("PRUEBAS")) {
                lanzarPruebas();
                bwr.write("OK\r\n");
            } else if (this.peticion.startsWith("PUT")) {
                //Creamos las preguntas del test concreto
                List<Pregunta> lPreguntas = crearTest(sacarPreguntas(), 10, new Random());
                List<String> respuestas = new ArrayList<String>();
                for (Pregunta p : lPreguntas) {
                    //Escribo
                    bwr.write(p.getEnunciado() + "\r\n");
                    bwr.flush();
                    List<Respuesta> rs = p.getRespuestas().getlRespuestas();
                    int n = 0;
                    for (Respuesta r : rs) {
                        bwr.write(n + ") " + r.getTxtRespuesta() + "\r\n");
                        bwr.flush();
                        n++;
                    }
                    bwr.write("< ---------------------------------------- >\r\n");
                    bwr.flush();
                    System.out.println("Esperando respuesta del cliente...");
                    //Leo la respuesta
                    String respuestaCliente = brr.readLine();
                    respuestas.add(respuestaCliente);
                }
                bwr.write(procesarResultados(respuestas, lPreguntas));
                bwr.flush();
            } else {
                File fich = buscaFichero(this.peticion);
                try {
                    if (fich.exists()) {
                        String cType = "";
                        if (fich.getName().endsWith(".css")) {
                            cType = "text/css";
                        } else {
                            cType = URLConnection.guessContentTypeFromName(fich.getName());
                        }
                        if (this.peticion.startsWith("HEAD ")) {
                            sendMIMEHeading(this.s.getOutputStream(), 200, cType, fich.length());
                        } else {
                            if (fich.isFile()) {
                                try (BufferedInputStream dis = new BufferedInputStream(new FileInputStream(fich))) {
                                    sendMIMEHeading(this.s.getOutputStream(), 200, cType, fich.length());

                                    int bytesLeidos;
                                    byte[] buff = new byte[1024 * 32];
                                    System.out.println(peticion);

                                    while ((bytesLeidos = dis.read(buff)) != -1) {
                                        s.getOutputStream().write(buff, 0, bytesLeidos);
                                    }

                                    s.getOutputStream().flush();
                                }
                            } else {
                                String error = makeHTMLErrorText(501, "No implementado");
                                sendMIMEHeading(this.s.getOutputStream(), 501, "text/css", error.length());
                                s.getOutputStream().write(error.getBytes());
                                s.getOutputStream().flush();
                            }
                        }
                    } else {
                        String error = makeHTMLErrorText(404, "Pagina no encontrada");
                        sendMIMEHeading(this.s.getOutputStream(), 404, "text/html", error.length());
                        s.getOutputStream().write(error.getBytes());
                        s.getOutputStream().flush();
                    }
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String procesarResultados(List<String> respuestas, List<Pregunta> lPreguntas) {
        //Cogemos las respuestas, las comparamos con las preguntas y sumamos los puntos de cada pregunta
        Map<String, Integer> mapa = mapaNaturalezas();
        int i = 0;
        for (Pregunta p : lPreguntas) {
            List<Respuesta> lP = p.getRespuestas().getlRespuestas();
            //Seleccionamos solo la respuesta que ha escogido el cliente
            Respuesta r = lP.get(Integer.parseInt(respuestas.get(i)));
            //Sacamos todas las naturalezas que suma la misma
            List<Naturaleza> lN = r.getNaturalezas().getlNaturalezas();
            for (Naturaleza n : lN) {
                String nombreNatur = n.getNombreNaturaleza();
                int puntos = n.getPuntos();
                int puntosEnMapa = mapa.get(nombreNatur);
                mapa.put(nombreNatur, puntosEnMapa + puntos);
            }
            i++;
        }
        String naturaleza = Collections.max(mapa.entrySet(), Map.Entry.comparingByValue()).getKey();
        List<DescripcionNaturaleza> lDN = sacarNaturalezas();
        for (DescripcionNaturaleza dn : lDN) {
            if (dn.getNombre().equals(naturaleza)) {
                return "Tu naturaleza es: " + dn.getNombre() + "!\r\n" + dn.getDescripcion() + "\r\n Una persona asi solo podria ser... "+dn.getPosiblesPokemon().get(0) + " y " + dn.getPosiblesPokemon().get(1)+"\r\n";
            }
        }

        return "Ha habido un error \r\n";
    }

    public Map<String, Integer> mapaNaturalezas() {
        Map<String, Integer> mapa = new HashMap<String, Integer>();
        List<DescripcionNaturaleza> lDescripciones = sacarNaturalezas();
        for (DescripcionNaturaleza dn : lDescripciones) {
            mapa.put(dn.getNombre(), 0);
        }
        return mapa;
    }

    public List<DescripcionNaturaleza> sacarNaturalezas() {
        List<DescripcionNaturaleza> listadoDescripcionNaturalezas;
        try {
            JAXBContext context = JAXBContext.newInstance(ListaDescripcionNaturalezas.class);
            Unmarshaller um = context.createUnmarshaller();
            FileReader file = new FileReader("Datos/naturalezas.xml");
            ListaDescripcionNaturalezas listaDescripcionNaturalezas = (ListaDescripcionNaturalezas) um.unmarshal(file);
            listadoDescripcionNaturalezas = listaDescripcionNaturalezas.getNaturalezas();

        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return listadoDescripcionNaturalezas;
    }

    public List<Pregunta> sacarPreguntas() {
        List<Pregunta> lPreguntas;
        try {
            JAXBContext context = JAXBContext.newInstance(Preguntas.class);
            Unmarshaller um = context.createUnmarshaller();
            FileReader file = new FileReader("Datos/preguntas.xml");
            Preguntas preguntas = (Preguntas) um.unmarshal(file);
            lPreguntas = preguntas.getlPreguntas();
        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return lPreguntas;
    }

    /* < ------------------------------------------------------------------------------------------------------ >*/

    public void lanzarPruebas() {
        pruebaNaturalezas();
        pruebaPreguntas();
        pruebaRespuesta();
    }

    public void pruebaNaturalezas() {
        try {
            JAXBContext context = JAXBContext.newInstance(ListaDescripcionNaturalezas.class);
            Unmarshaller um = context.createUnmarshaller();
            FileReader file = new FileReader("Datos/naturalezas.xml");
            ListaDescripcionNaturalezas listaDescripcionNaturalezas = (ListaDescripcionNaturalezas) um.unmarshal(file);
            List<DescripcionNaturaleza> listadoDescripcionNaturalezas = listaDescripcionNaturalezas.getNaturalezas();
            for (DescripcionNaturaleza n : listadoDescripcionNaturalezas) {
                System.out.println(n.toString());
            }

        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void pruebaPreguntas() {
        try {
            JAXBContext context = JAXBContext.newInstance(Preguntas.class);
            Unmarshaller um = context.createUnmarshaller();
            FileReader file = new FileReader("Datos/preguntas.xml");
            Preguntas preguntas = (Preguntas) um.unmarshal(file);
            List<Pregunta> lPreguntas = preguntas.getlPreguntas();
            for (Pregunta p : lPreguntas) {
                System.out.println(p.toString());
            }
        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void pruebaRespuesta() {
        try {
            JAXBContext context = JAXBContext.newInstance(Respuesta.class);
            Unmarshaller um = context.createUnmarshaller();
            FileReader file = new FileReader("Datos/pruebaRespuestas.xml");
            Respuesta respuesta = (Respuesta) um.unmarshal(file);

            System.out.println(respuesta.toString());

        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    /*
     * < --------------------- METODOS DADOS EN LA PRACTICA --------------------- >
     */
    private File buscaFichero(String m) {
        String fileName = "";
        if (m.startsWith("GET ")) {
            // A partir de una cadena de mensaje (m) correcta (comienza por GET)
            fileName = m.substring(4, m.indexOf(" ", 5));
            if (fileName.equals("/")) {
                fileName += "index.html";
            }
        }
        if (m.startsWith("HEAD ")) {
            // A partir de una cadena de mensaje (m) correcta (comienza por HEAD)
            fileName = m.substring(6, m.indexOf(" ", 7));
            if (fileName.equals("/")) {
                fileName += "index.html";
            }
        }
        return new File(HOMEDIR, fileName);
    }

    private void sendMIMEHeading(OutputStream os, int code, String cType, long fSize) {
        PrintStream dos = new PrintStream(os);
        dos.print("HTTP/1.1 " + code + " ");
        if (code == 200) {
            dos.print("OK\r\n");
            dos.print("Date: " + new Date() + "\r\n");
            dos.print("Server: Cutre http Server ver. -6.0\r\n");
            dos.print("Connection: close\r\n");
            dos.print("Content-length: " + fSize + "\r\n");
            dos.print("Content-type: " + cType + "\r\n");
            dos.print("\r\n");
        } else if (code == 404) {
            dos.print("File Not Found\r\n");
            dos.print("Date: " + new Date() + "\r\n");
            dos.print("Server: Cutre http Server ver. -6.0\r\n");
            dos.print("Connection: close\r\n");
            dos.print("Content-length: " + fSize + "\r\n");
            dos.print("Content-type: " + "text/html" + "\r\n");
            dos.print("\r\n");
        } else if (code == 501) {
            dos.print("Not Implemented\r\n");
            dos.print("Date: " + new Date() + "\r\n");
            dos.print("Server: Cutre http Server ver. -6.0\r\n");
            dos.print("Connection: close\r\n");
            dos.print("Content-length: " + fSize + "\r\n");
            dos.print("Content-type: " + "text/html" + "\r\n");
            dos.print("\r\n");
        }
        dos.flush();
    }

    private String makeHTMLErrorText(int code, String txt) {
        StringBuffer msg = new StringBuffer("<HTML>\r\n");
        msg.append(" <HEAD>\r\n");
        msg.append(" <TITLE>" + txt + "</TITLE>\r\n");
        msg.append(" </HEAD>\r\n");
        msg.append(" <BODY>\r\n");
        msg.append(" <H1>HTTP Error " + code + ": " + txt + "</H1>\r\n");
        msg.append(" </BODY>\r\n");
        msg.append("</HTML>\r\n");
        return msg.toString();
    }
}
