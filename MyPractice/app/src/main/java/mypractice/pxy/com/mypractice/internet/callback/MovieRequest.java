package mypractice.pxy.com.mypractice.internet.callback;

import java.util.List;

import mypractice.pxy.com.mypractice.entity.HttpResult;
import mypractice.pxy.com.mypractice.entity.Subject;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/1/4.
 */
public interface MovieRequest {
    //最原始的
//    @GET("top250")
//    Call<HttpResult> getTopMovie(@Query("start") int start, @Query("count") int count);

    //引入rxjava后的
//    @GET("top250")
//    Observable<HttpResult> getTopMovie(@Query("start") int start, @Query("count") int count);

    @GET("top250")
    Observable<HttpResult<List<Subject>>> getTopMovie(@Query("start") int start, @Query("count") int count);
}
