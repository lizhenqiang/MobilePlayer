package atguigu.mobileplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import atguigu.mobileplayer.R;
import atguigu.mobileplayer.SystemPlayerActivity;
import atguigu.mobileplayer.base.BaseFragment;
import atguigu.mobileplayer.domain.MediaItem;
import atguigu.mobileplayer.utils.CacheUtils;
import atguigu.mobileplayer.utils.Constants;
import atguigu.mobileplayer.utils.LogUtil;

/**
 * Created by Administrator on 2016/9/28.
 */
public class NetVideoFragment extends BaseFragment {





  /*  private ListView listview_net;
    private MyAdapter myAdapter;
    private InputStream is;
    private ByteArrayOutputStream baos;
    private HttpURLConnection conn;
    private ArrayList<MediaItem> list;
    private MaterialRefreshLayout refresh;
    *//**
     * 初始化视图
     * @return
     *//*
    @Override
    public View initView() {
        LogUtil.e("网络视频UI创建了");
        View view = View.inflate(getContext(), R.layout.fragment_netvideo,null);
        refresh = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        refresh.setMaterialRefreshListener(new MyMaterialRefreshListener());
        listview_net = (ListView) view.findViewById(R.id.listview_net);

        listview_net.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaItem mediaItem = list.get(position);
                Intent intent = new Intent(context,SystemPlayerActivity.class);
                intent.setDataAndType(Uri.parse(mediaItem.getData()),"video*//*");
                context.startActivity(intent);
            }
        });



      return view;
    }

    @Override
    public void initData() {
        super.initData();


        getList();
        getAdapter();
        listview_net.setAdapter(myAdapter);


    }

    private void getAdapter() {
        myAdapter = new MyAdapter();
    }

    private void getList() {
        list = new ArrayList<>();
        new Thread(){
            public void run(){
                String path = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
                try {
                    URL url = new URL(path);
                     conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);

                    conn.connect();
                    is = conn.getInputStream();
                     baos = new ByteArrayOutputStream();
                    byte[]arr = new byte[1024];
                    int len;
                    while ((len=is.read(arr))!=-1){
                        baos.write(arr,0,len);
                    }
                    String jsonArr=baos.toString();
                    try {
                        JSONObject jsonObject = new JSONObject(jsonArr);
                        JSONArray jsonArray = jsonObject.optJSONArray("trailers");


                        for (int i = 0; i < jsonArr.length(); i++) {
                            JSONObject item = (JSONObject) jsonArray.get(i);
                            if (item != null) {
                                MediaItem mediaItem = new MediaItem();

                                String name = item.optString("movieName");
                                mediaItem.setName(name);

                                String desc = item.optString("videoTitle");
                                mediaItem.setDesc(desc);

                                String imageUrl = item.optString("coverImg");
                                mediaItem.setImgUrl(imageUrl);

                                String data = item.optString("url");
                                mediaItem.setData(data);

                                list.add(mediaItem);

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Log.e("TAG", "list"+list.size());


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(is!=null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(baos!=null) {
                        try {
                            baos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(conn!=null) {
                        conn.disconnect();
                    }


                }


            }
        }.start();
    }

    private class MyAdapter extends BaseAdapter {
        private ImageOptions imageOptions;


        public MyAdapter() {
            imageOptions = new ImageOptions.Builder()
                    .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(80))
                    .setRadius(DensityUtil.dip2px(5))
                    // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                    .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                    // 加载中或错误图片的ScaleType
                    //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                    .setImageScaleType(ImageView.ScaleType.FIT_XY)
                    .setLoadingDrawableId(R.drawable.video_default)
                    .setFailureDrawableId(R.drawable.video_default)
                    .build();
        }

        @Override
        public int getCount() {
            return  list==null?0:list.size();
        }

        @Override
        public Object getItem(int position) {
            return list==null?null:list.get(position);

        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            imageOptions = new ImageOptions.Builder()
                    .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(80))
                    .setRadius(DensityUtil.dip2px(5))
                    // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                    .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                    // 加载中或错误图片的ScaleType
                    //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                    .setImageScaleType(ImageView.ScaleType.FIT_XY)
                    .setLoadingDrawableId(R.drawable.video_default)
                    .setFailureDrawableId(R.drawable.video_default)
                    .build();
            if(convertView==null) {
                 viewHolder = new ViewHolder();
                convertView=View.inflate(getContext(),R.layout.item_netvideo_fragment,null);
                viewHolder.iv_video_icon = (ImageView) convertView.findViewById(R.id.iv_video_icon);
                viewHolder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tv_desc= (TextView) convertView.findViewById(R.id.tv_desc);
                convertView.setTag(viewHolder);
                MediaItem netMediaItem = list.get(position);
                viewHolder.tv_name.setText(netMediaItem.getName());
                viewHolder.tv_desc.setText(netMediaItem.getDesc());
                x.image().bind(viewHolder.iv_video_icon, netMediaItem.getImgUrl(),imageOptions);





            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }
            return convertView;
        }
    }

    private class ViewHolder {
        private ImageView iv_video_icon;
        private TextView tv_name;
        private TextView tv_desc;
    }

    private class MyMaterialRefreshListener extends MaterialRefreshListener {
        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            getDataFromNet();
        }
    }*/

    private ListView listview;
    private ProgressBar progressbar;
    private TextView tv_nomedia;
    private MaterialRefreshLayout refresh;

    private ArrayList<MediaItem> mediaItems;

    private MyAdapter myAdapter;

    @Override
    public View initView() {
        Log.e("TAG", "网络视频视图（页面）初始化了...");
        View view = View.inflate(context, R.layout.fragment_netvideo, null);
        listview = (ListView) view.findViewById(R.id.listview_net_video);
        progressbar = (ProgressBar) view.findViewById(R.id.progressbar_net_video);
        tv_nomedia = (TextView) view.findViewById(R.id.tv_nodata);
        refresh = (MaterialRefreshLayout) view.findViewById(R.id.refresh);

        //设置点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //3.传递视频列表
                Intent intent = new Intent(context,SystemPlayerActivity.class);

                //传递列表
                Bundle bundle = new Bundle();
                bundle.putSerializable("videolist", mediaItems);
                intent.putExtras(bundle);

                //传递位置
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            /**
             * 下拉刷新
             * @param materialRefreshLayout
             */
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {

                getDataFromNet();

            }


            /**
             * 加载更多
             * @param materialRefreshLayout
             */
            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                getMoreDataFromNet();
            }
        });


        return view;
    }

    private void getMoreDataFromNet() {
        RequestParams reques = new RequestParams("http://api.m.mtime.cn/PageSubArea/TrailerList.api");
        x.http().get(reques, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                LogUtil.e("onSuccess==" + result);
                processMoreData(result);
                refresh.finishRefreshLoadMore();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG", "网络视频数据初始化了...");

        String saveJson = CacheUtils.getString(context, Constants.NET_VIDEO_URL);
        if(!TextUtils.isEmpty(saveJson)){
            processData(saveJson);
        }

        getDataFromNet();
    }

    private void getDataFromNet() {
        final RequestParams reques = new RequestParams(Constants.NET_VIDEO_URL);
        x.http().get(reques, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                CacheUtils.putString(context,Constants.NET_VIDEO_URL,result);
                LogUtil.e("onSuccess==" + result);
                processData(result);
                refresh.finishRefresh();
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError==" + ex.getMessage());
                refresh.finishRefresh();

                progressbar.setVisibility(View.GONE);
                refresh.finishRefreshLoadMore();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
            }
        });

    }

    /**
     * 解析和绑定数据
     *
     * @param json
     */
    private void processData(String json) {

        mediaItems = parsedJson(json);

        if(mediaItems != null && mediaItems.size() >0){
            //有视频
            tv_nomedia.setVisibility(View.GONE);
            //设置适配器
            myAdapter = new MyAdapter();
            listview.setAdapter(myAdapter);
        }else{
            //没有视频
            tv_nomedia.setVisibility(View.VISIBLE);
        }

        progressbar.setVisibility(View.GONE);

    }

    /**
     * 解析和绑定数据
     *
     * @param json
     */
    private void processMoreData(String json) {

        mediaItems.addAll(parsedJson(json));
        myAdapter.notifyDataSetChanged();


    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mediaItems.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = View.inflate(context,R.layout.item_netvideo_fragment,null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_video_icon);
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //根据位置获得对应的数据
            MediaItem mediaItem = mediaItems.get(position);
            x.image().bind(viewHolder.iv_icon,mediaItem.getImgUrl());
            viewHolder.tv_name.setText(mediaItem.getName());
            viewHolder.tv_desc.setText(mediaItem.getDesc());


            return convertView;
        }
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_desc;

    }

    /**
     * 手动解析json数据
     * @param json
     * @return
     */
    private ArrayList<MediaItem> parsedJson(String json) {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray trailers = jsonObject.optJSONArray("trailers");
            for (int i = 0; i < trailers.length(); i++) {
                JSONObject item = (JSONObject) trailers.get(i);
                if (item != null) {
                    MediaItem mediaItem = new MediaItem();

                    String name = item.optString("movieName");
                    mediaItem.setName(name);

                    String desc = item.optString("videoTitle");
                    mediaItem.setDesc(desc);

                    String imageUrl = item.optString("coverImg");
                    mediaItem.setImgUrl(imageUrl);

                    String data = item.optString("url");
                    mediaItem.setData(data);

                    mediaItems.add(mediaItem);

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mediaItems;
    }
}
