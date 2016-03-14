package com.example.mary.hospital.Connection;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.mary.hospital.ExtraResource;

public class Connector extends AsyncTask<String, Void, Void> {
    private Context context;

    public Connector(Context context) {
        if (context != null) {
            this.context = context;
        } else {
            this.cancel(true);
        }
    }

    /**
     * start with execute method
     * @param params message to server. If it nothing to send, it just will reload BD
     */
    @Override
    protected Void doInBackground(String ... params) {
        TCPClient client = new TCPClient();
        client.run();
        if (client.isConnected()) {
            for (String message : params) {
                client.sendMessage(message);
            }
            client.sendMessage(ExtraResource.STOP_WORDS);
            client.updateDB(context);
            client.stopClient();
        } else {
            this.cancel(true);
        }
        return  null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (context != null) {
            Toast.makeText(context, "No connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(context, "Operation complete", Toast.LENGTH_LONG).show();
    }
}
