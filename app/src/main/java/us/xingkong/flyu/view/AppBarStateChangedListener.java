package us.xingkong.flyu.view;

import android.support.design.widget.AppBarLayout;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 11:47
 * @描述:
 * @更新日志:
 */
public abstract class AppBarStateChangedListener implements AppBarLayout.OnOffsetChangedListener {

    public enum State {
        EXPANDED,
        COLLAPSED,
        COLLAPSING
    }

    private State currentState = State.COLLAPSING;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            if (currentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            currentState = State.EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (currentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            currentState = State.COLLAPSED;
        }else {
            if (currentState != State.COLLAPSING) {
                onStateChanged(appBarLayout, State.COLLAPSING);
            }
            currentState = State.COLLAPSING;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
}
