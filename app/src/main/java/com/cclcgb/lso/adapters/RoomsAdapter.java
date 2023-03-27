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
import java.util.Map;

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
        View view =  LayoutInflater.from(mContext).inflate(R.layout.item_room, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsAdapter.MyViewHolder holder, int position) {
        Room room = mRooms.get(position);

        assert room != null;

        holder.roomName.setText(room.getName());
        holder.counter.setText(mContext.getString(R.string.counterHint, room.getCount(), room.getMaxCount()));

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
