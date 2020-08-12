package com.nextbuy.demo.backend;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;


//WIFI CONNECTION  -- READS DATA
public class WiFiConn {


    private static final int serverPort = 37899;

    private ServerSocket server;
    private Socket connection;
    private BufferedWriter output;
    private BufferedReader input;

    private String message = "";


    public void startRunning() {
        try {

            server = new ServerSocket(serverPort, 100);
            while (true) {
                try {
                    waitForConnection();
                    setupStreams();
                    whileConnected();
                } catch (EOFException eofException) {
                    System.out.println("Client terminated connection");
                } catch (IOException ioException) {
                    System.out.println("Could not connect...");
                } finally {
                    closeStreams();
                }
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    private void waitForConnection() throws IOException {

        System.out.println("Waiting for someone to connect...");
        connection = server.accept(); //once someone asks to connect, it accepts the connection to the socket this gets repeated fast
        System.out.println("Now connected to " + connection.getInetAddress().getHostName()); //shows IP adress of client

    }

    private void setupStreams() throws IOException {

        System.out.println("creating streams...");
        output = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        output.flush();
        input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        System.out.println("Streams are setup!");

    }

    //reading
    private void whileConnected() throws IOException {

        do {

            char x = (char) input.read();
            while (x != '\n') {
                message += x;
                x = (char) input.read();
            }
            System.out.println(message);
            message = "";

        } while (!message.equals("END")); //if the user has not disconnected, by sending "END"

    }

    private void closeStreams() {

        System.out.println(false);

        System.out.println("Closing streams...");
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        try {
            output.write(message + '\n');
            output.flush();
            System.out.println("Sent: " + message);
        } catch (IOException ex) {
            System.out.println("\nSomething messed up whilst sending messages...");
        }

    }

}
