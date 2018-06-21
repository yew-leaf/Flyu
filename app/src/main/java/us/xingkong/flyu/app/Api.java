package us.xingkong.flyu.app;


import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import us.xingkong.flyu.model.DownloadModel;
import us.xingkong.flyu.model.UploadModel;


/**
 * @作者: Xuer
 * @创建时间: 2018/6/16 19:51
 * @描述:
 * @更新日志:
 */
public interface Api {
    @GET("signin")
    Observable<String> login(@Query("u") String username, @Query("p") String password);

    @GET("insert")
    Observable<String> register(@Query("u") String username, @Query("e") String email, @Query("p") String password);

    @GET("pwdchange")
    Observable<String> updatePassword(@Query("u") String name, @Query("p") String password, @Query("cp") String repassword);

    @POST("api/fetch")
    Observable<DownloadModel> loadDynamic(@Query("name") String name);

    @POST("api/send")
    Observable<UploadModel> uploadImageAndText(@Body MultipartBody multipartBody);
}
