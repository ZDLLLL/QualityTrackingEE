package zjc.qualitytrackingee.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import com.newcaoguo.easyrollingnumber.view.ScrollingDigitalAnimation;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.View.RuningNumView;
import zjc.qualitytrackingee.activity.AddGoodsClassActivity;
import zjc.qualitytrackingee.activity.AddJobActivity;
import zjc.qualitytrackingee.activity.AnalysisActivity;
import zjc.qualitytrackingee.activity.CheckActivity;
import zjc.qualitytrackingee.activity.MessageActivity;
import zjc.qualitytrackingee.activity.ScanActivity;
import zjc.qualitytrackingee.activity.StaffManagementActivity;
import zjc.qualitytrackingee.activity.UserFeedBackActivity;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.RunTextView;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.JOB_SCHEDULER_SERVICE;
import static com.mob.tools.utils.DeviceHelper.getApplication;
import static zjc.qualitytrackingee.MyApplication.getContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    @BindView(R.id.scan_ib)
    ImageButton scan_ib;
    @BindView(R.id.home_message_ib)
    ImageButton home_message_ib;
    @BindView(R.id.home_srl)
    SwipeRefreshLayout home_srl;
    //    @BindView(R.id.tickerView)
//    TextView tickerView;
    // @BindView(R.id.home_srl) SwipeRefreshLayout home_srl;
    private int REQUEST_CODE_SCAN = 111;
    private String code="0";
    RuningNumView tx_num1, tx_num2, tx_num3,tx_num4,tx_num5, tx_num6;
    private  static String c_id;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        c_id=MyApplication.getC_id();
        ButterKnife.bind(this, view);
        c_id=getActivity().getIntent().getStringExtra("c_id");
        tx_num1 = view.findViewById(R.id.tx_num1);
        tx_num2 = view.findViewById(R.id.tx_num2);
        tx_num3 = view.findViewById(R.id.tx_num3);
        tx_num4 = view.findViewById(R.id.tx_num4);
        tx_num5 = view.findViewById(R.id.tx_num5);
        tx_num6 = view.findViewById(R.id.tx_num6);
        getAllgoodsNumber();
        tx_num1.setDuration(2000).setTextAndDigit(code, 6);
        tx_num2.setDuration(2000).setTextAndDigit(code, 5);
        tx_num3.setDuration(2000).setTextAndDigit(code, 4);
        tx_num4.setDuration(2000).setTextAndDigit(code, 3);
        tx_num5.setDuration(2000).setTextAndDigit(code, 2);
        tx_num6.setDuration(2000).setTextAndDigit(code, 1);
        tx_num1.startAnimation();
        tx_num2.startAnimation();
        tx_num3.startAnimation();
        tx_num4.startAnimation();
        tx_num5.startAnimation();
        tx_num6.startAnimation();
        home_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                home_srl.setRefreshing(false);
                getAllgoodsNumber();
                tx_num1.setDuration(2000).setTextAndDigit(code, 6);
                tx_num2.setDuration(2000).setTextAndDigit(code, 5);
                tx_num3.setDuration(2000).setTextAndDigit(code, 4);
                tx_num4.setDuration(2000).setTextAndDigit(code, 3);
                tx_num5.setDuration(2000).setTextAndDigit(code, 2);
                tx_num6.setDuration(2000).setTextAndDigit(code, 1);
                tx_num1.startAnimation();
                tx_num2.startAnimation();
                tx_num3.startAnimation();
                tx_num4.startAnimation();
                tx_num5.startAnimation();
                tx_num6.startAnimation();
            }
        });
        return view;
    }


    @OnClick(R.id.home_message_ib)
    public void home_message_ib_Onclick(){
        Intent intent=new Intent(getActivity(),MessageActivity.class);
        getActivity().startActivity(intent);
    }
    @OnClick(R.id.scan_ib)
    public void scan_ib_Onclick() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(true);//是否播放扫描声音 默认为true
        config.setShake(true);//是否震动  默认为true
        config.setDecodeBarCode(true);//是否扫描条形码 默认为true
        config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
        config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
        config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                final String content = data.getStringExtra(Constant.CODED_CONTENT);
                final String url = Net.SaoYiSao + "?e_phone=" + MyApplication.getE_phone() + "&co_id=" + content;
                VolleyRequest.RequestGet(getContext(), url, "EdecodeQr",
                        new VolleyInterface(getContext(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {

                            @Override
                            public void onMySuccess(String result) {
                                try {

                                    JSONObject jsonObject=new JSONObject(result);
                                    String code=jsonObject.getString("EdecodeQr");
                                    if(code.equals("no commodity")){
                                        Toast.makeText(getActivity(), "没有该物品的信息", Toast.LENGTH_LONG).show();
                                    }else{
                                        Intent intent = new Intent(getActivity(), AnalysisActivity.class);
                                        intent.putExtra("content", content);
                                        startActivity(intent);
                                        Toast.makeText(getActivity(), "扫描结果为：" + content, Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onMyError(VolleyError error) {
                                Log.e("TAG", error.getMessage(), error);
                                Toast.makeText(getActivity(),"╮(╯▽╰)╭连接不上了",Toast.LENGTH_SHORT).show();
                            }
                        });

                //result.setText("扫描结果为：" + content);
            }
        }
    }
    public void getAllgoodsNumber() {
        String url = Net.GetAllGoodsNumber + "?c_id=" + c_id;
        VolleyRequest.RequestGet(getContext(), url, "getCompanyNum",
                new VolleyInterface(getContext(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            JSONObject jsonObject1=jsonObject.getJSONObject("getCompanyNum");
                            code=jsonObject1.getString("co_yield").toString();
                            tx_num1.setDuration(2000).setTextAndDigit(code, 6);
                            tx_num2.setDuration(2000).setTextAndDigit(code, 5);
                            tx_num3.setDuration(2000).setTextAndDigit(code, 4);
                            tx_num4.setDuration(2000).setTextAndDigit(code, 3);
                            tx_num5.setDuration(2000).setTextAndDigit(code, 2);
                            tx_num6.setDuration(2000).setTextAndDigit(code, 1);
                            tx_num1.startAnimation();
                            tx_num2.startAnimation();
                            tx_num3.startAnimation();
                            tx_num4.startAnimation();
                            tx_num5.startAnimation();
                            tx_num6.startAnimation();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(getContext(), "╮(╯▽╰)╭连接不上了", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
//    public void initView(){
//        sacn_rl=view.findViewById(R.id.scan_rl);
//        check_rl=view.findViewById(R.id.check_rl);
//        add_goodsclass_rl=view.findViewById(R.id.add_goodsclass_rl);
//        job_rl=view.findViewById(R.id.job_rl);
//        staff_manage_rl=view.findViewById(R.id.staff_manage_rl);
//        feedback_rl=view.findViewById(R.id.feedback_rl);
//        home_banner=view.findViewById(R.id.home_banner);
//        sacn_rl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(getActivity(), ScanActivity.class);
//                startActivity(intent);
//            }
//        });
//        check_rl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(getActivity(),CheckActivity.class);
//                startActivity(intent);
//            }
//        });
//        staff_manage_rl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(getActivity(), StaffManagementActivity.class);
//                startActivity(intent);
//            }
//        });
//        job_rl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(getActivity(), AddJobActivity.class);
//                startActivity(intent);
//            }
//        });
//        add_goodsclass_rl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(getActivity(), AddGoodsClassActivity.class);
//                startActivity(intent);
//            }
//        });
//        feedback_rl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(getActivity(), UserFeedBackActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//    private void initBanner(){
//        List<Integer> images=new ArrayList<>();
//        images.add(R.drawable.bannerimg1);
//        images.add(R.drawable.bannerimg2);
//        images.add(R.drawable.bannerimg3);
//        images.add(R.drawable.bannerimg4);
//        images.add(R.drawable.bannerimg5);
//        home_banner=view.findViewById(R.id.home_banner);
//        //设置轮播图的显示图片数组
//        home_banner.setImages(images);
//        //利用glide生成轮播图控件的5个imgaeView
//        home_banner.setImageLoader(new ImageLoader() {
//            @Override
//            public void displayImage(Context context, Object path, ImageView imageView) {
//                Glide.with(getContext()).load(path).into(imageView);
//            }
//        });
//        //播放轮播图
//       home_banner.setDelayTime(3000);
//        home_banner.start();
//    }


