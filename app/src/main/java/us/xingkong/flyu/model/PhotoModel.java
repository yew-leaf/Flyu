package us.xingkong.flyu.model;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.Serializable;

/**
 * @作者: Xuer
 * @包名: us.xingkong.flyu
 * @类名: PhotoBean
 * @创建时间: 2018/5/12 22:12
 * @最后修改于:
 * @版本: 1.0
 * @描述:
 * @更新日志:
 */
public class PhotoModel implements Serializable {
    private String uri;
    private int position;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static String convertPath(Uri uri, Activity activity) {
        String[] project = {MediaStore.Video.Media.DATA};
        Cursor cursor = activity.managedQuery(uri, project, null, null, null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);
    }
}
