package com.example.wayezi_onetoonechat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MessageViewHolder>
{

    private List<Messages> GroupMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef,groupRef;


    public GroupAdapter(List<Messages> groupMessagesList) {
        this.GroupMessagesList = groupMessagesList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_message_layout,parent,false);
        mAuth = FirebaseAuth.getInstance();
        return new GroupAdapter.MessageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position)
    {
        String messageSendrId = mAuth.getCurrentUser().getUid();
        Messages message = GroupMessagesList.get(position);

        String fromUId = message.getFrom();
        String fromMessageType = message.getType();

      


        if (fromMessageType.equals("text"))
        {
            holder.linearLayout.setVisibility(View.INVISIBLE);
            holder.SenderMessage.setVisibility(View.INVISIBLE);
            holder.recieverMessage.setVisibility(View.INVISIBLE);
        //    holder.userName.setVisibility(View.INVISIBLE);

            if(fromUId.equals(messageSendrId))
            {
                holder.SenderMessage.setVisibility(View.VISIBLE);
                holder.SenderMessage.setBackgroundResource(R.drawable.sender_messages_layout);
                holder.SenderMessage.setText(message.getMessage());
            }
            else
            {

              holder.linearLayout.setVisibility(View.VISIBLE);
                holder.recieverMessage.setVisibility(View.VISIBLE);
  //              holder.userName.setVisibility(View.VISIBLE);
    //            holder.linearLayout.setBackgroundResource(R.drawable.reciever_messages_layout);
                holder.recieverMessage.setText(message.getMessage());
                holder.recieverMessage.setBackgroundResource(R.drawable.reciever_messages_layout);
      //          holder.userName.setText("name");


            }
        }
    }

    @Override
    public int getItemCount()
    {
        return GroupMessagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder
    {
        public LinearLayout linearLayout;
        public TextView SenderMessage,recieverMessage,userName;
        public MessageViewHolder(@NonNull View itemView)
        {
            super(itemView);
            SenderMessage = (TextView) itemView.findViewById(R.id.sender_message_text);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            recieverMessage = (TextView) itemView.findViewById(R.id.recieve_message_text);
            userName= (TextView) itemView.findViewById(R.id.user_name);
        }
    }
}
