package dev.bpv.freenasmonitor.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.sonelli.juicessh.performancemonitor.R;

public class Settings extends Activity {

    public static final String BROADCAST_ADDRESS = "BROADCAST_ADDRESS";
    public static final String MAC_ADDRESS = "MAC_ADDRESS";

    String broadcastAddress = "";
    String macAddress = "";

    EditText broadcastTextView;
    EditText macTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences = getSharedPreferences("WOL", Context.MODE_PRIVATE);
        //SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        try{
            broadcastAddress = sharedPreferences.getString(BROADCAST_ADDRESS,"192.168.1.255");
            macAddress = sharedPreferences.getString(MAC_ADDRESS,"FF:FF:FF:FF:FF:FF");
        }catch (Exception e){
            broadcastAddress = "192.168.1.255";
            macAddress = "FF:FF:FF:FF:FF:FF";
        }

        broadcastTextView = (EditText) findViewById(R.id.ip_settings_textfield);
        macTextView = (EditText) findViewById(R.id.mac_settings_textfield);

        broadcastTextView.setText(broadcastAddress);
        macTextView.setText(macAddress);

    }

    public void saveSettings(View v){
        SharedPreferences sharedPreferences = getSharedPreferences("WOL", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        broadcastTextView = (EditText) findViewById(R.id.ip_settings_textfield);
        macTextView = (EditText) findViewById(R.id.mac_settings_textfield);

        sharedPreferencesEditor.putString(BROADCAST_ADDRESS, broadcastTextView.getText().toString().trim());
        sharedPreferencesEditor.putString(MAC_ADDRESS, macTextView.getText().toString().trim());

        sharedPreferencesEditor.commit();

        finish();
    }

}
