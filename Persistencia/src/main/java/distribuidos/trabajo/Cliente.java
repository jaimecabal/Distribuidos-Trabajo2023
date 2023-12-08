package distribuidos.trabajo;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        System.out.println("Vas a realizar un test de 10 preguntas que determinara su personalidad \r\n 1) Hacer el test \r\n 0) Hacer la bateria de pruebas \r\n");
        Scanner scan = new Scanner(System.in);
        String respuesta = scan.nextLine(),peticion = "";

        if(respuesta.equals("0")){
            peticion += "TEST";
        } else if(respuesta.equals("1")){
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

            if(peticion.contains("TEST")){
                // Ahora esperamos la respuesta del servidor
                System.out.println("< ----------------------------------------------------- >");
                System.out.println("Fin " + brr.readLine());
                // Si eso sale ok
                System.out.println("< ----------------------------------------------------- >");
            } else {
                String response = "";
                while(!(response = brr.readLine()).equals("*")){
                    System.out.println(response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}