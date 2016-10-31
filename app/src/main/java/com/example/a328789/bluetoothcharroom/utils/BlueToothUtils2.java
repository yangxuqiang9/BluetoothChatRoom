package com.example.a328789.bluetoothcharroom.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by 328789 on 2016/10/30.
 */

public class BlueToothUtils2 {
    private final Handler mHandler;
    private final Context context;
    private final BluetoothAdapter defaultAdapter;
    private final UUID SPP= UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ConnectThread connectThread;
    private AcceptConnectThread acceptConnectThread;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BluetoothSocket socket=null;

    public BlueToothUtils2(Context context, Handler mHandler){
        this.context=context;
        this.mHandler=mHandler;
        defaultAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 连接蓝牙
     */
    public void connect(String address){
        if(connectThread!=null){
            connectThread.cancel();
            connectThread=null;
        }
        if(acceptConnectThread!=null){
            acceptConnectThread.cancel();
            acceptConnectThread=null;
        }

        connectThread = new ConnectThread(address);
        connectThread.start();
    }
    /**
     * 发送数据
     */
    public void writeData(String date){
        DateThread dateThread = new DateThread(socket);
        dateThread.start();
        dateThread.write(date.getBytes());
    }

    /**
     * 连接蓝牙的线程
     */
    public class ConnectThread extends Thread{
        private final String address;


        public ConnectThread(String address){
            this.address=address;
        }
        public void cancel(){
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            super.run();
            BluetoothSocket temp=null;
            BluetoothDevice remoteDevice = defaultAdapter.getRemoteDevice(address);
            try {
                temp  = remoteDevice.createRfcommSocketToServiceRecord(SPP);
            } catch (IOException e) {
                e.printStackTrace();
                if(temp!=null){
                    try {
                        temp.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            socket=temp;
            try {
                socket.connect();
                Log.e("soket:","已经与:"+socket.getRemoteDevice().getName()+"连接成功");
            } catch (IOException e) {
                e.printStackTrace();
                //做为客户端连接
                acceptConnectThread = new AcceptConnectThread();
                acceptConnectThread.start();

            }



        }
    }
    /**
     * 客户端连接蓝牙
     */
    public class AcceptConnectThread extends Thread{

        public AcceptConnectThread(){

        }

        public void cancel(){
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        public void run() {
            super.run();
            BluetoothSocket temp=null;
            try {
                BluetoothServerSocket serverSocket = defaultAdapter.listenUsingRfcommWithServiceRecord("name", SPP);
                temp = serverSocket.accept();

            } catch (IOException e) {
                e.printStackTrace();
                if(temp!=null){
                    try {
                        temp.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            socket=null;
            socket=temp;
        }
    }


    public class DateThread extends Thread{
        public DateThread(BluetoothSocket socket){
            try {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            super.run();
            byte[] byt=new byte[1024];
            int len=0;
            try {
                for (int i=0;i<10;i++){

                    while(inputStream.available()>0){
                        StringBuffer stringBuffer = new StringBuffer();
//                        while((len=inputStream.read(byt))!=-1){
                        if(inputStream.read(byt)!=-1){
                            String s = new String(byt,"utf-8");
                            stringBuffer.append(s);
                        }



//                        }
                        Message message = mHandler.obtainMessage();
                        message.what=1;
                        message.obj=stringBuffer.toString();
                        mHandler.sendMessage(message);
                        return;
                    }
                    Thread.sleep(100);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void write(byte[] byt){
            try {
                outputStream.write(byt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 关闭输入输出流
     */
    public void closeIO(){
        if(inputStream!=null){
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(outputStream!=null){
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket!=null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
