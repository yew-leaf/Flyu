package us.xingkong.flyu.mine;

import java.util.Collections;
import java.util.List;

import us.xingkong.flyu.DownloadModel;
import us.xingkong.flyu.adapter.DynamicAdapter;
import us.xingkong.flyu.base.OnRequestListener;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/9 1:15
 * @描述:
 * @更新日志:
 */
public class MinePresenter implements MineContract.Presenter,
        OnRequestListener<DownloadModel> {

    private MineContract.View mView;
    private MineModel model;
    private List<DownloadModel.Message> mList;
    private DynamicAdapter mAdapter;

    MinePresenter(MineContract.View view) {
        mView = view;
        mView.setPresenter(this);
        model = new MineModel();
        model.setOnRequestListener(this);
    }

    @Override
    public void load() {
        mView.setRefresh(true);
        model.load(mView.getUsername());
    }

    @Override
    public void display() {
        mAdapter = new DynamicAdapter(mView.getContext(), mList);
        mAdapter.setUsername(mView.getUsername());
        mAdapter.notifyDataSetChanged();
        mView.setAdapter(mAdapter);
        mView.setRefresh(false);
    }

    @Override
    public void start() {
        if (!mView.isActive()) {
            return;
        }
        load();
        display();
    }

    @Override
    public void destroy() {

    }

    @Override
    public void success(DownloadModel result) {
        mList = result.getMessage();
        Collections.reverse(mList);
    }

    @Override
    public void failure(String errorMsg) {
        mView.showMessage(errorMsg);
    }
}
