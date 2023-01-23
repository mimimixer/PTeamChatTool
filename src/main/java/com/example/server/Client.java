package com.example.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

//for the client we need create a new CLientObject with this Socket
//implementing Runnable means, Objects will be executed by a Thread
//sowhen we create Client and run it, it will be assigned the Socket, a read and write function,
// and a ClientHandler, beeing executed by an own Thread

public class Client implements Runnable {
    private Socket client;
    private BufferedWriter out; //= new BufferedWriter(new OutputStreamWriter(new StringWriter(socket.getInputStream)));
    private BufferedReader readFromClient;

    public Client ()  {
        try {
            client = new Socket("localhost", 8000);
            System.out.println("Client check");
        }catch (IOException e){
            System.out.println("couldnt create client");
        }
    }

    public void run() {
        try {
        //    out = new BufferedWriter(new OutputStreamWriter(client.getOuputStream()));
            readFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            ClientHandler inHandler= new ClientHandler(client);
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

    //The ClientHandler is assigned a CLient, waits for incoming and outgoing messages to forward

    class ClientHandler implements Runnable {
        private Socket client;
        private BufferedWriter printToScreen;
        private BufferedReader readFromClient;
        private String clientUsername;

        public ClientHandler(Socket client){
            this.client=client;
        }

        public void run() {
            try {
                printToScreen = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                BufferedReader textIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String message = textIn.readLine();
                printToScreen.write(message);
            } catch (IOException e) {
                System.out.println("Coudn't connect client");
            }
        }
    }

    public static void main(String[] args) {
        Client client=new Client();
        client.run();
    }

}
