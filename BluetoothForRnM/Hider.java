package com.example.geonsu.runningman;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Hider extends AppCompatActivity {
    Socket sock;
    String ID;
    DataOutputStream outstream;
    DataInputStream instream;
    protected static String ip = "192.9.88.141";
    int port = 7777;
    Intent intent;
    String userName;
    ImageButton hider11,hider22,hider33,hider44;
    Integer min=5,sec=0,count=0,chance;
    TextView restTime,text11;
    Button button,nameTag;
    Handler handler;
    int alive=4;    // Integer.valueOf(hiderNum);
    ScrollView scrollView;
    TableLayout tableLayout;
    LinearLayout linearLayout;
    ImageButton[] tempImage = new ImageButton[10];
    View temp,temp2;
    private String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hider);
        // 받은 인텐트의 인스턴스를 취득한다
        Intent intent = getIntent();
        // 인텐트 액션이 ACTION_NDEF_DISCOVERED인 경우에 실행한다
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            // 받은 인텐트에서 Ndef 메시지를 취득한다
            Parcelable[] rawMsgs = intent
                    .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            // Android Beam에서는 한 번에 한 개의 메시지만 송수신 가능
            NdefMessage msg = (NdefMessage) rawMsgs[0];

            // 첫 번째 레코드에 MIME데이터가 포함된다
            String text = new String(msg.getRecords()[0].getPayload());
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
        /*게임진행
           걸음수 기록
           중간에 사진 보내기
           메세지 받았을 때 사진 찍어서 보내기
           확인누르면 UI변경
           시간종료시 종료
         */
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        try {
            sock= new Socket(ip, port);
            outstream = new DataOutputStream(sock.getOutputStream());
            instream = new DataInputStream(sock.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        intent = getIntent(); // login한 닉네임
        userName = intent.getStringExtra("loginID");
        ID=intent.getStringExtra("ID");

        try {
            outstream.writeUTF("-1 "+String.valueOf(ID));
            outstream.flush();
            ID=instream.readUTF();
        } catch(Exception e) {
            e.printStackTrace();
        }
       /*if(블루투스 연결시) 블루투스 술래 주변에 있으면
        Toast.makeText(getApplicationContext(),"술래가 주변에 있습니다!",Toast.LENGTH_LONG).show(); // 블루투스연결
        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);*/

        text11=findViewById(R.id.text11);
        //text11.setText(술래이름);
        handler = new Handler();

        tableLayout = (TableLayout)findViewById(R.id.table11);
        for(int i=0;i<(6+1)/2;i++) { // i<(Integer.valueOf(hiderNum)+1)/2
            //   Toast.makeText(getApplicationContext(),"asd",Toast.LENGTH_SHORT).show();
            TableRow tempRow = new TableRow(Hider.this);
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
                nameTag.setTypeface(null, Typeface.BOLD_ITALIC);
                nameTag.setBackgroundColor(Color.argb(0,255,255,255));
                nameTag.setTypeface(null, Typeface.BOLD_ITALIC);
                nameTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder dd = new AlertDialog.Builder(Hider.this);
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
                Drawable myImage = getResources().getDrawable(R.drawable.empty);
                tempImage[i*2+j].setImageDrawable(myImage);
                tempImage[i*2+j].setScaleType(ImageView.ScaleType.FIT_XY);
                linearLayout.addView(nameTag);
                linearLayout.addView(temp);
                linearLayout.addView(tempImage[i*2+j]);
                linearLayout.addView(temp2);
                linearLayout.setGravity(Gravity.CENTER);
                tempRow.addView(linearLayout);
            }
            tableLayout.addView(tempRow);
        }

        restTime = findViewById(R.id.restTime);

        //     Intent getIntent = getIntent();
//        min = Integer.parseInt(getIntent.getStringExtra("time").toString());
        //      chance = Integer.parseInt(getIntent.getStringExtra("chance").toString());

        min=5; chance=30;
        MyThread dd = new MyThread();
        dd.start();

        button = findViewById(R.id.takePicture);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File picture = savePictureFile();
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture));
                startActivityForResult(takePicture,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 서버에전달
        BitmapFactory.Options factory = new BitmapFactory.Options();
        factory.inSampleSize=4;
        factory.inJustDecodeBounds=false;
        factory.inPurgeable=true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath,factory);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Toast.makeText(getApplicationContext(),""+(bitmap.getWidth()),Toast.LENGTH_SHORT).show();
        Bitmap rotated = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        //     rotated = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),true);
        // rotated.compress(Bitmap.CompressFormat.JPEG,100,rotated);

        tempImage[0].setImageBitmap(rotated); // nameTag랑 userName이랑 같은 index
        tempImage[0].setScaleType(ImageView.ScaleType.FIT_XY);
        //bitmap을 서버로보내기
    }
    private File savePictureFile(){
        String fileName = "hider1"; // 유저이름으로하면 좋을듯
        File pictureStorage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Running");
        if(!pictureStorage.exists())
            pictureStorage.mkdirs();
        try{
            File file = File.createTempFile(fileName,".jpg",pictureStorage);
            imagePath=file.getAbsolutePath();
            Intent mediaScan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(imagePath);
            Uri contentUri = Uri.fromFile(f);
            mediaScan.setData(contentUri);
            this.sendBroadcast(mediaScan);
            return file;
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
    /*게임진행
        걸음수 기록
        중간에 사진 보내기
        메세지 받았을 때 사진 찍어서 보내기
        확인누르면 UI변경
       시간종료시 종료
      */
    //thread만들어서 남은시간 줄이고 찬스주기20초동안 보여주기
    class MyThread extends Thread {
        public void run() {
            while (min != 0 || sec != 0) {

                try {
                    Thread.sleep(1000);
                    if (sec != 0)
                        sec--;
                    else {
                        min--;
                        sec = 59;
                    }
                    count++;

                } catch (Exception ex) {
                }
                handler.post(new Runnable() {
                    public void run() {
                        restTime.setText("남은 시간: " + min + "분 " + sec + "초");

                        if (count >= chance && (count % chance) <= 20) {
                            button.setVisibility(VISIBLE);
                            button.setText(20 - (count % chance) + "초안에 사진을 보내세요");
                        } else
                            ;//  button.setVisibility(GONE);

                    }
                });
                if(alive==0){
                    min=0;
                    sec=0;
                }
            }
            Intent intent2=new Intent(getApplicationContext(),GameOver.class);
            intent2.putExtra("userName",userName);
            intent2.putExtra("code",ID);
            startActivity(intent2);
        }
    }
}