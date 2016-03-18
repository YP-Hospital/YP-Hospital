package com.example.mary.hospital.Connection;

import android.content.Context;
import android.util.Log;

import com.example.mary.hospital.DatabaseHelper;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient {

    public static final int SERVER_PORT = 8080;
//    public static final String SERVER_IP = "192.168.43.229"; /** My phone wifi */ //TODO It changes every time. Don't forget this!
//    public static final String SERVER_IP = "127.0.0.1"; /** For locallhost */
    public static final String SERVER_IP = "172.20.217.98"; /** For wi-fi on work */

    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private Socket socket;

    public Boolean isConnected() {
        return this.socket != null;
    }

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
    public String getAnswer() {
        String answer = "";
        try {
            if (inputStream != null) {
                answer = inputStream.readUTF();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }
    public void stopClient() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            Log.e("TCP Client", "C: Connecting...");
            socket = new Socket(serverAddr, SERVER_PORT);
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();
            inputStream = new DataInputStream(sin);
            outputStream = new DataOutputStream(sout);
        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }
    }
}