package com.example.notificationapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.notificationapp.modal.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList){
        this.context = context;
        this.userList = userList;

    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users,parent,false);
        return  new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {

        final User user = userList.get(position);
        holder.textViewEmali.setText(user.email);

        holder.textViewEmali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,SendNoteActivity.class);
                i.putExtra("user",user.getEmail());
                i.putExtra("token",user.getToken());
                i.putExtra("email",user.getEmail());
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        TextView textViewEmali;

        public UserViewHolder(View itemView) {
            super(itemView);

            textViewEmali = itemView.findViewById(R.id.textemail);

        }
    }
}
