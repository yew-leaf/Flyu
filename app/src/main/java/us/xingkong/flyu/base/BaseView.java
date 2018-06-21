package us.xingkong.flyu.base;

import android.app.Activity;
import android.content.Context;

/**
 * @作者: Xuer
 * @创建时间: 2018/5/29 19:45
 * @描述:
 * @更新日志:
 */
public interface BaseView {
    Context getContext();

    Activity getActivity();

    void showMessage(String message);
}
