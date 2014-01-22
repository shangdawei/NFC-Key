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


import pl.net.szafraniec.NFCKey.AboutDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import pl.net.szafraniec.NFCKey.R;
import android.content.pm.PackageManager.NameNotFoundException;

public class MainActivity extends Activity {
	final public int ABOUT = 0;
	public static String version;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
		version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
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
            	Intent intent = new Intent(getApplicationContext(), CloneReadActivity.class);
                startActivity(intent);
                finish();
            }
        });
        
      
		ImageView IV = (ImageView) findViewById(R.id.NFCLogo);
        IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View self) {
            	Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                startActivity(intent);
            }
        });
        
		Button r = (Button) findViewById(R.id.ReadBtn);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View self) {
            	Intent intent = new Intent(getApplicationContext(), ReadTagActivity.class);
                startActivity(intent);
            }
        });

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
	        case R.id.donate:
            	Intent intent = new Intent(getApplicationContext(), DonateActivity.class);
                startActivity(intent);
    			break;
	    }
	            return true;
	}
	
	
}
