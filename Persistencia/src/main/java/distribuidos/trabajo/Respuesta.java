package distribuidos.trabajo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "respuesta")
public class Respuesta implements Serializable {
    private static final long serialVersionUID = 1L;
    private String textRespuesta;
    private Naturalezas listaNaturalezas;

    public String getContenido() {
        return textRespuesta;
    }

    public void setContenido(String contenido) {
        this.textRespuesta = contenido;
    }

    @XmlElement(name = "respuestas")
    public Naturalezas getNaturalezas() {
        return listaNaturalezas;
    }

    public void setNaturalezas(Naturalezas naturalezas) {
        this.listaNaturalezas = naturalezas;
    }


    public String toString() {
        String reply = "Texto: " + textRespuesta + "\r\n";
        List<Naturaleza> naturalezaList = listaNaturalezas.getlNaturalezas();
        for (Naturaleza pn : naturalezaList) {
            reply += "Naturaleza y sus puntos: " + pn.toString() + "\r\n";
        }

        return reply;
    }
}
