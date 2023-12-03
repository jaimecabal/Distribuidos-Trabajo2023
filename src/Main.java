package src;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static Map<String, Integer> personalidad = new HashMap<String,Integer>();
    private static Map<String,List<String>> preguntasRespuestas = new HashMap<String, List<String>>();
    public static void main(String[] args) {
        iniciarPersonalidad();
        iniciarPreguntasRespuestas();
    }

    private static void iniciarPreguntasRespuestas() {

    }

    private static void iniciarPersonalidad() {
        personalidad.put("Fuerte",0);
        personalidad.put("Docil",0);
        personalidad.put("Osada",0);
        personalidad.put("Alegre",0);
        personalidad.put("Agitada",0);
        personalidad.put("Ingenua",0);
        personalidad.put("Miedosa",0);
        personalidad.put("Activa",0);
        personalidad.put("Grosera",0);
        personalidad.put("Serena",0);
        personalidad.put("Placida",0);
        personalidad.put("Huraña",0);
        personalidad.put("Rara",0);
        personalidad.put("Mansa",0);
        personalidad.put("Alocada",0);
        personalidad.put("Audaz",0);
    }

}