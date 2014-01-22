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

import pl.net.szafraniec.NFCKey.R;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CloneWriteNFCActivity extends Activity {
//private static final String LOG_TAG = "WriteNFCActivity";


    protected void onCreate(Bundle sis) {
        super.onCreate(sis);
        setContentView(R.layout.activity_write_nfc);
        TextView tv1 = (TextView) findViewById(R.id.textView);
        tv1.setText(getString(R.string.PlaceCloneTag));
        ProgressBar pb1 = (ProgressBar) findViewById(R.id.progressBar1);
        pb1.setVisibility(View.INVISIBLE);
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
    	ProgressBar pb1 = (ProgressBar) findViewById(R.id.progressBar1);
        String action = intent.getAction();
        pb1.setVisibility(View.VISIBLE);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            int success = 0;
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
             try{
              ndef.connect();
              ndef.writeNdefMessage(WriteActivity.nfc_payload);
              ndef.close();
              success = 1;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "IOExceptionWrite", Toast.LENGTH_SHORT).show();
          
            } catch (NullPointerException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "NullPointerWrite", Toast.LENGTH_SHORT).show();
             
            } catch (FormatException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "FormatExceptionWrite", Toast.LENGTH_SHORT).show();
            }
 
            } else {
              NdefFormatable format = NdefFormatable.get(tag);
              if (format != null) {
               try{  
                format.connect();
                format.format(WriteActivity.nfc_payload);
                format.close();
                success = 1;
              } catch (IOException e) {
                  e.printStackTrace();
                  Toast.makeText(getApplicationContext(), "IOExceptionFormat", Toast.LENGTH_SHORT).show();
            
              } catch (NullPointerException e) {
                  e.printStackTrace();
                  Toast.makeText(getApplicationContext(), "NullPointerFormat", Toast.LENGTH_SHORT).show();
               
              } catch (FormatException e) {
                  e.printStackTrace();
                  Toast.makeText(getApplicationContext(), "FormatExceptionFormat", Toast.LENGTH_SHORT).show();
              	}
              }
        
            } 

            if (success == 1) {
            	Toast.makeText(getApplicationContext(), getString(R.string.Success), Toast.LENGTH_SHORT).show();
            } else {Toast.makeText(getApplicationContext(), getString(R.string.Failed), Toast.LENGTH_SHORT).show();}
         
        }
        
    }
}
