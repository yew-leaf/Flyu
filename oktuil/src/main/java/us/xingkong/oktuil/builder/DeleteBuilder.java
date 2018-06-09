package us.xingkong.oktuil.builder;

import okhttp3.Request;
import us.xingkong.oktuil.OkUtil;
import us.xingkong.oktuil.callback.Callback;
import us.xingkong.oktuil.response.ResponseInterface;


/**
 * @作者: Xuer
 * @创建时间: 2018/6/4 20:53
 * @描述:
 * @更新日志:
 */
public class DeleteBuilder extends BaseRequestBuilder<DeleteBuilder> {

    public DeleteBuilder(OkUtil okUtil) {
        super(okUtil);
    }

    @Override
    public void enqueue(ResponseInterface responseInterface) {
        try {
            if (mUrl == null || mUrl.length() == 0) {
                throw new IllegalArgumentException("Null Url");
            }

            Request.Builder builder = new Request.Builder().url(mUrl).delete();
            appendHeaders(builder, mHeaders);

            if (mTag != null) {
                builder.tag(mTag);
            }

            Request request = builder.build();

            OkUtil.getOkHttpClient()
                    .newCall(request)
                    .enqueue(new Callback(responseInterface));
        } catch (Exception e) {
            e.printStackTrace();
            responseInterface.onFailure(0, e.getMessage());
        }
    }
}
