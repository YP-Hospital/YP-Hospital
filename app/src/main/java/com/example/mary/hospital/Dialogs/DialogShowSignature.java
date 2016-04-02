package com.example.mary.hospital.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.example.mary.hospital.R;

/**
 * Created by Grishalive on 25.03.2016.
 */
public class DialogShowSignature {
    static EditText editText;

    public static AlertDialog getDialog(Activity activity, String signature){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_enter_private_key, null); // Получаем layout по его ID
        builder.setView(view);
        editText = (EditText)view.findViewById(R.id.dialogEnterKeyEditText);
        editText.setText(signature);
        editText.setEnabled(false);
        builder.setTitle(R.string.this_is_signature_of_the_last_modified);
        builder.setCancelable(true);
        return builder.create();
    }
}

