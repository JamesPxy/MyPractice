package mypractice.pxy.com.mypractice.internet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mypractice.pxy.com.mypractice.R;
import mypractice.pxy.com.mypractice.entity.HttpResult;
import mypractice.pxy.com.mypractice.entity.Subject;
import mypractice.pxy.com.mypractice.entity.SubjectPostApi;
import mypractice.pxy.com.mypractice.internet.callback.HttpOnNextListener;
import mypractice.pxy.com.mypractice.internet.callback.HttpService;
import mypractice.pxy.com.mypractice.internet.callback.SubscriberOnNextListener;
import mypractice.pxy.com.mypractice.internet.http.HttpManger;
import mypractice.pxy.com.mypractice.internet.http.ProgressSubscriber;
import mypractice.pxy.com.mypractice.internet.http.cookie.RequestSubscriber;
import rx.Observable;

/**
 * A login screen that offers login via email/password.
 */
public class TestNetActivity extends AppCompatActivity {


    @Bind(R.id.button)
    Button button;
    @Bind(R.id.tv_result)
    TextView tvResult;
    private SubscriberOnNextListener<List<Subject>> getTopMovieOnNext;

    private HttpOnNextListener<List<Subject>>   httpOnNextListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testnet);
        ButterKnife.bind(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRequest5();
            }
        });

    }

    /**
     * 法1  原始的网络请求
     */
//    private void doRequest1() {
//        String baseUrl = "https://api.douban.com/v2/movie/";
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        HttpService movieRequest = retrofit.create(HttpService.class);
//        Call<HttpResult> call = movieRequest.getTopMovie(0, 10);
//        call.enqueue(new Callback<HttpResult>() {
//            @Override
//            public void onResponse(Call<HttpResult> call, Response<HttpResult> response) {
//                tvResult.setText(response.body().toString());
//            }
//
//            @Override
//            public void onFailure(Call<HttpResult> call, Throwable t) {
//                tvResult.setText(t.getMessage());
//            }
//        });
//    }


    /**
     * 法2：  引入rxjava后的封装
     */
//    public void  doRequest2(){
//        String baseUrl = "https://api.douban.com/v2/movie/";
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//
//
//        HttpService movieRequest = retrofit.create(HttpService.class);
//
//        movieRequest.getTopMovie(0, 10)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<HttpResult>() {
//                    @Override
//                    public void onCompleted() {
//                        Toast.makeText(TestNetActivity.this, "Get Top Movie request Completed", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        tvResult.setText(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(HttpResult movieEntity) {
//                        tvResult.setText(movieEntity.toString());
//                    }
//                });
//    }


    /**
     * 封装之后
     */

    public void doRequest3() {

        getTopMovieOnNext = new SubscriberOnNextListener<List<Subject>>() {
            @Override
            public void onNext(List<Subject> subjects) {
                String  result="";
                for (Subject subject : subjects) {
                    result=result+subject.toString()+"\n\n";
                }
                tvResult.setText(result);
            }
        };
        HttpManger.getInstance().getTopMovie(new ProgressSubscriber(getTopMovieOnNext, TestNetActivity.this), 0, 3);
    }

    public void  doRequest4(){

        getTopMovieOnNext = new SubscriberOnNextListener<List<Subject>>() {
            @Override
            public void onNext(List<Subject> subjects) {
                String  result="";
                for (Subject subject : subjects) {
                    result=result+subject.toString()+"\n\n";
                }
                tvResult.setText(result);
            }
        };

        HttpManger httpManger = HttpManger.getInstance();

        HttpService httpService = httpManger.getRetrofit().create(HttpService.class);

        Observable observable = httpService.getTopMovie(3,6)
                .map(new HttpManger.HttpResultFunc<List<Subject>>());

        httpManger.initSubscribe(observable,new ProgressSubscriber(getTopMovieOnNext,TestNetActivity.this));
    }

    public void  doRequest5(){

        httpOnNextListener=new HttpOnNextListener<List<Subject>>() {
            @Override
            public void onNext(List<Subject> subjects) {
                String  result="";
                for (Subject subject : subjects) {
                    result=result+subject.toString()+"\n\n";
                }
                tvResult.setText("正常网络请求返回数据--"+result);
            }

            @Override
            public void onCacheNext(String cache) {
                super.onCacheNext(cache);
                /*缓存回调*/
                Gson gson=new Gson();
                Type type = new TypeToken<HttpResult<List<Subject>>>() {}.getType();
                HttpResult resultEntity= gson.fromJson(cache, type);
                tvResult.setText("缓存返回：\n"+resultEntity.getSubjects().toString() );
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                tvResult.setText("onError");
            }
            @Override
            public void onCancel() {
                super.onCancel();
                tvResult.setText("onCancel");
            }
        };



        SubjectPostApi postEntity = new SubjectPostApi(httpOnNextListener,TestNetActivity.this);
        postEntity.setStart(0);
        postEntity.setCount(3);
        // TODO: 2017/1/6
//        postEntity.setMothed();

        HttpManger.getInstance().getTopMovie(new RequestSubscriber<List<Subject>>(postEntity),0,2);

    }

}

