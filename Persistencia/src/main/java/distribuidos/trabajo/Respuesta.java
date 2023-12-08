package distribuidos.trabajo;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "respuesta")
public class Respuesta implements Serializable {
    private static final long serialVersionUID = 1L;
    private Naturalezas listaNaturalezas;
    private String txtRespuesta;


    @XmlElement(name = "naturalezas")
    public Naturalezas getNaturalezas() {
        return listaNaturalezas;
    }

    public void setNaturalezas(Naturalezas naturalezas) {
        this.listaNaturalezas = naturalezas;
    }

    @XmlElement(name = "txtRespuesta")
    public String getTxtRespuesta() {
        return txtRespuesta;
    }

    public void setTxtRespuesta(String txtRespuesta) {
        this.txtRespuesta = txtRespuesta;
    }

    public String toString() {
        String reply = "Texto: " + txtRespuesta + "\r\nNaturaleza y sus puntos: \r\n";
        List<Naturaleza> naturalezaList = listaNaturalezas.getlNaturalezas();
        for (Naturaleza pn : naturalezaList) {
            reply += pn.toString() + " \r\n";
        }
        return reply;
    }
}
