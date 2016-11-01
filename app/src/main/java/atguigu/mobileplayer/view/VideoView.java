package atguigu.mobileplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 作者：尚硅谷-杨光福 on 2016/9/29 15:32
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：自定义VideoView
 */
public class VideoView extends android.widget.VideoView {
    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }

    /**
     * 设置视频的宽和高
     * @param videoWidth
     * @param videoHeight
     */
    public void setVideoSize(int videoWidth,int videoHeight){
        ViewGroup.LayoutParams l = getLayoutParams();
        l.width = videoWidth;
        l.height = videoHeight;
        setLayoutParams(l);
    }
}
