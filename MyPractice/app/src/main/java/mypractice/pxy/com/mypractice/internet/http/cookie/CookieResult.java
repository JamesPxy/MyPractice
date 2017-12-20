package mypractice.pxy.com.mypractice.internet.http.cookie;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * post請求缓存数据
 */
@Entity
public class CookieResult {
    @Id
    private Long id;
    /*url*/
    private String url;
    /*返回结果*/
    private String result;
    /*时间*/
    private long time;

    @Generated(hash = 43459054)
    public CookieResult() {
    }
    public CookieResult(String url, String result, long time) {
        this.url = url;
        this.result = result;
        this.time = time;
    }
    @Generated(hash = 430401114)
    public CookieResult(Long id, String url, String result, long time) {
        this.id = id;
        this.url = url;
        this.result = result;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
