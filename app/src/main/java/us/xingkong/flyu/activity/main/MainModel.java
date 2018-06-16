package us.xingkong.flyu.activity.main;

import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import us.xingkong.flyu.model.UploadModel;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.app.Constants;
import us.xingkong.oktuil.OkUtil;
import us.xingkong.oktuil.response.GsonResponse;


/**
 * @作者: Xuer
 * @创建时间: 2018/6/4 17:14
 * @描述:
 * @更新日志:
 */
public class MainModel {

    private OnRequestListener mListener;
    //private String mResult;

    public interface OnRequestListener {
        void success(String result);

        void progress(long currentBytes, long targetBytes);

        void failure(String result);
    }

    public void setOnRequestListener(OnRequestListener listener) {
        mListener = listener;
    }

    public void uploadImageAndText(String name, String text, List<File> files) {
        if (text.length() == 0 || files.size() == 0) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("text", text);
        Map<String, File> fileMap = new LinkedHashMap<>();
        for (File file : files) {
            fileMap.put("img[]", file);
        }
        OkUtil okUtil = App.getInstance().getOkUtil();
        okUtil.upload()
                .url(Constants.UPLOAD_IMAGE_AND_TEXT)
                .params(params)
                .files(fileMap)
                .tag(this)
                /*.enqueue(new ResultResponse() {
                    @Override
                    public void onSuccess(int statusCode, String result) {
                        Log.e("MainModel", result);
                        //mResult = result;
                        if (!result.equals("error")) {
                            mListener.success("success");
                        } else
                            mListener.failure(result);
                    }

                    @Override
                    public void onFailure(int statusCode, String errorMsg) {
                        Log.e("errorMsg", errorMsg);
                        //mResult = Constants.NETWORK_IS_UNAVAILABLE;
                        mListener.failure(Constants.NETWORK_IS_UNAVAILABLE);
                    }
                });*/
                .enqueue(new GsonResponse<UploadModel>() {
                    @Override
                    public void onFailure(int statusCode, String errorMsg) {
                        mListener.failure(Constants.NETWORK_IS_UNAVAILABLE);
                    }

                    @Override
                    public void onProgress(long currentBytes, long targetBytes) {
                        mListener.progress(currentBytes, targetBytes);
                    }

                    @Override
                    public void onSuccess(int statusCode, String result, UploadModel response) {
                        Log.e("MainModel", result);
                        if (!result.equals("error")) {
                            mListener.success("Success");
                        } else {
                            mListener.failure(result);
                        }
                    }
                });
    }
}
