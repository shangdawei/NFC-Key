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
import android.content.ComponentName;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;
import pl.net.szafraniec.NFCKey.R;

public class ReadActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		byte[] payload = null;
		Intent intent = getIntent();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			Parcelable[] rawMsgs = intent
					.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
				for (int j = 0; j < rawMsgs.length; j++) {
					msgs[j] = (NdefMessage) rawMsgs[j];
					NdefRecord record = msgs[j].getRecords()[0];
					if (record.getTnf() == NdefRecord.TNF_MIME_MEDIA) {
						String mimetype = record.toMimeType();
						if (mimetype.equals(NFCKEYSettings.nfc_mime_type)) {
							payload = record.getPayload();
						}
					}
				}
			}
		}

		if (payload != null) {
			load_from_nfc(payload);
		}
	}

	private boolean load_from_nfc(byte[] payload) {
		try {
			DatabaseInfo dbinfo = DatabaseInfo.deserialise(this, payload);
			return startKeepassActivity(dbinfo);
		} catch (CryptoFailedException e) {
			Toast.makeText(this, getString(R.string.DecryptError),
					Toast.LENGTH_LONG).show();
			Log.d(DatabaseInfo.LOG_TAG, "CryptoFailedException-deserialize");
			finish();
			return false;
		}
	}

	private boolean startKeepassActivity(DatabaseInfo dbinfo) {
		if (dbinfo.database != null) {
			Intent intent = new Intent();
			boolean app_found = false;
			if (NFCKEYSettings.Default_APP == 0) {
				Toast.makeText(getApplicationContext(),
						getString(R.string.SearchingForKeePass),
						Toast.LENGTH_SHORT).show();
			}
			if ((NFCKEYSettings.Default_APP == 1)
					|| (NFCKEYSettings.Default_APP == 0)) {
				intent.setComponent(new ComponentName(
						"keepass2android.keepass2android",
						"keepass2android.PasswordActivity"));
				intent.putExtra("fileName", dbinfo.database);
				intent.putExtra("keyFile", dbinfo.keyfile_filename);
				intent.putExtra("password", dbinfo.password);
				intent.putExtra("launchImmediately",
						dbinfo.config != NFCKEYSettings.CONFIG_PASSWORD_ASK);
				if (NFCKEYSettings.Default_APP > 0) {
					Toast.makeText(getApplicationContext(),
							getString(R.string.LaunchingKeepass2),
							Toast.LENGTH_SHORT).show();
				}
				try {
					startActivity(intent);
					app_found = true;
				} catch (RuntimeException r) {
					app_found = false;
					r.printStackTrace();
					if (NFCKEYSettings.Default_APP > 0) {
						Toast.makeText(getApplicationContext(),
								getString(R.string.CantFindKeepass2Android),
								Toast.LENGTH_LONG).show();
					}
				}
			}
			if ((NFCKEYSettings.Default_APP == 2)
					|| ((NFCKEYSettings.Default_APP == 0) && !app_found)) {
				intent.setComponent(new ComponentName(
						"keepass2android.keepass2android_nonet",
						"keepass2android.PasswordActivity"));
				intent.putExtra("fileName", dbinfo.database);
				intent.putExtra("keyFile", dbinfo.keyfile_filename);
				intent.putExtra("password", dbinfo.password);
				intent.putExtra("launchImmediately",
						dbinfo.config != NFCKEYSettings.CONFIG_PASSWORD_ASK);
				if (NFCKEYSettings.Default_APP > 0) {
					Toast.makeText(getApplicationContext(),
							getString(R.string.LaunchingKeepass2offline),
							Toast.LENGTH_SHORT).show();
				}
				try {
					startActivity(intent);
					app_found = true;
				} catch (RuntimeException r) {
					app_found = false;
					r.printStackTrace();
					if (NFCKEYSettings.Default_APP > 0) {
						Toast.makeText(
								getApplicationContext(),
								getString(R.string.CantFindKeepass2AndroidOffline),
								Toast.LENGTH_LONG).show();
					}
				}
			}
			if ((NFCKEYSettings.Default_APP == 3)
					|| ((NFCKEYSettings.Default_APP == 0) && !app_found)) {
				intent.setComponent(new ComponentName("com.android.keepass",
						"com.keepassdroid.PasswordActivity"));
				intent.putExtra("fileName", dbinfo.database);
				intent.putExtra("keyFile", dbinfo.keyfile_filename);
				intent.putExtra("password", dbinfo.password);
				intent.putExtra("launchImmediately",
						dbinfo.config != NFCKEYSettings.CONFIG_PASSWORD_ASK);
				if (NFCKEYSettings.Default_APP > 0) {
					Toast.makeText(getApplicationContext(),
							getString(R.string.LaunchingKeePass),
							Toast.LENGTH_SHORT).show();
				}
				try {
					startActivity(intent);
					app_found = true;
				} catch (RuntimeException rr) {
					app_found = false;
					rr.printStackTrace();
					if (NFCKEYSettings.Default_APP > 0) {
						Toast.makeText(getApplicationContext(),
								getString(R.string.CantFindKeePassDroid),
								Toast.LENGTH_LONG).show();
					}
				}
			}

		} else {
			Toast.makeText(getApplicationContext(),
					getString(R.string.DatabaseMissing), Toast.LENGTH_LONG)
					.show();
		}
		finish();
		return true;
	}

}
