package com.example.mary.hospital.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.example.mary.hospital.R;

/**
 * Created by Grishalive on 22.03.2016.
 */
public class DialogEnterPrivateKey{
    static EditText editText;

    public static AlertDialog getDialog(Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_enter_private_key, null); // Получаем layout по его ID
        builder.setView(view);
        builder.setTitle(R.string.please_enter_your_private_key);
        builder.setCancelable(true);
        return builder.create();
    }
}
