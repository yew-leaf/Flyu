package us.xingkong.flyu.main;

import android.app.Activity;
import android.content.Intent;

import java.io.Serializable;
import java.util.List;

import us.xingkong.flyu.PhotoBean;
import us.xingkong.flyu.adapter.PhotosAdapter;
import us.xingkong.flyu.browse.Browse;

/**
 * @作者: Xuer
 * @创建时间: 2018/5/29 20:09
 * @描述:
 * @更新日志:
 */
public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private Activity activity;
    private PhotosAdapter mAdapter;

    MainPresenter(MainContract.View view) {
        mView = view;
        mView.setPresenter(this);
        activity = mView.getActivity();
    }

    @Override
    public void display(final List<PhotoBean> list) {
        mAdapter = new PhotosAdapter(activity, list);
        mView.setAdapter(mAdapter);
        mAdapter.setItemClickListener(new PhotosAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position < list.size()) {
                    PhotoBean bean = list.get(position);
                    bean.setPosition(position);
                    Intent intent = new Intent(activity, Browse.class);
                    intent.putExtra("photo", (Serializable) list);
                    intent.putExtra("bean", bean);
                    mView.toOtherActivity(intent);
                }
            }
        });
    }

    @Override
    public void start() {
        mView.setFooterView();
    }

    @Override
    public void destroy() {
        //mAdapter.clear();
    }
}
