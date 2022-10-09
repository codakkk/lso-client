package com.cclcgb.lso.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cclcgb.lso.R;
import com.cclcgb.lso.databinding.ItemReceiveBinding;
import com.cclcgb.lso.databinding.ItemSentBinding;
import com.cclcgb.lso.models.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChatMessagesAdapter extends RecyclerView.Adapter {

    Context context;
    List<Message> messages;

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    public ChatMessagesAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SENT){
            View view = LayoutInflater.from(context).inflate(R.layout.item_sent, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_receive, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(!message.getSenderId().equals("-1")) {
            return ITEM_SENT;
        }
        /*if (FirebaseAuth.getInstance().getUid().equals(message.getSenderId())) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }*/
        return ITEM_RECEIVE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        if(holder.getClass()==SentViewHolder.class){
            SentViewHolder viewHolder = (SentViewHolder) holder;
            long time = message.getTimestamp();
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

            if(message.getMessage().equals("Photo")){
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
            }
            // viewHolder.binding.messageTime.setText(dateFormat.format(new Date(time)));
            viewHolder.binding.message.setText(message.getMessage());

        } else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            long time = message.getTimestamp();
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            if(message.getMessage().equals("Photo")){
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
            }
            // viewHolder.binding.messageTime.setText(dateFormat.format(new Date(time)));
            viewHolder.binding.message.setText(message.getMessage());

        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {
        ItemSentBinding binding;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSentBinding.bind(itemView);
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
        ItemReceiveBinding binding;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemReceiveBinding.bind(itemView);
        }
    }
}
