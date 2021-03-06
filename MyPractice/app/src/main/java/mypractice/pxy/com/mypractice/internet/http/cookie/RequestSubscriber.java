package mypractice.pxy.com.mypractice.internet.http.cookie;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import java.lang.ref.SoftReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import mypractice.pxy.com.mypractice.MyApplication;
import mypractice.pxy.com.mypractice.internet.callback.HttpOnNextListener;
import mypractice.pxy.com.mypractice.internet.http.HttpException;
import mypractice.pxy.com.mypractice.utils.AppUtil;
import mypractice.pxy.com.mypractice.utils.CookieDbUtil;
import rx.Observable;
import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by WZG on 2016/7/16.
 */
public class RequestSubscriber<T> extends Subscriber<T> {
    /*是否弹框*/
    private boolean showDialog = true;
    //    回调接口
    private SoftReference<HttpOnNextListener> mSubscriberOnNextListener;
    //    弱引用防止内存泄露
    private SoftReference<Context> mActivity;
    //    加载框可自己定义
    private ProgressDialog pd;
    /*请求数据*/
    private BaseApi api;


    /**
     * 构造
     *
     * @param api
     */
    public RequestSubscriber(BaseApi api) {
        this.api = api;
        this.mSubscriberOnNextListener = api.getListener();
        this.mActivity = new SoftReference<>(api.getContext());
        setShowDialog(api.isShowProgress());
        if (api.isShowProgress()) {
            initProgressDialog(api.isCancel());
        }
    }


    /**
     * 初始化加载框
     */
    private void initProgressDialog(boolean cancel) {
        Context context = mActivity.get();
        if (pd == null && context != null) {
            pd = new ProgressDialog(context);
            pd.setCancelable(cancel);
            if (cancel) {
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        onCancelProgress();
                    }
                });
            }
        }
    }


    /**
     * 显示加载框
     */
    private void showProgressDialog() {
        if (!isShowDialog()) return;
        Context context = mActivity.get();
        if (pd == null || context == null) return;
        if (!pd.isShowing()) {
            pd.show();
        }
    }


    /**
     * 隐藏
     */
    private void dismissProgressDialog() {
        if (!isShowDialog()) return;
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }


    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        showProgressDialog();
        if(api.getCacheStrategy()== BaseApi.CACHE_CONTROL.FORCE_INTERNET){
            return;
        }
        /*缓存并且有网*/
        if (api.isCache() && AppUtil.isNetworkAvailable(MyApplication.app)) {
             /*通过url获取缓存数据*/
            CookieResult cookieResult = CookieDbUtil.getInstance().queryCookieBy(api.getUrl());
            if (cookieResult != null) {
                long time = (System.currentTimeMillis() - cookieResult.getTime()) / 1000;
                if (time < api.getCookieNetWorkTime()) {//有网络情况下 缓存时间没有超过一分钟  则优先从缓存中取数据
                    if (mSubscriberOnNextListener.get() != null) {
                        mSubscriberOnNextListener.get().onCacheNext(cookieResult.getResult());
                    }
                    onCompleted();
                    unsubscribe();
                }
            }
        }
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        /*需要緩存并且本地有缓存才返回*/
        if (api.isCache()) {
            Observable.just(api.getUrl()).subscribe(new Subscriber<String>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    doErrorBiz(e);
                }

                @Override
                public void onNext(String s) {
                    /*获取缓存数据*/
                    CookieResult cookieResult = CookieDbUtil.getInstance().queryCookieBy(s);
                    if (cookieResult == null) {
                        throw new HttpException("网络异常，请检查网络设置");
                    }
                    long time = (System.currentTimeMillis() - cookieResult.getTime()) / 1000;
                    if (time < api.getCookieNoNetWorkTime()) {
                        if (mSubscriberOnNextListener.get() != null) {
                            mSubscriberOnNextListener.get().onCacheNext(cookieResult.getResult());
                        }
                    } else {
                        CookieDbUtil.getInstance().deleteCookie(cookieResult);
                        throw new HttpException("网络异常，请检查网络设置");
                    }
                }
            });
        } else {
            doErrorBiz(e);
        }
    }

    /*错误统一处理*/
    private void doErrorBiz(Throwable e) {
        Context context = mActivity.get();
        if (context == null) return;
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "SocketTimeoutException网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(context, "ConnectException网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "doErrorBiz onError---" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onError(e);
        }
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onNext(t);
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }


    public boolean isShowDialog() {
        return showDialog;
    }

    /**
     * 是否需要弹框设置
     *
     * @param showDialog
     */
    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }
}