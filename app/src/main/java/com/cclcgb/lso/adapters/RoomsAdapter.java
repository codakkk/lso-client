package com.cclcgb.lso.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cclcgb.lso.R;
import com.cclcgb.lso.models.Room;

import java.util.List;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.MyViewHolder> {
    Context mContext;
    List<Room> mRooms;

    IOnRoomClicked mOnRoomClicked;

    public RoomsAdapter(Context context, List<Room> mRooms, IOnRoomClicked onRoomClicked) {
        this.mContext = context;
        this.mRooms = mRooms;
        mOnRoomClicked = onRoomClicked;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(mContext).inflate(R.layout.room_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsAdapter.MyViewHolder holder, int position) {
        Room room = mRooms.get(position);

        holder.roomName.setText(room.getName());
        holder.counter.setText(mContext.getString(R.string.counterHint, room.getCount(), room.getMaxCount()));

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
        holder.itemView.setOnClickListener(v -> mOnRoomClicked.onClick(room));
    }


    @Override
    public int getItemCount() {
        return mRooms.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView roomName, counter;

        public MyViewHolder(@NonNull  View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.roomName);
            counter = itemView.findViewById(R.id.counter);
        }
    }

    public interface IOnRoomClicked {
        void onClick(Room room);
    }
}
