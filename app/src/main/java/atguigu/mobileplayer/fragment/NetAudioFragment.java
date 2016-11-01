package atguigu.mobileplayer.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import atguigu.mobileplayer.base.BaseFragment;
import atguigu.mobileplayer.utils.LogUtil;

/**
 * Created by Administrator on 2016/9/28.
 */
public class NetAudioFragment extends BaseFragment {
    private TextView textView;

    /**
     * 初始化视图
     * @return
     */
    @Override
    public View initView() {
        LogUtil.e("网络音频UI创建了");
        textView = new TextView(context);
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("网络音乐的内容");
    }
}
