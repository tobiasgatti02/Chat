package com.example.client;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Client(Socket socket){
        try {
            this.socket = socket;
            new InputStreamReader(socket.getInputStream());
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public void sendMessageToServer(String messageToServer){
        try {
            bufferedWriter.write(messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){e.printStackTrace();
            closeEverything(socket,bufferedReader,bufferedWriter);}
    }

    public void receiveMessageFromServer(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()){
                    String messageFromServer = null;
                    try {
                        messageFromServer = bufferedReader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                        closeEverything(socket,bufferedReader,bufferedWriter);
                        break;
                    }
                    Controller.addLabel(messageFromServer,vBox);
                }
            }
        }).start();
    }

    public void closeEverything ( Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            if (bufferedReader!=null){
                bufferedReader.close();
            }
            if (bufferedWriter !=null){
                bufferedWriter.close();
            }
            if(socket!=null){
                socket.close();
            }
        }catch (IOException e){e.printStackTrace();}
    }

}
