package us.xingkong.flyu.base;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import us.xingkong.flyu.app.App;
import us.xingkong.oktuil.OkUtil;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/5 21:24
 * @描述:
 * @更新日志:
 */
public abstract class BaseFragment<P extends BasePresenter>
        extends Fragment implements BaseView<P> {

    protected Context mContext;
    protected Activity mActivity;
    protected Unbinder bind;
    protected OkUtil mOkUtil;
    protected P mPresenter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        mOkUtil = App.getInstance().getOkUtil();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(bindLayout(), container, false);
        bind = ButterKnife.bind(this, root);
        initView(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.start();
        }
    }

    protected P getPresenter() {
        return mPresenter;
    }

    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public Context getContext() {
        return mContext;
    }

    /**
     * 绑定布局
     *
     * @return 布局id
     */
    protected abstract int bindLayout();

    /**
     * 需要在onCreate调用的方法
     */
    protected abstract void init();

    /**
     * 初始化View
     */
    protected abstract void initView(View root);

    /**
     * 获取数据
     */
    protected abstract void initData();

    /**
     * 普通控件的监听事件
     */
    protected abstract void initListener();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
        mOkUtil.cancel(this);
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }
}
