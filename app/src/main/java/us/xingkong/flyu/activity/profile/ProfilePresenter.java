package us.xingkong.flyu.activity.profile;

import android.support.annotation.NonNull;

import us.xingkong.flyu.base.BasePresenterImpl;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 11:40
 * @描述:
 * @更新日志:
 */
public class ProfilePresenter extends BasePresenterImpl<ProfileContract.View>
        implements ProfileContract.Presenter {

    ProfilePresenter(@NonNull ProfileContract.View view) {
        super(view);
    }

    @Override
    public void subscribe() {
        super.subscribe();
        if (!mView.isActive()) {
            return;
        }
        mView.displayProfile();
    }

    @Override
    public void unSubscribe() {
        super.unSubscribe();
    }
}
