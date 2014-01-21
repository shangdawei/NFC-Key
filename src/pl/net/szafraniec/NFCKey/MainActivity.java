
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

public class MainActivity extends Activity {
	final public int ABOUT = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
