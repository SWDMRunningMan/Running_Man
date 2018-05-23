package com.example.asdfg.runningman;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import java.io.File;
import java.io.IOException;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Hider extends AppCompatActivity {
    ImageButton hider11,hider22;
    Integer min=5,sec=0,count=0,chance;
    TextView restTime;
    Button button;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hider);

        Toast.makeText(getApplicationContext(),"술래가 주변에 있습니다!",Toast.LENGTH_LONG).show();
        handler = new Handler();
        hider11=findViewById(R.id.hider11Image);
        hider22=findViewById(R.id.hider22Image);
        restTime = findViewById(R.id.restTime);
        Intent getIntent = getIntent();
        min = Integer.parseInt(getIntent.getStringExtra("time").toString());
        chance = Integer.parseInt(getIntent.getStringExtra("chance").toString());
        MyThread dd = new MyThread();
        dd.start();

        //블루투스 술래 주변에 있으면
        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        button = findViewById(R.id.takePicture);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture,1);
            }
        });
        hider22.setOnClickListener(new View.OnClickListener() {  // 술래가 화면 클릭하면 서버 통해서 무슨 신호 보내서 유저 화면에서 팝업창띄워야함 // 지금 이 버튼 말고
            @Override
            public void onClick(View view) {
             AlertDialog.Builder dd = new AlertDialog.Builder(Hider.this);
             dd.setTitle("경고");
             dd.setMessage("Yea 님에게 잡히셨습니까?");
             dd.setPositiveButton("예",new DialogInterface.OnClickListener(){
                 public void onClick(DialogInterface dialog,int whichButton){
                    //서버에 잡혔다는 신호보내기
                     hider11.setColorFilter(Color.BLACK);
                 }
             });
                dd.setNegativeButton("아니오",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int whichButton){
                        //서버에 안잡혔다는 신호보내기
                    }
                });
                dd.show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // data.getData()서버에전달 hider11.setImageURI(data.getData()); // 작동이안되네
    }
    /*게임진행
        걸음수 기록
        중간에 사진 보내기
        메세지 받았을 때 사진 찍어서 보내기
        확인누르면 UI변경
        시간종료시 종료
      */
    //thread만들어서 남은시간 줄이고 찬스주기20초동안 보여주기
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
                    count++;
               }catch(Exception ex){}
               handler.post(new Runnable(){
                   public void run(){
                      restTime.setText("남은 시간: " + min + "분 " + sec +"초" );
                       if(count>=chance && (count%chance)<=20)
                       {
                           button.setVisibility(VISIBLE);
                           button.setText(20-(count%chance)+"초안에 사진을 보내세요");
                       }
                       else
                           button.setVisibility(GONE);
                   }
               });
           }
       }
   }
}