package mypractice.pxy.com.mypractice.entity;

import android.content.Context;

import mypractice.pxy.com.mypractice.internet.callback.HttpOnNextListener;
import mypractice.pxy.com.mypractice.internet.callback.MovieRequest;
import mypractice.pxy.com.mypractice.internet.http.cookie.BaseApi;
import rx.Observable;

/**
 * 测试数据
 * Created by WZG on 2016/7/16.
 */
public class SubjectPostApi extends BaseApi {
//    接口需要传入的参数 可自定义不同类型
    private boolean all;
    /*任何你先要传递的参数*/
//    String xxxxx;
//    String xxxxx;
//    String xxxxx;
//    String xxxxx;
    int start;
    int  count;


    /**
     * 默认初始化需要给定回调和rx周期类
     * 可以额外设置请求设置加载框显示，回调等（可扩展）
     * @param listener
     * @param rxAppCompatActivity
     */
    public SubjectPostApi(HttpOnNextListener listener, Context context) {
        super(listener,context);
        setShowProgress(true);
        setCancel(true);
        setCache(true);
        setMothed("top250?start=0&count=2");
        setCookieNetWorkTime(60);
        setCookieNoNetWorkTime(24*60*60);
    }



    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public Observable getObservable(MovieRequest methods) {
        return methods.getTopMovie(start,count);
    }
}
