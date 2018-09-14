package com.example.mengqiqiweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mengqiqiweather.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FirstActivity extends AppCompatActivity {
    private ImageView firstImage;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        firstImage=(ImageView)findViewById(R.id.first_image);
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String firstPic=preferences.getString("bing_pic",null);
        if(firstPic!=null){
            Glide.with(this).load(firstPic).into(firstImage);
        }else {
            loadBingPic();
        }
        floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(FirstActivity.this);
                if(preferences.getString("weather",null)!=null){
                    Intent intent=new Intent(FirstActivity.this,WeatherActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent=new Intent(FirstActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    private void loadBingPic(){
        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FirstActivity.this,"获取网络图片失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(FirstActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(FirstActivity.this).load(bingPic).into(firstImage);
                    }
                });
            }
        });
    }
}
