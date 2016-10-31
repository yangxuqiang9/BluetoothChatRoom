package com.example.a328789.bluetoothcharroom.eventbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.a328789.bluetoothcharroom.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 328789 on 2016/10/25.
 */
public class EventbusTest extends AppCompatActivity {
    @BindView(R.id.frist_button)
    Button fristButton;
    @BindView(R.id.second_button)
    Button secondButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventbustest_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        EventBus.getDefault().register(this);
        fristButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转
                startActivity(new Intent(EventbusTest.this,SecondActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void onEventwwwwww(String s){
        Log.e("1","oneventwwww");
    }
    @Subscribe
    public void onEventAsync(int i){
        Log.e("2","oneventasync");
    }
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackgroundThread(FristBean secondBean){
        Log.e("3",Thread.currentThread().getName());
    }
    @Subscribe
    public void onEventMainThread(FristBean fristBean){

        Log.e("4",Thread.currentThread().getName());
        Log.e("4","oneventmainthread");
    }
    @Subscribe
    public void onEventMainThread(){
        Log.e("5","oneventmainthread2");
    }


}
