package com.example.mary.hospital.Action;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mary.hospital.R;

/**
 * Created by Grishalive on 22.03.2016.
 */
public class DialogScreen {
    static EditText  editText;
    public static AlertDialog getDialog(Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_show_private_key, null); // Получаем layout по его ID
        builder.setView(view);
        Button copy = (Button)view.findViewById(R.id.dialogCopyButton);
        editText = (EditText)view.findViewById(R.id.dialogEditText);

        builder.setTitle(R.string.dialog_settings_title);

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("12321");
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() { // Переход на оценку приложения
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
