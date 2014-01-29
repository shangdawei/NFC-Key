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

import java.io.File;
import java.security.SecureRandom;

import pl.net.szafraniec.NFCKey.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;

public class WriteActivity extends Activity {

	private static final int PASSWORD_NO = 0;
	private static final int PASSWORD_ASK = 1;
	private static final int PASSWORD_YES = 2;
	private static final int KEYFILE_NO = 0;
	private static final int KEYFILE_YES = 1;
	private static final int REQUEST_KEYFILE = 0;
	private static final int REQUEST_DATABASE = 1;
	private static final int REQUEST_NFC_WRITE = 2;
	private File keyfile = null;
	private File database = null;
	private byte[] random_bytes = new byte[NFCKEYSettings.key_length];
	public static NdefMessage nfc_payload;
	private int keyfile_option = KEYFILE_NO;
	private int password_option = PASSWORD_YES;
	public Boolean szyfr;
	public String nfc_mime_type_tmp;

	@Override
	protected void onCreate(Bundle sis) {
		super.onCreate(sis);
		setContentView(R.layout.activity_write);
		szyfr = false;
		if (sis != null) {
			password_option = sis.getInt("password_option");
			keyfile_option = sis.getInt("keyfile_option");
			if (sis.getString("keyfile").compareTo("") != 0)
				keyfile = new File(sis.getString("keyfile"));
			else
				keyfile = null;
		}

		initialiseView();

	}

	@Override
	protected void onSaveInstanceState(Bundle sis) {
		super.onSaveInstanceState(sis);
		if (keyfile == null)
			sis.putString("keyfile", "");
		else
			sis.putString("keyfile", keyfile.getAbsolutePath());
		sis.putInt("keyfile_option", keyfile_option);
		sis.putInt("password_option", password_option);
	}

	private void initialiseView() {
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		updateRadioViews();
		updateNonRadioViews();

		Switch sw = (Switch) findViewById(R.id.autostart);
		sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (!isChecked) {
					// The toggle is enabled
					szyfr = true;
					Toast.makeText(getApplicationContext(),
							getString(R.string.AutorunDisabled),
							Toast.LENGTH_SHORT).show();
					Toast.makeText(getApplicationContext(),
							getString(R.string.HiddenKey), Toast.LENGTH_SHORT)
							.show();
				} else {
					// The toggle is disabled
					Toast.makeText(getApplicationContext(),
							getString(R.string.AutorunEnabed),
							Toast.LENGTH_SHORT).show();
					szyfr = false;
				}
			}
		});

		RadioButton rb;

		rb = (RadioButton) findViewById(R.id.keyfile_no);
		rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked)
					keyfile_option = KEYFILE_NO;
				else
					keyfile_option = KEYFILE_YES;
				updateNonRadioViews();
			}
		});

		rb = (RadioButton) findViewById(R.id.password_yes);
		rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked)
					password_option = PASSWORD_YES;
				else
					password_option = PASSWORD_NO;
				updateNonRadioViews();
			}
		});

		Button b = (Button) findViewById(R.id.write_nfc);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View self) {
				if (database == null) {
					Toast.makeText(getApplicationContext(),
							getString(R.string.SelectDatabaseFirst),
							Toast.LENGTH_SHORT).show();
				} else {
					create_random_bytes();
					self.setEnabled(false);
					Intent intent = new Intent(getApplicationContext(),
							WriteNFCActivity.class);
					startActivityForResult(intent, REQUEST_NFC_WRITE);
				}
			}
		});

		Button f = (Button) findViewById(R.id.app_quit);
		f.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View self) {
				finish();
			}
		});

		ImageButton ib = (ImageButton) findViewById(R.id.choose_keyfile);
		ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				keyfile_option = KEYFILE_YES;
				setRadio(R.id.keyfile_yes, true);
				Intent target = FileUtils.createGetContentIntent();
				Intent intent = Intent.createChooser(target,
						getString(R.string.SelectAKeyfile));
				try {
					startActivityForResult(intent, REQUEST_KEYFILE);
				} catch (ActivityNotFoundException e) {
					Log.d(DatabaseInfo.LOG_TAG, "ActivityNotFoundException");
					e.printStackTrace();
				}
			}

		});

		ib = (ImageButton) findViewById(R.id.choose_database);
		ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent target = FileUtils.createGetContentIntent();
				Intent intent = Intent.createChooser(target,
						getString(R.string.SelectADatabase));
				try {
					startActivityForResult(intent, REQUEST_DATABASE);
				} catch (ActivityNotFoundException e) {
					Log.d(DatabaseInfo.LOG_TAG, "ActivityNotFoundException");
					e.printStackTrace();
				}
			}
		});
	}

	// Stuff came back from file chooser
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_KEYFILE:
			if (resultCode == RESULT_OK) {
				// The URI of the selected file
				final Uri uri = data.getData();
				// Create a File from this Uri
				keyfile = FileUtils.getFile(this, uri);
				updateNonRadioViews();
			}
			break;
		case REQUEST_DATABASE:
			if (resultCode == RESULT_OK) {
				final Uri uri = data.getData();
				database = FileUtils.getFile(this, uri);
				updateNonRadioViews();
			}
			break;
		case REQUEST_NFC_WRITE:
			// Re-enable NFC writing.
			Button nfc_write = (Button) findViewById(R.id.write_nfc);
			nfc_write.setEnabled(true);

			if (resultCode == 1) {
				if (encrypt_and_store()) {
					// Job well done! Let's have some toast.
					Toast.makeText(getApplicationContext(),
							getString(R.string.Success), Toast.LENGTH_SHORT)
							.show();
					finish();
				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.ErrorDatabaseWriting),
							Toast.LENGTH_SHORT).show();
				}
			} else {
				// can't think of a good toast analogy for fail
				Toast.makeText(getApplicationContext(),
						getString(R.string.Failed), Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void create_random_bytes() {
		SecureRandom rng = new SecureRandom();
		rng.nextBytes(random_bytes);
		// Create the NFC version of this data
		if (szyfr == true) {
			nfc_mime_type_tmp = NFCKEYSettings.nfc_mime_type_hidden;
		} else {
			nfc_mime_type_tmp = NFCKEYSettings.nfc_mime_type;
		}
		NdefRecord ndef_records = NdefRecord.createMime(nfc_mime_type_tmp,
				random_bytes);
		nfc_payload = new NdefMessage(ndef_records);
	}

	private boolean encrypt_and_store() {
		DatabaseInfo dbinfo;
		int config;
		String keyfile_filename;
		String password;

		if (password_option == PASSWORD_ASK)
			config = NFCKEYSettings.CONFIG_PASSWORD_ASK;
		else
			config = NFCKEYSettings.CONFIG_NOTHING;

		// Policy: no password is stored as null password (bit silly?)
		if (password_option == PASSWORD_NO)
			password = "";
		else {
			EditText et_password = (EditText) findViewById(R.id.password);
			password = et_password.getText().toString();
		}

		// Policy: no keyfile is stored as empty filename.
		if (keyfile_option == KEYFILE_NO)
			keyfile_filename = "";
		else {
			keyfile_filename = keyfile.getAbsolutePath();
		}

		dbinfo = new DatabaseInfo(database.getAbsolutePath(), keyfile_filename,
				password, config);

		try {
			return dbinfo.serialise(this, random_bytes);
		} catch (CryptoFailedException e) {
			Log.d(DatabaseInfo.LOG_TAG, "CryptoFailedException-encrypt");
			Toast.makeText(getApplicationContext(),
					getString(R.string.EncryptError), Toast.LENGTH_SHORT)
					.show();
			return false;
		}
	}

	private void updateRadioViews() {
		setRadio(R.id.keyfile_no, keyfile_option == KEYFILE_NO);
		setRadio(R.id.keyfile_yes, keyfile_option == KEYFILE_YES);
		setRadio(R.id.password_no, password_option == PASSWORD_NO);
		setRadio(R.id.password_ask, password_option == PASSWORD_ASK);
		setRadio(R.id.password_yes, password_option == PASSWORD_YES);
	}

	private void updateNonRadioViews() {
		EditText et_password = (EditText) findViewById(R.id.password);
		et_password.setEnabled(password_option == PASSWORD_YES);

		TextView tv_keyfile = (TextView) findViewById(R.id.keyfile_name);
		tv_keyfile.setEnabled(keyfile_option == KEYFILE_YES);
		if (keyfile != null)
			tv_keyfile.setText(keyfile.getAbsolutePath());
		else
			tv_keyfile.setText(getString(R.string.missing));

		TextView tv_database = (TextView) findViewById(R.id.database_name);
		if (database != null)
			tv_database.setText(database.getAbsolutePath());
		else
			tv_database.setText(getString(R.string.missing));
	}

	private void setRadio(int id, boolean checked) {
		RadioButton rb = (RadioButton) findViewById(id);
		rb.setChecked(checked);
	}

}
