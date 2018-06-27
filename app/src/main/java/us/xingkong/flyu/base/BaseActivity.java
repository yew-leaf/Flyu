package us.xingkong.flyu.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import us.xingkong.flyu.R;

/**
 * @作者: Xuer
 * @创建时间: 2018/5/29 19:45
 * @描述:
 * @更新日志:
 */
public abstract class BaseActivity<P extends BasePresenter>
        extends AppCompatActivity implements BaseView, BGASwipeBackHelper.Delegate {

    protected BGASwipeBackHelper mSwipeBackHelper;
    protected Unbinder unbinder;
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initSwipeBackFinish();
        setContentView(bindLayout());

        unbinder = ButterKnife.bind(this);
        mPresenter = newPresenter();

        initView();
        initData();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);
        // 设置滑动返回是否可用。默认值为 true
        //mSwipeBackHelper.setSwipeBackEnable(false);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(false);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     * @return 是否支持滑动返回
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    /**
     * 正在滑动返回
     * @param slideOffset 从0到1
     */
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {

    }

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    @Override
    public void onSwipeBackLayoutCancel() {

    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    /**
     * 返回presenter，可以为null
     *
     * @return 子类返回的presenter
     */
    protected abstract P newPresenter();

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
    }
}
