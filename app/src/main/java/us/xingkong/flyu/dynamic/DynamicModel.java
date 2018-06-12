package us.xingkong.flyu.dynamic;

import android.util.Log;

import com.google.gson.Gson;

import us.xingkong.flyu.DownloadModel;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.app.Constants;
import us.xingkong.flyu.base.OnRequestListener;
import us.xingkong.oktuil.OkUtil;
import us.xingkong.oktuil.response.ResultResponse;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 19:19
 * @描述:
 * @更新日志:
 */
public class DynamicModel {

    private OnRequestListener<DownloadModel> mListener;

    public void setOnRequestListener(OnRequestListener<DownloadModel> listener) {
        mListener = listener;
    }

    public void load(String name) {
        if (name == null) {
            mListener.failure("Gson Error");
            return;
        }
        OkUtil okUtil = App.getInstance().getOkUtil();
        okUtil.post()
                .url(Constants.DOWNLOAD_IMAGE_AND_TEXT)
                .addParam("name", name)
                .tag(this)
                .enqueue(new ResultResponse() {
                    @Override
                    public void onSuccess(int statusCode, String result) {
                        if (!result.equals("error")) {
                            Log.i("DynamicModel", result);
                            Gson gson = new Gson();
                            DownloadModel downloadModel = gson.fromJson(result, DownloadModel.class);
                            mListener.success(downloadModel);
                        } else {
                            mListener.failure("Gson Error");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String errorMsg) {
                        mListener.failure(errorMsg);
                    }
                });
    }
}
