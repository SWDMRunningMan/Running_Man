package com.runningman.asdfg.runningman;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends Activity {
    Button btn1, btn2, btn3, makeRoom, cancel;
    Intent intent, intent1, intent2;
    String userName, roomList, roomName;
    RadioGroup time, chance;
    EditText editText, roomNameText, playerNum, seekerNum;
    PopupWindow window;
    Socket sock;
    TableRow tableRow;
    TableLayout table;
    TextView no, rn, player;
    View temp;
    int size, index;
    int i;
    String ID;
    DataOutputStream outstream;
    DataInputStream instream;
    protected static String ip = "192.168.0.19";
    int port = 7777;
    ArrayList <String> roomlist;
    ArrayList <Integer> roomID, n, m;
    Handler handler;
    int next=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // textView = findViewById(R.id.roomList);
        // roomList= 방 리스트 어디서 갖고오지?
        // textView.setText(roomList);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        try {
            sock = new Socket(ip, port);
            outstream = new DataOutputStream(sock.getOutputStream());
            instream = new DataInputStream(sock.getInputStream());

            //editText = findViewById(R.id.searchRoomName);
            //roomName = editText.getText().toString();

            intent = getIntent(); // login한 닉네임
            userName = intent.getStringExtra("loginID");
            ID = intent.getStringExtra("code");
            btn1 = findViewById(R.id.refreshBtn); // 방검색하기
            btn2 = findViewById(R.id.roomMakeBtn); //방만들기
            btn3 = findViewById(R.id.roomEnterBtn); //방접속하기
            table = findViewById(R.id.table);

            outstream.writeUTF("-1 " + ID);
            outstream.flush();
            ID = instream.readUTF();
            outstream.writeUTF("200");
            outstream.flush();
            size = instream.readInt();
            Log.v("M", String.valueOf(size));
            roomlist =new ArrayList <String>();
            roomID = new ArrayList <Integer>();
            n = new ArrayList <Integer>();
            m = new ArrayList <Integer>();
            handler = new Handler();
            final MyThread thread = new MyThread(sock);
            thread.start();
            for (i = 0; i < size; i++) {
                String[] str = instream.readUTF().split(" ");
                Log.v("M", str[0] + " " + str[1] + " " + str[2] + " " + str[3]);
                roomID.add(Integer.valueOf(str[0]));
                roomlist.add(str[1]);
                n.add(Integer.valueOf(str[2]));
                m.add(Integer.valueOf(str[3]));
            }
            for (i = 0; i < size; i++) {
                final int num=i;
                tableRow = new TableRow(MainActivity.this);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                no = new TextView(MainActivity.this);
                rn = new TextView(MainActivity.this);
                player = new TextView(MainActivity.this);
                tableRow.setBackgroundColor(Color.argb(170, 255, 255, 255));
                no.setTextColor(Color.parseColor("#ff000000"));
                no.setPaintFlags(no.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                no.setHeight(90);
                no.setGravity(Gravity.CENTER);
                no.setPadding(1, 1, 1, 1);
                no.setText(String.valueOf(i + 1));
                rn.setTextColor(Color.parseColor("#ff000000"));
                rn.setPaintFlags(no.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                rn.setHeight(90);
                rn.setGravity(Gravity.CENTER);
                rn.setPadding(1, 1, 1, 1);
                rn.setText(roomlist.get(i));
                player.setTextColor(Color.parseColor("#ff000000"));
                player.setPaintFlags(no.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                player.setHeight(90);
                player.setGravity(Gravity.CENTER);
                player.setPadding(1, 1, 1, 1);
                player.setText(n.get(i) + "/" + m.get(i));

                temp = new View(MainActivity.this);
                temp.setBackgroundColor(Color.parseColor("#44000000"));
                temp.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));

                tableRow.addView(no);
                tableRow.addView(rn);
                tableRow.addView(player);
                tableRow.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        index = num;
                    }
                });
                table.addView(tableRow);
                table.addView(temp);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {       // 새로고침

                // 새로고침

            }

        });
        btn2.setOnClickListener(new View.OnClickListener() {      // 방만들기
            @Override
            public void onClick(View v) {                   // 방만들기
                final View popupView = getLayoutInflater().inflate(R.layout.popupwindow, null);
                window = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                window.setAnimationStyle(-1);
                window.setFocusable(true);
                window.update();
                window.showAsDropDown(findViewById(R.id.dropdown), 20, 400);
                roomNameText = popupView.findViewById(R.id.roomNameText);
                playerNum = popupView.findViewById(R.id.playerNum);
                seekerNum = popupView.findViewById(R.id.seekerNum);
                time = popupView.findViewById(R.id.timeGroup);
                chance = popupView.findViewById(R.id.chanceGroup);
                makeRoom = popupView.findViewById(R.id.makeRoom);
                cancel = popupView.findViewById(R.id.cancel);

                makeRoom.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String room = roomNameText.getText().toString();
                        String pNum = playerNum.getText().toString();
                        String sNum = seekerNum.getText().toString();
                        int timeID = time.getCheckedRadioButtonId();
                        int chanceID = chance.getCheckedRadioButtonId();
                        if (room.length() < 1)
                            Toast.makeText(MainActivity.this, "방 제목을 입력하세요", Toast.LENGTH_SHORT).show();
                        else if (pNum.length() == 0)
                            Toast.makeText(MainActivity.this, "참여 인원을 입력하세요", Toast.LENGTH_SHORT).show();
                        else if (sNum.length() == 0)
                            Toast.makeText(MainActivity.this, "술래 인원을 입력하세요", Toast.LENGTH_SHORT).show();
                        else if (Integer.parseInt(pNum) <= Integer.parseInt(sNum))
                            Toast.makeText(MainActivity.this, "술래 인원이 더 많을 수 없습니다", Toast.LENGTH_SHORT).show();
                        else if (timeID != R.id.min5 && timeID != R.id.min10 && timeID != R.id.min15)
                            Toast.makeText(MainActivity.this, "플레이 시간을 입력하세요", Toast.LENGTH_SHORT).show();
                        else if (chanceID != R.id.sec30 && chanceID != R.id.sec60 && chanceID != R.id.sec120)
                            Toast.makeText(MainActivity.this, "찬스 주기를 입력하세요", Toast.LENGTH_SHORT).show();
                        else {

                            intent1 = new Intent(getApplicationContext(), Room.class);
                            intent1.putExtra("userName", userName);
                            intent1.putExtra("roomName", room);
                            intent1.putExtra("playerNum", pNum);
                            intent1.putExtra("seekerNum", sNum);
                            int t = 5;
                            int c = 0;
                            if (timeID == R.id.min5) {
                                intent1.putExtra("time", "5");
                                t = 5;
                            }
                            if (timeID == R.id.min10) {
                                intent1.putExtra("time", "10");
                                t = 10;
                            }
                            if (timeID == R.id.min15) {
                                intent1.putExtra("time", "15");
                            }

                            if (chanceID == R.id.sec30) {
                                intent1.putExtra("chance", "30");
                                c = 30;
                            }
                            if (chanceID == R.id.sec60) {
                                intent1.putExtra("chance", "60");
                                c = 60;
                            }
                            if (chanceID == R.id.sec120) {
                                intent1.putExtra("chance", "120");
                                c = 120;
                            }
                            intent1.putExtra("userName", userName);
                            intent1.putExtra("code", ID);
                            intent1.putExtra("owner", ID);
                            try {

                                Date d = new Date();
                                int i = (int) (d.getTime() % 1000000000);
                                intent1.putExtra("rID", String.valueOf(i));
                                outstream.writeUTF("1 " + String.valueOf(ID) + " " + userName + " " + String.valueOf(i) + " " + room + " " + pNum + " " + sNum + " " + String.valueOf(t) + " " + String.valueOf(c));
                                outstream.flush();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            next=1;
                            startActivity(intent1);
                            finish();
                            window.dismiss();
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        window.dismiss();
                    }
                });
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {      // 방 접속하기
            public void onClick(View v) {                  // 방접속
                try {
                    outstream.writeUTF("104 "+roomID.get(index)+ " "+ID+" "+userName);
                    outstream.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    protected void onDestroy(){
        Intent intent=new Intent(getApplicationContext(),Login.class);
        if(next==0)
            startActivity(intent);
        super.onDestroy();
        try {
            outstream.close();
            instream.close();
            sock.close();

        } catch (IOException e) {
        }
    }
    class MyThread extends Thread {
        Socket socket;
        DataOutputStream outstream;
        DataInputStream instream;

        public MyThread(Socket sc) {
            socket = sc;
        }

        public void run() {
            try {
                outstream = new DataOutputStream(socket.getOutputStream());
                instream = new DataInputStream(socket.getInputStream());
            } catch (Exception e) {
            }
            while (true) {
                try {
                    int I = instream.readInt();
                    if(I ==-1){
                        Toast.makeText(getApplicationContext(), "자리가 꽉 찼습니다.", Toast.LENGTH_SHORT).show();
                    }else if (I ==1){ // 접속
                        intent1 = new Intent(getApplicationContext(), Room.class);
                        String owner=instream.readUTF();
                        int pNum=instream.readInt();
                        int sNum=instream.readInt();
                        int time=instream.readInt();
                        int chance=instream.readInt();
                        intent1.putExtra("userName", userName);
                        intent1.putExtra("roomName", roomlist.get(index));
                        intent1.putExtra("playerNum", String.valueOf(pNum));
                        intent1.putExtra("seekerNum", String.valueOf(sNum));
                        intent1.putExtra("time", time);
                        intent1.putExtra("chance", chance);
                        intent1.putExtra("code", ID);
                        intent1.putExtra("owner", owner);
                        intent1.putExtra("rID", String.valueOf(roomID.get(index)));
                        next=1;
                        startActivity(intent1);
                        finish();
                    }
                    if (I == 11) { // 새로고침
                        outstream.writeUTF("200");
                        outstream.flush();
                        size = instream.readInt();
                        Log.v("M", String.valueOf(size));
                        roomlist.clear();
                        roomID.clear();
                        n .clear();
                        m .clear();
                        for (i = 0; i < size; i++) {
                            String[] str = instream.readUTF().split(" ");
                            Log.v("M", str[0] + " " + str[1] + " " + str[2] + " " + str[3]);
                            roomID.add(Integer.valueOf(str[0]));
                            roomlist.add(str[1]);
                            n.add(Integer.valueOf(str[2]));
                            m.add(Integer.valueOf(str[3]));
                        }
                        handler.post(new Runnable() {
                            public void run() {
                                Log.v("M", "run");
                                table.removeAllViews();
                                for (i = 0; i < size; i++) {
                                    final int num=i;
                                    tableRow = new TableRow(MainActivity.this);
                                    tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    no = new TextView(MainActivity.this);
                                    rn = new TextView(MainActivity.this);
                                    player = new TextView(MainActivity.this);
                                    tableRow.setBackgroundColor(Color.argb(170, 255, 255, 255));
                                    no.setTextColor(Color.parseColor("#ff000000"));
                                    no.setPaintFlags(no.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                                    no.setHeight(90);
                                    no.setGravity(Gravity.CENTER);
                                    no.setPadding(1, 1, 1, 1);
                                    no.setText(String.valueOf(i + 1));
                                    rn.setTextColor(Color.parseColor("#ff000000"));
                                    rn.setPaintFlags(no.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                                    rn.setHeight(90);
                                    rn.setGravity(Gravity.CENTER);
                                    rn.setPadding(1, 1, 1, 1);
                                    rn.setText(roomlist.get(i));
                                    player.setTextColor(Color.parseColor("#ff000000"));
                                    player.setPaintFlags(no.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                                    player.setHeight(90);
                                    player.setGravity(Gravity.CENTER);
                                    player.setPadding(1, 1, 1, 1);
                                    player.setText(n.get(i) + "/" + m.get(i));

                                    temp = new View(MainActivity.this);
                                    temp.setBackgroundColor(Color.parseColor("#44000000"));
                                    temp.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));

                                    tableRow.addView(no);
                                    tableRow.addView(rn);
                                    tableRow.addView(player);
                                    tableRow.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View view) {
                                            index = num;
                                        }
                                    });
                                    table.addView(tableRow);
                                    table.addView(temp);

                                }
                            }
                        });
                    }
                } catch (Exception e) {
                }

            }
        }
    }
}