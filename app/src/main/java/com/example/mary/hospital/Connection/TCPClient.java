package com.example.mary.hospital.Connection;

import android.util.Log;
import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient {

    public static final int SERVER_PORT = 8080;
    public static final String SERVER_IP = "172.20.44.45"; //TODO Change every time. Don't forget this!

    private String serverMessage;
    private boolean running = false;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage (String message) {
        try {
            if (outputStream != null) {
                outputStream.writeUTF(message);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void stopClient(){
        running = false;
    }

    public void run() {
        running = true;
        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            Log.e("TCP Client", "C: Connecting...");
            Socket socket = new Socket(serverAddr, SERVER_PORT);
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();
            inputStream = new DataInputStream(sin);
            outputStream = new DataOutputStream(sout);
            try {
                sendMessage("Hello from Android");
                while (running) {
                    serverMessage = inputStream.readUTF();
                    if (inputStream != null) {
                        System.out.println(inputStream.readUTF());
                    }
                    serverMessage = null;
                }
            } catch (Exception e) {
                Log.e("TCP", "S: Error", e);
            } finally {
                socket.close();
            }
        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }
    }
}