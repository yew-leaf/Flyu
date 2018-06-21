package us.xingkong.flyu.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/21 20:59
 * @描述:
 * @更新日志:
 */
public class T {

    public static void shortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
