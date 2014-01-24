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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import pl.net.szafraniec.NFCKey.R;

public class DonateActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_donate);

	    ImageView iv = (ImageView) findViewById(R.id.bitcoin);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View self) {
            	String url = getString(R.string.donateBitcoin_uri);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                //return false;
            }
        });
        
	    ImageView pp = (ImageView) findViewById(R.id.paypal);
        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View self) {
            	String url = getString(R.string.donatePayPal_uri);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                //return false;
            }
        });
	    // TODO Auto-generated method stub
	}

}
