package us.xingkong.flyu.activity.main;


import android.support.annotation.NonNull;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.app.Constants;
import us.xingkong.flyu.model.UploadModel;
import us.xingkong.flyu.rx.RxSchedulers;
import us.xingkong.flyu.util.RetrofitUtil;
import us.xingkong.oktuil.builder.UploadBuilder;


/**
 * @作者: Xuer
 * @创建时间: 2018/6/4 17:14
 * @描述:
 * @更新日志:
 */
public class MainModel {

    public Observable<UploadModel> uploadImageAndText(@NonNull String name, @NonNull String text, @NonNull List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        builder.addFormDataPart("name", name);
        builder.addFormDataPart("text", text);
        for (File file : files) {
            String fileName = file.getName();
            RequestBody requestBody = RequestBody.create(MediaType.parse(getContentType(fileName)), file);
            builder.addFormDataPart("img[]", fileName, requestBody);
        }

        return RetrofitUtil
                .getInstance()
                .url(Constants.BASE_UPLOAD_DOWNLOAD_URL)
                .create()
                .uploadImageAndText(builder.build())
                .compose(RxSchedulers.<UploadModel>compose());
    }

    public UploadBuilder upload(@NonNull String name, @NonNull String text, @NonNull List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        builder.addFormDataPart("name", name);
        builder.addFormDataPart("text", text);
        for (File file : files) {
            String fileName = file.getName();
            RequestBody requestBody = RequestBody.create(MediaType.parse(getContentType(fileName)), file);
            builder.addFormDataPart("img[]", fileName, requestBody);
        }

        return App
                .getInstance()
                .getOkUtil()
                .upload()
                .url(Constants.BASE_UPLOAD_DOWNLOAD_URL+"api/send")
                .addBody(builder);
    }

    private String getContentType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
