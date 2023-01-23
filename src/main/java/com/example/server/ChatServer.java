package com.example.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatServer implements Runnable {
    private int port=8000;
    private Socket socket;
    private ServerSocket server;
    private ArrayList<ConnectionHandler> userList ;
    private BufferedWriter out; //= new BufferedWriter(new OutputStreamWriter(new StringWriter(socket.getInputStream)));
    private BufferedReader readFromClient;// = new BufferedReader(new StringReader(ServerController.onSendButtonClick()));

    public ChatServer(){
      //  this.port=port;
        userList = new ArrayList<>();
    }

    @Override
    public void run() {
         try {
             server = new ServerSocket(port);
             Socket client = server.accept();
             ConnectionHandler newClient=new ConnectionHandler(client);
             userList.add(newClient);
             Thread thread=new Thread(newClient);
             thread.start();
         //   this.out = new BufferedWriter(new OutputStreamWriter(client.getOuputStream());
         //   this.readFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            System.out.println("Server could not connect");
            e.printStackTrace();
        }
    }

    public void broadcast(String message) {
        for (ConnectionHandler client : userList) {
            if (client!=null){
                client.sendMessage(message);
            }
        }
        }
    public void closeServer (){
        try {
            if (!server.isClosed()) {
                server.close();
            }
        } catch (IOException e){
            System.out.println("couldn't close server");
        }
    }


        class ConnectionHandler implements Runnable {
            private Socket client;
            private BufferedWriter printToScreen;
            private BufferedReader readFromClient;
            private String clientUsername;


            public ConnectionHandler(Socket client) {
                this.client = client;
            }

            public String getClientUsername() {
                return clientUsername;
            }

            public void run() {
                try {
                    this.clientUsername = Integer.toString(client.getLocalPort());
                    printToScreen = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                    readFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    //  printToScreen = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                    //  readFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    printToScreen.write(clientUsername + "Please enter a nickname:");
                    clientUsername=readFromClient.readLine();
                    broadcast(clientUsername + "joined the chat");
                } catch (IOException e) {
                    System.out.println("Coudn't connect client"); //eigentlich sollt das aufm interface
                }
            }


            public void sendMessage (String message){
                    try {
                        out.write(message);
                    } catch (IOException e) {
                        System.out.println("error writing message");
                    }
            }
            public void closeClient (Socket client, BufferedReader readFromClient, BufferedWriter out){
                try {
                    if (readFromClient != null) {
                        readFromClient.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                    if (client != null) {
                        client.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    public static void main(String[] args) {
        ChatServer server=new ChatServer();
        server.run();
    }

    }

/*
     public void writeMessageToSocket(com.example.server.ConnectionHandler client, String messageToServer) throws IOException {
                try {
                    printToScreen.write(messageToServer);
                    printToScreen.newLine();
                    printToScreen.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error sending message to the Client!");
                    printToScreen.close();
                }
            }

        }

    public static void writeMessageToSocket(String messageToServer, BufferedWriter bufferedWriter) throws IOException {
        try {
            bufferedWriter.write(messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending message to the Client!");
            bufferedWriter.close();
        }

 */


