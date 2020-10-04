package com.Tregaki.designooq;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private         DatabaseReference userDb ;
    private String friendImage;

    public MessageAdapter(List<Messages> messagesList,String friendIdString) {

        this.messagesList = messagesList;
        userDb = FirebaseDatabase.getInstance().getReference("user").child(friendIdString);
        userDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 friendImage = snapshot.child("image").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private List<Messages> messagesList;
    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout,parent,false);

    return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.MessageViewHolder holder, int position) {
         String currentUserStirng = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Messages messages = messagesList.get(position);
        String from = messages.getFrom();
        Log.d("CHAT_LOG","MESSAGE LOADING : " + this.getClass().getName());
        if(currentUserStirng.equals(from)){
            holder.myMessageText.setBackgroundResource(R.drawable.message_text_background);
            holder.frienMessageText.setTextColor(Color.WHITE);
            holder.myMessageText.setText(messages.getMessage());
            holder.frienMessageText.setEnabled(false);
            holder.frienMessageText.setVisibility(View.INVISIBLE);
            holder.senderImage.setVisibility(View.INVISIBLE);
            holder.senderImage.setEnabled(false);
            holder.myMessageText.setEnabled(true);
            holder.myMessageText.setVisibility(View.VISIBLE);
        }
        else{
            holder.frienMessageText.setBackgroundResource(R.drawable.message_text_background);
            holder.frienMessageText.setTextColor(Color.WHITE);
            holder.frienMessageText.setText(messages.getMessage());
            holder.myMessageText.setEnabled(false);
            holder.myMessageText.setVisibility(View.INVISIBLE);
            holder.frienMessageText.setEnabled(true);
            holder.frienMessageText.setVisibility(View.VISIBLE);
            holder.senderImage.setVisibility(View.VISIBLE);
            Picasso.get().load(friendImage).placeholder(R.drawable.account_image).into(holder.senderImage);

        }


        Log.d("CHAT_LOG",messages.getMessage());

    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    //the single holder which holds message ui
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView senderImage;
        public TextView frienMessageText;
        public TextView myMessageText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderImage = (CircleImageView)itemView.findViewById(R.id.message_user_image);
            frienMessageText = (TextView)itemView.findViewById(R.id.message_message_textview);
            myMessageText = (TextView)itemView.findViewById(R.id.message_my_message_textview);
        }
    }
}
