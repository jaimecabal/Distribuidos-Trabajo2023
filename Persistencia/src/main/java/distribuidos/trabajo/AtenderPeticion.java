package distribuidos.trabajo;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.Socket;
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
            } else if (this.peticion.startsWith("GET")) {
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
}
