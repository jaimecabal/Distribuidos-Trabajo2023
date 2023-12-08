package distribuidos.trabajo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "naturaleza")
public class PuntosNaturaleza implements Serializable {
    private int puntos;

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public String getNombreNaturaleza() {
        return nombreNaturaleza;
    }

    public void setNombreNaturaleza(String nombreNaturaleza) {
        this.nombreNaturaleza = nombreNaturaleza;
    }

    private String nombreNaturaleza;
}
