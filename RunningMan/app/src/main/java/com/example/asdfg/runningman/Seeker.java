package com.example.asdfg.runningman;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.style.TabStopSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.nio.charset.Charset;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
public class Seeker extends AppCompatActivity implements CreateNdefMessageCallback{
    Socket sock;
    int ID=-1;
    DataOutputStream outstream;
    DataInputStream instream;
    protected static String ip = "192.168.0.19";
    int port = 7777;
    PopupWindow window;
    Intent intent;
    String userName;
    TableLayout table111;
    LinearLayout linearLayout;
    TextView text1;
    Button nameTag;
    View temp,temp2;
    ImageButton[] tempImage = new ImageButton[10];
    ImageButton bigSize;
    public static final int TYPE_TEXT=1;
    private NfcAdapter mNfcAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker);
        /*게임진행
           걸음수 기록
           중간에 사진 불러오기
           유저눌렀을때 유저에게 메세지 전달
           확인받으면 UI변경
           시간종료시 종료
         */
        table111 = (TableLayout)findViewById(R.id.table111);
        text1=findViewById(R.id.text1);

      // text1.setText("술래 이름");

        for(int i=0;i<(6+1)/2;i++) { // i<(Integer.valueOf(hiderNum)+1)/2
            TableRow tempRow = new TableRow(Seeker.this);
            tempRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            for (int j = 0; j < 2; j++) {
                linearLayout = new LinearLayout(tempRow.getContext());
                linearLayout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                nameTag = new Button(this);
                nameTag.setText("hider : ?"); // (i*2+j) index의 hider 이름
                nameTag.setLayoutParams(new TableRow.LayoutParams(450, 150));
                nameTag.setGravity(Gravity.CENTER);
                nameTag.setTextSize(20);
                nameTag.setBackgroundColor(Color.argb(0,255,255,255));
                nameTag.setTypeface(null, Typeface.BOLD_ITALIC);
                nameTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder dd = new AlertDialog.Builder(Seeker.this);
                        dd.setTitle("신고");
                        dd.setMessage(nameTag.getText().toString()+ "님을 신고하시겠습니까?"); //
                        dd.setNegativeButton("아니오",null);
                        dd.setPositiveButton("예",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog,int whichButton){
                                // 신고하면 유저들에게 뿌려주기
                            }
                        });

                        dd.show();
                    }
                });

                temp = new View(this);
                temp.setBackgroundColor(Color.argb(255, 255, 255, 255));
                temp.setLayoutParams(new ViewGroup.LayoutParams(450, 10));
                temp2 = new View(this);
                temp2.setBackgroundColor(Color.argb(255, 255, 255, 255));
                temp2.setLayoutParams(new ViewGroup.LayoutParams(450, 10));

                tempImage[i*2+j] = new ImageButton(this);
                tempImage[i*2+j].setBackgroundColor(Color.argb(0,255,255,255));
                tempImage[i*2+j].setLayoutParams(new ViewGroup.LayoutParams(450, 500));
                //(i*2+j) index hider의 사진 get
               final Drawable myImage = getResources().getDrawable(R.drawable.empty); // 처음엔 empty 나중엔 유저의사진
                tempImage[i*2+j].setImageDrawable(myImage);
                tempImage[i*2+j].setScaleType(ImageView.ScaleType.FIT_XY);
                tempImage[i*2+j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final View popupView = getLayoutInflater().inflate(R.layout.biggersize, null);
                        window = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                        window.setAnimationStyle(-1);
                        window.setFocusable(true);
                        bigSize = popupView.findViewById(R.id.p1ID);
                        bigSize.setImageDrawable(myImage);
                        bigSize.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                window.dismiss();
                            }
                        });
                        window.update();
                        window.showAsDropDown(table111, 20, 200);
                        // 사진을 유저껄로 바꿔야함
                    }
                });

                linearLayout.addView(nameTag);
                linearLayout.addView(temp);
                linearLayout.addView(tempImage[i*2+j]);
                linearLayout.addView(temp2);
                linearLayout.setGravity(Gravity.CENTER);
                tempRow.addView(linearLayout);
            }
            table111.addView(tempRow);
        }
      /*  StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        try {
            sock= new Socket(ip, port);
            outstream = new DataOutputStream(sock.getOutputStream());
            instream = new DataInputStream(sock.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent getIntent = getIntent();
        userName = getIntent.getStringExtra("loginID");
        ID=getIntent.getIntExtra("code",-1);
        try {
            if (ID == -1) {
                outstream.writeUTF("-1");
            } else {
                outstream.writeUTF("-1 " + String.valueOf(ID));
            }
            outstream.flush();
            ID = instream.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent1=new Intent(getApplicationContext(),GameOver.class);
        intent1.putExtra("userName",userName);
        intent1.putExtra("code",ID);
        startActivity(intent1);*/

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mNfcAdapter.setNdefPushMessageCallback(this,this);
    }

    public NdefMessage createNdefMessage(NfcEvent event){
        String text="맥 주소는 뭐야";
        NdefMessage msg = new NdefMessage(new NdefRecord[]{
                createMimeRecord("mac address",text.getBytes())});
        return msg;
    }
    public NdefRecord createMimeRecord(String mimeType,byte[] payload){
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));

        NdefRecord mimeRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes,new byte[0],payload);
        return mimeRecord;
    }
}