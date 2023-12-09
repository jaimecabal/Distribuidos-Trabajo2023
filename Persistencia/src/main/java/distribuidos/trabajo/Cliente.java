package distribuidos.trabajo;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        System.out.println("Vas a realizar un test de 10 preguntas que determinara su personalidad \r\n 1) Hacer el test \r\n 0) Hacer la bateria de pruebas \r\n");
        Scanner scan = new Scanner(System.in);
        String respuesta = scan.nextLine(), peticion = "";

        if (respuesta.equals("0")) {
            peticion += "PRUEBAS";
        } else if (respuesta.equals("1")) {
            peticion += "GET";
        }
        //Conectamos al servidor
        try {
            Socket s = new Socket("localhost", 8080);
            BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            BufferedReader brr = new BufferedReader(new InputStreamReader(s.getInputStream()));

            peticion += "\r\n";
            // Se añade el contacto al servidor
            bwr.write(peticion);
            bwr.flush();

            if (peticion.contains("PRUEBAS")) {
                // Ahora esperamos la respuesta del servidor
                System.out.println("< ----------------------------------------------------- >");
                System.out.println("Fin " + brr.readLine());
                // Si eso sale ok
                System.out.println("< ----------------------------------------------------- >");
            } else {
                String response = "";
                //Toda la parte del test
                while (!(response = brr.readLine()).equals("*")) {
                    if (!response.contains("< ---------------------------------------- >")) {
                        System.out.println(response);
                    } else {
                        System.out.println("Selecciona tu respuesta: ");
                        String respuestaTest = scan.nextLine();
                        while(!respuestaTest.matches("[0-3]")){
                            System.out.println("No puede ser esta respuesta, por favor escriba otra");
                            respuestaTest = scan.nextLine();
                        }
                        bwr.write(respuestaTest+"\r\n");
                        bwr.flush();
                    }
                }
                //Respuesta del servidor con la naturaleza procesada
                while ((response = brr.readLine()) != null) {
                    System.out.println(response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}