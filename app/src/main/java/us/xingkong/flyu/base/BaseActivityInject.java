package us.xingkong.flyu.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.di.component.ActivityComponent;
import us.xingkong.flyu.di.component.DaggerActivityComponent;
import us.xingkong.flyu.di.module.ActivityModule;

/**
 * @作者: Xuer
 * @创建时间: 2018/5/29 19:45
 * @描述:
 * @更新日志:
 */
public abstract class BaseActivityInject<P extends BasePresenter>
        extends AppCompatActivity implements BaseView {

    protected Unbinder unbinder;
    @Inject
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //App.addActivity(this);
        init();
        setContentView(bindLayout());

        unbinder = ButterKnife.bind(this);
        //mPresenter = newPresenter();

        initInject();
        initView();
        initData();
        initListener();
    }

    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder()
                .appComponent(App.getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Context getContext() {
        return this;
    }


    protected abstract void initInject();

    /**
     * 绑定布局
     *
     * @return 布局id
     */
    protected abstract int bindLayout();

    /**
     * 需要在setContentView()之前调用的方法
     */
    protected abstract void init();

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 获取数据
     */
    protected abstract void initData();

    /**
     * 普通控件的监听事件
     */
    protected abstract void initListener();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        //App.removeActivity(this);
    }
}
