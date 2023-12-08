package distribuidos.trabajo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "naturaleza")
public class DescripcionNaturaleza implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombre;
    private String descripcion;
    private List<String> posiblesPokemon;

    @XmlElement(name = "nombre")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlElement(name = "descripcion")
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlElementWrapper(name = "opciones")
    @XmlElement(name = "pokemon")
    public List<String> getPosiblesPokemon() {
        return posiblesPokemon;
    }

    public void setPosiblesPokemon(List<String> posiblesPokemon) {
        this.posiblesPokemon = posiblesPokemon;
    }

    public String toString() {
        String respuesta =  "Nombre: " + this.nombre + " \r\n Descripcion: " + this.descripcion + " \r\n ";

        for(String poke : posiblesPokemon){
            respuesta += "Nombre Pokemon: " + poke + " \r\n ";
        }

        return respuesta;
    }
}