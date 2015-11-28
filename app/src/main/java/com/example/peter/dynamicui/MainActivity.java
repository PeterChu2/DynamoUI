package com.example.peter.dynamicui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dynamoui.core.Dynamo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dynamo.getContext().init(this, "MyCoolApp");
        setContentView(R.layout.activity_main);
    }
}
