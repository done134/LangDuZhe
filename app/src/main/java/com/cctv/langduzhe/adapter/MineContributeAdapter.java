package com.cctv.langduzhe.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.data.entites.ContributeEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gentleyin
 * on 2018/3/18.
 * 说明：
 */
public class MineContributeAdapter extends BaseRecyclerViewAdapter<MineContributeAdapter.ViewHolder> {


    private List<ContributeEntity.DataBean> list;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_contribute, null);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContributeEntity.DataBean contribute = list.get(position);
        holder.tvMessageContent.setText(contribute.getContent());
        holder.tvMessageTime.setText(contribute.getCreateDate());
        holder.tvMessageTitle.setText(contribute.getTitle());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public List<ContributeEntity.DataBean> getList() {
        return list;
    }

    public void setList(List<ContributeEntity.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public void addList(List<ContributeEntity.DataBean> list) {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_message_title)
        TextView tvMessageTitle;
        @BindView(R.id.tv_message_content)
        TextView tvMessageContent;
        @BindView(R.id.tv_message_time)
        TextView tvMessageTime;
        ViewHolder(View itemView, MineContributeAdapter messageAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> onItemHolderClick(0, getLayoutPosition(), false));

        }
    }
}
