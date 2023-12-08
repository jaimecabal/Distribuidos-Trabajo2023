package distribuidos.trabajo;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlRootElement(name = "naturaleza")
public class Naturaleza implements Serializable {
    private static final long serialVersionUID = 1L;
    private int puntos;
    private String nombreNaturaleza;

    @XmlAttribute(name = "puntos")
    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    @XmlValue
    public String getNombreNaturaleza() {
        return nombreNaturaleza;
    }

    public void setNombreNaturaleza(String nombreNaturaleza) {
        this.nombreNaturaleza = nombreNaturaleza;
    }

    public String toString() {
        return "Puntos: " + puntos + " Naturaleza: " + nombreNaturaleza + "\r\n";
    }
}