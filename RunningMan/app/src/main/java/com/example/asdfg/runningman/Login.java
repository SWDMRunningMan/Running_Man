package com.example.asdfg.runningman;

import android.content.Intent;
import android.os.StrictMode;
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
    int ID=-1;
    DataOutputStream outstream;
    DataInputStream instream;
    protected static String ip = "192.168.55.4";
    int port = 7777;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
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
                try {
                    if(ID==-1){
                        outstream.writeUTF("-1");
                    }else{
                        outstream.writeUTF("-1 "+String.valueOf(ID));
                    }
                    outstream.flush();
                    ID=instream.readInt();
                    outstream.writeUTF("0 "+name );
                    outstream.flush();
                    outstream.close();
                    instream.close();
                    sock.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }

                intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("loginID",name);
                intent.putExtra("code",ID);
                startActivity(intent);
                finish();
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