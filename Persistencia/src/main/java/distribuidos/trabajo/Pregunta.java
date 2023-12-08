package distribuidos.trabajo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "pregunta")
public class Pregunta implements Serializable {
    private String enunciado;
    private List<Respuesta> respuestas;

    @XmlElement(name = "enunciado")
    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    @XmlElementWrapper(name = "respuestas")
    @XmlElement(name = "respuesta")
    public List<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }
}
