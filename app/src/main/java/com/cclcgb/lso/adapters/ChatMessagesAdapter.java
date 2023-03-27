package com.cclcgb.lso.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cclcgb.lso.R;
import com.cclcgb.lso.api.APIManager;
import com.cclcgb.lso.databinding.ItemJoinRequestBinding;
import com.cclcgb.lso.databinding.ItemReceiveBinding;
import com.cclcgb.lso.databinding.ItemSentBinding;
import com.cclcgb.lso.databinding.ItemServerBinding;
import com.cclcgb.lso.models.ChatMessage;
import com.cclcgb.lso.models.ChatMessageJoinRequestState;
import com.cclcgb.lso.models.ChatMessageType;
import com.cclcgb.lso.models.User;

import java.util.List;

public class ChatMessagesAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<ChatMessage> mChatMessages;

    private IChatMessageJoinRequestCallback mCallback;

    final static int ITEM_SENT = 1;
    final static int ITEM_RECEIVE = 2;
    final static int ITEM_SERVER = 3;
    final static int ITEM_JOIN_REQUEST = 4;



    public ChatMessagesAdapter(Context mContext, List<ChatMessage> chatMessages, IChatMessageJoinRequestCallback callback) {
        this.mContext = mContext;
        this.mChatMessages = chatMessages;
        this.mCallback = callback;
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
        } else if(viewType == ITEM_JOIN_REQUEST) {
            ItemJoinRequestBinding binding = ItemJoinRequestBinding.inflate(LayoutInflater.from(mContext), parent, false);
            return new ItemJoinRequestViewHolder(binding);
        }
        ItemReceiveBinding binding = ItemReceiveBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new ReceiverViewHolder(binding);
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = mChatMessages.get(position);
        if(chatMessage.getType() == ChatMessageType.Sent) {
            return ITEM_SENT;
        } else if(chatMessage.getType() == ChatMessageType.Received) {
            return ITEM_RECEIVE;
        } else if(chatMessage.getType() == ChatMessageType.JoinRequest) {
            return ITEM_JOIN_REQUEST;
        }
        return ITEM_SERVER;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage chatMessage = mChatMessages.get(position);

        if(holder instanceof SentViewHolder viewHolder){
            viewHolder.mBinding.chatMessage.setText(chatMessage.getMessage());

        } else if(holder instanceof ReceiverViewHolder viewHolder) {
            viewHolder.mBinding.nickname.setTextColor(Color.RED);
            viewHolder.mBinding.nickname.setText(chatMessage.getUser().getName());
            viewHolder.mBinding.chatMessage.setText(chatMessage.getMessage());

        } else if(holder instanceof ServerViewHolder viewHolder) {
            viewHolder.mBinding.chatMessage.setText(chatMessage.getMessage());
        } else if(holder instanceof ItemJoinRequestViewHolder viewHolder) {
            switch (chatMessage.getRequestState()) {
                case Waiting -> {
                    viewHolder.mBinding.btnReject.setVisibility(View.VISIBLE);
                    viewHolder.mBinding.btnAccept.setVisibility(View.VISIBLE);
                    viewHolder.mBinding.btnAccept.setOnClickListener((v) -> mCallback.onAccept(position, chatMessage));
                    viewHolder.mBinding.btnReject.setOnClickListener((v) -> mCallback.onReject(position, chatMessage));
                    viewHolder.mBinding.chatMessage.setText(chatMessage.getUser().getName() + " vuole entrare. Accetti?");
                }
                case Accepted, Rejected -> {
                    viewHolder.mBinding.btnReject.setVisibility(View.GONE);
                    viewHolder.mBinding.btnAccept.setVisibility(View.GONE);
                    viewHolder.mBinding.btnReject.setOnClickListener(null);
                    viewHolder.mBinding.btnAccept.setOnClickListener(null);
                    String message = "Hai accettato " + chatMessage.getUser().getName() + ".";
                    if (chatMessage.getRequestState() == ChatMessageJoinRequestState.Rejected) {
                        message = "Hai rifiutato " + chatMessage.getUser().getName() + ".";
                    }
                    viewHolder.mBinding.chatMessage.setText(message);
                }
            }
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

    public static class ItemJoinRequestViewHolder extends RecyclerView.ViewHolder {
        ItemJoinRequestBinding mBinding;
        public ItemJoinRequestViewHolder(@NonNull ItemJoinRequestBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

    public interface IChatMessageJoinRequestCallback {
        void onAccept(int position, ChatMessage message);
        void onReject(int position, ChatMessage message);
    }
}
