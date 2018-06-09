package us.xingkong.flyu.mine;

import java.util.List;

import us.xingkong.flyu.DownloadModel;
import us.xingkong.flyu.app.App;
import us.xingkong.flyu.app.Constants;
import us.xingkong.flyu.base.OnRequestListener;
import us.xingkong.oktuil.OkUtil;
import us.xingkong.oktuil.response.GsonResponse;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 19:19
 * @描述:
 * @更新日志:
 */
public class MineModel {

    private OnRequestListener<DownloadModel> mListener;

    public void setOnRequestListener(OnRequestListener<DownloadModel> listener) {
        mListener = listener;
    }

    public void load(String name) {
        OkUtil okUtil = App.getInstance().getOkUtil();
        okUtil.post()
                .url(Constants.DOWNLOAD_IMAGE_AND_TEXT)
                .addParam("name", name)
                .tag(this)
                .enqueue(new GsonResponse<DownloadModel>() {
                    @Override
                    public void onFailure(int statusCode, String errorMsg) {
                        mListener.failure(Constants.NETWORK_IS_UNAVAILABLE);
                    }

                    @Override
                    public void onSuccess(int statusCode, DownloadModel response) {
                        List<DownloadModel.Message> messages = response.getMessage();
                        if (messages != null && messages.size() > 0) {
                            mListener.success(response);
                        }
                    }
                });
    }
}
