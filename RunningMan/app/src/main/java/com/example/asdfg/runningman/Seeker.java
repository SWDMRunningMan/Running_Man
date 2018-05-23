package com.example.asdfg.runningman;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class Seeker extends AppCompatActivity {
ImageButton p1,p2,p3,p4,close;
Button c1,c2,c3,c4;
TextView leftTime;
Integer min=5,sec=0;
    PopupWindow window;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker);

        p1=findViewById(R.id.hider1Image);
        p2=findViewById(R.id.hider2Image);
        p3=findViewById(R.id.hider3Image);
        p4=findViewById(R.id.hider4Image);

        c1=findViewById(R.id.catch1);
        c2=findViewById(R.id.catch2);
        c3=findViewById(R.id.catch3);
        c4=findViewById(R.id.catch4);
        leftTime=findViewById(R.id.leftTime);
        Intent getIntent = getIntent();
        min = Integer.parseInt(getIntent.getStringExtra("time").toString());

        leftTime.setText("남은 시간: " + min + "분 " + sec +"초" );
        MyThread my = new MyThread();
        my.start();
        p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {   // 화면확대
                final View popupView = getLayoutInflater().inflate(R.layout.biggersize,null);
                window = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                window.setAnimationStyle(-1);
                window.setFocusable(true);
                window.update();
                window.showAsDropDown(p1,40,-500);

                close=popupView.findViewById(R.id.p1ID);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        window.dismiss();
                    }
                });
            }
        });
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dd = new AlertDialog.Builder(Seeker.this);
                dd.setTitle("경고");
                dd.setMessage("Hong님을 잡으셨습니까?");
                dd.setPositiveButton("예",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int whichButton){
                        // 서버에 누구 잡았다는 신호 보내기
                        // Hong화면에 dialog 띄워줘야함
                        p1.setColorFilter(Color.BLACK);
                    }
                });
                dd.setNegativeButton("아니오",null);
                dd.show();
            }
        });
        /*게임진행
           걸음수 기록
           중간에 사진 불러오기
           유저눌렀을때 유저에게 메세지 전달
           확인받으면 UI변경
           남은시간 업데이트
           시간종료시 종료
         */
    }
    class MyThread extends Thread{
        public void run(){
            while(min!=0 || sec!=0){
                try{
                    Thread.sleep(1000);
                    if(sec!=0)
                        sec--;
                    else{
                        min--;
                        sec=59;
                    }

                }catch(Exception ex){}
                handler.post(new Runnable(){
                    public void run(){
                        leftTime.setText("남은 시간: " + min + "분 " + sec +"초" );
                    }
                });
            }
        }
    }
}