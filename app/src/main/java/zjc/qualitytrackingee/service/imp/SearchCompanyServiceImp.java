package zjc.qualitytrackingee.service.imp;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.activity.EnrollActivity;
import zjc.qualitytrackingee.beans.SearchBean;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.service.SearchCompanyService;
import zjc.qualitytrackingee.utils.GsonImpl;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class SearchCompanyServiceImp implements SearchCompanyService {
    private EnrollActivity enrollActivity;
    public SearchCompanyServiceImp(EnrollActivity enrollActivity){
        this.enrollActivity=enrollActivity;
    }
    @Override
    public void searchLoadingER(final String key) {
        StringRequest search = new StringRequest(Request.Method.POST, Net.Search, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                SearchBean searchBean= GsonImpl.get().toObject(s,SearchBean.class);
                List<SearchBean.search> searchList=searchBean.getSearch();
                enrollActivity.showSearchER(searchList);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(),"╮(╯▽╰)╭连接不上了",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("key",key);
                return map;
            }

        };
        search.setTag("search");
        MyApplication.getHttpRequestQueue().add(search);
    }
    @Override
    public void searchLoadingEE(final String key) {
        StringRequest search = new StringRequest(Request.Method.POST, Net.Search, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                SearchBean searchBean= GsonImpl.get().toObject(s,SearchBean.class);
                List<SearchBean.search> searchList=searchBean.getSearch();
                enrollActivity.showSearchEE(searchList);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(),"╮(╯▽╰)╭连接不上了",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("key",key);
                return map;
            }

        };
        search.setTag("search");
        MyApplication.getHttpRequestQueue().add(search);
    }
}
