package us.xingkong.flyu.util;

/**
 * @作者: Xuer
 * @创建时间: 2018/5/7 20:30
 * @描述:
 * @更新日志:
 */
public class OkUtil {
//    private static OkUtil okUtils;
//    private static OkHttpClient okHttpClient;
//    private Handler mHandler;
//
//    private OkUtil() {
//        okHttpClient = new OkHttpClient();
//        okHttpClient.newBuilder().connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS);
//        mHandler = new Handler(Looper.getMainLooper());
//    }
//
//    private static OkUtil getInstance() {
//        if (okUtils == null) {
//            synchronized (OkUtil.class) {
//                if (okUtils == null) {
//                    okUtils = new OkUtil();
//                }
//            }
//        }
//        return okUtils;
//    }
//
//    public interface DataCallBack {
//        void onSuccess(String result) throws Exception;
//
//        void onFailure(Request request, IOException e);
//    }
//
//    public static void get(String url, Map<String, String> params, DataCallBack callBack) {
//        getInstance().Get(url, params, callBack);
//    }
//
//    public static void post(String url, Map<String, String> params, DataCallBack callBack) {
//        getInstance().Post(url, params, callBack);
//    }
//
//    private void Get(String url, Map<String, String> params, final DataCallBack callBack) {
//        if (params == null) {
//            params = new HashMap<>();
//        }
//        String Url = joint(url, params);
//        final Request request = new Request.Builder().url(Url).build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                failure(request, e, callBack);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String result = response.body().string();
//                    success(result, callBack);
//                } else {
//                    throw new IOException(response + "");
//                }
//            }
//        });
//    }
//
//    private void Post(String url, Map<String, String> params, final DataCallBack callBack) {
//        RequestBody body;
//        if (params == null) {
//            params = new HashMap<>();
//        }
//        FormBody.Builder builder = new FormBody.Builder();
//        for (Map.Entry<String, String> map : params.entrySet()) {
//            String key = map.getKey();
//            String value;
//            if (map.getValue() == null) {
//                value = "";
//            } else {
//                value = map.getValue();
//            }
//            builder.add(key, value);
//        }
//        body = builder.build();
//        final Request request = new Request.Builder().url(url).post(body).build();
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                failure(request, e, callBack);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String result = response.body().string();
//                    success(result, callBack);
//                } else {
//                    throw new IOException(response + "");
//                }
//            }
//        });
//    }
//
//    private void failure(final Request request, final IOException e, final DataCallBack callBack) {
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (callBack != null) {
//                    callBack.onFailure(request, e);
//                }
//            }
//        });
//    }
//
//    private void success(final String result, final DataCallBack callBack) {
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (callBack != null) {
//                    try {
//                        callBack.onSuccess(result);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }
//
//    private static String joint(String url, Map<String, String> params) {
//        StringBuffer sb = new StringBuffer(url);
//        boolean isFirst = true;
//        Set<Map.Entry<String, String>> set = params.entrySet();
//        for (Map.Entry<String, String> entry : set) {
//            if (isFirst && !url.contains("?")) {
//                isFirst = false;
//                sb.append("?");
//            } else {
//                sb.append("&");
//            }
//            sb.append(entry.getKey());
//            sb.append("=");
//            sb.append(entry.getValue());
//        }
//        Log.i("url", sb.toString());
//        return sb.toString();
//    }
}
