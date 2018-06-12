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
public class DialogUtil {

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
}
