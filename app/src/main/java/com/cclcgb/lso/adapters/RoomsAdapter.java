package com.cclcgb.lso.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cclcgb.lso.activities.ChatActivity;
import com.cclcgb.lso.R;
import com.cclcgb.lso.models.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.MyViewHolder> {
    Context context;
    List<Room> rooms;

    public RoomsAdapter(Context context, List<Room> rooms) {
        this.context = context;
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.room_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsAdapter.MyViewHolder holder, int position) {
        Room room = rooms.get(position);

        holder.roomName.setText(room.getName());
        holder.counter.setText(context.getString(R.string.counterHint, room.getCount()));

        /*FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                            long time = snapshot.child("lastMsgTime").getValue(Long.class);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                            holder.messageTime.setText(dateFormat.format(new Date(time)));
                            holder.lastMessage.setText(lastMsg);
                        } else {
                            holder.lastMessage.setText("Tap to chat");
                            holder.messageTime.setVisibility(View.INVISIBLE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });*/
        /*holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("name", user.getName());
            intent.putExtra("uid", user.getUid());
            intent.putExtra("image_url", user.getProfileImage());
            context.startActivity(intent);
        });*/
    }


    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView roomName, counter;

        public MyViewHolder(@NonNull  View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.roomName);
            counter = itemView.findViewById(R.id.counter);
        }
    }
}
