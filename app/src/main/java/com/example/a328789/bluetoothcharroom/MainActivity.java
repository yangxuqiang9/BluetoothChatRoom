package com.example.a328789.bluetoothcharroom;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.a328789.bluetoothcharroom.observable.StudentObserve;
import com.example.a328789.bluetoothcharroom.observable.TeacherObservable;
import com.example.a328789.bluetoothcharroom.utils.BlueToothUtils;
import com.example.a328789.bluetoothcharroom.utils.MHandler;
import com.example.a328789.bluetoothcharroom.view.DeviceItemAdapter;
import com.example.a328789.bluetoothcharroom.view.MyRecyclerViewAdapter;
import com.example.a328789.bluetoothcharroom.view.TopScrollListview;
import com.example.a328789.bluetoothcharroom.view.mDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DeviceItemAdapter.DeviceItemClickListener {
    @BindView(R.id.add_friend)
    Button addFriend;
    @BindView(R.id.open_bluetooth)
    Button openBlueTooth;
    @BindView(R.id.recycler_list)
    TopScrollListview recyclerList;

    public MHandler mhandler=new MHandler(this);
    private BlueToothUtils blueToothUtils;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private DeviceItemAdapter bluetoothDeviceDeviceItemAdapter;
    private MainActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        blueToothUtils=new BlueToothUtils(mhandler);
        init();
        this.mContext=this;
    }

    private void init() {
        addFriend.setOnClickListener(this);
        openBlueTooth.setOnClickListener(this);
        recyclerList.setLayoutManager(new LinearLayoutManager(this));

        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this, blueToothUtils.getBTDivers());
        recyclerList.setAdapter(myRecyclerViewAdapter);
        recyclerList.addItemDecoration(new mDecoration(this,0));
        myRecyclerViewAdapter.setOnItemClickListener(new DeviceItemAdapter.DeviceItemClickListener(){

            @Override
            public void onItemClick(View view, Object tag) {
                //建立通讯,进入聊天室
                BluetoothDevice device= (BluetoothDevice) tag;
                Intent intent = new Intent(mContext, CharRoomActivity.class);
                intent.putExtra("address",device.getAddress());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_friend://添加好友
                searchBT();
//                TeacherObservable teacherObservable = new TeacherObservable();
//                teacherObservable.addObserver(new StudentObserve());
//                teacherObservable.sendmessage("hello");
                break;
            case R.id.open_bluetooth://打开蓝牙
                blueToothUtils.openBT(this,mhandler);
                //eventbus简单的应用入口
//                startActivity(new Intent(this, EventbusTest.class));
                break;
            default:
                break;
        }
    }

    private void searchBT() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("正在搜索蓝牙");
        View view = View.inflate(this, R.layout.device_item, null);
        RecyclerView recycleItem = (RecyclerView) view.findViewById(R.id.device_item);
        recycleItem.setLayoutManager(new LinearLayoutManager(this));
        bluetoothDeviceDeviceItemAdapter = new DeviceItemAdapter(this);
        recycleItem.setAdapter(bluetoothDeviceDeviceItemAdapter);
        builder.setView(view);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                blueToothUtils.stopSearch(mContext);
                refreshView();
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                refreshView();
                Log.e("finish", "222222");
            }
        }).show();
        blueToothUtils.searchDivers(this,bluetoothDeviceDeviceItemAdapter);
        bluetoothDeviceDeviceItemAdapter.setOnItemClickListener(this);
    }

    public void openBT(){
        //询问用户是否打开
        Intent Intemtenabler=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivity(Intemtenabler);//同startActivity(enabler);
    }
    public void refreshView(){
        myRecyclerViewAdapter.setList(blueToothUtils.getBTDivers());
        myRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(myRecyclerViewAdapter!=null)
        refreshView();
    }
    public void showToas(){
        Toast.makeText(MainActivity.this.getApplicationContext(),"hello",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onItemClick(View view, Object tag) {
        BluetoothDevice b=(BluetoothDevice)tag;
        Toast.makeText(this,b.getName()+":"+b.getAddress(),Toast.LENGTH_SHORT).show();
        blueToothUtils.createBound(b);
    }
    public void unuser(){
        int a=0;
    }
}
