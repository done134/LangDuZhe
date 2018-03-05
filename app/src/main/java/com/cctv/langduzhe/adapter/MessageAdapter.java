package com.cctv.langduzhe.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.data.entites.MessageEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gentleyin
 * on 2018/1/17.
 * 说明：主页面消息列表adapter
 */
public class MessageAdapter extends BaseRecyclerViewAdapter<MessageAdapter.ViewHolder> {

    private List<MessageEntity> messageList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MessageEntity message = messageList.get(position);
//        holder.tvMessageContent.setText(message.messageContent);
//        holder.tvMessageTime.setText(message.messageTime);
    }

    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    public void setMessageList(List<MessageEntity> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    public List<MessageEntity> getMessageList() {
        return messageList;
    }

    public void addMessageList(List<MessageEntity> messages) {
        if (messageList == null) {
            messageList = new ArrayList<>();
        }
        messageList.addAll(messages);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_message_content)
        TextView tvMessageContent;
        @BindView(R.id.tv_message_time)
        TextView tvMessageTime;

        ViewHolder(View itemView, MessageAdapter messageAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> messageAdapter.onItemHolderClick(0,getLayoutPosition(),false));
        }
    }
}
