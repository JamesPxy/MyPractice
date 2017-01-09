package mypractice.pxy.com.mypractice.entity;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import mypractice.pxy.com.mypractice.internet.callback.HttpOnNextListener;
import mypractice.pxy.com.mypractice.internet.callback.HttpService;
import mypractice.pxy.com.mypractice.internet.http.cookie.BaseApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * 上传请求api
 * Created by WZG on 2016/10/20.
 */

public class UploadApi<T> extends BaseApi<T> {
    /*需要上传的文件*/
    private MultipartBody.Part part;


    public UploadApi(HttpOnNextListener listener, RxAppCompatActivity rxAppCompatActivity) {
        super(listener, rxAppCompatActivity);
        setShowProgress(true);
        setMothed("AppFiftyToneGraph/videoLink");
    }

    public MultipartBody.Part getPart() {
        return part;
    }

    public void setPart(MultipartBody.Part part) {
        this.part = part;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        RequestBody uid= RequestBody.create(MediaType.parse("text/plain"), "4811420");
        RequestBody key = RequestBody.create(MediaType.parse("text/plain"), "cfed6cc8caad0d79ea56d917376dc4df");
        return methods.uploadImage(uid,key,getPart());
    }

}
