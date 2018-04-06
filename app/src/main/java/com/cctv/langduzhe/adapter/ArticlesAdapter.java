package com.cctv.langduzhe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.data.entites.ArticlesEntity;
import com.cctv.langduzhe.util.picasco.PicassoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gentleyin
 * on 2018/3/19.
 * 说明：
 */
public class ArticlesAdapter extends BaseRecyclerViewAdapter<ArticlesAdapter.ViewHolder> {


    private Context context;

    private List<ArticlesEntity.DataBean> list;
    public ArticlesAdapter(Context context) {
        this.context = context;
    }

    public List<ArticlesEntity.DataBean> getList() {
        return list;
    }

    public void setList(List<ArticlesEntity.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public void addList(List<ArticlesEntity.DataBean> list) {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_articles, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArticlesEntity.DataBean article = list.get(position);
        holder.tvArticleContent.setText(article.getShortContent());
        holder.tvArticleTitle.setText(article.getTitle());
        holder.ivArticleCover.setVisibility(View.GONE);
//        if(TextUtils.isEmpty())
//        PicassoUtils.loadImageByurl(context,article.getContent(),holder.ivArticleCover);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_article_content)
        TextView tvArticleContent;
        @BindView(R.id.tv_article_title)
        TextView tvArticleTitle;
        @BindView(R.id.iv_article_cover)
        ImageView ivArticleCover;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> onItemHolderClick(0, getLayoutPosition(), false));

        }
    }
}
