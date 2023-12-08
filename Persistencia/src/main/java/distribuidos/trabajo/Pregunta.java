package distribuidos.trabajo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "pregunta")
public class Pregunta implements Serializable {
    private static final long serialVersionUID = 1L;
    private String enunciado;
    private Respuestas respuestas;

    @XmlElement(name = "enunciado")
    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    @XmlElement(name = "respuestas")
    public Respuestas getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(Respuestas respuestas) {
        this.respuestas = respuestas;
    }

    public String toString() {
        String reply = "Enunciado: " + getEnunciado() + "\r\n";
        List<Respuesta> lRespuestas = respuestas.getlRespuestas();
        for (Respuesta r : lRespuestas) {
            reply += "Respuesta: \r\n" + r.toString() + "\r\n";
        }

        return reply;
    }
}
