package com.example.asdfg.runningman;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
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
import static android.widget.LinearLayout.VERTICAL;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorListener;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
public class Hider extends AppCompatActivity implements SensorEventListener {
    Socket sock;
    private static final String TAG = "BT";
    String ID;
    DataOutputStream outstream;
    DataInputStream instream;
    protected static String ip =  "192.9.88.141";
    int port = 7777;
    Intent intent;
    String userName;
    ImageButton hider11,hider22,hider33,hider44;
    Integer min=5,sec=0,count=0,chance,step=0;
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
    BluetoothAdapter btAdapter;
    String [] scannedDeviceName = new String[1000];
    String [] scannedDeviceAddress = new String[1000];
    String myMacAddress;
    String seekerMacAddress ="1C:AF:05:94:64:B2";
    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private float x,y,z;
    private static final int SHAKE_THRESHOLD = 800;
    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hider);

        /*게임진행
           걸음수 기록
           중간에 사진 보내기
           메세지 받았을 때 사진 찍어서 보내기
           확인누르면 UI변경
           시간종료시 종료
         */
        /*
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
        }*/

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);//센서 메니저를 생성
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//센서 객체를 형성

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

        // Register for broadcasts when a device is discovered 블루투스
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(broadcastReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(broadcastReceiver, filter);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        myMacAddress = btAdapter.getAddress(); //자신의 맥주소

        enableBluetooth();
        doDiscovery();

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
        factory.inSampleSize=16;
        factory.inJustDecodeBounds=false;
        factory.inPurgeable=true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath,factory);
      try {
          ExifInterface exif = new ExifInterface(imagePath);
          int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
          int exifDegree = exifOrientationToDegrees(exifOrientation);
          Matrix matrix = new Matrix();
          matrix.postRotate(exifDegree);

          Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
          tempImage[0].setImageBitmap(rotated); // index 바꾸기
          // rotated 서버에 전송
      }catch(Exception e){e.printStackTrace();}
        Toast.makeText(getApplicationContext(), "" + (bitmap.getWidth()), Toast.LENGTH_SHORT).show();

    }
    public int exifOrientationToDegrees(int exifOrientation){
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
            return 90;
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
            return 180;
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
            return 270;
        return 0;
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
    protected void onStart(){
        super.onStart();
        if(accelerometerSensor!=null)
            sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_GAME);
    }
    protected void onStop(){
        super.onStop();
        //if(sensorManager!=null)
        // sensorManager.unregisterListener(this); 센싱 안하도록 하는 부분
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
;
    }
    public void onSensorChanged(SensorEvent event) {//센서 리슨 하는 부분
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);
            if (gabOfTime > 100) {//100ms에 한 번 업데이트
                lastTime = currentTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];
                speed = Math.abs(x + y + z - lastX - lastY - lastZ) /
                        gabOfTime * 10000;
                if (speed > SHAKE_THRESHOLD) {
                    step++;    // 서버로전달
                }

                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }
        }
    }
    protected void onDestroy() {
        super.onDestroy();
        if(btAdapter!=null)
            btAdapter.cancelDiscovery();
        this.unregisterReceiver(broadcastReceiver);
    }

    public void enableBluetooth(){
        if(btAdapter.isEnabled()){
            //블루투스 켜져있음
            //Toast.makeText(this,"켜져있음",Toast.LENGTH_LONG).show();
        }else{
            //Toast.makeText(this,"켜야함",Toast.LENGTH_LONG).show();
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
            String action = intent.getAction();

            //블루투스 감지
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getAddress().equals(seekerMacAddress)) {    // seeker 맥 어드레스 서버에서 받아와야함
                    Toast.makeText(getApplicationContext(),"술래가 주변에 있습니다!",Toast.LENGTH_LONG).show(); // 블루투스연결
                    Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(2000);
                }
            }//검색이 끝나면
            else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                    doDiscovery();
            }
        }
    };
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

            // step 서버에 보내기
            startActivity(intent2);

        }
    }
}