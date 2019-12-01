package com.example.cloudmessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private TextView tokenText;
    private BroadcastReceiver broadcastReceiver;
    private EditText topic;
    private Button subscribeBtn;
    private Button unsubscribeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tokenText = (TextView) findViewById(R.id.Token);
        topic = (EditText) findViewById(R.id.Topic);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("Main Activity Token", SharedPrefManager.getInstance(MainActivity.this).getToken() );
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(MyFirebaseMessagingService.TOKEN_BROADCAST));
        if (SharedPrefManager.getInstance(MainActivity.this).getToken() != null){
            Log.d("Main Activity Token", SharedPrefManager.getInstance(MainActivity.this).getToken() );
        }

        subscribeBtn = (Button) findViewById(R.id.Subscribe);
        unsubscribeBtn = (Button) findViewById(R.id.Unsubscribe);

        if (getIntent() != null && getIntent().hasExtra("key1")) {
            tokenText.setText("");

            for (String key : getIntent().getExtras().keySet()) {
                Log.d("Message received:", "onCreate: Key: " + key + " Data: " + getIntent().getExtras().getString(key));
                tokenText.append(getIntent().getExtras().getString(key) + "\n");
            }

        }

        subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().subscribeToTopic(topic.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = "Subscription Successful!";
                                if (!task.isSuccessful()) {
                                    msg = "Subscription Failed!";
                                }
                                Log.d("Main Activity", msg);
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        unsubscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(topic.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = "UnSubscrubed Successfully!";
                                if (!task.isSuccessful()) {
                                    msg = "Failed!";
                                }
                                Log.d("Main Activity", msg);
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}
