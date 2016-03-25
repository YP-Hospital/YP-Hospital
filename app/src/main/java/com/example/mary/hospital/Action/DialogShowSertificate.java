package com.example.mary.hospital.Action;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.example.mary.hospital.Model.Certificate;
import com.example.mary.hospital.R;

/**
 * Created by Grishalive on 25.03.2016.
 */
public class DialogShowSertificate {
    static EditText editText;

    public static AlertDialog getDialog(Activity activity, Certificate cert){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.activity_dialog_enter_private_key, null); // Получаем layout по его ID
        builder.setView(view);
        editText = (EditText)view.findViewById(R.id.editText);
        editText.setText(cert.getOpenKey());
        editText.setEnabled(false);
        builder.setTitle(R.string.dialog_show_key);
      /*  builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() { // Переход на оценку приложения
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
        });*/
        builder.setCancelable(true);
        return builder.create();
    }
}