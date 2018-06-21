package us.xingkong.flyu.activity.dynamic;


import android.support.annotation.NonNull;

import io.reactivex.Observable;
import us.xingkong.flyu.rx.RxSchedulers;
import us.xingkong.flyu.app.Constants;
import us.xingkong.flyu.model.DownloadModel;
import us.xingkong.flyu.util.RetrofitUtil;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 19:19
 * @描述:
 * @更新日志:
 */
public class DynamicModel {

    public Observable<DownloadModel> loadDynamic(@NonNull String name) {
        return RetrofitUtil
                .getInstance()
                .url(Constants.BASE_UPLOAD_DOWNLOAD_URL)
                .create()
                .loadDynamic(name)
                .compose(RxSchedulers.<DownloadModel>compose());
    }
}
