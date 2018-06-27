package us.xingkong.flyu.activity.splash;

import us.xingkong.flyu.base.BasePresenterImpl;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/10 18:58
 * @描述:
 * @更新日志:
 */
public class SplashPresenter extends BasePresenterImpl<SplashContract.View>
        implements SplashContract.Presenter {

    SplashPresenter(SplashContract.View view) {
        super(view);
    }

    @Override
    public void loadUser() {
        /*if (mView.getUserState()) {
            mView.showMessage("初次见面");
            return;
        }
        UserModel user = mView.getUser();
        if (user == null) {
            mView.toOtherActivity(null);
            return;
        }
        mView.toOtherActivity(user);*/

        if (mView.getBmobUser() == null) {
            mView.showMessage("初次见面");
            return;
        }
        mView.toOtherActivity();
    }

    @Override
    public void subscribe() {
        super.subscribe();
        mView.applyPermissions();
    }

    @Override
    public void unSubscribe() {
        super.unSubscribe();
    }
}
