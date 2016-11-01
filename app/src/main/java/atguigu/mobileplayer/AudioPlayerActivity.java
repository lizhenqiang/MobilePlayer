package atguigu.mobileplayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.DensityUtil;

import java.io.File;
import java.util.ArrayList;

import atguigu.mobileplayer.adapter.VideoAndAudioAdapter;
import atguigu.mobileplayer.domain.Lyric;
import atguigu.mobileplayer.domain.MediaItem;
import atguigu.mobileplayer.service.MusicPlayerService;
import atguigu.mobileplayer.utils.LyricUtils;
import atguigu.mobileplayer.utils.Utils;
import atguigu.mobileplayer.view.ShowLyricView;

public class AudioPlayerActivity extends Activity implements View.OnClickListener {

    /**
     * 进度更新
     */
    private static final int PROGERSS = 1;
    private static final int SHOWLYRIC = 2;
    private ImageView iv_icon;
    private int position;
    private TextView tvArtist;
    private TextView tvName;
    private TextView tvTime;
    private SeekBar seekbarAudio;
    private Button btnAudioPlaymode;
    private Button btnAudioPre;
    private Button btnAudioStartPause;
    private Button btnAudioNext;
    private Button btnAudioSwichLyricCover;
    private TextView list_item_tv;
    private Button list_playmode;
    private ListView listView;
    private  View view;

    private Utils utils;
    private boolean notification;
    private ShowLyricView show_lyric_view;
    private PopupWindow popupWindow;
    private  ArrayList<MediaItem> list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        //注册reserver
        initData();

        findViews();
        //得到传过来的position
        getData();

        //启动服务
        startAndBindService();




    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case SHOWLYRIC:
                    //得到当前播放进度
                    try {
                        int position = service.getCurrentPosition();

                        //根据当前歌曲的播放进度，找到歌词列表的索引
                        //重新绘制
                        show_lyric_view.setNextShowLyric(position);


                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    removeMessages(SHOWLYRIC);
                    sendEmptyMessage(SHOWLYRIC);
                    break;
                case  PROGERSS:
                    int currentPosition = 0;
                    try {
                        currentPosition = service.getCurrentPosition();
                        //更新时间进度
                        tvTime.setText(utils.stringForTime(currentPosition)+"/"+utils.stringForTime(service.getDuration()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    seekbarAudio.setProgress(currentPosition);


                    removeMessages(PROGERSS);
                    sendEmptyMessageDelayed(PROGERSS,1000);
                    break;
            }
        }
    };
    private void startAndBindService() {
        Intent intent = new Intent(this,MusicPlayerService.class);
        intent.setAction("com.atguigu.mobileplayer.OPENAUDIO");
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
        startService(intent);//防止服务多次实例化
        Log.e("TAG", "qidongfuwu");
    }

    private void getData() {
        notification = getIntent().getBooleanExtra("notification", false);
        if (!notification) {
            position = getIntent().getIntExtra("position", 0);//列表
        }
          

    }

    private void initData() {

        Log.e("TAG", "zhuceguangbo");
        utils = new Utils();
        //1.注册-this是AudioPlayerActivity
        EventBus.getDefault().register(this);

    }

    private void findViews() {

        view = View.inflate(this,R.layout.popuowindow,null);
        list_item_tv= (TextView) view.findViewById(R.id.list_item_tv);
        list_playmode = (Button) view.findViewById(R.id.list_playmode);
        listView = (ListView) view.findViewById(R.id.listview);
        setContentView(R.layout.activity_audio_player);
        tvArtist = (TextView) findViewById(R.id.tv_artist);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvTime = (TextView) findViewById(R.id.tv_time);
        seekbarAudio = (SeekBar) findViewById(R.id.seekbar_audio);
        btnAudioPlaymode = (Button) findViewById(R.id.btn_audio_playmode);
        btnAudioPre = (Button) findViewById(R.id.btn_audio_pre);
        btnAudioStartPause = (Button) findViewById(R.id.btn_audio_start_pause);
        btnAudioNext = (Button) findViewById(R.id.btn_audio_next);
        btnAudioSwichLyricCover = (Button) findViewById(R.id.btn_audio_swich_lyric_cover);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        iv_icon.setBackgroundResource(R.drawable.animation_list);
        AnimationDrawable animationDrawable = (AnimationDrawable) iv_icon.getBackground();
        animationDrawable.start();
        show_lyric_view = (ShowLyricView) findViewById(R .id.show_lyric_view);
        btnAudioPlaymode.setOnClickListener(this);
        btnAudioPre.setOnClickListener(this);
        btnAudioStartPause.setOnClickListener(this);
        btnAudioNext.setOnClickListener(this);
        btnAudioSwichLyricCover.setOnClickListener(this);
        list_playmode.setOnClickListener(this);

        //设置音频进度的拖拽
        seekbarAudio.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }

    @Override
    public void onClick(View v) {

        if (v == btnAudioPlaymode) {

            changePlaymode();

            // Handle clicks for btnAudioPlaymode
        } else if(v==list_playmode) {
            Log.e("MM", "dianjile");
            changePlaymode();

        }



        else if (v == btnAudioPre) {
            try {
                service.pre();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            // Handle clicks for btnAudioPre
        } else if (v == btnAudioStartPause) {
            try {
                if (service.isPlaying()) {
                    //暂停

                    service.pause();
                    //按钮设置-播放状态
                    btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                } else {
                    //播放
                    service.start();
                    //按钮设置-暂停
                    btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            // Handle clicks for btnAudioStartPause
        } else if (v == btnAudioNext) {
            try {
                service.next();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            // Handle clicks for btnAudioNext
        } else if (v == btnAudioSwichLyricCover) {
            // Handle clicks for btnAudioSwichLyricCover
            showList();
        }

    }

    private void showList() {
        popupWindow=new PopupWindow(AudioPlayerActivity.this);
        popupWindow.setWidth(DensityUtil.dip2px(200));
        popupWindow.setHeight(DensityUtil.dip2px(300));

        popupWindow.setContentView(view);
        popupWindow.setFocusable(true);



        popupWindow.showAtLocation(tvTime, Gravity.RIGHT,0,250);


    }

    private void changePlaymode() {
        Log.e("TAG", "changePlaymode");
        try {
            int playmode = service.getPlayMode();
            Log.e("TAG", "playmode="+playmode);
            if (playmode == MusicPlayerService.REPEAT_NORMAL) {
                playmode = MusicPlayerService.REPEAT_SINGLE;
            } else if (playmode == MusicPlayerService.REPEAT_SINGLE) {
                playmode = MusicPlayerService.REPEAT_ALL;
            } else if (playmode == MusicPlayerService.REPEAT_ALL) {
                playmode = MusicPlayerService.REPEAT_NORMAL;
            } else {
                playmode = MusicPlayerService.REPEAT_NORMAL;
            }
            //保存模式-内存中
            service.setPlayMode(playmode);


            showPlaymode();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示播放模式
     */
    private void showPlaymode() {
        Log.e("TAG", "showPlaymode");
        try {
            //从内存中获取播放模式
            int playmode = service.getPlayMode();
            Log.e("TAG", "playmode="+playmode);
            if (playmode == MusicPlayerService.REPEAT_NORMAL) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
                list_playmode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
                Toast.makeText(AudioPlayerActivity.this, "随机播放", Toast.LENGTH_SHORT).show();
            } else if (playmode == MusicPlayerService.REPEAT_SINGLE) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_single_selector);
                list_playmode.setBackgroundResource(R.drawable.btn_audio_playmode_single_selector);
                Toast.makeText(AudioPlayerActivity.this, "单曲循环", Toast.LENGTH_SHORT).show();
            } else if (playmode == MusicPlayerService.REPEAT_ALL) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
                list_playmode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
                Toast.makeText(AudioPlayerActivity.this, "全部循环", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AudioPlayerActivity.this, "随机播放", Toast.LENGTH_SHORT).show();
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验显示播放模式
     */
    private void checkPlaymode() {
        try {
            //从内存中获取播放模式
            int playmode = service.getPlayMode();
            if (playmode == MusicPlayerService.REPEAT_NORMAL) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
            } else if (playmode == MusicPlayerService.REPEAT_SINGLE) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_single_selector);
            } else if (playmode == MusicPlayerService.REPEAT_ALL) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
            } else {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }



    private class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                try {
                    service.seekTo(progress);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }


    private IMusicPlayerService service;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            service = IMusicPlayerService.Stub.asInterface(binder);
            try {
                if (notification) {
                    //状态栏
                    service.notifyChange(MusicPlayerService.OPENAUDIO);
                   //showProgress();

                } else {
                    //列表
                    service.openAudio(position);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            showData();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void showData() {
        try {
            tvArtist.setText(service.getArtistName());
            tvName.setText(service.getAudioName());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("TAG", "guangboshoudao");
            //设置哥们和艺术家
            showData();
            //发送handler
            showProgress();
            //设置模式
            checkPlaymode();
            //显示歌词同步
            showLyric();
        }
    }

    //@Subscribe(threadMode = ThreadMode.MAIN,sticky = false,priority = 0)

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = false,priority = 0)
    public void setList(ArrayList mediaItems){
        showData();
        showProgress();

        checkPlaymode();
        showLyric();
        //开启频谱
        Log.e("MM", "list:"+mediaItems);
        list_item_tv.setText("("+mediaItems.size()+")");
        listView.setAdapter(new VideoAndAudioAdapter(AudioPlayerActivity.this,mediaItems,false));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                finish();
                Intent intent = new Intent(AudioPlayerActivity.this,AudioPlayerActivity.class);

                intent.putExtra("position", position);

                AudioPlayerActivity.this.startActivity(intent);

            }
        });
    }



    private void showLyric() {
        //1.得到音频的播放地址
        LyricUtils lyricUtils = new LyricUtils();
        try {
            String path = service.getAudioPath();//mnt/sdcard/audio/beijingbeijing.mp3
            path = path.substring(0, path.lastIndexOf("."));//mnt/sdcard/audio/beijingbeijing

            File file = new File(path + ".lrc");//mnt/sdcard/audio/beijingbeijing.lrc
            if (!file.exists()) {
                file = new File(path + ".txt");//mnt/sdcard/audio/beijingbeijing.txt
            }

            //解析歌词
            lyricUtils.readLyricFile(file);

            ArrayList<Lyric> lyrics = lyricUtils.getLyrics();
            show_lyric_view.setLyric(lyrics);


        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //2.变成歌词文件的地址

        if (lyricUtils.isExistsLyric()) {
            handler.sendEmptyMessage(SHOWLYRIC);
        }
    }

    private void showProgress() {
        try {
            seekbarAudio.setMax(service.getDuration());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        handler.sendEmptyMessage(PROGERSS);

    }

    @Override
    protected void onDestroy() {
       if(conn!=null) {
           unbindService(conn);
           conn=null;
       }



        //2.取消注册
      EventBus.getDefault().unregister(this);

        super.onDestroy();
    }


}
