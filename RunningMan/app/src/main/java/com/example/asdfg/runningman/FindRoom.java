package com.example.asdfg.runningman;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FindRoom extends AppCompatActivity {
    EditText edit;
    Button btn;
    String str;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findroom);
        edit=findViewById(R.id.edit);
        btn=findViewById(R.id.btn);
        intent=new Intent(this,Room.class);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = edit.getText().toString();
                if (true/*str==서버의 방 아이디*/) {
                }else{
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}