package com.example.a328789.bluetoothcharroom.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a328789.bluetoothcharroom.R;
import com.example.a328789.bluetoothcharroom.utils.BlueToothUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 328789 on 2016/10/15.
 */
public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomHolder> {
    private final Context context;
    private List<String> list =new ArrayList<>();
    private boolean isOnSelf;


    public ChatRoomAdapter(Context context) {
        this.context=context;
    }

    @Override
    public ChatRoomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatRoomHolder(LayoutInflater.from(context).inflate( R.layout.chat_room_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ChatRoomHolder holder, int position) {

        if(list!=null){
            String s = list.get(position);
            if(isOnSelf){
                holder.right.setText(s);
//                holder.left.setText("ddddd");
            }else {
                holder.left.setText(s);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(list!=null)
            return list.size();
        return 0;
    }
    public void setData(String s,boolean isOneSelf){
        this.isOnSelf=isOneSelf;
        list.add(s);
    }

    class ChatRoomHolder extends RecyclerView.ViewHolder{

        private final TextView left;
        private final TextView right;

        public ChatRoomHolder(View itemView) {
            super(itemView);
            right = (TextView) itemView.findViewById(R.id.right);
            left = (TextView) itemView.findViewById(R.id.left);
        }
    }
}
