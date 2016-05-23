package com.example.mary.hospital.Connection;

import android.util.Log;


import com.example.mary.hospital.ExtraResource;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPClient {

    public static final int SERVER_PORT = 8080;
 //   public static final String SERVER_IP = "192.168.43.229"; /** My phone wifi */ //TODO It changes every time. Don't forget this!
//    public static final String SERVER_IP = "127.0.0.1"; /** For locallhost */
//    public static final String SERVER_IP = "192.168.43.161"; /** For Masha mobile Grisha comp */
 //   public static final String SERVER_IP = "10.160.59.178"; /** For GrishaServ eduroam*/
//   public static final String SERVER_IP = "10.160.16.174"; /** For MashaServ eduroam*/
//    public static final String SERVER_IP = "10.150.5.140"; /** For 507 univer */
 // public static final String SERVER_IP = "192.168.43.161"; /** Grisha some wi-fi */
  public static final String SERVER_IP = "192.168.1.3"; /** Grisha home wi-fi */
//    public static final String SERVER_IP = "172.20.216.106"; /** For wi-fi on work */
//    public static final String SERVER_IP = "172.20.44.45"; /** For web on work */

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
    public List<String> getAnswers() {
        List<String> answers = new ArrayList<>();
        try {
            if (inputStream != null) {
                String message = "";
                while (!message.equals(ExtraResource.STOP_WORDS)) {
                    message = inputStream.readUTF();
                    answers.add(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answers;
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