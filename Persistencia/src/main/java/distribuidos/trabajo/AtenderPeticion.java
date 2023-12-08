package distribuidos.trabajo;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.Socket;
import java.util.List;

public class AtenderPeticion implements Runnable {
    private Socket s;

    public AtenderPeticion(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        //Esto es solo para probar que el marshalleo funcion bien con los resultados
        //pruebaNaturalezas();
        pruebaPreguntas();
    }
    public void pruebaNaturalezas(){
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
    public void pruebaPreguntas(){
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
}
