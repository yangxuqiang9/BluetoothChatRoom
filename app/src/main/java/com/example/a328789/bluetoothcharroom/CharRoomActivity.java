package com.example.a328789.bluetoothcharroom;

import android.app.AlarmManager;
import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.a328789.bluetoothcharroom.utils.BlueToothUtils;
import com.example.a328789.bluetoothcharroom.utils.BlueToothUtils2;
import com.example.a328789.bluetoothcharroom.utils.MHandler;
import com.example.a328789.bluetoothcharroom.view.ChatRoomAdapter;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CharRoomActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.send)
    Button send;
    @BindView(R.id.centent)
    EditText centent;
    @BindView(R.id.char_content)
    RecyclerView charContent;

//    private BlueToothUtils blueToothUtils;
    private ChatRoomAdapter chatRoomAdapter;
    private Timer timer;
    private String read;
    public MHandler mHandler;
    private BlueToothUtils2 blueToothUtils2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_char_room);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        mHandler = new MHandler(this);
//        blueToothUtils=new BlueToothUtils(mHandler);
//        BluetoothDevice device = blueToothUtils.getRemote(getIntent().getStringExtra("address"));
//        blueToothUtils.connectBTThread(device);
        //连接蓝牙
        blueToothUtils2 = new BlueToothUtils2(this, mHandler);
        blueToothUtils2.connect(getIntent().getStringExtra("address"));

        send.setOnClickListener(this);
        charContent.setLayoutManager(new LinearLayoutManager(this));
        chatRoomAdapter = new ChatRoomAdapter(this);
        charContent.setAdapter(chatRoomAdapter);

//        whileAccept();
    }

    private void whileAccept() {
        timer = new Timer();
        TimerTask task= new TimerTask(){
            @Override
            public void run() {
                Log.e("d",Thread.currentThread().getName());
                synchronized (this){
//                    read = blueToothUtils.read();
//                    mHandler.sendEmptyMessage(1);
                }

            }
        };
        timer.schedule(task,1000L,1000L);
    }

    @Override
    public void onClick(View v) {
        String chatContent = centent.getText().toString().trim();
        if(!checkContent(chatContent)){
            //发送数据
            chatRoomAdapter.setData(chatContent,true);
            chatRoomAdapter.notifyDataSetChanged();
//            blueToothUtils.writeThread(chatContent);
            blueToothUtils2.writeData(chatContent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        timer.cancel();
    }

    public void refreshContext(String readDate){
        chatRoomAdapter.setData(readDate,false);
        chatRoomAdapter.notifyDataSetChanged();
    }

    private boolean checkContent(String chatContent) {
        return TextUtils.isEmpty(chatContent);
    }
}
