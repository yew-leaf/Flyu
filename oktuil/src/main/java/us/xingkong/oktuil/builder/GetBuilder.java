package us.xingkong.oktuil.builder;

import java.util.Map;
import java.util.Set;

import okhttp3.Request;
import us.xingkong.oktuil.OkUtil;
import us.xingkong.oktuil.callback.Callback;
import us.xingkong.oktuil.response.ResponseInterface;


/**
 * @作者: Xuer
 * @创建时间: 2018/6/4 23:16
 * @描述:
 * @更新日志:
 */
public class GetBuilder extends BaseRequestBuilderWithParams<GetBuilder> {

    public GetBuilder(OkUtil okUtil) {
        super(okUtil);
    }

    @Override
    public void enqueue(ResponseInterface responseInterface) {
        try {
            if (mUrl == null || mUrl.length() == 0) {
                throw new IllegalArgumentException("Null Url");
            }

            if (mParams != null && mParams.size() > 0) {
                mUrl = appendParams(mUrl, mParams);
            }

            Request.Builder builder = new Request.Builder().url(mUrl).get();
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
            responseInterface.onFailure(0, e.toString());
        }
    }

    private String appendParams(String url, Map<String, String> params) {
        StringBuffer sb = new StringBuffer(url);
        boolean isFirst = true;
        Set<Map.Entry<String, String>> set = params.entrySet();
        for (Map.Entry<String, String> entry : set) {
            if (isFirst && !url.contains("?")) {
                isFirst = false;
                sb.append("?");
            } else {
                sb.append("&");
            }
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
        }
        return sb.toString();
    }
}
