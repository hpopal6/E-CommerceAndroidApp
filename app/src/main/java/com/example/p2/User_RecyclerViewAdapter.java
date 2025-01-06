package com.example.p2;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class User_RecyclerViewAdapter extends RecyclerView.Adapter<User_RecyclerViewAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    static List<User> userList;
    public User_RecyclerViewAdapter(Context context, List<User> userList, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.userList = userList;
        this.recyclerViewInterface = recyclerViewInterface;


    }
    @NonNull
    @Override
    public User_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent,false);
        return new User_RecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull User_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.userName.setText(userList.get(position).getUserName());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView = itemView.findViewById(R.id.imageViewDelete);
        TextView userName;


        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface){
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewDelete);
            userName = itemView.findViewById(R.id.textViewAccount);

            for(int i = 0; i < userList.size(); i++) {
                if(userList.get(i).isAdmin()) {
                    imageView.setVisibility(View.INVISIBLE);
                } else{
                    imageView.setVisibility(View.VISIBLE);
                }
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

            /*itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemLongClick(pos);
                        }
                    }
                    return true;
                }
            });*/

        }

    }
}
