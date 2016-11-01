package atguigu.mobileplayer.adapter;

/**
 * Created by lzq on 2016/10/13.
 */
/*
public class MyRecycleAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<AllDataBean.ListBean> datas;
    private Utils utils;
    */
/**
     * 视频
     *//*

    private static final int TYPE_VIDEO = 0;

    */
/**
     * 图片
     *//*

    private static final int TYPE_IMAGE = 1;

    */
/**
     * 文字
     *//*

    private static final int TYPE_TEXT = 2;

    */
/**
     * GIF图片
     *//*

    private static final int TYPE_GIF = 3;


    */
/**
     * 软件推广
     *//*

    private static final int TYPE_AD = 4;

    public MyRecycleAdapter(Context context, List<AllDataBean.ListBean> datas) {
        this.context = context;
        this.datas = datas;
        utils = new Utils();
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = initView(viewType);
        return new ViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindData(position,getItemViewType(position),(ViewHolder) holder);
    }


    */
/**
     * 当前item是什么类型
     *
     * @param position
     * @return
     *//*

    @Override
    public int getItemViewType(int position) {
        int itemViewType = -1;
        //根据位置，从列表中得到一个数据对象
        AllDataBean.ListBean listBean = datas.get(position);
        String type = listBean.getType();//得到类型
        if ("video".equals(type)) {
            itemViewType = TYPE_VIDEO;
        } else if ("image".equals(type)) {
            itemViewType = TYPE_IMAGE;
        } else if ("text".equals(type)) {
            itemViewType = TYPE_TEXT;
        } else if ("gif".equals(type)) {
            itemViewType = TYPE_GIF;
        } else {
            itemViewType = TYPE_AD;//广播
        }
        return itemViewType;
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }


    private View initView(int itemViewType) {
        View convertView = null;
        switch (itemViewType) {
            case TYPE_VIDEO://视频
                convertView = View.inflate(context, R.layout.all_video_item, null);
                break;
            case TYPE_IMAGE://图片
                convertView = View.inflate(context, R.layout.all_image_item, null);
                break;
            case TYPE_TEXT://文字
                convertView = View.inflate(context, R.layout.all_text_item, null);
                break;
            case TYPE_GIF://gif
                convertView = View.inflate(context, R.layout.all_gif_item, null);
                break;
            case TYPE_AD://软件广告
                convertView = View.inflate(context, R.layout.all_ad_item, null);
                break;
        }
        return convertView;
    }


    private void bindData(int position, int itemViewType, ViewHolder viewHolder) {
        //根据位置得到数据,绑定数据
        AllDataBean.ListBean mediaItem = datas.get(position);
        switch (itemViewType) {
            case TYPE_VIDEO://视频
                bindData(viewHolder, mediaItem);
                //第一个参数是视频播放地址，第二个参数是显示封面的地址，第三参数是标题
                boolean setUp = viewHolder.jcv_videoplayer.setUp(
                        mediaItem.getVideo().getVideo().get(0), JCVideoPlayer.SCREEN_LAYOUT_LIST,
                        "");
                //加载图片
                if (setUp) {
                    ImageLoader.getInstance().displayImage(mediaItem.getVideo().getThumbnail().get(0),
                            viewHolder.jcv_videoplayer.thumbImageView);
                }
                viewHolder.tv_play_nums.setText(mediaItem.getVideo().getPlaycount() + "次播放");
                viewHolder.tv_video_duration.setText(utils.stringForTime(mediaItem.getVideo().getDuration() * 1000) + "");

                break;
            case TYPE_IMAGE://图片
                bindData(viewHolder, mediaItem);
                viewHolder.iv_image_icon.setImageResource(R.drawable.bg_item);
//                int  height = mediaItem.getImage().getImage().getHeight()<= DensityUtil.getScreenHeight()*0.75?mediaItem.getImage().getImage().getHeight(): (int) (DensityUtil.getScreenHeight() * 0.75);
//
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.getScreenWidth(),height);
//                viewHolder.iv_image_icon.setLayoutParams(params);
//                LogUtil.e("mediaItem.getImage().getImage().getBig().get(0)=="+mediaItem.getImage().getImage().getSmall().get(0));
                if (mediaItem.getImage() != null && mediaItem.getImage() != null && mediaItem.getImage().getSmall() != null) {
//                    x.image().bind(viewHolder.iv_image_icon, mediaItem.getImage().getBig().get(0));
                    Glide.with(context).load(mediaItem.getImage().getDownload_url().get(0)).placeholder(R.drawable.bg_item).error(R.drawable.bg_item).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.iv_image_icon);
                }
                break;
            case TYPE_TEXT://文字
                bindData(viewHolder, mediaItem);
                break;
            case TYPE_GIF://gif
                bindData(viewHolder, mediaItem);
                if (mediaItem.getGif() != null && mediaItem.getGif() != null && mediaItem.getGif().getImages() != null) {
                    Glide.with(context).load(mediaItem.getGif().getImages().get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.iv_image_gif);
                }
                break;
            case TYPE_AD://软件广告
                break;
        }

        //设置文本
        viewHolder.tv_context.setText(mediaItem.getText() + "_" + mediaItem.getType());

    }


    private void bindData(ViewHolder viewHolder, AllDataBean.ListBean mediaItem) {
        if (mediaItem.getU() != null && mediaItem.getU().getHeader() != null && mediaItem.getU().getHeader().get(0) != null) {
            x.image().bind(viewHolder.iv_headpic, mediaItem.getU().getHeader().get(0));
        }
        if (mediaItem.getU() != null && mediaItem.getU().getName() != null) {
            viewHolder.tv_name.setText(mediaItem.getU().getName() + "");
        }

        viewHolder.tv_time_refresh.setText(mediaItem.getPasstime());

        //设置标签
        List<AllDataBean.ListBean.TagsBean> tagsEntities = mediaItem.getTags();
        if (tagsEntities != null && tagsEntities.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < tagsEntities.size(); i++) {
                buffer.append(tagsEntities.get(i).getName() + " ");
            }
            viewHolder.tv_video_kind_text.setText(buffer.toString());
        }

        //设置点赞，踩,转发
        viewHolder.tv_shenhe_ding_number.setText(mediaItem.getUp());
        viewHolder.tv_shenhe_cai_number.setText(mediaItem.getDown() + "");
        viewHolder.tv_posts_number.setText(mediaItem.getForward() + "");

    }


    */
/**
     * 设置数据
     *
     * @param datas
     *//*

    public void setData(List<AllDataBean.ListBean> datas) {
        this.datas = datas;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        //user_info
        ImageView iv_headpic;
        TextView tv_name;
        TextView tv_time_refresh;
        ImageView iv_right_more;
        //bottom
        ImageView iv_video_kind;
        TextView tv_video_kind_text;
        TextView tv_shenhe_ding_number;
        TextView tv_shenhe_cai_number;
        TextView tv_posts_number;
        LinearLayout ll_download;

        //中间公共部分 -所有的都有
        TextView tv_context;


        //Video
//        TextView tv_context;
        TextView tv_play_nums;
        TextView tv_video_duration;
        ImageView iv_commant;
        TextView tv_commant_context;
        JCVideoPlayerStandard jcv_videoplayer;

        //Image
        ImageView iv_image_icon;
//        TextView tv_context;

        //Text
//        TextView tv_context;

        //Gif
        ImageView iv_image_gif;

        //软件推广
        Button btn_install;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            switch (viewType) {

                case TYPE_VIDEO://视频
                    //在这里实例化特有的
                    tv_play_nums = (TextView) itemView.findViewById(R.id.tv_play_nums);
                    tv_video_duration = (TextView) itemView.findViewById(R.id.tv_video_duration);
                    iv_commant = (ImageView) itemView.findViewById(R.id.iv_commant);
                    tv_commant_context = (TextView) itemView.findViewById(R.id.tv_commant_context);
                    jcv_videoplayer = (JCVideoPlayerStandard) itemView.findViewById(R.id.jcv_videoplayer);
                    break;
                case TYPE_IMAGE://图片
                    iv_image_icon = (ImageView) itemView.findViewById(R.id.iv_image_icon);
                    break;
                case TYPE_TEXT://文字
                    break;
                case TYPE_GIF://gif
                    iv_image_gif = (ImageView) itemView.findViewById(R.id.iv_image_gif);
                    break;
                case TYPE_AD://软件广告
                    btn_install = (Button) itemView.findViewById(R.id.btn_install);
                    iv_image_icon = (ImageView) itemView.findViewById(R.id.iv_image_icon);
                    break;
            }

            switch (viewType) {
                case TYPE_VIDEO://视频
                case TYPE_IMAGE://图片
                case TYPE_TEXT://文字
                case TYPE_GIF://gif
                    //加载除开广告部分的公共部分视图
                    //user info
                    iv_headpic = (ImageView) itemView.findViewById(R.id.iv_headpic);
                    tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                    tv_time_refresh = (TextView) itemView.findViewById(R.id.tv_time_refresh);
                    iv_right_more = (ImageView) itemView.findViewById(R.id.iv_right_more);
                    //bottom
                    iv_video_kind = (ImageView) itemView.findViewById(R.id.iv_video_kind);
                    tv_video_kind_text = (TextView) itemView.findViewById(R.id.tv_video_kind_text);
                    tv_shenhe_ding_number = (TextView) itemView.findViewById(R.id.tv_shenhe_ding_number);
                    tv_shenhe_cai_number = (TextView) itemView.findViewById(R.id.tv_shenhe_cai_number);
                    tv_posts_number = (TextView) itemView.findViewById(R.id.tv_posts_number);
                    ll_download = (LinearLayout) itemView.findViewById(R.id.ll_download);

                    break;
            }


            //中间公共部分 -所有的都有
            tv_context = (TextView) itemView.findViewById(R.id.tv_context);



            //设置item的点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AllDataBean.ListBean listEntity = datas.get(getLayoutPosition());
                    if(listEntity !=null ){
                        //3.传递视频列表
                        Intent intent = new Intent(context,ShowImageAndGifActivity.class);
                        if(listEntity.getType().equals("gif")){
                            String url = listEntity.getGif().getImages().get(0);
                            intent.putExtra("url",url);
                            context.startActivity(intent);
                        }else if(listEntity.getType().equals("image")){
                            String url = listEntity.getImage().getBig().get(0);
                            intent.putExtra("url",url);
                            context.startActivity(intent);
                        }
                    }
                }
            });


        }


    }

}
*/
