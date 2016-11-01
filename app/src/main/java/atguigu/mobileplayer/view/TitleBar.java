package atguigu.mobileplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import atguigu.mobileplayer.R;


/**
 * 作者：尚硅谷-杨光福 on 2016/9/28 10:37
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：标题栏
 */
public class TitleBar extends LinearLayout implements View.OnClickListener {

    private TextView textView;
    private RelativeLayout relativeLayout;
    private ImageView imageView;

    private Context context;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
//        View.inflate(context,R.layout.titblebar,this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        textView = (TextView) getChildAt(1);
        relativeLayout = (RelativeLayout) getChildAt(2);
        imageView = (ImageView) getChildAt(3);

        //设置点击事件
        textView.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);
        imageView.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search:
                Toast.makeText(context, "搜索", Toast.LENGTH_SHORT).show();

                break;
            case R.id.rl_game:
                Toast.makeText(context, "游戏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_history:
                Toast.makeText(context, "历史", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
