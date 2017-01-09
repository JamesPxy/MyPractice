package mypractice.pxy.com.mypractice.internet.callback;

import java.util.List;

import mypractice.pxy.com.mypractice.entity.HttpResult;
import mypractice.pxy.com.mypractice.entity.Subject;
import mypractice.pxy.com.mypractice.entity.UploadResulte;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2017/1/4.
 */
public interface HttpService {
    //最原始的
//    @GET("top250")
//    Call<HttpResult> getTopMovie(@Query("start") int start, @Query("count") int count);

    //引入rxjava后的
//    @GET("top250")
//    Observable<HttpResult> getTopMovie(@Query("start") int start, @Query("count") int count);

    @GET("top250")
    Observable<HttpResult<List<Subject>>> getTopMovie(@Query("start") int start, @Query("count") int count);



    /*断点续传下载接口*/
    @Streaming/*大文件需要加入这个判断，防止下载过程中写入到内存中*/
    @GET
    Observable<ResponseBody> download(@Header("RANGE") String start, @Url String url);

    /*上传文件*/
    @Multipart
    @POST("AppYuFaKu/uploadHeadImg")
    Observable<HttpResult<UploadResulte>> uploadImage(@Part("uid") RequestBody uid, @Part("auth_key") RequestBody  auth_key,
                                                            @Part MultipartBody.Part file);

}
