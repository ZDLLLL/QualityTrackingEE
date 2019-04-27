package zjc.qualitytrackingee.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class GoodsFeedBackActivity extends AppCompatActivity {
    @BindView(R.id.goods_feedback_info_back_ll)
    LinearLayout goods_feedback_info_back_ll;
    @BindView(R.id.goods_feedback_info_time_tv)
    TextView goods_feedback_info_time_tv;
    @BindView(R.id.goods_feedback_info_goodsname_tv)
    TextView goods_feedback_info_goodsname_tv;
    @BindView(R.id.goods_feedback_info_image1_iv)
    ImageView goods_feedback_info_image1_iv;
    @BindView(R.id.goods_feedback_info_image2_iv)
    ImageView goods_feedback_info_image2_iv;
    @BindView(R.id.goods_feedback_info_image3_iv)
    ImageView goods_feedback_info_image3_iv;
    @BindView(R.id.goods_feedback_info_userimage_iv)
    ImageView goods_feedback_info_userimage_iv;
    @BindView(R.id.goods_feedback_info_messgae_tv)
    TextView goods_feedback_info_messgae_tv;
    @BindView(R.id.goods_feedback_info_username_tv)
    TextView goods_feedback_info_username_tv;
//    @BindView(R.id.user_feedback_info_read_bt)
//    Button goods_feedback_info_read_bt;
    @BindView(R.id.goods_feedback_info_score_tv)
    TextView goods_feedback_info_score_tv;
    private String co_id;
    private String f_score;
    private String image1;
    private String image2;
    private String image3;
    private String time;
    private String score;
    private String username;
    private String uImage;
    private String coname;
    private String describe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_feed_back);
        ButterKnife.bind(this);
        co_id=getIntent().getStringExtra("co_id");
        coname=getIntent().getStringExtra("co_name");
        GoodsFeedBack();
        InitView();
    }
    public void InitView(){
        goods_feedback_info_score_tv.setText(f_score);
        goods_feedback_info_time_tv.setText(time);
        goods_feedback_info_username_tv.setText(username);
        goods_feedback_info_goodsname_tv.setText(coname);
        goods_feedback_info_messgae_tv.setText(describe);
        Glide.with(MyApplication.getContext())
                .load(image1)
                .asBitmap()
                //.error(R.drawable.white)
                .into(goods_feedback_info_image1_iv);
        Glide.with(MyApplication.getContext())
                .load(image2)
                .asBitmap()
                //.error(R.drawable.white)
                .into(goods_feedback_info_image2_iv);
        Glide.with(MyApplication.getContext())
                .load(image3)
                .asBitmap()
              //  .error(R.drawable.white)
                .into(goods_feedback_info_image3_iv);
        Glide.with(MyApplication.getContext())
                .load(uImage)
                .asBitmap()
                .error(R.drawable.head1)
                .into(goods_feedback_info_userimage_iv);
    }
    @OnClick(R.id.goods_feedback_info_back_ll)
    public void goods_feedback_info_back_llOnclick(){
        finish();
    }

    public void GoodsFeedBack(){
        String url= Net.GetGoodsFeedBack+"?co_id="+co_id;
        VolleyRequest.RequestGet(getContext(),url, "getFeedbackBycoid",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            JSONArray jsonArray=jsonObject.getJSONArray("getFeedbackBycoid");
                            JSONObject jsonObject1=jsonArray.getJSONObject(0);
                             f_score=jsonObject1.getString("f_score");
                             image1=jsonObject1.getString("f_img1");
                            //image1.replaceAll("\\\\","");
                             image2=jsonObject1.getString("f_img2");
                            image3=jsonObject1.getString("f_img3");
                            uImage=jsonObject1.getString("u_img");
                            score=jsonObject1.getString("f_score");
                            time=jsonObject1.getString("f_time");
                            describe=jsonObject1.getString("f_describe");
                            username=jsonObject1.getString("u_name");
                            goods_feedback_info_score_tv.setText(f_score);
                            goods_feedback_info_time_tv.setText(time);
                            goods_feedback_info_username_tv.setText(username);
                            goods_feedback_info_goodsname_tv.setText(coname);
                            goods_feedback_info_messgae_tv.setText(describe);
                            Glide.with(MyApplication.getContext())
                                    .load(image1.replaceAll("\\\\",""))
                                    .asBitmap()
                                    //.error(R.drawable.head1)
                                    .into(goods_feedback_info_image1_iv);
                            Glide.with(MyApplication.getContext())
                                    .load(image2.replaceAll("\\\\",""))
                                    .asBitmap()
                                   // .error(R.drawable.head1)
                                    .into(goods_feedback_info_image2_iv);
                            Glide.with(MyApplication.getContext())
                                    .load(image3.replaceAll("\\\\",""))
                                    .asBitmap()
                                   // .error(R.drawable.head1)
                                    .into(goods_feedback_info_image3_iv);
                            Glide.with(MyApplication.getContext())
                                    .load(uImage.replaceAll("\\\\",""))
                                    .asBitmap()
                                    .error(R.drawable.head1)
                                    .into(goods_feedback_info_userimage_iv);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(getContext(),"╮(╯▽╰)╭连接不上了",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
