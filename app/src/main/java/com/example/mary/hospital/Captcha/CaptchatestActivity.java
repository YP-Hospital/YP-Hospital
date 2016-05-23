package com.example.mary.hospital.Captcha;

import com.example.mary.hospital.Action.Login;
import com.example.mary.hospital.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CaptchatestActivity extends AppCompatActivity {

	ImageView imageView;
	Button button;
	EditText editText;
	TextView textView;
	Captcha captcha;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_show_captcha);
        imageView = (ImageView)findViewById(R.id.dialogCaptchaImageView);
        button = (Button)findViewById(R.id.dialogCaptchaOkButton);
        editText = (EditText)findViewById(R.id.dialogCaptchaEditText);
		textView = (TextView)findViewById(R.id.dialogCaptchaTextView);
		textView.setText(R.string.enter_captcha);
		captcha = new TextCaptcha(300, 100, 5, TextCaptcha.TextOptions.NUMBERS_AND_LETTERS);
		imageView.setImageBitmap(captcha.image);
        button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String answer = String.valueOf(editText.getText());
				if (captcha.checkAnswer(answer)) {
					textView.setText(R.string.good_captcha);
					startActivity(new Intent(v.getContext(), Login.class));
				} else {
					captcha = new TextCaptcha(300, 100, 5, TextCaptcha.TextOptions.NUMBERS_AND_LETTERS);
					imageView.setImageBitmap(captcha.image);
					editText.setText("");
					textView.setText(R.string.wrong_answer);
				}
			}
		});
    }
	public void onBackPressed(){
		moveTaskToBack(true);
	}
}