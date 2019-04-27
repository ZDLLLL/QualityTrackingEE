package zjc.qualitytrackingee.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class UserFeedBackInfoActivity extends AppCompatActivity {
    @BindView(R.id.user_feedback_info_back_ll)
    LinearLayout user_feedback_info_back_ll;
    @BindView(R.id.user_feedback_info_time_tv)
    TextView user_feedback_info_time_tv;
    @BindView(R.id.user_feedback_info_goodsname_tv)
    TextView user_feedback_info_goodsname_tv;
    @BindView(R.id.user_feedback_info_image1_iv)
    ImageView user_feedback_info_image1_iv;
    @BindView(R.id.user_feedback_info_image2_iv)
    ImageView user_feedback_info_image2_iv;
    @BindView(R.id.user_feedback_info_image3_iv)
    ImageView user_feedback_info_image3_iv;
    @BindView(R.id.user_feedback_info_userimage_iv)
    ImageView user_feedback_info_userimage_iv;
    @BindView(R.id.user_feedback_info_messgae_tv)
    TextView user_feedback_info_messgae_tv;
    @BindView(R.id.user_feedback_info_username_tv)
    TextView user_feedback_info_username_tv;
    @BindView(R.id.user_feedback_info_read_bt)
    Button user_feedback_info_read_bt;
    @BindView(R.id.user_feedback_info_score_tv)
    TextView user_feedback_info_score_tv;
    private String coname;
    private String username;
    private String userImage;
    private String image1;
    private  String image2;
    private String image3;
    private String score;
    private String message;
    private String time;
    private String status;
    private String f_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed_back_info);
        ButterKnife.bind(this);
        coname=getIntent().getStringExtra("coname");
        message=getIntent().getStringExtra("message");
        score=getIntent().getStringExtra("score");
        image2=getIntent().getStringExtra("image2");
        image1=getIntent().getStringExtra("image1");
        image3=getIntent().getStringExtra("image3");
        userImage=getIntent().getStringExtra("userImage");
        time=getIntent().getStringExtra("time");
        username=getIntent().getStringExtra("username");
       // status=getIntent().getStringExtra("status");
        f_id=getIntent().getStringExtra("f_id");
        InitView();
    }
    @OnClick(R.id.user_feedback_info_back_ll)
    public void user_feedback_info_back_llOnclick(){
        finish();
    }
    @OnClick(R.id.user_feedback_info_read_bt)
    public void user_feedback_info_read_bt_Onclick(){
        submitRead();
        finish();
    }
    public void InitView(){
        Glide.with(MyApplication.getContext())
                .load(image1)
                .asBitmap()
              //  .error(R.drawable.white)
                .into(user_feedback_info_image1_iv);
        Glide.with(MyApplication.getContext())
                .load(image2)
                .asBitmap()
               // .error(R.drawable.white)
                .into(user_feedback_info_image2_iv);
        Glide.with(MyApplication.getContext())
                .load(image3)
                .asBitmap()
               // .error(R.drawable.white)
                .into(user_feedback_info_image3_iv);
        Glide.with(MyApplication.getContext())
                .load(userImage)
                .asBitmap()
                .error(R.drawable.head0)
                .into(user_feedback_info_userimage_iv);
        user_feedback_info_score_tv.setText(score);
        user_feedback_info_messgae_tv.setText(message);
        user_feedback_info_username_tv.setText(username);
        user_feedback_info_time_tv.setText(time);
        user_feedback_info_goodsname_tv.setText(coname);
        user_feedback_info_image2_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    public void submitRead() {
        status="0";
        String url = Net.SubmitRead+"?f_id="+f_id+"&f_status="+status;
        VolleyRequest.RequestGet(getContext(), url, "getAllUserFeedBack",
                new VolleyInterface(getContext(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        Toast.makeText(UserFeedBackInfoActivity.this,"标记成功",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(getContext(),"登陆失败，请检查网络连接",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
