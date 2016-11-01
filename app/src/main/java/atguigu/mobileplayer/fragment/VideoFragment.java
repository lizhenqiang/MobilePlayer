package atguigu.mobileplayer.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import atguigu.mobileplayer.R;
import atguigu.mobileplayer.SystemPlayerActivity;
import atguigu.mobileplayer.base.BaseFragment;
import atguigu.mobileplayer.domain.MediaItem;
import atguigu.mobileplayer.utils.LogUtil;
import atguigu.mobileplayer.utils.OF;
import atguigu.mobileplayer.utils.Utils;

/**
 * Created by Administrator on 2016/9/28.
 */
public class VideoFragment extends BaseFragment {
    private ArrayList<MediaItem> mediaItems_lv_myfavrite;
    private ListView listview;
    private TextView tv_nomedia;
    private ArrayList<MediaItem> mediaItems;
    private MyAdapter adapter;
    private Utils utils;
    private OF of;





    public VideoFragment( ) {

        utils=new Utils();
        of = new OF();

    }
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
           if(mediaItems!=null&&mediaItems.size()>0) {
               tv_nomedia.setVisibility(View.GONE);
               adapter=new MyAdapter();


               listview.setAdapter(adapter);
           }else {
               tv_nomedia.setVisibility(View.VISIBLE);
           }
        }
    };

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mediaItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mediaItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if(convertView==null) {
                viewHolder=new ViewHolder();
                convertView=View.inflate(context,R.layout.item_video_fragment,null);
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tv_duration = (TextView) convertView.findViewById(R.id.tv_duration);
                viewHolder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);

                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            //装配数据
            MediaItem mediaItem = mediaItems.get(position);
            viewHolder.tv_name.setText(mediaItem.getName());
            //设置时间
          /*  viewHolder.tv_duration.setText(utils.stringForTime((int) mediaItem.getDuration()));
            viewHolder.tv_size.setText(Formatter.formatFileSize(context,mediaItem.getSize()));*/
            viewHolder.tv_duration.setText(utils.stringForTime((int) mediaItem.getDuration()));
            viewHolder.tv_size.setText(Formatter.formatFileSize(context,mediaItem.getSize()));



            return convertView;
        }
    }

    private class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_duration;
        TextView tv_size;
    }







    /**
     * 初始化视图
     * @return
     */
    @Override
    public View initView() {
        LogUtil.e("本地视频UI创建了");

        View view =View.inflate(getActivity(), R.layout.fragment_video,null);


        tv_nomedia = (TextView) view.findViewById(R.id.tv_nomedia);
        listview= (ListView) view.findViewById(R.id.listview);



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              /*  MediaItem mediaItem = mediaItems.get(position);
                *//*Intent intent = new Intent(context,SystemPlayerActivity.class);
                intent.setDataAndType(Uri.parse(mediaItem.getData()),"video*//**//*");
                context.startActivity(intent);*//*
                Intent intent = new Intent(context,SystemPlayerActivity.class);
                intent.setDataAndType(Uri.parse(mediaItem.getData()),"video*//*");
                context.startActivity(intent);*/
                MediaItem mediaItem = mediaItems.get(position);
               /* Intent intent = new Intent(context,SystemPlayerActivity.class);
                intent.setDataAndType(Uri.parse(mediaItem.getData()),"video*//*");
                context.startActivity(intent);*/

                //调起自己的播放器
                Intent intent = new Intent(context,SystemPlayerActivity.class);
//            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");

                //使用Bundler传递列表数据
                /*Bundle bundle = new Bundle();
                bundle.putSerializable("medialist",mediaItems);
                intent.putExtra("position",position);
                intent.putExtras(bundle);
                context.startActivity(intent);*/
                Bundle bundle = new Bundle();
                bundle.putSerializable("medialist",mediaItems);
                intent.putExtra("position",position);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });


        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                of.write(mediaItems.get(position));
                Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return view;



    }


    @Override
    public void initData() {
        super.initData();
        LogUtil.e("本地视频数据绑定了");
        isGrantExternalRW((Activity) context);

        getData();
    }

    private void getData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                mediaItems_lv_myfavrite=new ArrayList<MediaItem>();
                mediaItems=new ArrayList<MediaItem>();


                ContentResolver resolver = context.getContentResolver();
                //Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String [] objs = {
                      /*  MediaStore.Video.Media.DISPLAY_NAME,//在sdcard时候的名称
                        MediaStore.Video.Media.DURATION,//视频的时长，毫秒
                        MediaStore.Video.Media.SIZE,//文件大小，单位字节
                        MediaStore.Video.Media.ARTIST,//演唱者
                        MediaStore.Video.Media.DATA//在sdcard上路径      */
                        MediaStore.Video.Media.DISPLAY_NAME,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.ARTIST,
                        MediaStore.Video.Media.DATA
                };


                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if(cursor!=null) {
                    while(cursor.moveToNext()) {
                        MediaItem mediaItem = new MediaItem();
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
                        mediaItems.add(mediaItem);
                    }
                }
                cursor.close();
                handler.sendEmptyMessage(0);
            }

        }.start();
    }

    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return false;
        }

        return true;
    }








}
