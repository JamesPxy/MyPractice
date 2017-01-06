package mypractice.pxy.com.mypractice.utils;

/**
 * Created by Administrator on 2016/12/28.
 */

public class Utils {

    /**
     * 将首字母转化为大写字母
     * @param word
     * @return
     */
    public static String capitalize(final String word) {
        if (word.length() > 1) {
            return String.valueOf(word.charAt(0)).toUpperCase() + word.substring(1);
        }
        return word;
    }

}
