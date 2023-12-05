package distribuidos.trabajo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
@XmlRootElement(name = "naturalezas")
public class Naturalezas implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Naturaleza> naturalezas;

    @XmlElement(name = "naturaleza")
    public List<Naturaleza> getNaturalezas() {
        return naturalezas;
    }

    public void setNaturalezas(List<Naturaleza> naturalezas) {
        this.naturalezas = naturalezas;
    }
}
