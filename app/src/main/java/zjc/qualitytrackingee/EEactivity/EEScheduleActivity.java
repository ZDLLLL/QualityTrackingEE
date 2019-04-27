package zjc.qualitytrackingee.EEactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

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

public class EEScheduleActivity extends AppCompatActivity {
@BindView(R.id.eeschedule_back_ll)
    LinearLayout eeschedule_back_ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eeschedule);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.eeschedule_back_ll)
    public  void eeschedule_back_ll_Onclick(){
        finish();
    }
    public void getAllSechedule(){
        String url= Net.GetAllSchedule+"?e_phone="+ MyApplication.getE_phone();
        VolleyRequest.RequestGet(getContext(),url, "getAllSchedule",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            String code=jsonObject.getString("getScheduleByphone");
                            JSONArray jsonArray=jsonObject.getJSONArray("getScheduleByphone");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {

                    }
                });
    }
}
