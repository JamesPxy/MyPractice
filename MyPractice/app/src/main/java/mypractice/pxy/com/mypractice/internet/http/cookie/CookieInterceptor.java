package mypractice.pxy.com.mypractice.internet.http.cookie;


import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;

import mypractice.pxy.com.mypractice.utils.CookieDbUtil;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * gson持久化截取保存数据
 */
public class CookieInterceptor implements Interceptor {
    private String TAG = "CookieInterceptor";
    private CookieDbUtil dbUtil;
    /*是否缓存标识*/
    private boolean cache;

    public CookieInterceptor(boolean cache) {
        dbUtil = CookieDbUtil.getInstance();
        this.cache = cache;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (cache) {
            if (bodyEncoded(response.headers())) {
                // TODO: 2017/12/19  内容部分被压缩了  gzip 需要特殊处理一下
                Log.e(TAG, "body  has  been  encoded");
            }
            ResponseBody body = response.body();
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = Charset.defaultCharset();
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            String bodyString = buffer.clone().readString(charset);
//            response.request().url()
            String url = response.request().url().toString().split("\\?")[0];
//            String array[]=url.split("\\?");
//            Log.i(TAG, "intercept: url0--"+array[0]);
//            if(null!=array[0]){
//                url=array[0];
//            }
            CookieResult result = dbUtil.queryCookieBy(url);
            long time = System.currentTimeMillis();
            /*保存和更新本地数据*/
            if (result == null) {
                result = new CookieResult(url, bodyString, time);
                dbUtil.saveCookie(result);
            } else {
                result.setResult(bodyString);
                result.setTime(time);
                dbUtil.updateCookie(result);
            }
        }
        return response;
    }

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

}
