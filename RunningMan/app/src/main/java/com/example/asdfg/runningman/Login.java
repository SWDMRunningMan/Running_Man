package com.example.asdfg.runningman;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Login extends AppCompatActivity {
    EditText loginID;
    Button enterBtn,exitBtn;
    Intent intent;
    Socket sock;
    String ID="8C:8E:F2:33:B7:09";
    BluetoothAdapter btAdapter;
    DataOutputStream outstream;
    DataInputStream instream;
    protected static String ip = "192.168.55.4";
    int port = 7777;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
             ID = android.provider.Settings.Secure.getString(this.getContentResolver(), "bluetooth_address");//자신의 맥주소
        }catch (Exception e){
           // ID="8C:8E:F2:33:B7:09";
        }

        try {
            sock= new Socket(ip, port);
            outstream = new DataOutputStream(sock.getOutputStream());
            instream = new DataInputStream(sock.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        loginID=findViewById(R.id.loginID);
        enterBtn=findViewById(R.id.enterBtn);
        exitBtn=findViewById(R.id.exitBtn);

        enterBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //room
                String name = loginID.getText().toString();
                if(name.length()>=1) {
                    try {
                        outstream.writeUTF("-1 " + ID);
                        outstream.flush();
                        ID = instream.readUTF();
                        outstream.writeUTF("0 " + name);
                        outstream.flush();
                        outstream.close();
                        instream.close();
                        sock.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("loginID", name);
                    intent.putExtra("code", ID);
                    startActivity(intent);
                    finish();
                }
                else{
                    AlertDialog.Builder dd = new AlertDialog.Builder(Login.this);
                    dd.setTitle("경고");
                    dd.setMessage("아이디가 없습니다.");
                    dd.setPositiveButton("다시 만들기",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog,int whichButton){
                            //서버에 잡혔다는 신호보내기
                        }
                    });
                    dd.show();
                }
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                try {
                    instream.close();
                    outstream.close();
                    sock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                finish();
            }
        });

    }

}
