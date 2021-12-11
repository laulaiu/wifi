package com.example.wifi;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private WifiManager wifiManager;
    private List<ScanResult> resultsSucess;
    private List<ScanResult> resultsError;
    private ListView Lista_wifi;

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                } else {

                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Lista_wifi = findViewById(R.id.Lista_wifi);

        wifiManager = (WifiManager)
                getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    scanSuccess();
                    exibeLista(resultsSucess);
                } else {
                    // scan failure handling
                    scanFailure();
                    exibeLista(resultsError);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);

        boolean success = wifiManager.startScan();
        if (!success) {
            // scan failure handling
            scanFailure();
            Toast.makeText(this, "falha-2", Toast.LENGTH_SHORT).show();
        }

    }

    private void scanSuccess() {
        resultsSucess = wifiManager.getScanResults();
        Toast.makeText(this, "Sucesso", Toast.LENGTH_SHORT).show();
   //... use new scan results ...
    }

    private void scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        resultsError = wifiManager.getScanResults();
        Toast.makeText(this, "falha", Toast.LENGTH_SHORT).show();
  //... potentially use older scan results ...
    }

    public void exibeLista(List<ScanResult> lista){
        ArrayAdapter<ScanResult> adaptador = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_list_item_1, lista);
    }



}