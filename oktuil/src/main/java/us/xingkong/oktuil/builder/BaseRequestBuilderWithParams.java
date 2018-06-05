package us.xingkong.oktuil.builder;

import java.util.LinkedHashMap;
import java.util.Map;

import us.xingkong.oktuil.OkUtil;


/**
 * @作者: Xuer
 * @创建时间: 2018/6/4 22:50
 * @描述:
 * @更新日志:
 */
public abstract class BaseRequestBuilderWithParams<T extends BaseRequestBuilderWithParams>
        extends BaseRequestBuilder<T> {

    Map<String, String> mParams;

    BaseRequestBuilderWithParams(OkUtil okUtil) {
        super(okUtil);
    }

    public T params(Map<String, String> params) {
        mParams = params;
        return (T) this;
    }

    public T addParam(String key, String value) {
        if (mParams == null) {
            mParams = new LinkedHashMap<>();
        }
        mParams.put(key, value);
        return (T) this;
    }
}
