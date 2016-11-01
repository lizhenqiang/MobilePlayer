package atguigu.mobileplayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import atguigu.mobileplayer.base.BaseFragment;
import atguigu.mobileplayer.domain.MediaItem;
import atguigu.mobileplayer.fragment.AudioFragment;
import atguigu.mobileplayer.fragment.NetAudioFragment;
import atguigu.mobileplayer.fragment.NetRes1Fragment;
//import atguigu.mobileplayer.fragment.NetRes2Fragment;
import atguigu.mobileplayer.fragment.NetVideoFragment;
import atguigu.mobileplayer.fragment.VideoFragment;
import atguigu.mobileplayer.utils.OF;
import atguigu.mobileplayer.utils.Utils;

public class MainActivity extends FragmentActivity {
    private ArrayList<BaseFragment> fragments;
    private int position;
    private BaseFragment fragment;
    private RadioGroup rg_main;
    private DragLayout dl;
    private ImageView ic_topbanner_logo;
    private ListView listView2;
    private ArrayList<MediaItem> mediaItems2;
    private Utils utils = new Utils();
    private MyAdapter myAdapter;
    private OF of;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        of=new OF();
        listView2= (ListView) findViewById(R.id.lv_myfavrite);
        setAdapter();


        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              /*  MediaItem mediaItem = mediaItems.get(position);
                *//*Intent intent = new Intent(context,SystemPlayerActivity.class);
                intent.setDataAndType(Uri.parse(mediaItem.getData()),"video*//**//*");
                context.startActivity(intent);*//*
                Intent intent = new Intent(context,SystemPlayerActivity.class);
                intent.setDataAndType(Uri.parse(mediaItem.getData()),"video*//*");
                context.startActivity(intent);*/
                MediaItem mediaItem = mediaItems2.get(position);
                Intent intent = new Intent(MainActivity.this,SystemPlayerActivity.class);
                intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
                MainActivity.this.startActivity(intent);

            }
        });
        listView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


                new AlertDialog.Builder(MainActivity.this)
                            .setTitle("移除"+mediaItems2.get(position).getName())

                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    of.delete(mediaItems2.get(position));
                                    setAdapter();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                of.delete(mediaItems2.get(position));

                return true;
            }
        });

        ic_topbanner_logo = (ImageView) findViewById(R.id.ic_topbanner_logo);
        ic_topbanner_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dl.open();
            }
        });
        initDragLayout();
        rg_main= (RadioGroup) findViewById(R.id.rg_main);
        initFragment();

        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){

                    case R.id.rb_local_video:
                        position = 0;
                        break;
                    case R.id.rb_local_audio:
                        position = 1;
                        break;
                    case R.id.rb_net_video:
                        position = 2;
                        break;
                    case R.id.rb_net_audio:
                        position = 3;
                        break;
                    case R.id.rb_net_res1:
                        position=4;
                        break;
                    case R.id.rb_net_res2:
                        position=5;
                        break;
                }

                BaseFragment toFragment = fragments.get(position);
                changeFragment(fragment,toFragment);
            }
        });
        rg_main.check(R.id.rb_local_video);


    }

    private void setAdapter() {
        getlist();
        getadapter();
        listView2.setAdapter(myAdapter);
    }

    private void getadapter() {
        myAdapter=new MyAdapter();
    }

    private void getlist() {
        mediaItems2 = new ArrayList<>();
        File[] files = of.read();


        if(files!=null) {


            for (File file : files) {
                MediaItem mediaItem = null;
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(file.getCanonicalPath())));
                    mediaItem = (MediaItem) ois.readObject();
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                }
                mediaItems2.add(mediaItem);

            }

        }
        else {
            Log.e("TAG", "集合为null");
        }

    }

    private void changeFragment(BaseFragment fromFragment,BaseFragment toFragment) {

        if(toFragment!=fromFragment) {
            if(toFragment!=null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();



                if(!toFragment.isAdded()) {
                    transaction.add(R.id.fl_main_container,toFragment).commit();
                    if(fromFragment!=null) {
                        transaction.hide(fromFragment);
                    }
                }else {
                    transaction.show(toFragment).commit();
                    if(fromFragment!=null) {
                        transaction.hide(fromFragment);
                    }
                }
                fragment= toFragment;
            }
        }
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(new VideoFragment());
        fragments.add(new AudioFragment());

        fragments.add(new NetVideoFragment());
        fragments.add(new NetAudioFragment());
        fragments.add(new NetRes1Fragment());
        //fragments.add(new NetRes2Fragment());

    }
    private void initDragLayout() {
        dl = (DragLayout) findViewById(R.id.dl);
        dl.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {
                //lv.smoothScrollToPosition(new Random().nextInt(30));
                setAdapter();

            }

            @Override
            public void onClose() {
                shake();
            }

            @Override
            public void onDrag(float percent) {
                ViewHelper.setAlpha(ic_topbanner_logo, 1 - percent);
            }
        });
    }
    private void shake() {
        ic_topbanner_logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
    }


    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mediaItems2==null?null:mediaItems2.size();
        }

        @Override
        public Object getItem(int position) {
            return mediaItems2==null?null:mediaItems2.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder2 viewHolder;
            if(convertView==null) {
                viewHolder=new ViewHolder2();
                convertView=View.inflate(MainActivity.this,R.layout.item_video_fragment,null);
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tv_duration = (TextView) convertView.findViewById(R.id.tv_duration);
                viewHolder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);

                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder2) convertView.getTag();
            }
            //装配数据
            MediaItem mediaItem = mediaItems2.get(position);
            if(mediaItem!=null) {


                viewHolder.tv_name.setText(mediaItem.getName());
                //设置时间
          /*  viewHolder.tv_duration.setText(utils.stringForTime((int) mediaItem.getDuration()));
            viewHolder.tv_size.setText(Formatter.formatFileSize(context,mediaItem.getSize()));*/
                viewHolder.tv_duration.setText(utils.stringForTime((int) mediaItem.getDuration()));
                viewHolder.tv_size.setText(Formatter.formatFileSize(MainActivity.this, mediaItem.getSize()));


            }

            return convertView;
        }
    }

    private class ViewHolder2 {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_duration;
        TextView tv_size;
    }


    private boolean isExit = false;
    private Handler handler = new Handler();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_BACK){
             if(!isExit){
                isExit = true;
                Toast.makeText(MainActivity.this, "再点击一次退出", Toast.LENGTH_SHORT).show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                },2000);

                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
