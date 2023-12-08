package distribuidos.trabajo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "respuestas")
public class Respuestas implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Respuesta> lRespuestas;

    @XmlElement(name = "respuesta")
    public List<Respuesta> getlRespuestas() {
        return lRespuestas;
    }

    public void setlRespuestas(List<Respuesta> lRespuestas) {
        this.lRespuestas = lRespuestas;
    }
}
