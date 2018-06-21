package us.xingkong.flyu.util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import us.xingkong.flyu.app.Api;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/16 20:42
 * @描述:
 * @更新日志:
 */
public class RetrofitUtil {

    private volatile static RetrofitUtil mRetrofitUtil;
    private static OkHttpClient mOkHttpClient;
    private String mUrl;


    public static RetrofitUtil getInstance() {
        if (mRetrofitUtil == null) {
            synchronized (RetrofitUtil.class) {
                if (mRetrofitUtil == null) {
                    mRetrofitUtil = new RetrofitUtil();
                }
            }
        }
        return mRetrofitUtil;
    }

    public RetrofitUtil url(String url) {
        mUrl = url;
        return mRetrofitUtil;
    }

    public Api create() {
        return new Retrofit.Builder()
                .client(mOkHttpClient)
                .baseUrl(mUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(Api.class);
    }

    private RetrofitUtil() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                /*.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder builder = chain.request().newBuilder();
                        //在这里可以处理token失效时,重新获取token，或者添加header等等
                        return chain.proceed(builder.build());
                    }
                })*/
                .addInterceptor(new HttpLoggingInterceptor(
                        new HttpLoggingInterceptor.Logger() {
                            @Override
                            public void log(String message) {
                                L.i("RetrofitUtil", message);
                            }
                        })
                        .setLevel(HttpLoggingInterceptor.Level.BASIC)
                )
                .build();
    }
}
