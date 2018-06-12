package us.xingkong.flyu.welcome;

import us.xingkong.flyu.DownloadModel;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.base.OnRequestListener;
import us.xingkong.flyu.dynamic.DynamicModel;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/10 18:58
 * @描述:
 * @更新日志:
 */
public class WelcomePresenter implements WelcomeContract.Presenter,
        OnRequestListener<DownloadModel> {

    private WelcomeContract.View mView;
    private DynamicModel model;
    private UserModel user;


    WelcomePresenter(WelcomeContract.View view) {
        mView = view;
        mView.setPresenter(this);
        model = new DynamicModel();
        model.setOnRequestListener(this);
    }

    @Override
    public void load() {
        user = mView.getUser();
        if (user == null) {
            mView.toOtherActivity(null, null);
            return;
        }
        model.load(mView.getUser().getUsername());
    }

    @Override
    public void start() {
        load();
    }

    @Override
    public void destroy() {

    }

    @Override
    public void success(DownloadModel result) {
        mView.toOtherActivity(user, result);
    }

    @Override
    public void failure(String errorMsg) {
        mView.toOtherActivity(null, null);
    }
}
