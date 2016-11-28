package com.jexapps.bloodhub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
/**
 * Created by mahnoor on 28/11/2016.
 */

public class SplashActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        try {
//            // stall for .5 seconds
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//
//        }
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
