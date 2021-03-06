package com.runningman.asdfg.runningman;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class Room extends AppCompatActivity {
    TableLayout seekerTable, hiderTable;
    Button btn1, btn2, btn3, btn4;
    TextView text1, text2;
    Intent intent1, intent2;
    Socket sock;
    String ID;
    int rID=-1;
    DataOutputStream outstream;
    DataInputStream instream;
    protected static String ip = "192.168.0.19";
    int port = 7777;
    String userName;
    String RoomName ;
    int playerNum ;
    int seekerNum ;
    String time ;
    String chance ;
    String owner;
    Handler handler;
    TableRow tableRow;
    TextView empty;
    View temp;
    int toggle=0;
    int toggle2=0;
    ArrayList<String> user=new  ArrayList<>();
    ArrayList<String> userid=new  ArrayList<>();
    ArrayList<Integer> feet=new  ArrayList<>();
    ArrayList<String> seeker=new  ArrayList<>();
    ArrayList<String> hider=new  ArrayList<>();
    ArrayList<String> seeker2=new  ArrayList<>();
    ArrayList<String> hider2=new  ArrayList<>();
    int num,num_S,num_H;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        try {
            sock = new Socket(ip, port);
            outstream = new DataOutputStream(sock.getOutputStream());
            instream = new DataInputStream(sock.getInputStream());
            Intent getIntent = getIntent();
            userName = getIntent.getStringExtra("userName");
            ID = getIntent.getStringExtra("code");
            RoomName = getIntent.getStringExtra("roomName");
            playerNum = Integer.parseInt(getIntent.getStringExtra("playerNum"));
            seekerNum = Integer.parseInt(getIntent.getStringExtra("seekerNum"));
            time = getIntent.getStringExtra("time");
            chance = getIntent.getStringExtra("chance");
            owner= getIntent.getStringExtra("owner");
            rID= Integer.parseInt(getIntent.getStringExtra("rID"));
            btn1 = findViewById(R.id.seekerButton);//seeker
            btn2 = findViewById(R.id.hiderButton);//hider
            btn3 = findViewById(R.id.startGame);//start
            btn4 = findViewById(R.id.roomExit);
            text1 = findViewById(R.id.text1);//seeker목록
            text2 = findViewById(R.id.text2);//hider목록
            seekerTable = findViewById(R.id.seekerTable);
            hiderTable = findViewById(R.id.hiderTable);
            intent1 = new Intent(getApplicationContext(), Seeker.class);
            intent2 = new Intent(getApplicationContext(), Hider.class);
            handler = new Handler();
        /*
            서버에서 방정보(유저목록, 누가 어디에 있는지)
            가져오고 자신을 자동으로 seeker나 hider에 위치 시키고 서버로 전송
         */
            outstream.writeUTF("-1 "+ID+" " +rID);
            outstream.flush();
            ID = instream.readUTF();
            if(owner.equals(ID)){
                btn3.setVisibility(View.VISIBLE);
            }

            final MyThread thread=new MyThread(sock);
            thread.start();
        } catch (Exception e) {
        }

        btn1.setOnClickListener(new View.OnClickListener() { // seeker쪽으로 유저 이동
            @Override
            public void onClick(View v) {
                //text1목록에 유저추가
                //갱신
                if(toggle==1) {
                    try {
                        Log.v("M", "101");
                        outstream.writeUTF("101 " + String.valueOf(rID) + " " + String.valueOf(ID) + " " + userName);
                        outstream.flush();
                        Log.v("M", "100");
                        outstream.writeUTF("100 " + String.valueOf(rID) + " " + String.valueOf(ID)+ " " + userName);
                        outstream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    toggle = 0;
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() { // hider쪽으로 유저 이동
            @Override
            public void onClick(View v) {
                //text2목록에 유저추가
                //갱신
                if(toggle==0) {
                    try {
                        Log.v("M", "102");
                        outstream.writeUTF("102 " + String.valueOf(rID) + " " + String.valueOf(ID)+ " " + userName);
                        outstream.flush();
                        Log.v("M", "100");
                        outstream.writeUTF("100 " + String.valueOf(rID) + " " + String.valueOf(ID)+ " " + userName);
                        outstream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    toggle = 1;
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle2=1;
                try {
                    outstream.writeUTF("300 " +String.valueOf(rID));
                    outstream.flush();
                } catch (IOException e) {
                }

            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.v("M", "103");
                    outstream.writeUTF("103 " + String.valueOf(rID) + " " + String.valueOf(ID));
                    outstream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        /*
         * thread로 유저목록 실시간으로 불러오기
         */

    }
    protected void onDestroy(){
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("loginID", userName );
        intent.putExtra("code", ID);
       // startActivity(intent);
        super.onDestroy();
        if(toggle2!=1){
            try {
                Log.v("M", "103");
                outstream.writeUTF("103 " + String.valueOf(rID) + " " + String.valueOf(ID));
                outstream.flush();
                outstream.close();
                instream.close();
                sock.close();
            } catch (IOException e) {
            }
        }
    }
    class MyThread extends Thread {
        Socket socket;
        DataOutputStream outstream;
        DataInputStream instream;

        MyThread(Socket sc) {
            socket=sc;
        }
        public void run() {
            try {
                outstream = new DataOutputStream(socket.getOutputStream());
                instream = new DataInputStream(socket.getInputStream());
                outstream.writeUTF("100 " + String.valueOf(rID) + " " + ID);
                outstream.flush();
            } catch (Exception e) {
            }
            while (true) {
                try {
                    int m = instream.readInt();
                    if (m == 100) {
                        user.clear();
                        userid.clear();
                        feet.clear();
                        seeker.clear();
                        hider.clear();
                        String msg;
                        String str[];
                        msg = instream.readUTF();
                        str = msg.split(" ");
                        num = Integer.valueOf(str[0]);
                        num_S = Integer.valueOf(str[1]);
                        num_H = Integer.valueOf(str[2]);
                        time = str[3];
                        chance = str[4];

                        for (int j = 0; j < num; j++) {
                            msg = instream.readUTF();
                            str = msg.split(" ");
                            user.add(str[0]);
                            userid.add(str[1]);
                            feet.add(Integer.valueOf(str[2]));
                        }
                        for (int j = 0; j < num_S; j++) {
                            msg = instream.readUTF();
                            String temp[] = msg.split(" ");
                            seeker.add(temp[0]);
                            seeker2.add(temp[1]);
                            //seeker2.add(msg);
                        }
                        for (int j = 0; j < num_H; j++) {
                            msg = instream.readUTF();
                            String temp[] = msg.split(" ");
                            hider.add(temp[0]);
                            hider2.add(temp[1]);
                            //hider2.add(msg);
                        }
                        handler.post(new Runnable() {
                            public void run() {
                                Log.v("M", "run");
                                seekerTable.removeAllViews();
                                hiderTable.removeAllViews();
                                for (int i = 0; i < seekerNum; i++) {
                                    tableRow = new TableRow(Room.this);
                                    tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                                    empty = new TextView(Room.this);
                                    if (i < num_S) {
                                        String N = null;
                                        Log.v("M", num + " " + num_S);
                                        for (int j = 0; j < num; j++) {

                                            if (userid.get(j).equals(seeker.get(i))) {
                                                N = user.get(j);

                                                break;
                                            }
                                        }
                                        empty.setText(N);
                                    } else
                                        empty.setText("empty");
                                    empty.setTextSize(15);
                                    empty.setPadding(5, 7, 5, 7);
                                    empty.setGravity(Gravity.CENTER);
                                    empty.setBackgroundColor(Color.argb(176, 255, 255, 255));
                                    empty.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    temp = new View(Room.this);
                                    temp.setBackgroundColor(Color.argb(255, 255, 255, 255));
                                    temp.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
                                    tableRow.addView(empty);
                                    seekerTable.addView(tableRow);
                                    seekerTable.addView(temp);
                                }
                                for (int i = 0; i < playerNum - seekerNum; i++) {
                                    tableRow = new TableRow(Room.this);
                                    tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                                    empty = new TextView(Room.this);
                                    if (i < num_H) {
                                        String N = null;
                                        for (int j = 0; j < num; j++) {
                                            if (userid.get(j).equals(hider.get(i))) {
                                                N = user.get(j);
                                                break;
                                            }
                                        }
                                        empty.setText(N);
                                    } else
                                        empty.setText("empty");
                                    empty.setTextSize(15);
                                    empty.setPadding(5, 7, 5, 7);
                                    empty.setGravity(Gravity.CENTER);
                                    empty.setBackgroundColor(Color.argb(176, 255, 255, 255));
                                    empty.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    temp = new View(Room.this);
                                    temp.setBackgroundColor(Color.argb(255, 255, 255, 255));
                                    temp.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
                                    tableRow.addView(empty);
                                    hiderTable.addView(tableRow);
                                    hiderTable.addView(temp);
                                }
                            }
                        });
                    }
                    if (m == 300) {
                        toggle2 = 1;
                        if (toggle == 0) {    // *seeker인 경우
                            intent1.putExtra("ID", ID);
                            intent1.putExtra("rID", String.valueOf(rID));
                            intent1.putExtra("num_S", String.valueOf(num_S));
                            intent1.putExtra("num_H", String.valueOf(num_H));
                            intent1.putExtra("time", time);
                            intent1.putExtra("chance", chance);
                            intent1.putExtra("user", user);
                            intent1.putExtra("userid", userid);
                            intent1.putExtra("seeker", seeker);
                            intent1.putExtra("seeker2",seeker2);
                            intent1.putExtra("hider", hider);
                            intent1.putExtra("hider2",hider2);
                            startActivity(intent1);
                            finish();
                        }
                        if (toggle == 1) {    ///*hider인 경우
                            intent2.putExtra("ID", ID);
                            intent2.putExtra("rID", String.valueOf(rID));
                            intent2.putExtra("num_S", String.valueOf(num_S));
                            intent2.putExtra("num_H", String.valueOf(num_H));
                            intent2.putExtra("time", time);
                            intent2.putExtra("chance", chance);
                            intent2.putExtra("user", user);
                            intent2.putExtra("userid", userid);
                            intent2.putExtra("seeker", seeker);
                            intent2.putExtra("seeker2",seeker2);
                            intent2.putExtra("hider", hider);
                            intent2.putExtra("hider2",hider2);
                            startActivity(intent2);
                            finish();
                        }
                    }
                } catch (Exception e) {
                }
            }

        }
    }
}