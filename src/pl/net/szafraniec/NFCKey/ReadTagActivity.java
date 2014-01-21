package pl.net.szafraniec.NFCKey;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import pl.net.szafraniec.NFCKey.R;

public class ReadTagActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_nfc);

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
			load_from_nfc(payload);
		}
	}
	
	private boolean load_from_nfc(byte[] payload)
	{
		// Ignore first two bytes of payload (it's a filename index which is unused)
		DatabaseInfo dbinfo = DatabaseInfo.deserialise(this, payload, 2);
		
		return startKeepassActivity(dbinfo);
	}
	
	private boolean startKeepassActivity(DatabaseInfo dbinfo)
	{
		if (dbinfo.database != null) {
		Intent intent = new Intent();
		intent.setComponent(new ComponentName("com.android.keepass", "com.keepassdroid.PasswordActivity"));
		intent.putExtra("fileName", dbinfo.database);
		intent.putExtra("keyFile", dbinfo.keyfile_filename);
		intent.putExtra("password", dbinfo.password);
		intent.putExtra("launchImmediately", dbinfo.config != Settings.CONFIG_PASSWORD_ASK);
		Toast.makeText(getApplicationContext(), getString(R.string.LaunchingKeePass), Toast.LENGTH_SHORT).show();
		try {
			startActivity(intent);
		} catch (RuntimeException r) {
            r.printStackTrace();
 
        }} else {Toast.makeText(getApplicationContext(), getString(R.string.DatabaseMissing), Toast.LENGTH_LONG).show();}
		finish();
		return true;

	}

}
