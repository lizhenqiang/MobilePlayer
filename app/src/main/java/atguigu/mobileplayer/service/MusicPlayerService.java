package atguigu.mobileplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import atguigu.mobileplayer.AudioPlayerActivity;
import atguigu.mobileplayer.IMusicPlayerService;
import atguigu.mobileplayer.R;
import atguigu.mobileplayer.domain.MediaItem;
import atguigu.mobileplayer.utils.CacheUtils;

/**
 * Created by lzq on 2016/10/8.
 */
public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    public static final String OPENAUDIO = "com.atguigu.mobilepalyer.OPENAUDIO";


    /**
     * 当前播放列表中的位置
     */
    private int position=0;

    private ArrayList<MediaItem> mediaItems;
    private IMusicPlayerService.Stub stub = new IMusicPlayerService.Stub() {
        MusicPlayerService service =   MusicPlayerService.this;
        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public String getAudioName() throws RemoteException {
            return service.getAudioName();
        }

        @Override
        public String getArtistName() throws RemoteException {
            return service.getArtistName();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public void next() throws RemoteException {
            service.next();
        }

        @Override
        public void pre() throws RemoteException {
            service.pre();

        }

        @Override
        public int getPlayMode() throws RemoteException {
            return service.getPlayMode();
        }


        @Override
        public void setPlayMode(int mode) throws RemoteException {
            service.setPlayMode(mode);
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            service.seekTo(position );
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return mediaPlayer.isPlaying();
        }

        @Override
        public void notifyChange(String action) throws RemoteException {
            service.notifyChange(action);
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return mediaItem.getData();
        }

        @Override
        public int getAudioSessionId() throws RemoteException {

            Log.e("TAG", "getAudioSessionId"+mediaPlayer.getAudioSessionId());
            return mediaPlayer.getAudioSessionId();
        }


    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    /**
     * 顺序播放
     */
    public static final int REPEAT_NORMAL = 0;

    /**
     * 单曲循环
     */
    public static final int REPEAT_SINGLE = 1;


    /**
     * 全部循环
     */
    public static final int REPEAT_ALL = 3;

    /**
     * 播放模式
     */
    private int playmode = REPEAT_NORMAL;
    @Override
    public void onCreate() {
        super.onCreate();
        getData();
        //EventBus.getDefault().post(mediaItems);
    }
    private void getData() {
        ///
        new Thread() {
            @Override
            public void run() {
                super.run();

                mediaItems = new ArrayList<MediaItem>();
                ContentResolver resolver = getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = new String[]{
                        MediaStore.Audio.Media.DISPLAY_NAME,//在sdcard时候的名称
                        MediaStore.Audio.Media.DURATION,//视频的时长，毫秒
                        MediaStore.Audio.Media.SIZE,//文件大小，单位字节
                        MediaStore.Audio.Media.ARTIST,//演唱者
                        MediaStore.Audio.Media.DATA//在sdcard上路径
                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if (cursor != null) {


                    //循环
                    while (cursor.moveToNext()) {

                        //创建了一个视频信息类
                        MediaItem mediaItem = new MediaItem();
                        //添加到集合中
                        mediaItems.add(mediaItem);

                        String name = cursor.getString(0);
                        mediaItem.setName(name);
                        long duration = cursor.getLong(1);
                        mediaItem.setDuration(duration);
                        long size = cursor.getLong(2);
                        mediaItem.setSize(size);
                        String artist = cursor.getString(3);
                        mediaItem.setArtist(artist);
                        String data = cursor.getString(4);
                        mediaItem.setData(data);


                    }


                    cursor.close();//关闭


                }


                //发消息
//                handler.sendEmptyMessage(0);
            }
        }.start();

        ///
    }

    /**
     * 这个音频的信息
     */
    private MediaItem mediaItem;


    /**
     * 根据位置打开一个音频并且播放
     *
     * @param position
     */
    private MediaPlayer mediaPlayer;
    public void openAudio(int position) {

        this.position=position;
        if(mediaItems!=null&&mediaItems.size()>0) {
            if(position<mediaItems.size()) {
                mediaItem=mediaItems.get(position);

            }
        }
        if(mediaPlayer!=null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer=null;
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);

        try {
            mediaPlayer.setDataSource(mediaItem.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();


    }

    /**
     * 开始播放音频
     */

    private NotificationManager manager;
    public void start() {

        mediaPlayer.start();

        //显示通知栏
        showNotification();

    }
    private void notifyChange(String action) {

        EventBus.getDefault().post(mediaItems);

        /*Intent intent = new Intent(action);
        sendBroadcast(intent);*/
    }

    private void showNotification() {

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, AudioPlayerActivity.class);

        intent.putExtra("notification",true);
        PendingIntent pending = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this).
                setSmallIcon(R.drawable.notification_music_playing)
                .setContentTitle("321影音")
                .setContentText("正在播放:" + getAudioName())
                .setContentIntent(pending)
                .build();

        notification.flags = Notification.FLAG_ONGOING_EVENT;//点击的时候，不会消失
        manager.notify(1, notification);
    }

    /**
     * 暂停
     */
    public void pause() {
        mediaPlayer.pause();
        manager.cancel(1);
    }

    /**
     * 得到歌曲的名称
     */
    public String getAudioName() {
        if(mediaItem != null){
            return mediaItem.getName();
        }
        return "";
    }

    /**
     * 得到歌曲演唱者的名字
     */
    public String getArtistName() {

        if(mediaItem != null){
            return mediaItem.getArtist();
        }
        return "";
    }

    /**
     * 得到歌曲的当前播放进度
     */
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * 得到歌曲的当前总进度
     */
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    /**
     * 播放下一首歌曲
     */
    public void next() {
        //设置播放的位置
        setNextPosition();
        //根据位置播放对应的音频
        openAudio(position);

    }
    private void autonext() {
        //设置播放的位置
        setNextAutoPosition();
        //根据位置播放对应的音频
        openAudio(position);

    }

    private void setNextAutoPosition() {
        int playmode = getPlayMode();
       if(playmode==REPEAT_NORMAL) {
           random();
       }
         else if (playmode == REPEAT_SINGLE) {

        } else if (playmode == MusicPlayerService.REPEAT_ALL) {
            position ++;
            if(position >mediaItems.size()-1){
                position = 0;
            }
        }
    }

    private void random() {
        myRandom();
    }





    private void setNextPosition() {

        position++;
        if(position>mediaItems.size()-1) {
            position=0;
        }
        if(playmode==REPEAT_NORMAL) {
            myRandom();
        }



    }

    private void myRandom() {
        int preposition = position;
        position= new Random().nextInt(mediaItems.size());
        while (preposition==position){

                position= new Random().nextInt(mediaItems.size());

        }
        Log.e("OOOO", "suijishu"+position);
    }

    /**
     * 播放上一首歌曲
     */
    public void pre() {
        setPrePosition();
        openAudio(position);

    }



    private void setPrePosition() {
       position--;
        if(position<0) {
            position=mediaItems.size()-1;
        }
        if(playmode==REPEAT_NORMAL) {
            myRandom();
        }


    }

    /**
     * 得到播放模式
     */
    public int getPlayMode() {

        CacheUtils.getPlaymode(this,"playmode");
        return playmode;
    }

    /**
     * 设置播放模式
     */
    public void setPlayMode(int mode) {
        this.playmode = mode;
        //保存模式
        CacheUtils.savePlaymode(this, "playmode", playmode);
    }
    public void seekTo(int position){
        mediaPlayer.seekTo(position);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        start();
        //notifyChange(OPENAUDIO);

       notifyChange(OPENAUDIO);




    }


    @Override
    public void onCompletion(MediaPlayer mp) {

        Log.e("s", "播放完成了");
        autonext();

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        next();
        return true;
    }
}
