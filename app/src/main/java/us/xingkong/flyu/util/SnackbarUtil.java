package us.xingkong.flyu.util;

import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import us.xingkong.flyu.R;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/10 0:49
 * @描述:
 * @更新日志:
 */
public class SnackbarUtil {
    private static int background = 0xff1d8ae7;
    private static int textColor = 0xfffafafa;

    public static Snackbar shortSnackbar(View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        setSnackbarColor(snackbar, textColor, background);
        return snackbar;
    }

    public static void setSnackbarColor(Snackbar snackbar, int textColor, int backgroundColor) {
        View view = snackbar.getView();
        view.setBackgroundColor(backgroundColor);
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(textColor);
    }

    public static void SnackbarAddView(Snackbar snackbar, int layoutId, int index) {
        View view = snackbar.getView();
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) view;

        View add = LayoutInflater.from(view.getContext()).inflate(layoutId, null);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.gravity = Gravity.CENTER_VERTICAL;

        snackbarLayout.addView(add, index, p);
    }
}
