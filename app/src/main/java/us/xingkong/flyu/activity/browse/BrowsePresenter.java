package us.xingkong.flyu.activity.browse;


/**
 * @作者: Xuer
 * @创建时间: 2018/5/31 18:28
 * @描述:
 * @更新日志:
 */
public class BrowsePresenter implements BrowseContract.Presenter {

    private BrowseContract.View mView;

    BrowsePresenter(BrowseContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {

    }
}
