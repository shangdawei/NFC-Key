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
