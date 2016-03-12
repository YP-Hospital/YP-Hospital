package com.example.mary.hospital.Connection;

import android.os.AsyncTask;

public class Connector extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {
        TCPClient client = new TCPClient();
        client.run();
        return null;
    }
}
