package com.example.a328789.bluetoothcharroom.observable;

import android.util.Log;

import com.example.a328789.bluetoothcharroom.MainActivity;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by 328789 on 2016/11/8.
 */

public class StudentObserve implements Observer {
    @Override
    public void update(Observable observable, Object data){
        Log.e("observable",data+"");
//        new MainActivity().showToas();
    }

}
