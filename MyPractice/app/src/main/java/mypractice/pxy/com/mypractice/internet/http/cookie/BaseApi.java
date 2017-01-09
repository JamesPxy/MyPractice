package mypractice.pxy.com.mypractice.internet.http.cookie;

import android.content.Context;

import java.lang.ref.SoftReference;

import mypractice.pxy.com.mypractice.entity.HttpResult;
import mypractice.pxy.com.mypractice.internet.callback.HttpOnNextListener;
import mypractice.pxy.com.mypractice.internet.callback.HttpService;
import mypractice.pxy.com.mypractice.internet.http.HttpException;
import rx.Observable;
import rx.functions.Func1;


/**
 * 请求数据统一封装类
 * Created by WZG on 2016/7/16.
 */
public abstract class BaseApi<T> implements Func1<HttpResult<T>, T> {
    //rx生命周期管理
    private SoftReference<Context> mContext;
    /*回调*/
    private SoftReference<HttpOnNextListener> listener;
    /*是否能取消加载框*/
    private boolean cancel;
    /*是否显示加载框*/
    private boolean showProgress;

    /*基础url*/
    private String baseUrl = "https://api.douban.com/v2/movie/";

    /*超时时间-默认6秒*/
    private int connectionTime = 6;
    //// TODO: 2017/1/9 重要属性
    /*有网情况下的本地缓存时间默认60秒  如果需要实时请求则  设置此时间为0*/
    private int cookieNetWorkTime = 60;
    /*无网络的情况下本地缓存时间默认30天*/
    private int cookieNoNetWorkTime = 24 * 60 * 60 * 30;
    /*是否需要缓存处理*/
    private boolean cache;
    /*方法-如果需要缓存必须设置这个参数；不需要不用設置   该方法名用于区分缓存数据对应请求接口*/
    private String mothed;

//    public static  final int  FORCE_CACHE=0;
//
//    public static  final int  FORCE_INTERNET=1;

    private CACHE_CONTROL cacheStrategy = CACHE_CONTROL.FORCE_INTERNET;

    public enum CACHE_CONTROL {
        FORCE_CACHE, //缓存优先
        FORCE_INTERNET;//网络优先
    }


    public BaseApi(HttpOnNextListener listener, Context context) {
        setListener(listener);
        setContext(context);
        setShowProgress(true);
        setCache(true);
    }


    public CACHE_CONTROL getCacheStrategy() {
        return cacheStrategy;
    }

    public void setCacheStrategy(CACHE_CONTROL strategy) {
        cacheStrategy = strategy;
    }

    /**
     * 设置参数
     *
     * @param methods
     * @return
     */
    public abstract Observable getObservable(HttpService methods);


    public int getCookieNoNetWorkTime() {
        return cookieNoNetWorkTime;
    }

    public void setCookieNoNetWorkTime(int cookieNoNetWorkTime) {
        this.cookieNoNetWorkTime = cookieNoNetWorkTime;
    }

    public int getCookieNetWorkTime() {
        return cookieNetWorkTime;
    }

    public void setCookieNetWorkTime(int cookieNetWorkTime) {
        this.cookieNetWorkTime = cookieNetWorkTime;
    }

    public String getMothed() {
        return mothed;
    }

    public int getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(int connectionTime) {
        this.connectionTime = connectionTime;
    }

    public void setMothed(String mothed) {
        this.mothed = mothed;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUrl() {
        return baseUrl + mothed;
    }

    public void setContext(Context mContext) {
        this.mContext = new SoftReference(mContext);
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public SoftReference<HttpOnNextListener> getListener() {
        return listener;
    }

    public void setListener(HttpOnNextListener listener) {
        this.listener = new SoftReference(listener);
    }

    /*
     * 获取当前rx生命周期
     * @return
     */
    public Context getContext() {
        return mContext.get();
    }


    /*
       处理返回数据  以便得到相要数据
     */
    @Override
    public T call(HttpResult<T> httpResult) {
        if (httpResult.getCount() == 0) {
            throw new HttpException(httpResult.getTitle());
        }
        return httpResult.getSubjects();
    }
}
