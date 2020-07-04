package com.example.wayezi_onetoonechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupActivity extends AppCompatActivity
{
    private  String messagerecieverId,messagerecieverNumber,messageRecieverName,messagSenderId;
    private DatabaseReference RootRef,UserRef;
    private TextView userName,UserLastSeen;
    private Toolbar chatToolbar;
    private ImageButton sendMesssageButton;
    private EditText messageInputText;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private GroupAdapter groupAdapter;
    private RecyclerView GroupMessagesList;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        mAuth = FirebaseAuth.getInstance();
        messagSenderId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");




        intializeControllers();

        userName.setText(messageRecieverName);

        sendMesssageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sendMessage();


            }
        });
    }
    private void intializeControllers()
    {
        chatToolbar = (Toolbar)findViewById(R.id.chat_toolbar);
        setSupportActionBar(chatToolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View actionBarView = layoutInflater.inflate(R.layout.custom_chat_bar,null);
        actionBar.setCustomView(actionBarView);

        userName = (TextView)findViewById(R.id.custom_profile_name);

        sendMesssageButton = (ImageButton)findViewById(R.id.send_message_personal_btn);
        messageInputText = (EditText)findViewById(R.id.input_message);
        groupAdapter = new GroupAdapter(messagesList);
        GroupMessagesList = (RecyclerView)findViewById(R.id.private_message_list);
        linearLayoutManager = new LinearLayoutManager(this);
        GroupMessagesList.setLayoutManager(linearLayoutManager);
        GroupMessagesList.setAdapter(groupAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();



        RootRef.child("Group").child(messagSenderId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                        Messages messages = dataSnapshot.getValue(Messages.class);
                        messagesList.add(messages);
                        groupAdapter.notifyDataSetChanged();
                        //for scrolling to last message
                        GroupMessagesList.smoothScrollToPosition(GroupMessagesList.getAdapter().getItemCount());


                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {


                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)
                    {


                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void sendMessage()
    {
        String messageText = messageInputText.getText().toString();
        if(TextUtils.isEmpty(messageText))
        {
            Toast.makeText(GroupActivity.this, "First Write Your Message", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String messageSenderRef =  messagSenderId + "/";
//            String messageRecieverRef = "Messages/" + messagerecieverId + "/" + messagSenderId;

            DatabaseReference userMessageKeyRef = RootRef.child("Group")
                    .child(messagSenderId).push();

            String messagePushId = userMessageKeyRef.getKey();

            Map messageTextBody = new HashMap();
            messageTextBody.put("message",messageText);
            messageTextBody.put("type","text");
            messageTextBody.put("from",messagSenderId);

            Map messagebodyDetails  = new HashMap();
            messagebodyDetails.put(messageSenderRef + "/" + messagePushId,messageTextBody);
//            messagebodyDetails.put(messageRecieverRef + "/" + messagePushId,messageTextBody);

            RootRef.child("Group").updateChildren(messagebodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
//                        Toast.makeText(GroupActivity.this, "Message sent ", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(GroupActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    messageInputText.setText("");

                }
            });

        }

    }
}