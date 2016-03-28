package com.example.lumia.agent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.login_btn);
        button.setOnClickListener(myOnClickListener);
    }

    private View.OnClickListener myOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            TextView text = (TextView) findViewById(R.id.user_name_et);
            String textval = text.getText()!=null ? text.getText().toString() : "";
            if(TextUtils.isEmpty(textval)){
                text.setError("必须填写登录账号");
                return;
            }
            text = (TextView) findViewById(R.id.password_et);
            textval = text.getText()!=null ? text.getText().toString() : "";
            if(TextUtils.isEmpty(textval)){
                text.setError("必须填写登录密码");
                return;
            }

        }
    };
}
