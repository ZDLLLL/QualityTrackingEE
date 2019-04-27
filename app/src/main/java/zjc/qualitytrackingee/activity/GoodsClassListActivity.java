package zjc.qualitytrackingee.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.adapter.GoodsClassListSortAdapter;
import zjc.qualitytrackingee.beans.GoodsClassBean;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.SideBar;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class GoodsClassListActivity extends AppCompatActivity {
    private SideBar activity_goodsclasslist_sb;
    private ListView acvitivity_goodsclasslist_lv;
   // private ArrayList<GoodsClassBean> list=new ArrayList<>();
    public  ArrayList<GoodsClassBean> list1=new ArrayList<>();
    private LinearLayout goodsclass_back_ll;
    public static String co_name;
    public static String co_price;
    public static String coc_classid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_class_list);
        acvitivity_goodsclasslist_lv = (ListView) findViewById(R.id.acvitivity_goodsclasslist_lv);
        activity_goodsclasslist_sb = (SideBar) findViewById(R.id.activity_goodsclasslist_sb);
        goodsclass_back_ll=findViewById(R.id.goodsclass_back_ll);
      //  getSupportActionBar().hide();
        co_name=getIntent().getStringExtra("co_name");
        co_price=getIntent().getStringExtra("co_price");
        initView();
        loadingList();
        //initData();

    }
    private void initView() {
        activity_goodsclasslist_sb.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                for (int i = 0; i < list1.size(); i++) {
                    if (selectStr.equalsIgnoreCase(list1.get(i).getFirstLetter())) {
                        acvitivity_goodsclasslist_lv.setSelection(i); // 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });
        goodsclass_back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initData() {
       //loadingList();
        // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
        GoodsClassListSortAdapter adapter = new GoodsClassListSortAdapter(this, list1,this);
        acvitivity_goodsclasslist_lv.setAdapter(adapter);

    }
    public void loadingList(){
//        list.add(new GoodsClassBean("Android开发交流群"));
//        list.add(new GoodsClassBean("IOS开发交流群"));
//        list.add(new GoodsClassBean("Scala开发交流群"));
//        list.add(new GoodsClassBean("C++开发交流群"));
//        list.add(new GoodsClassBean("Java开发交流群"));
//        list.add(new GoodsClassBean("Android开发交流群"));
//        Collections.sort(list);
        //list=new ArrayList<>();
        list1=new ArrayList<>();
        String url= Net.GetAllGoodsClass+"?c_id="+ MyApplication.getC_id();
        VolleyRequest.RequestGet(getContext(),url, "getallGoodsclass",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {

                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            JSONArray jsonArray=jsonObject.getJSONArray("getAllCommodityClassByCid");
                            GoodsClassBean goodsClassBean;
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                goodsClassBean=new GoodsClassBean();
                                //String coclass=jsonObject1.getString("coc_name");
                                String coname=jsonObject1.getString("coc_name");
                                String coclass=jsonObject1.getString("coc_id");
                                goodsClassBean.setCo_classid(coclass);
                                goodsClassBean.setCo_class(coname);
                                //list.add(new GoodsClassBean(coname));
                                list1.add(new GoodsClassBean(coname,coclass));

                            }
                            //Collections.sort(list);
                            Collections.sort(list1);
                            initData();
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
