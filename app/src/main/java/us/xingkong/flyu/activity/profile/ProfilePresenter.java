package us.xingkong.flyu.activity.profile;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 11:40
 * @描述:
 * @更新日志:
 */
public class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.View mView;

    ProfilePresenter(ProfileContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if (!mView.isActive()) {
            return;
        }
        mView.display();
    }

    @Override
    public void destroy() {

    }
}
