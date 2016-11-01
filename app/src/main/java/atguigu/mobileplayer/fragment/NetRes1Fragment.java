package atguigu.mobileplayer.fragment;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import atguigu.mobileplayer.R;
import atguigu.mobileplayer.adapter.MyListAdapter;
import atguigu.mobileplayer.base.BaseFragment;
import atguigu.mobileplayer.domain.AllDataBean;

/**
 * Created by lzq on 2016/10/13.
 */
public class NetRes1Fragment extends BaseFragment {
    private ProgressBar progressbar_net_res1;
    private String url = "http://s.budejie.com/topic/list/jingxuan/1/budejie-android-6.2.8/0-20.json?market=baidu&udid=863425026599592&appname=baisibudejie&os=4.2.2&client=android&visiting=&mac=98%3A6c%3Af5%3A4b%3A72%3A6d&ver=6.2.8";
    private ListView listview_net_res1;
    private InputStream is;
    private ByteArrayOutputStream baos;
    private HttpURLConnection conn;
    private ArrayList<AllDataBean.ListBean> list_net_res1;
    private TextView tv_net_res1;
    private MaterialRefreshLayout refresh;
    @Override
    public View initView() {
        View view = View.inflate(getContext(), R.layout.fragment_net_res1,null);
        progressbar_net_res1 = (ProgressBar) view.findViewById(R.id.progressbar_net_res1);
        tv_net_res1 = (TextView) view.findViewById(R.id.tv_net_res1);
        listview_net_res1 = (ListView) view.findViewById(R.id.listview_net_res1);
        listview_net_res1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        refresh = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getDataFromNet(url);
                Log.e("TAG", "下拉刷新");
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                getMoreDataFromNet();
            }
        });


        return view;
    }



    private void getMoreDataFromNet() {
        getDataFromNet(url);
        list_net_res1.addAll(list_net_res1);
        new MyListAdapter(getContext(),list_net_res1).notifyDataSetChanged();
        refresh.finishRefreshLoadMore();

    }

    @Override
    public void initData() {
        super.initData();

        getDataFromNet(url);


    }
    private void getDataFromNet(String url) {
        Log.e("TAG", "请求方法已调用"+url);
        RequestParams paranms = new RequestParams(url);


        x.http().get(paranms, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "请求成功=="+result);
                //解析和显示数据
                progressbar_net_res1.setVisibility(View.GONE);
                processData(result);
                refresh.finishRefresh();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "请求失败==" + ex.getMessage());
                progressbar_net_res1.setVisibility(View.GONE);
                tv_net_res1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("TAG", "onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
            }
        });

    }

    private void processData(String result) {
        Gson gson = new Gson();
        AllDataBean trailer = gson.fromJson(result, AllDataBean.class);
        list_net_res1 = (ArrayList<AllDataBean.ListBean>) trailer.getList();
        listview_net_res1.setAdapter(new MyListAdapter(getActivity(),list_net_res1));

    }


}
