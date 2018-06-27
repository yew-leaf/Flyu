package us.xingkong.flyu.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import us.xingkong.flyu.R;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/11 21:45
 * @描述:
 * @更新日志:
 */
public abstract class DialogUtil /*extends AlertDialog.Builder*/ {

    public static AlertDialog.Builder aboutDialog(Activity activity) {
        return new AlertDialog.Builder(activity)
                .setTitle("关于这个app")
                .setMessage(R.string.about_text)
                .setCancelable(false)
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    public static AlertDialog.Builder aboutDialog(String title, Activity activity) {
        return new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(R.string.about_text)
                .setCancelable(false)
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    public static AlertDialog.Builder editDialog(Activity activity, String title) {
        return new AlertDialog.Builder(activity)
                .setTitle(title)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    public abstract void onPositiveListener(DialogInterface dialog, int which);

    /*private String title;
    private String message;

     public DialogUtil(@NonNull Context context) {
        super(context);

    public DialogUtil title(String title) {
        this.title = title;
        return this;
    }

    public DialogUtil message(String message) {
        this.message = message;
        return this;
    }

    public DialogUtil positiveListener() {
        setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
    }

    public void display() {
        setTitle(title);
        setMessage(message);
        setCancelable(false);
    }*/
}
