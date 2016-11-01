package atguigu.mobileplayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import atguigu.mobileplayer.domain.MediaItem;
import atguigu.mobileplayer.utils.LogUtil;
import atguigu.mobileplayer.utils.Utils;
import atguigu.mobileplayer.view.VideoView;

public class SystemPlayerActivity extends Activity implements View.OnClickListener {

    private static final int PROGRESS = 1;
    private static final int HIDE_MEDIACONTROLL = 2;
    /**
     * 默认
     */
    private static final int SCREEN_DEFULT = 1;
    /**
     * 全屏
     */
    private static final int SCREEN_FULL = 2;
    private boolean isFullScreen = false;
    private VideoView videoView;
    private Uri uri;
    private Utils utils;
    private GestureDetector detector;

    private int screenWidth;
    private int screenHeight;
    /**
     * 真实视频的宽和高
     */
    private int videoWidht;
    private int videoHeight;
    /**
     * 调节声音
     */
    private AudioManager am;
    /**
     * 当前音量
     */
    private int currentVolume;
    //最大音量
    private int maxVolume;
    private boolean isMute = false;


    private int position;
    private ArrayList<MediaItem> mediaItems;
    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private Button btnVideoVoice;
    private SeekBar seekbarVoice;
    private Button btnVideoSwichPlayer;
    private LinearLayout llBottom;
    private TextView tvCurrent;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button btnVideoExit;
    private Button btnVideoPre;
    private Button btnVideoStartPause;
    private Button btnVideoSwichScreen;
    private Button btnVideoNext;
    private TextView tv_system_time;
    private int progress;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-10-05 19:43:05 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {

        setContentView(R.layout.activity_system_player);
        videoView= (VideoView) findViewById(R.id.videoview);
        llTop = (LinearLayout)findViewById( R.id.ll_top );
        tvName = (TextView)findViewById( R.id.tv_name );
        ivBattery = (ImageView)findViewById( R.id.iv_battery );
        btnVideoVoice = (Button)findViewById( R.id.btn_video_voice );
        seekbarVoice = (SeekBar)findViewById( R.id.seekbar_voice );
        btnVideoSwichPlayer = (Button)findViewById( R.id.btn_video_swich_player );
        llBottom = (LinearLayout)findViewById( R.id.ll_bottom );
        tvCurrent = (TextView)findViewById( R.id.tv_current );
        seekbarVideo = (SeekBar)findViewById( R.id.seekbar_video );
        tvDuration = (TextView)findViewById( R.id.tv_duration );
        btnVideoExit = (Button)findViewById( R.id.btn_video_exit );
        btnVideoPre = (Button)findViewById( R.id.btn_video_pre );
        btnVideoStartPause = (Button)findViewById( R.id.btn_video_start_pause );
        btnVideoSwichScreen = (Button)findViewById( R.id.btn_video_swich_screen );
        btnVideoNext = (Button) findViewById(R.id.btn_video_next);


        tv_system_time = (TextView) findViewById(R.id.tv_system_time);
        btnVideoVoice.setOnClickListener( this );
        btnVideoSwichPlayer.setOnClickListener( this );
        btnVideoNext.setOnClickListener( this );
        btnVideoPre.setOnClickListener( this );
        btnVideoStartPause.setOnClickListener( this );
        btnVideoSwichScreen.setOnClickListener( this );
        btnVideoExit.setOnClickListener(this);
        seekbarVoice.setMax(maxVolume);
        seekbarVoice.setProgress(currentVolume);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-10-05 19:43:05 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnVideoVoice ) {
            isMute = !isMute;
            updataVolume(currentVolume);
            // Handle clicks for btnVideoVoice
        } else if ( v == btnVideoSwichPlayer ) {
            // Handle clicks for btnVideoSwichPlayer
        } else if ( v == btnVideoNext ) {
            Log.e("TAG", "xiayige");
            setPlayNextVideo();
            // Handle clicks for btnVideoExit
        } else if ( v == btnVideoPre ) {
            setPlayPreVideo();
            // Handle clicks for btnVideoPre
        } else if ( v == btnVideoStartPause ) {
            // Handle clicks for btnVideoStartPause

           if(videoView.isPlaying()) {
               videoView.pause();
               btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);
           } else{
               videoView.start();
               btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
           }

        } else if ( v == btnVideoSwichScreen ) {
            // Handle clicks for btnVideoSwichScreen
            setVideoMode();

        }
    }

    private void updataVolume(int currentVolume) {
        if (isMute) {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            seekbarVoice.setProgress(0);
        } else {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            currentVolume = progress;
            seekbarVoice.setProgress(progress);
        }

    }


    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case PROGRESS:
                    int currentPosition = videoView.getCurrentPosition();
                    seekbarVideo.setProgress(currentPosition);
                    tvCurrent.setText(utils.stringForTime(currentPosition));
                    tv_system_time.setText(getSystemTime());
                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS,1000);
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        findViews();
        utils = new Utils();

        getData();
        setData();



        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {


                videoWidht = mp.getVideoWidth();
                videoHeight = mp.getVideoHeight();
                videoView.start();



                int duration = videoView.getDuration();
                seekbarVideo.setMax(duration);
                tvDuration.setText(utils.stringForTime(duration));
                handler.sendEmptyMessage(PROGRESS);
            }
        });


        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(SystemPlayerActivity.this, "播放出错了", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //finish();
                setPlayNextVideo();

            }
        });

        //videoView.setVideoURI(uri);
        //videoView.setMediaController(new MediaController(this));

        seekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void getData() {

            uri = getIntent().getData();//视频播放地址-文件-->null
            LogUtil.e("uri==" + uri);

            mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("medialist");
            position = getIntent().getIntExtra("position", 0);



    }private void setData() {
        if (mediaItems != null && mediaItems.size() > 0) {
            //有列表数据

            MediaItem mediaItem = mediaItems.get(position);
            tvName.setText(mediaItem.getName());
            Log.e("TAG", "getName"+mediaItem.getName());
            Log.e("TAG", "getData"+mediaItem.getData());
            videoView.setVideoPath(mediaItem.getData());

            Log.e("TAG", "aaaaaa");

        }
        //文件，第三方应用
        else if (uri != null) {
            //设置播放地址
            videoView.setVideoURI(uri);
        } else {
            Toast.makeText(SystemPlayerActivity.this, "没有传递数据进入播放器", Toast.LENGTH_SHORT).show();
        }

        //设置按钮状态
        setButtonState();
    }



    private void setPlayNextVideo() {
        Log.e("TAG", "mediaItems.size()"+mediaItems.size());
        if (mediaItems != null && mediaItems.size() > 0) {
            position++;
            if (position < mediaItems.size()) {

                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                videoView.setVideoPath(mediaItem.getData());

                setButtonState();
                if (position == mediaItems.size() - 1) {
                    Toast.makeText(SystemPlayerActivity.this, "播放最后一个视频", Toast.LENGTH_SHORT).show();
                }

            } else {
                position = mediaItems.size() - 1;
                //列表的播放完成
                finish();//退出播放器
            }
        } else if (uri != null) {//只有一个播放地址
            finish();//退出播放器
        }

    }

    private void setPlayPreVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            position--;
            if (position >= 0) {

                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                videoView.setVideoPath(mediaItem.getData());

                setButtonState();

            } else {
                //列表的播放完成
                position = 0;
            }
        }

    }

    private void setButtonState() {
        if (mediaItems != null && mediaItems.size() > 0) {

            //设置上一个和下一个可以点击
            setIsEnableButton(true);


            //如果是第0个，上一个不可以点
            if (position == 0) {
                btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                btnVideoPre.setEnabled(false);
            }

            //如果是最后一个，下一个按钮不可以点
            if (position == mediaItems.size() - 1) {
                btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                btnVideoNext.setEnabled(false);
            }


        } else if (uri != null) {//只有一个播放地址
            setIsEnableButton(false);

        }
    }




        private void setIsEnableButton(boolean enable) {
            if (enable) {
                btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
                btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
            } else {
                btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
            }
    }

    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }


    private void initData() {
        utils = new Utils();
        MyBraodcastReceiver receiver = new MyBraodcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver,filter);
        Log.e("TAG", "zhuce广播");

        detector = new GestureDetector(this,new MySimpleOnGestureListener());
        //得到屏幕的宽和高
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;
    }


    @Override
    protected void onDestroy() {


        handler.removeCallbacksAndMessages(null);




        super.onDestroy();
    }

    class MyBraodcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level  = intent.getIntExtra("level",0);
            Log.e("TAG", "level"+level);
            setBattery(level);
        }
    }

    private void setBattery(int level) {
        if(level <=0){
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        }else if(level<=10){
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        }else  if(level <=20){
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        }else  if(level <=40){
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        }else  if(level <=60){
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        }else  if(level <=80){
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        }else  if(level <=100){
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }else{
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }
    private boolean isShowMediaController = false;

    /**
     * 隐藏控制面板
     */
    private void hideMediaController() {
        isShowMediaController = false;
        llTop.setVisibility(View.GONE);
        llBottom.setVisibility(View.GONE);
    }

    /**
     * 显示控制面板
     */
    private void showMediaController() {
        isShowMediaController = true;
        llTop.setVisibility(View.VISIBLE);
        llBottom.setVisibility(View.VISIBLE);
    }

    private class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Toast.makeText(SystemPlayerActivity.this, "我被单击了", Toast.LENGTH_SHORT).show();
            if (isShowMediaController) {
                //隐藏
                hideMediaController();
                //把消息移除
                handler.removeMessages(HIDE_MEDIACONTROLL);
            } else {
                //显示
                showMediaController();
                //发延迟消息
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLL, 5000);
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            setVideoMode();
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            Toast.makeText(SystemPlayerActivity.this, "我被长按了", Toast.LENGTH_SHORT).show();
            setStartAndPause();
            super.onLongPress(e);
        }
    }

    private void setStartAndPause() {
        if (videoView.isPlaying()) {
            //暂停
            videoView.pause();
            //按钮状态--播放
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);
        } else {
            //播放
            videoView.start();
            //按钮-暂停
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
        }
    }
    private void setVideoType(int videoType) {
        switch (videoType) {
            case SCREEN_FULL:
                isFullScreen  = true;
                videoView.setVideoSize(screenWidth,screenHeight);

                //按钮状态
                btnVideoSwichScreen.setBackgroundResource(R.drawable.btn_video_swich_screen_defualt_selector);

                break;
            case SCREEN_DEFULT:
                isFullScreen  = false;


                //真实视频的宽和高
                int mVideoWidth = videoWidht;
                int mVideoHeight = videoHeight;

                //屏幕的真实宽和高
                int width  = screenWidth;
                int height = screenHeight;

                // for compatibility, we adjust size based on aspect ratio
                if ( mVideoWidth * height  < width * mVideoHeight ) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if ( mVideoWidth * height  > width * mVideoHeight ) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }

                videoView.setVideoSize(width,height);

                //按钮状态
                btnVideoSwichScreen.setBackgroundResource(R.drawable.btn_video_swich_screen_full_selector);

                break;
        }
    }

    private void setVideoMode() {
        if (isFullScreen) {
            //默认
            setVideoType(SCREEN_DEFULT);
        } else {
            //全屏
            setVideoType(SCREEN_FULL);
        }
    }
}
