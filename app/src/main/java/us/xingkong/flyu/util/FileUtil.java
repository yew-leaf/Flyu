package us.xingkong.flyu.util;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/24 11:01
 * @描述:
 * @更新日志:
 */
public class FileUtil {

    public static String convertPath(Activity activity, Uri uri) {
        String[] project = {MediaStore.Video.Media.DATA};
        String path = null;
        Cursor cursor = activity.getContentResolver().query(uri, project, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}
