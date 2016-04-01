package com.example.mary.hospital.Dialogs;

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
public class DialogShowPrivateKey {
    static EditText editText;
    static String key;

    public static AlertDialog getDialog(Activity activity, String k){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_show_private_key, null); // Получаем layout по его ID
        builder.setView(view);
        Button copy = (Button)view.findViewById(R.id.dialogCopyButton);
        editText = (EditText)view.findViewById(R.id.dialogEditText);
        builder.setTitle(R.string.dialog_show_key);
        key = k;
        editText.setText(key);
        copy.setVisibility(View.GONE);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                //ClipData clip = ClipData.newPlainText(label, text);
                //clipboard.setPrimaryClip(clip);
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() { // Переход на оценку приложения
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ,,,
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        return builder.create();
    }
    //
}
