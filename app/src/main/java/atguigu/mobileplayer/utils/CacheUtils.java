package atguigu.mobileplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import atguigu.mobileplayer.service.MusicPlayerService;

/**
 * 作者：尚硅谷-杨光福 on 2016/10/8 10:37
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：缓存工具类-共享偏好
 */
public class CacheUtils {
    /**
     * 缓存文本数据
     * @param context
     * @param key
     * @param values
     */
    public static void putString(Context context, String key, String values) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putString(key,values).commit();
    }

    /**
     * 得到缓存文本信息
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }

    /**
     * 保存模式模式
     * @param context
     * @param key
     * @param value
     */
    public static void savePlaymode(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).commit();
    }

    /**
     * 得到播放模式
     * @param context
     * @param key
     * @return
     */
    public static int getPlaymode(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getInt(key, MusicPlayerService.REPEAT_NORMAL);
    }
}
