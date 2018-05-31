package us.xingkong.flyu.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import us.xingkong.flyu.app.App;

/**
 * @作者: Xuer
 * @创建时间: 2018/5/29 19:45
 * @描述:
 * @更新日志:
 */
public abstract class BaseActivity<P extends BasePresenter>
        extends AppCompatActivity implements BaseView<P> {

    private Unbinder bind;
    private P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.addActivity(this);
        init();
        setContentView(bindLayout());
        bind = ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.start();
        }
    }

    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Context getContext() {
        return this;
    }

    /**
     * 返回Contract里的Presenter
     * @return presenter
     */
    protected P getPresenter() {
        return mPresenter;
    }

    /**
     * 绑定布局
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
        App.removeActivity(this);
    }
}
