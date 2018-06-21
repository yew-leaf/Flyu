package us.xingkong.flyu.base;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/16 13:32
 * @描述:
 * @更新日志:
 */
public class BasePresenterImpl<V> implements BasePresenter {

    private Reference<V> mReference;
    protected V mView;
    protected CompositeDisposable mCompositeDisposable;


    protected BasePresenterImpl(V view) {
        mReference = new SoftReference<>(view);
        mView = mReference.get();
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        if (checkReference(mReference)) {
            mReference.clear();
        }
        mCompositeDisposable.clear();
    }

    protected static boolean checkReference(Reference reference) {
        return reference != null && reference.get() != null;
    }

    protected boolean isAttached() {
        return mReference != null && mReference.get() != null;
    }
}
