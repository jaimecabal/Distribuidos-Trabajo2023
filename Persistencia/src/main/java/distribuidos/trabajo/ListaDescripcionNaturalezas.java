package distribuidos.trabajo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
@XmlRootElement(name = "naturalezas")
public class ListaDescripcionNaturalezas implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<DescripcionNaturaleza> descripcionNaturalezas;

    @XmlElement(name = "naturaleza")
    public List<DescripcionNaturaleza> getNaturalezas() {
        return descripcionNaturalezas;
    }

    public void setNaturalezas(List<DescripcionNaturaleza> descripcionNaturalezas) {
        this.descripcionNaturalezas = descripcionNaturalezas;
    }
}
