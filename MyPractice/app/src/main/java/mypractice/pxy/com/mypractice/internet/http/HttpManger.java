package mypractice.pxy.com.mypractice.internet.http;

import java.util.List;
import java.util.concurrent.TimeUnit;

import mypractice.pxy.com.mypractice.MyApplication;
import mypractice.pxy.com.mypractice.entity.HttpResult;
import mypractice.pxy.com.mypractice.entity.Subject;
import mypractice.pxy.com.mypractice.internet.callback.HttpService;
import mypractice.pxy.com.mypractice.internet.http.cookie.CacheInterceptor;
import mypractice.pxy.com.mypractice.internet.http.cookie.CookieInterceptor;
import mypractice.pxy.com.mypractice.internet.http.cookie.RetryWhenNetworkException;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/1/4.
 */

public class HttpManger {

    private static HttpManger mInstance;

    /*请求通用  url*/
    public static final String BASE_URL = "https://api.douban.com/v2/movie/";
    /*超时时间-默认5秒*/
    private static final int DEFAULT_CONNECT_TIMEOUT = 5;

    /*有网情况下的本地缓存时间默认60秒*/
    private int cookieNetWorkTime=60;
    /*无网络的情况下本地缓存时间默认30天*/
    private int cookieNoNetWorkTime=24*60*60*30;

    private Retrofit retrofit;
    private HttpService httpService;

    private HttpManger() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.addInterceptor(new CacheInterceptor());//get缓存方式拦截器
        builder.addNetworkInterceptor(new CookieInterceptor(true));//缓存请求返回数据
        builder.cache(new Cache(MyApplication.app.getCacheDir(),10*1024*1024));//缓存位置和大小 10M

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                //对http请求结果进行统一的预处理 GosnResponseBodyConvert
//                .addConverterFactory(ResponseConvertFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        httpService = retrofit.create(HttpService.class);

    }

    /**
     * 双重校验锁  构建单例
     *
     * @return
     */
    public static final HttpManger getInstance() {
        if (mInstance == null) {
            synchronized (HttpManger.class) {
                if (mInstance == null) {
                    mInstance = new HttpManger();
                }
            }
        }
        return mInstance;
    }

    public Retrofit  getRetrofit(){
        return this.retrofit;
    }

    /**
     * 用于获取豆瓣电影Top250的数据
     *
     * @param subscriber 由调用者传过来的观察者对象
     * @param start      起始位置
     * @param count      获取长度
     */
    public void getTopMovie(Subscriber<List<Subject>> subscriber, int start, int count) {

//        httpService.getTopMovie(start, count)
//                .map(new HttpResultFunc<List<Subject>>())
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);

//        HttpResultFunc<List<HttpResult>> result = new HttpResultFunc<List<HttpResult>>();

        Observable observable = httpService.getTopMovie(start, count)
                .map(new HttpResultFunc<List<Subject>>());

        initSubscribe(observable, subscriber);
    }

    public  <T> void initSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                 /*失败后的retry配置*/
                .retryWhen(new RetryWhenNetworkException())
//                .compose()
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    public static class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getCount() == 0) {
                throw new HttpException(100);
            }
            return httpResult.getSubjects();
        }
    }

}
