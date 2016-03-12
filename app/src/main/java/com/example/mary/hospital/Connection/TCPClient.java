package com.example.mary.hospital.Connection;

import android.util.Log;
import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient {

    public static final int SERVER_PORT = 8080;

    private String serverMessage;
    private boolean running = false;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage (String message) throws Exception {
        if (outputStream != null) {
            outputStream.writeUTF(message);
            outputStream.flush();
        }
    }
    public void stopClient(){
        running = false;
    }

    public void run() {
        running = true;
        try {
            DatagramSocket datagramSocket = new DatagramSocket(SERVER_PORT);
            InetAddress serverAddr = datagramSocket.getInetAddress();
            Log.e("TCP Client", "C: Connecting...");
            Socket socket = new Socket(serverAddr, SERVER_PORT);
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();
            inputStream = new DataInputStream(sin);
            outputStream = new DataOutputStream(sout);
            try {
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