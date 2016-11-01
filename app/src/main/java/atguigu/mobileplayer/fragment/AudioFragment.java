package atguigu.mobileplayer.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import atguigu.mobileplayer.AudioPlayerActivity;
import atguigu.mobileplayer.R;
import atguigu.mobileplayer.adapter.VideoAndAudioAdapter;
import atguigu.mobileplayer.base.BaseFragment;
import atguigu.mobileplayer.domain.MediaItem;
import atguigu.mobileplayer.utils.LogUtil;
import atguigu.mobileplayer.utils.Utils;

/**
 * Created by Administrator on 2016/9/28.
 */
public class AudioFragment extends BaseFragment {



    private TextView textView;

    private ListView listView;

    private TextView tv_nomedia;

    private ArrayList<MediaItem> mediaItems;

    private VideoAndAudioAdapter adapter;

    private Utils utils;

    public AudioFragment(){
        utils = new Utils();
    }


    /**
     * 初始化视图
     *
     * @return
     */
    @Override
    public View initView() {
        LogUtil.e("本地音频UI创建了");
        View view = View.inflate(context, R.layout.fragment_video, null);
        listView = (ListView) view.findViewById(R.id.listview);
        tv_nomedia = (TextView) view.findViewById(R.id.tv_nomedia);

        //设置点击事件
        listView.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MediaItem mediaItem = mediaItems.get(position);
//            Toast.makeText(context, "mediaItem=="+mediaItem, Toast.LENGTH_SHORT).show();
            //把系统的播放器调起来并且播放
//            Intent intent = new Intent();
//            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
//            context.startActivity(intent);

            //调起自己的播放器
            //调起自己的播放器
            Intent intent = new Intent(context,AudioPlayerActivity.class);
//            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");

            //使用Bundler传递列表数据
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("medialist",mediaItems);
            intent.putExtra("position", position);
//            intent.putExtras(bundle);
            context.startActivity(intent);

        }
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("本地音频数据绑定了");

//        isGrantExternalRW((Activity) context);
        getData();


    }



   private Handler handler = new Handler(){
       public void handleMessage(Message msg){
           if (mediaItems != null && mediaItems.size() > 0) {
               //有数据
               tv_nomedia.setVisibility(View.GONE);
               //设置适配器
               adapter = new VideoAndAudioAdapter(context,mediaItems,false);
               listView.setAdapter(adapter);

           } else {
               //没有数据
               tv_nomedia.setVisibility(View.VISIBLE);
           }

       }
   };

    private void getData() {
        ///
        new Thread() {
            @Override
            public void run() {
                super.run();

                mediaItems = new ArrayList<MediaItem>();
                ContentResolver resolver = context.getContentResolver();
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
                handler.sendEmptyMessage(0);
            }
        }.start();

        ///
    }

}
