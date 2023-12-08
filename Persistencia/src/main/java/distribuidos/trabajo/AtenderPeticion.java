package distribuidos.trabajo;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
            if (this.peticion.startsWith("TEST")) {
                lanzarPruebas();
            } else if (this.peticion.startsWith("GET")) {
                //Creamos las preguntas del test concreto
                List<Pregunta> lPreguntas = crearTest(sacarPreguntas(), 10, new Random());
                List<String> respuestas = new ArrayList<String>();
                for (Pregunta p : lPreguntas) {
                    //Escribo
                    bwr.write(p.getEnunciado() + "\r\n");
                    bwr.flush();
                    List<Respuesta> rs = p.getRespuestas().getlRespuestas();
                    int n = 1;
                    for (Respuesta r : rs) {
                        bwr.write(n + ") " + r.getTxtRespuesta() + "\r\n");
                        bwr.flush();
                        n++;
                    }
                    bwr.write("< ---------------------------------------- >\r\n");
                    bwr.flush();
                    //Leo la respuesta
                    respuestas.add(brr.readLine());
                }
                bwr.write(procesarResultados(respuestas,lPreguntas));
                bwr.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String procesarResultados(List<String> respuestas, List<Pregunta> lPreguntas) {
        //Cogemos las respuestas, las comparamos con las preguntas y sumamos los puntos de cada pregunta
        return "";
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
