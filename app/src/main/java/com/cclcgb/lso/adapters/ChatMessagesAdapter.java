package com.cclcgb.lso.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cclcgb.lso.R;
import com.cclcgb.lso.databinding.ItemReceiveBinding;
import com.cclcgb.lso.databinding.ItemSentBinding;
import com.cclcgb.lso.databinding.ItemServerBinding;
import com.cclcgb.lso.models.ChatMessage;

import java.util.List;

public class ChatMessagesAdapter extends RecyclerView.Adapter {

    Context mContext;
    List<ChatMessage> mChatMessages;

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;
    final int ITEM_SERVER = 3;

    public ChatMessagesAdapter(Context mContext, List<ChatMessage> chatMessages) {
        this.mContext = mContext;
        this.mChatMessages = chatMessages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SENT){
            ItemSentBinding binding = ItemSentBinding.inflate(LayoutInflater.from(mContext), parent, false);
            return new SentViewHolder(binding);
        }
        else if(viewType == ITEM_SERVER) {
            ItemServerBinding binding = ItemServerBinding.inflate(LayoutInflater.from(mContext), parent, false);
            return new ServerViewHolder(binding);
        }
        ItemReceiveBinding binding = ItemReceiveBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new ReceiverViewHolder(binding);
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = mChatMessages.get(position);
        if(chatMessage.getSenderId() == -1) {
            return ITEM_SERVER;
        }
        else if(chatMessage.getSenderId() == 0) {
            return ITEM_SENT;
        }
        return ITEM_RECEIVE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage chatMessage = mChatMessages.get(position);

        if(holder.getClass() == SentViewHolder.class){
            SentViewHolder viewHolder = (SentViewHolder) holder;

            if(chatMessage.getMessage().equals("Photo")) {
                viewHolder.mBinding.chatMessage.setVisibility(View.GONE);
            }
            viewHolder.mBinding.chatMessage.setText(chatMessage.getMessage());

        } else if(holder instanceof ReceiverViewHolder) {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;

            viewHolder.mBinding.chatMessage.setText(chatMessage.getMessage());

        } else if(holder instanceof ServerViewHolder) {
            ServerViewHolder viewHolder = (ServerViewHolder) holder;

            viewHolder.mBinding.chatMessage.setText(chatMessage.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mChatMessages.size();
    }

    public static class SentViewHolder extends RecyclerView.ViewHolder {
        ItemSentBinding mBinding;
        public SentViewHolder(@NonNull ItemSentBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
        }
    }

    public static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        ItemReceiveBinding mBinding;
        public ReceiverViewHolder(@NonNull ItemReceiveBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
        }
    }

    public static class ServerViewHolder extends RecyclerView.ViewHolder {
        ItemServerBinding mBinding;
        public ServerViewHolder(@NonNull ItemServerBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
