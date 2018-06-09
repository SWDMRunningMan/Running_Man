package com.example.geonsu.bluetoothforrnm;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "BT";
    Button btn_connect;
    Button btn_scan;
    TextView textView_result;
    BluetoothAdapter btAdapter;
    String [] scannedDeviceName = new String[1000];
    String [] scannedDeviceAddress = new String[1000];
    String myMacAddress;
    String seekerMacAddress ="8C:8E:F2:33:B7:09";
    int count =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(broadcastReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(broadcastReceiver, filter);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        myMacAddress = btAdapter.getAddress();//자신의 맥주소

        textView_result = findViewById(R.id.textView_result);
        btn_connect = findViewById(R.id.btn_connect);
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableBluetooth();//기기의 블루투스를 켠다
            }
        });

        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doDiscovery();
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(btAdapter!=null)
            btAdapter.cancelDiscovery();
        this.unregisterReceiver(broadcastReceiver);
    }

    public void enableBluetooth(){
        if(btAdapter.isEnabled()){
            //블루투스 켜져있음
            Toast.makeText(this,"켜져있음",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"켜야함",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(intent);
        }
    }
    public void doDiscovery(){
        Log.d(TAG,"doDiscovery()");
        if(btAdapter.isDiscovering())
            btAdapter.cancelDiscovery();
        else
        {
            btAdapter.startDiscovery();
            Log.d(TAG,"startDiscovery()");
        }

    }
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction(); //왜 변수 이름 action?

            //device를 찾아내면
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                //BluetoothDevice object를 Intent에서 얻어와라
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //배열에 기기 이름이랑 맥주소 저장
                scannedDeviceName[count] = device.getName();
                scannedDeviceAddress[count] = device.getAddress();
                count++;
            }//검색이 끝나면
            else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                if(count==0){
                    //발견된 기기가 없다
                    textView_result.setText("발견된 기기가 없음");
                }else{
                    for(int i=0;i<count;i++){
                        Log.d(TAG,"결과 출력 포문");
                        Log.d(TAG,"이름  =    "+scannedDeviceName[i]);
                        Log.d(TAG,"주소  =    "+scannedDeviceAddress[i]);
                        //textView_result.setText(i+"번째"+scannedDevice[i]+"\n");
                        if(scannedDeviceAddress[i].equals(seekerMacAddress)){
                            textView_result.setText(i+"번째 기기가 술래"+scannedDeviceName[i]+" "+scannedDeviceAddress[i]);
                        }
                    }
                }
                count =0;
                Toast.makeText(getApplicationContext(),"검색 끝",Toast.LENGTH_LONG).show();
            }
        }
    };
}
