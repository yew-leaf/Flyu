package us.xingkong.flyu.activity.welcome;

import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.base.BasePresenterImpl;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/10 18:58
 * @描述:
 * @更新日志:
 */
public class WelcomePresenter extends BasePresenterImpl<WelcomeContract.View>
        implements WelcomeContract.Presenter {

    WelcomePresenter(WelcomeContract.View view) {
        super(view);
    }

    @Override
    public void loadUser() {
        if (mView.getUserState()) {
            mView.showMessage("初次见面");
            return;
        }
        UserModel user = mView.getUser();
        if (user == null) {
            mView.toOtherActivity(null);
            return;
        }
        mView.toOtherActivity(user);

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
