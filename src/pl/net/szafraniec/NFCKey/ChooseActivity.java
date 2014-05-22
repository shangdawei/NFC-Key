/**
 * Copyright (C) 2014 Mateusz Szafraniec
 * This file is part of NFCKey.
 *
 * NFCKey is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * NFCKey is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NFCKey; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Ten plik jest częścią NFCKey.
 *
 * NFCKey jest wolnym oprogramowaniem; możesz go rozprowadzać dalej
 * i/lub modyfikować na warunkach Powszechnej Licencji Publicznej GNU,
 * wydanej przez Fundację Wolnego Oprogramowania - według wersji 2 tej
 * Licencji lub (według twojego wyboru) którejś z późniejszych wersji.
 *
 * Niniejszy program rozpowszechniany jest z nadzieją, iż będzie on
 * użyteczny - jednak BEZ JAKIEJKOLWIEK GWARANCJI, nawet domyślnej
 * gwarancji PRZYDATNOŚCI HANDLOWEJ albo PRZYDATNOŚCI DO OKREŚLONYCH
 * ZASTOSOWAŃ. W celu uzyskania bliższych informacji sięgnij do
 * Powszechnej Licencji Publicznej GNU.
 *
 * Z pewnością wraz z niniejszym programem otrzymałeś też egzemplarz
 * Powszechnej Licencji Publicznej GNU (GNU General Public License);
 * jeśli nie - napisz do Free Software Foundation, Inc., 59 Temple
 * Place, Fifth Floor, Boston, MA  02110-1301  USA
 */

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

	@Override
	protected void onStop() {
		super.onStop();
		SharedPreferences settings = getSharedPreferences(
				NFCKEYSettings.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("DefaultApp", NFCKEYSettings.Default_APP);
		editor.commit();
	}
}
