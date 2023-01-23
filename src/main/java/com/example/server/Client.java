package com.example.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Client implements Runnable {
    private Socket client;
    private BufferedWriter out; //= new BufferedWriter(new OutputStreamWriter(new StringWriter(socket.getInputStream)));
    private BufferedReader readFromClient;

    public void run() {
        try {
            Socket client = new Socket("127.0.0.1", 8000);
        //    out = new BufferedWriter(new OutputStreamWriter(client.getOuputStream()));
            readFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            ClientHandler inHandler= new ClientHandler();
            Thread thread=new Thread(inHandler);
            thread.start();
        } catch (IOException e) {
            System.out.println("Client could not connect");
            e.printStackTrace();
        }
    }

    public void closeClient (){
        try {
            out.close();
            readFromClient.close();
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e){
            System.out.println("couldn't close server");
        }
    }


    class ClientHandler implements Runnable {
        private Socket client;
        private BufferedWriter printToScreen;
        private BufferedReader readFromClient;
        private String clientUsername;

        public void run() {
            try {
                printToScreen = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                BufferedReader textIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String message = textIn.readLine();
                out.write(message);
            } catch (IOException e) {
                System.out.println("Coudn't connect client"); //eigentlich sollt das aufm interface
            }
        }
    }

    public static void main(String[] args) {
        Client client1=new Client();
        client1.run();
    }

}
