package us.xingkong.flyu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @作者: Xuer
 * @包名: us.xingkong.flyu
 * @类名: ViewPager
 * @创建时间: 2018/5/12 23:48
 * @最后修改于:
 * @版本: 1.0
 * @描述:
 * @更新日志:
 */
public class ViewPager extends android.support.v4.view.ViewPager {

    public ViewPager(@NonNull Context context) {
        super(context);
    }

    public ViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
