package atguigu.mobileplayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import org.xutils.common.util.DensityUtil;

import java.util.ArrayList;

import atguigu.mobileplayer.domain.Lyric;

/**
 * Created by lzq on 2016/10/10.
 */
public class ShowLyricView extends TextView {

    private ArrayList<Lyric> lyrics;
    private Paint paint;
    private Paint whitepaint;
    private int width;
    private int height;
    /**
     * 歌词列表中的索引
     */
    private int index;
    /**
     * 每行的高度
     */
    private float textHeight = 20;
    /**
     * 歌曲的播放进度
     */
    private float currentPosition;
    /**
     * 时间戳
     * 10-->10.11
     */
    private float timePoint;
    /**
     * 某一句的高亮显示时间
     */
    private float sleepTime;

    public ShowLyricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void initView() {

        textHeight = DensityUtil.dip2px(20);
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setTextSize(DensityUtil.dip2px(20));
        paint.setTextAlign(Paint.Align.CENTER);

        whitepaint = new Paint();
        whitepaint.setColor(Color.WHITE);
        whitepaint.setAntiAlias(true);
        whitepaint.setTextSize(DensityUtil.dip2px(16));
        whitepaint.setTextAlign(Paint.Align.CENTER);//水平居中对齐
        //设置假设歌词



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(lyrics!=null&&lyrics.size()>0) {
            float push = 0;

            if(sleepTime==0){
                push = 0;
            }else{
                // 这一句花的时间： 这一句休眠时间  =  这一句要移动的距离：总距离(行高)

                //这一句要移动的距离 = （这一句花的时间/这一句休眠时间） * 总距离(行高)
//                float delta =  ((currentPosition -timePoint)/sleepTime)*textHeight;

                //在屏幕上的坐标 =  总距离(行高) + 这一句要移动的距离
                push = textHeight + ((currentPosition -timePoint)/sleepTime)*textHeight;

            }
            canvas.translate(0,-push);

            String currentcontext = lyrics.get(index).getContent();
            canvas.drawText(currentcontext,width/2,height/2,paint);
            float tempY = height / 2;
            for (int i = index - 1; i >= 0; i--) {
                String preContent = lyrics.get(i).getContent();
                tempY = tempY - textHeight;
                if (tempY < 0) {
                    break;
                }
                canvas.drawText(preContent, width / 2, tempY, whitepaint);

            }
            //绘制后面部分
            tempY = height / 2;
            for (int i = index + 1; i < lyrics.size(); i++) {
                String nextContent = lyrics.get(i).getContent();
                tempY = tempY + textHeight;
                if (tempY > height) {
                    break;
                }
                canvas.drawText(nextContent, width / 2, tempY, whitepaint);
            }

        }
        else {
            canvas.drawText("没有歌词",width/2,height/2,paint);
        }
    }

    /**
     * @param position 当前歌曲的播放进度
     */
    public void setNextShowLyric(int position) {
        this.currentPosition = position;
        //根据当前歌曲的播放进度，找到歌词列表的索引
        //重新绘制
        if (lyrics == null || lyrics.size() == 0)
            return;

        //不为空
        for (int i = 1; i < lyrics.size(); i++) {

            if (currentPosition < lyrics.get(i).getTimePoint()) {


                int temtIndex = i - 1;//0
                if (currentPosition >= lyrics.get(temtIndex).getTimePoint()) {
                    //0；
                    index = temtIndex;

                    //缓缓往上推移
                    //时间戳
                    timePoint = lyrics.get(index).getTimePoint();
                    //高亮显示时间
                    sleepTime = lyrics.get(index).getSleepTime();
                }

            }

        }


        invalidate();//回调当作onDraw执行

    }

    public void setLyric(ArrayList<Lyric> lyrics) {
        this.lyrics = lyrics;
    }
}

