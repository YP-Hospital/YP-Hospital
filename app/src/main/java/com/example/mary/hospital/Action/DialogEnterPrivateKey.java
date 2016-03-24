package com.example.mary.hospital.Action;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mary.hospital.R;

/**
 * Created by Grishalive on 22.03.2016.
 */
public class DialogEnterPrivateKey {
    static EditText editText;

    public static AlertDialog getDialog(Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.activity_dialog_enter_private_key, null); // Получаем layout по его ID
        builder.setView(view);
        editText = (EditText)view.findViewById(R.id.dialogEditText);
        builder.setTitle(R.string.dialog_show_key);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() { // Переход на оценку приложения
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DiseaseActivity.checkPrivateKey(editText.getText().toString());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // Переход на оценку приложения
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Переход
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        return builder.create();
    }
}
