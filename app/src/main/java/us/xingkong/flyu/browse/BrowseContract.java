package us.xingkong.flyu.browse;

import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.base.BaseView;

/**
 * @作者: Xuer
 * @创建时间: 2018/5/31 18:23
 * @描述:
 * @更新日志:
 */
public interface BrowseContract {

    interface Presenter extends BasePresenter{

    }

    interface View extends BaseView<Presenter>{
        void goBack();
    }
}
