package distribuidos.trabajo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "preguntas")
public class Preguntas implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Pregunta> lPreguntas;

    @XmlElement(name = "pregunta")
    public List<Pregunta> getlPreguntas() {
        return lPreguntas;
    }

    public void setlPreguntas(List<Pregunta> lPreguntas) {
        this.lPreguntas = lPreguntas;
    }
}
