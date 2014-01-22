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
* Ten plik jest czêœci¹ NFCKey.
*
* NFCKey jest wolnym oprogramowaniem; mo¿esz go rozprowadzaæ dalej
* i/lub modyfikowaæ na warunkach Powszechnej Licencji Publicznej GNU,
* wydanej przez Fundacjê Wolnego Oprogramowania - wed³ug wersji 2 tej
* Licencji lub (wed³ug twojego wyboru) którejœ z póŸniejszych wersji.
*
* Niniejszy program rozpowszechniany jest z nadziej¹, i¿ bêdzie on
* u¿yteczny - jednak BEZ JAKIEJKOLWIEK GWARANCJI, nawet domyœlnej
* gwarancji PRZYDATNOŒCI HANDLOWEJ albo PRZYDATNOŒCI DO OKREŒLONYCH
* ZASTOSOWAÑ. W celu uzyskania bli¿szych informacji siêgnij do
* Powszechnej Licencji Publicznej GNU.
*
* Z pewnoœci¹ wraz z niniejszym programem otrzyma³eœ te¿ egzemplarz
* Powszechnej Licencji Publicznej GNU (GNU General Public License);
* jeœli nie - napisz do Free Software Foundation, Inc., 59 Temple
* Place, Fifth Floor, Boston, MA  02110-1301  USA
*/
package pl.net.szafraniec.NFCKey;


import java.io.IOException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import pl.net.szafraniec.NFCKey.R;

public class CloneReadActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_nfc);
        TextView tv1 = (TextView) findViewById(R.id.textView);
        tv1.setText(getString(R.string.PlaceSourceTag));
        setResult(0);

        Button b = (Button) findViewById(R.id.cancel_nfc_write_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View self) {
                nfc_disable();
                finish();
            }
        });
    }		
    private void nfc_enable()
    {
        // Register for any NFC event (only while we're in the foreground)

        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        PendingIntent pending_intent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        adapter.enableForegroundDispatch(this, pending_intent, null, null);
    }

    private void nfc_disable()
    {
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);

        adapter.disableForegroundDispatch(this);
    }
    @Override
    protected void onResume() {
        super.onResume();

        nfc_enable();
    }

    @Override
    protected void onPause() {
        super.onPause();

        nfc_disable();
    }

    @Override
    public void onNewIntent(Intent intent)
    {
		byte[] payload = null;
    	String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
	        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
	        if (rawMsgs != null) {
	            NdefMessage [] msgs = new NdefMessage[rawMsgs.length];
	            for (int j = 0; j < rawMsgs.length; j++) {
	                msgs[j] = (NdefMessage) rawMsgs[j];
	                NdefRecord record = msgs[j].getRecords()[0];
	                if (record.getTnf() == NdefRecord.TNF_MIME_MEDIA /* || record.getTnf() == NdefRecord.TNF_WELL_KNOWN*/)
	                {
	                	String mimetype = record.toMimeType();
	                	if (mimetype.equals(Settings.nfc_mime_type) || mimetype.equals(Settings.nfc_mime_type_hidden)) /*|| (record.getTnf() == NdefRecord.TNF_WELL_KNOWN) */ {
		                	payload = record.getPayload();
	                	}
	                }
	            }
	        }
		}
		
		if (payload != null) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			Ndef ndef = Ndef.get(tag);
			try{
	              ndef.connect();
	              WriteActivity.nfc_payload = ndef.getNdefMessage();
	              ndef.close();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "IOExceptionCloneRead", Toast.LENGTH_SHORT).show();
          
            } catch (NullPointerException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "NullPointerCloneRead", Toast.LENGTH_SHORT).show();
             
            } catch (FormatException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "FormatExceptionCloneRead", Toast.LENGTH_SHORT).show();
            }
			ProgressBar pb1 = (ProgressBar) findViewById(R.id.progressBar1);
			pb1.setVisibility(View.INVISIBLE);
            Intent intent2 = new Intent(getApplicationContext(), CloneWriteNFCActivity.class);
            startActivity(intent2);
            finish();
		}
	}
	

}
