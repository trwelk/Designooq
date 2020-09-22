package com.Tregaki.designooq;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    public MessageAdapter(List<Messages> messagesList) {
        this.messagesList = messagesList;
    }

    private List<Messages> messagesList;
    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout,parent,false);

    return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
         String currentUserStirng = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Messages messages = messagesList.get(position);
        String from = messages.getFrom();

        if(currentUserStirng.equals(from)){
            holder.messageText.setBackgroundColor(Color.WHITE);
            holder.messageText.setTextColor(Color.DKGRAY);

        }
        else{
            holder.messageText.setBackgroundResource(R.drawable.message_text_background);
            holder.messageText.setTextColor(Color.WHITE);
        }
        holder.messageText.setText(messages.getMessage());
        Log.d("CHAT_LOG",messages.getMessage());

    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView senderImage;
        public TextView messageText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderImage = (CircleImageView)itemView.findViewById(R.id.message_user_image);
            messageText = (TextView)itemView.findViewById(R.id.message_message_textview);

        }
    }
}
