package com.cctv.langduzhe.feature.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseFragment;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.contract.message.MessagePresenter;
import com.cctv.langduzhe.contract.message.MessageView;
import com.cctv.langduzhe.data.entites.MessageEntity;
import com.cctv.langduzhe.util.ToastUtils;
import com.cctv.langduzhe.adapter.MessageAdapter;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by gentleyin
 * on 2018/1/15.
 * 说明：消息fragment
 */
public class MessageFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener,
        PullLoadMoreRecyclerView.PullLoadMoreListener,MessageView{
    @BindView(R.id.rv_message_list)
    PullLoadMoreRecyclerView rvMessageList;
    Unbinder unbinder;
    private View mView;

    private MessageAdapter messageAdapter;
    private List<MessageEntity> list = new ArrayList<>();
    private MessagePresenter presenter;
    private int pageNum;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_message, container, false);
        }

        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMessageList.setLinearLayout();
        rvMessageList.setOnPullLoadMoreListener(this);
        messageAdapter = new MessageAdapter();
        messageAdapter.setOnItemClickListener(this::onItemClick);
        rvMessageList.setAdapter(messageAdapter);
        list.add(new MessageEntity());
        list.add(new MessageEntity());
        list.add(new MessageEntity());
        messageAdapter.setMessageList(list);

    }

    @Override
    public void requestData() {
//        onRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onRefresh() {
        pageNum = 0;
        presenter.getMessageList(pageNum);
    }

    @Override
    public void onLoadMore() {
        ToastUtils.showLong(context,"没有更多");
        rvMessageList.setPullLoadMoreCompleted();
    }

    @Override
    public void setPresenter() {
        presenter = new MessagePresenter(getContext(), this);
    }

    @Override
    public void onItemClick(int optType, int position,boolean yesOrNo) {
        Bundle bundle = new Bundle();
        bundle.putString("msg_content",messageAdapter.getMessageList().get(position).messageContent);
        toActivity(MessageDetailActivity.class);
    }
}
