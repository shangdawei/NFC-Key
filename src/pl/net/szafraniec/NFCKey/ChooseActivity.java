package pl.net.szafraniec.NFCKey;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class ChooseActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences settings = getSharedPreferences(
				NFCKEYSettings.PREFS_NAME, 0);
		NFCKEYSettings.Default_APP = settings.getInt("DefaultApp", 0);
		setContentView(R.layout.activity_choose);
		RadioButton rb0 = (RadioButton) findViewById(R.id.radio0);
		RadioButton rb1 = (RadioButton) findViewById(R.id.radio1);
		RadioButton rb2 = (RadioButton) findViewById(R.id.radio2);
		RadioButton rb3 = (RadioButton) findViewById(R.id.radio3);
		if (NFCKEYSettings.Default_APP == 0) {
			rb0.setChecked(true);
		}
		if (NFCKEYSettings.Default_APP == 1) {
			rb1.setChecked(true);
		}
		if (NFCKEYSettings.Default_APP == 2) {
			rb2.setChecked(true);
		}
		if (NFCKEYSettings.Default_APP == 3) {
			rb3.setChecked(true);
		}
		Button save = (Button) findViewById(R.id.a_save);
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View self) {
				finish();
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
		SharedPreferences settings = getSharedPreferences(
				NFCKEYSettings.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("DefaultApp", NFCKEYSettings.Default_APP);
		editor.commit();
	}

	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch (view.getId()) {
		case R.id.radio0:
			if (checked)
				NFCKEYSettings.Default_APP = 0;
			break;
		case R.id.radio1:
			if (checked)
				NFCKEYSettings.Default_APP = 1;
			break;
		case R.id.radio2:
			if (checked)
				NFCKEYSettings.Default_APP = 2;
			break;
		case R.id.radio3:
			if (checked)
				NFCKEYSettings.Default_APP = 3;
			break;
		}
	}
}
