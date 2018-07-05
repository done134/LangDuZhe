package com.cctv.langduzhe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.data.entites.CommandEntity;
import com.cctv.langduzhe.util.picasco.PicassoUtils;
import com.cctv.langduzhe.view.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gentleyin
 * on 2018/1/31.
 * 说明：弹窗评论列表adapter
 */
public class DialogCommandAdapter extends BaseRecyclerViewAdapter<DialogCommandAdapter.ViewHolder> {


    private List<CommandEntity.DataBean> list;
    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dialog_command, parent, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommandEntity.DataBean dataBean = list.get(position);
        holder.tvCommandContent.setText(dataBean.getContent());
        holder.tvCommandName.setText(dataBean.getReaderName());
        holder.tvCommandTime.setText(dataBean.getCreateDate());
        PicassoUtils.loadImageByurl(context, dataBean.getReaderImg(), holder.ivCommandHead);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public void setData(List<CommandEntity.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addData(List<CommandEntity.DataBean> list) {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    public void addNewData(CommandEntity.DataBean newComment) {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.add(0,newComment);
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_command_head)
        CircleImageView ivCommandHead;
        @BindView(R.id.tv_command_name)
        TextView tvCommandName;
        @BindView(R.id.tv_command_time)
        TextView tvCommandTime;
        @BindView(R.id.tv_command_content)
        TextView tvCommandContent;

        ViewHolder(View itemView, DialogCommandAdapter messageAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            itemView.setOnClickListener(v -> messageAdapter.onItemHolderClick(this));
        }
    }
}
