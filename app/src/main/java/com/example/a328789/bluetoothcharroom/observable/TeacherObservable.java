package com.example.a328789.bluetoothcharroom.observable;

import java.util.Observable;

/**
 * Created by 328789 on 2016/11/8.
 */

public class TeacherObservable extends Observable {
    public TeacherObservable() {

    }

    public void sendmessage(Object data){
        setChanged();
        notifyObservers(data);
    }
}
