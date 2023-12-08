package distribuidos.trabajo;

import java.io.Serializable;
import java.util.List;

public class Respuesta implements Serializable {
    private String textoRespuesta;
    private List<PuntosNaturaleza> naturalezaList;

    public String getTextoRespuesta() {
        return textoRespuesta;
    }

    public void setTextoRespuesta(String textoRespuesta) {
        this.textoRespuesta = textoRespuesta;
    }

    public List<PuntosNaturaleza> getNaturalezaList() {
        return naturalezaList;
    }

    public void setNaturalezaList(List<PuntosNaturaleza> naturalezaList) {
        this.naturalezaList = naturalezaList;
    }
}
