package src;

import java.io.Serializable;
import java.util.List;

@XmlRootElement(name="naturalezas")
public class Naturaleza implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombre;
    private String descripcion;
    private List<String> posiblesPokemon;

    @XmlElement(name="nombre")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    @XmlElement(name="descripcion")
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    @XmlElementWrapper(name="opciones")
    @XmlElement(name="pokemon")
    public List<String> getPosiblesPokemon() {
        return posiblesPokemon;
    }

    public void setPosiblesPokemon(List<String> posiblesPokemon) {
        this.posiblesPokemon = posiblesPokemon;
    }
}
