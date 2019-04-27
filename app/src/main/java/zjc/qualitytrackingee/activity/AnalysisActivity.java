package zjc.qualitytrackingee.activity;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zjc.qualitytrackingee.EEactivity.EETestActivity;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.MathTools;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class AnalysisActivity extends AppCompatActivity {
    private String content;
    @BindView(R.id.analysis_goodsclass_tv)
    TextView analysis_goodsclass_tv;
    @BindView(R.id.analysis_goodsdescribe_tv)
    TextView analysis_goodsdescribe_tv;
    @BindView(R.id.analysis_goodsprice_tv)
    TextView analysis_goodsprice_tv;
    @BindView(R.id.analysis_goodsimage_iv)
    ImageView analysis_goodsimage_iv;
    @BindView(R.id.analysis_goodsname_tv)
    TextView analysis_goodsname_tv;
    @BindView(R.id.analysis_back_ll)
    LinearLayout analysis_back_ll;
    @BindView(R.id.analysis_showevaluate_bt)
    Button analysis_showevaluate_bt;
    @BindView(R.id.analysis_showdutypeople_bt)
    Button analysis_showdutypeople_bt;
    private String goodsname;
    private String goodsclassname;
    private String goodsprice;
    private String goodsimage;
    private String goodsdescribe;
    private String co_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        ButterKnife.bind(this);
        content=getIntent().getStringExtra("content");
        analysis_showdutypeople_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String url= Net.GetGoodsDuty+"?co_id="+content;
                VolleyRequest.RequestGet(getContext(),url, "selectHeadPerson",
                        new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {
                            @Override
                            public void onMySuccess(String result) {

                                Log.d("zjc",url);
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String  code=jsonObject.getString("selectHeadPerson");
                                    if("no".equals(code)){
                                        Toast.makeText(AnalysisActivity.this,"该商品没有加工",Toast.LENGTH_LONG).show();
                                    }else {
                                        Intent intent=new Intent(AnalysisActivity.this,GoodsDutyActivity.class);
                                        intent.putExtra("co_id",content);
                                        startActivity(intent);
                                    }

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
        });
        uploadGoodsId();
        InitView();

    }
//    @OnClick(R.id.analysis_showdutypeople_bt)
//    public void analysis_showdutypeople_bt_Onclick(){
//        Intent intent=new Intent(AnalysisActivity.this,GoodsDutyActivity.class);
//        intent.putExtra("co_id",content);
//        startActivity(intent);
//
//    }
    @OnClick(R.id.analysis_showevaluate_bt)
    public  void analysis_showevaluate_bt_Onclick(){
        if(co_status.equals("0")) {
            Intent intent = new Intent(AnalysisActivity.this, GoodsFeedBackActivity.class);
            intent.putExtra("co_id", content);
            startActivity(intent);
        }else {
            Toast.makeText(AnalysisActivity.this,"该物品没有评价",Toast.LENGTH_SHORT).show();

        }
    }
    @OnClick(R.id.analysis_back_ll)
    public void analysis_back_ll_Onclick(){
        finish();
    }
    public void InitView(){
        analysis_goodsclass_tv.setText(goodsclassname);
        analysis_goodsdescribe_tv.setText(goodsdescribe);
        analysis_goodsname_tv.setText(goodsname);
        analysis_goodsprice_tv.setText(goodsprice);
        Glide.with(MyApplication.getContext())
                .load(goodsimage)
                .asBitmap()
                //.error(R.drawable.white)
                .into(analysis_goodsimage_iv);


    }
    public void uploadGoodsId() {
        String coid = MathTools.getMd5(content);
        String url = Net.SaoYiSao + "?e_phone=" + MyApplication.getE_phone() + "&co_id=" + content;
        VolleyRequest.RequestGet(getContext(), url, "getAllUserFeedBack",
                new VolleyInterface(getContext(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            JSONArray jsonArray=jsonObject.getJSONArray("EdecodeQr");
                            JSONObject jsonObject1=jsonArray.getJSONObject(0);
                            goodsclassname=jsonObject1.getString("coc_id");
                            goodsdescribe=jsonObject1.getString("co_describe");
                            String image=jsonObject1.getString("co_img");
                            goodsimage=image.replaceAll("////","");
                            goodsprice=jsonObject1.getString("co_price");
                            goodsname=jsonObject1.getString("co_name");
                            co_status=jsonObject1.getString("co_status");
                            InitView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(AnalysisActivity.this,"╮(╯▽╰)╭连接不上了",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
