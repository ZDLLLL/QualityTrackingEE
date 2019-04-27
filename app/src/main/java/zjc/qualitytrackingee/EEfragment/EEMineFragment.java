package zjc.qualitytrackingee.EEfragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import zjc.qualitytrackingee.EEactivity.EEScheduleActivity;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.activity.AboutUsActivity;
import zjc.qualitytrackingee.activity.ChangePwActivity;
import zjc.qualitytrackingee.activity.LoginActivity;
import zjc.qualitytrackingee.activity.UserInfoActivity;
import zjc.qualitytrackingee.fragment.MineFragment;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.CleanCacheUtil;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class EEMineFragment extends Fragment {
    private View view;
    private SharedPreferences pref;
    private static SharedPreferences.Editor editor;
//    @BindView(R.id.eemine_sfl)
//    SwipeRefreshLayout eemine_sfl;
    @BindView(R.id.ee_name_tv)
    TextView ee_name_tv;
    @BindView(R.id.ee_picture_url_iv)
    ImageView ee_picture_url_iv;
    @BindView(R.id.ee_exit_ll)
    LinearLayout ee_exit_ll;
    private static ImageView eemine_notify_iv;
    @BindView(R.id.eemine_sfl)
    SwipeRefreshLayout eemine_sfl;
    @BindView(R.id.eemine_aboutus_ll)
    LinearLayout eemine_aboutus_ll;
    @BindView(R.id.eemine_schedule_ll)
    LinearLayout eemine_schedule_ll;
    public String image;

    public EEMineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view=inflater.inflate(R.layout.fragment_eemine, container, false);
        ButterKnife.bind(this,view);
        pref= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplication());
        editor=pref.edit();
        eemine_notify_iv=(ImageView) view.findViewById(R.id.eemine_notify_iv);
        if(pref.getBoolean("notify",true)){
            eemine_notify_iv.setImageResource(R.drawable.on);
        }else {
           eemine_notify_iv.setImageResource(R.drawable.off);
        }
        getUser();
        initsrl();
        return view;
    }
    public void initsrl() {
        eemine_sfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUser();
                eemine_sfl.setRefreshing(false);

            }
        });
    }
    public void getUser(){
        String url= Net.GetUser+"?e_phone="+ MyApplication.getE_phone();
        VolleyRequest.RequestGet(getContext(),url, "getuser",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            JSONObject jsonObject1=jsonObject.getJSONObject("personInfo");
                            String e_name=jsonObject1.getString("e_name");
                            String e_img=jsonObject1.getString("e_img");
                            image=e_img.replaceAll("\\\\","");
                            Glide.with(MyApplication.getContext())
                                    .load(image)
                                    .asBitmap()
                                    .error(R.drawable.head1)
                                    .into(ee_picture_url_iv);
                            ee_name_tv.setText(e_name);
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

    //我的排班
    @OnClick(R.id.eemine_schedule_ll)
    public void eemine_schedule_ll_Onclick(){
        Intent intent=new Intent(getActivity(), EEScheduleActivity.class);
        startActivity(intent);
    }
    //关于我们
    @OnClick(R.id.eemine_aboutus_ll)
    public void eemine_aboutus_ll_Onclick(){
        Intent intent=new Intent(getActivity(), AboutUsActivity.class);
        startActivity(intent);
    }
    //清空缓存
    @OnClick(R.id.eemine_clean_ll)
    public void eemine_clean_ll_Click(){
        EEMineFragment.EEConfirmDialog.EEnewInstance("clean")
                .setMargin(60)
                .setOutCancel(false)
                .show(getFragmentManager());
    }
    //个人信息
    @OnClick(R.id.eemine_update_ll)
    public void eemine_update_ll_Onclick(){
        Intent intent=new Intent(getActivity(),UserInfoActivity.class);
        intent.putExtra("Image",image);
        getActivity().startActivity(intent);
    }
    //退出登陆
    @OnClick(R.id.ee_exit_ll)
    public void my_exit_ll_Click(){
        EEMineFragment.EEConfirmDialog.EEnewInstance("exit")
                .setMargin(60)
                .setOutCancel(false)
                .show(getFragmentManager());
    }
    @OnClick(R.id.eemine_notify_iv)
    public void eemine_notify_iv_Click(){
        if(pref.getBoolean("notify",true)){

            EEMineFragment.EEConfirmDialog.EEnewInstance("notify_true")
                    .setMargin(60)
                    .setOutCancel(false)
                    .show(getFragmentManager());
        }else{
            EEMineFragment.EEConfirmDialog.EEnewInstance("notify_false")
                    .setMargin(60)
                    .setOutCancel(false)
                    .show(getFragmentManager());
        }
    }
    @OnClick(R.id.eemine_changepw_ll)
    public void eemine_changepw_ll_Click(){
        Intent intent=new Intent(getActivity(), ChangePwActivity.class);
        startActivity(intent);
    }

    public static void exit(){
        MyApplication.setE_phone("");
        MyApplication.setE_token("");
        MyApplication.setE_power("");
        MyApplication.setC_id("");
        editor.clear();
        editor.apply();
        Intent intent=new Intent(MyApplication.getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getContext().startActivity(intent);
    }
    public static class EEConfirmDialog extends BaseNiceDialog {
        private String type;

        public static EEMineFragment.EEConfirmDialog EEnewInstance(String type) {
            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            EEMineFragment.EEConfirmDialog dialog = new EEMineFragment.EEConfirmDialog();
            dialog.setArguments(bundle);
            return dialog;
        }
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle bundle = getArguments();
            if (bundle == null) {
                return;
            }
            type = bundle.getString("type");
        }
        @Override
        public int intLayoutId() {
            return  R.layout.confirm_layout;
        }

        @Override
        public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
            if ("exit".equals(type)) {
                holder.setText(R.id.title, "提示");
                holder.setText(R.id.message, "是否退出登陆?");

                holder.setOnClickListener(R.id.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                holder.setOnClickListener(R.id.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        exit();
                    }
                });
            }else if("notify_true".equals(type)){
                holder.setText(R.id.title, "提示");
                holder.setText(R.id.message, "是否关闭消息提醒？");

                holder.setOnClickListener(R.id.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                holder.setOnClickListener(R.id.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        JPushInterface.stopPush(MyApplication.getContext());
                        eemine_notify_iv.setImageResource(R.drawable.off);
                        editor.putBoolean("notify",false);
                        editor.apply();
                    }
                });
            }else if ("notify_false".equals(type)) {
                holder.setText(R.id.title, "提示");
                holder.setText(R.id.message, "是否开启消息提醒？");

                holder.setOnClickListener(R.id.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                holder.setOnClickListener(R.id.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        JPushInterface.resumePush(MyApplication.getContext());
                        eemine_notify_iv.setImageResource(R.drawable.on);
                        editor.putBoolean("notify",true);
                        editor.apply();

                    }
                });
            }else  if ("clean".equals(type)) {
                holder.setText(R.id.title, "提示");
                holder.setText(R.id.message, "是否清除缓存?");

                holder.setOnClickListener(R.id.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                holder.setOnClickListener(R.id.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        CleanCacheUtil.clearAllCache(getContext());
                        Toast.makeText(getContext(),"缓存已清理完成",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }

}
