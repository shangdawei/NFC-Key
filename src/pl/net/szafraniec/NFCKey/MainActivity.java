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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final String PREF_RUNCOUNT = "run_count";
	private static final String APP_LINK = "https://play.google.com/store/apps/details?id="
			+ R.class.getPackage().getName();
	public static final Uri URI_APP_LINK = Uri.parse(APP_LINK);
	private static final String DONATE_APP_LINK = "https://play.google.com/store/apps/details?id=pl.net.szafraniec.kontakty";

	public static final Uri URI_DONATE_APP_LINK = Uri.parse(DONATE_APP_LINK);

	public static int getRunCount(Context context) {
		SharedPreferences prefs = context
				.getSharedPreferences(PREF_RUNCOUNT, 0);
		return prefs.getInt(PREF_RUNCOUNT, 0);
	}

	public static void setRunCount(Context context, int RunCount) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(
				PREF_RUNCOUNT, 0).edit();
		prefs.putInt(PREF_RUNCOUNT, RunCount);
		prefs.commit();
	}

	final public int ABOUT = 0;

	public static String version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		NfcAdapter Nfc = NfcAdapter.getDefaultAdapter(this);
		if (Nfc == null) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.CantFindNFCAdapter), Toast.LENGTH_LONG)
					.show();
			finish();
		}
		if (Nfc != null) {
			if (Nfc.isEnabled() != true) {
				Toast.makeText(getApplicationContext(),
						getString(R.string.EnabeNFCFirst), Toast.LENGTH_LONG)
						.show();
				startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
			}
		}
		int RunCount = getRunCount(this);
		if (RunCount == 4) {
			showDialog(1);
		}
		if (RunCount < 6) {
			RunCount = RunCount + 1;
			setRunCount(this, RunCount);
		}
		SharedPreferences settings = getSharedPreferences(
				NFCKEYSettings.PREFS_NAME, 0);
		NFCKEYSettings.Default_APP = settings.getInt("DefaultApp", 0);
		Button x = (Button) findViewById(R.id.quit);
		x.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View self) {
				finish();
			}
		});

		Button clone = (Button) findViewById(R.id.clone);
		clone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View self) {
				Intent intent = new Intent(getApplicationContext(),
						CloneReadActivity.class);
				startActivity(intent);
				finish();
			}
		});

		ImageView IV = (ImageView) findViewById(R.id.NFCLogo);
		IV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View self) {
				Intent intent = new Intent(getApplicationContext(),
						WriteActivity.class);
				startActivity(intent);
			}
		});

		Button r = (Button) findViewById(R.id.ReadBtn);
		r.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View self) {
				Intent intent = new Intent(getApplicationContext(),
						ReadTagActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// We have only one dialog.
		Dialog dialog;
		AlertDialog.Builder builder;
		switch (id) {
		case 1:
			builder = new AlertDialog.Builder(this);
			builder.setMessage(getResources().getString(R.string.rate_text))
					.setTitle(getResources().getString(R.string.menu_item_rate))
					.setCancelable(false)
					.setPositiveButton(
							getResources().getString(R.string.menu_item_rate),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									// Do something here
									Intent GooglePlayIntent = new Intent(
											Intent.ACTION_VIEW,
											MainActivity.URI_APP_LINK);
									startActivity(GooglePlayIntent);
								}
							})
					.setNegativeButton(
							getResources().getString(android.R.string.cancel),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									// Do something here
								}
							})
					.setNeutralButton(
							getResources().getString(R.string.menu_item_donate),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									// Do something here
									Intent GooglePlayIntent = new Intent(
											Intent.ACTION_VIEW,
											MainActivity.URI_DONATE_APP_LINK);
									startActivity(GooglePlayIntent);
								}
							});

			dialog = builder.create();
			break;
		case 2:
			builder = new AlertDialog.Builder(this);
			builder.setMessage(getResources().getString(R.string.donate_text))
					.setTitle(
							getResources().getString(R.string.menu_item_donate))
					.setCancelable(false)
					.setPositiveButton(
							getResources().getString(R.string.menu_item_donate),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									// Do something here
									Intent GooglePlayIntent = new Intent(
											Intent.ACTION_VIEW,
											MainActivity.URI_DONATE_APP_LINK);
									startActivity(GooglePlayIntent);
								}
							})
					.setNegativeButton(
							getResources().getString(android.R.string.cancel),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									// Do something here
								}
							});

			dialog = builder.create();
			break;
		default:
			dialog = null;
		}
		return dialog;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.ABOUT:
			AboutDialog about = new AboutDialog(this);
			about.setTitle(getString(R.string.About));
			about.show();
			break;
		case R.id.settings:
			Intent settings = new Intent(getApplicationContext(),
					ChooseActivity.class);
			startActivity(settings);
			break;
		case R.id.menu_item_rate:
			showDialog(1);
			// Intent marketIntent = new Intent(
			// Intent.ACTION_VIEW, URI_APP_LINK);
			// activity.startActivity(marketIntent);
			return true;
		case R.id.menu_item_donate:
			showDialog(2);
			// Intent marketIntent = new Intent(
			// Intent.ACTION_VIEW, URI_APP_LINK);
			// activity.startActivity(marketIntent);
			return true;
		}
		return true;
	}
}
