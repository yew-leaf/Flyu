package us.xingkong.flyu.container;

import us.xingkong.flyu.base.BasePresenter;
import us.xingkong.flyu.base.BaseView;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/5 20:55
 * @描述:
 * @更新日志:
 */
public interface ContainerContract {

    interface Presenter extends BasePresenter{

    }

    interface View extends BaseView<Presenter>{

    }
}
