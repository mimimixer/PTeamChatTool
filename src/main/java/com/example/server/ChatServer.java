package com.example.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer implements Runnable {
    private int port=8000;
    private Socket socket;
    private ServerSocket server;
    private ArrayList<ConnectionHandler> userList ;
    private BufferedWriter out; //= new BufferedWriter(new OutputStreamWriter(new StringWriter(socket.getInputStream)));
    private BufferedReader readFromClient;// = new BufferedReader(new StringReader(ServerController.onSendButtonClick()));


    //for the server we need an arraylist to know the clients who connected
    //implementing Runnable means, Objects will be executed by a Thread
    //sowhen we start server, we opent he arraylist and fill it in the run method
    // by accepting connections, accepting messages and broadcasting them

    //Clients will be handled by a ConnectionHandler Class, evey client will be new Object

    public ChatServer(){
      //  this.port=port;
        userList = new ArrayList<>();
        System.out.println("ChatServer");
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
             System.out.println("bis hierher gings");
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

    //Clients will be handled by a ConnectionHandler
    //ConnectionHandler opens an Object with each Client

        class ConnectionHandler implements Runnable {
            private Socket client;
            private BufferedWriter printToScreen;
            private BufferedReader readFromClient;
            private String clientUsername;


            public ConnectionHandler(Socket client) {
                this.client = client;
                this.clientUsername = Integer.toString(client.getLocalPort());
                System.out.println("ConnectionHandler");
            }
            // will operate by Threads because of runnable:
            //so wen we run it, Server has already accepted a connection in Socket
            // we created ConnectionHAndler Object with it, is operating in a Thread.
            //by running the object we do: get new username, print it to screen,

            public void run() {
                try {
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
            public String getClientUsername() {
                return clientUsername;
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
