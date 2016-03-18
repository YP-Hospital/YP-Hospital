package com.example.mary.hospital.Connection;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.R;

public class Connector extends AsyncTask<String, Void, String> {
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
     * @param params message to server.
     * messages must be like this:
     * "WHAT_TO_DO IN_WHAT_TABLE " +
     * If insert: "FIELDS_VALUE"
     * If update: "FIELD_NAME FIELD_VALUE OBJECT_TO_CHANGE_ID"
     * If delete: "OBJECT_TO_DELETE_ID"
     * If select: "FIELDS_TO_SELECT" + (not required)" where CHOOSE_BY_FIELDS VALUE_OF_THIS_FIELDS"
     */
    @Override
    protected String doInBackground(String ... params) {
        String answer = "";
        TCPClient client = new TCPClient();
        client.run();
        if (client.isConnected()) {
            for (String message : params) {
                client.sendMessage(message);
            }
            client.sendMessage(ExtraResource.STOP_WORDS);
            answer = client.getAnswer();
            client.stopClient();
        } else {
            this.cancel(true);
        }
        return answer;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (context != null) {
            Toast.makeText(context, R.string.no_connection, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPostExecute(String answer) {
        super.onPostExecute(answer);
        if (answer.startsWith("true")) {
            Toast.makeText(context, R.string.operation_complete, Toast.LENGTH_LONG).show();
        }
        else if (answer.equals("")) {
            ExtraResource.showErrorDialog(R.string.server_error, context);//TODO Server error instead close app
        }
    }
}
