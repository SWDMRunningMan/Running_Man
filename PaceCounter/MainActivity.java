package com.example.geonsu.pacecounter;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorListener;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends Activity implements SensorEventListener {

    EditText vi;
    Button initBtn;
    int count=0;
    String str = Integer.toString(count);

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

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBtn = findViewById(R.id.initBtn);
        vi = findViewById(R.id.tv);
        vi.setClickable(false);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);//센서 메니저를 생성
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//센서 객체를 형성

        initBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                str = Integer.toString(count);
                vi.setText(str);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(accelerometerSensor!= null)
        {   //마지막 인자가 리스너의 반응 속도
            sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_GAME);
        }
    }
    @Override
    protected void onStop(){
        super.onStop();
        //if(sensorManager!=null)
           // sensorManager.unregisterListener(this); 센싱 안하도록 하는 부분
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }
    @Override
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
                    count++;
                }
                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }
        }
    }

}
