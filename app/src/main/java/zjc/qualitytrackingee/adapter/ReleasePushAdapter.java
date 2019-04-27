package zjc.qualitytrackingee.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.activity.ReleasePushActivity;
import zjc.qualitytrackingee.activity.StaffManagementActivity;
import zjc.qualitytrackingee.activity.StaffManagementInfoActivity;
import zjc.qualitytrackingee.beans.ReleasePushBean;

public class ReleasePushAdapter extends RecyclerView.Adapter{
    private ReleasePushActivity releasePushActivity;
    public class ReleasePushViewHolder extends  RecyclerView.ViewHolder{
        private CheckBox releasepush_item_cb;
        private LinearLayout releasepuhs_item_ll;
        private TextView releasepush_item_name_tv;
        private TextView releasepush_item_phone_tv;
        //参数itemView就是country_item.xml中的大窗口View对象
        public ReleasePushViewHolder(View itemView) {
            super(itemView);
            releasepush_item_cb=itemView.findViewById(R.id.releasepush_item_cb);
            releasepuhs_item_ll=itemView.findViewById(R.id.releasepuhs_item_ll);
            releasepush_item_name_tv=itemView.findViewById(R.id.releasepush_item_name_tv);
            releasepush_item_phone_tv=itemView.findViewById(R.id.releasepush_item_phone_tv);
        }



    }
    private Context context;
   // private List<String> ImageList;
    private List<String> NameList;
    private List<String> PhoneList;
    private List<ReleasePushBean.ReleasePushsBean> list;

    public ReleasePushAdapter(Context context, List<String> phoneList, List<String> nameList) {
        //this.staffManagementActivity=staffManagementActivity;
        this.context = context;
        PhoneList = phoneList;
        NameList = nameList;

    }

    public ReleasePushAdapter(Context context, List<ReleasePushBean.ReleasePushsBean> list) {
        //this.staffManagementActivity=staffManagementActivity;
        this.context = context;
        this.list = list;


    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.releasepush_item,viewGroup,false);
        return new ReleasePushViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        ReleasePushViewHolder releasePushViewHolder=(ReleasePushViewHolder)holder;
        //releasePushViewHolder.releasepush_item_cb.setChecked(false);
        if(releasePushActivity.isSelectAll){
                releasePushViewHolder.releasepush_item_cb.setChecked(true);
        }else {
            releasePushViewHolder.releasepush_item_cb.setChecked(false);
        }

//        releasePushViewHolder.releasepush_item_phone_tv.setText(PhoneList.get(position));
//        releasePushViewHolder.releasepush_item_name_tv.setText(NameList.get(position));
            releasePushViewHolder.releasepush_item_name_tv.setText(list.get(position).getReleasepushName());
            releasePushViewHolder.releasepush_item_phone_tv.setText(list.get(position).getReleasepushPhone());
            releasePushViewHolder.releasepush_item_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean ischeck) {

                    if (ischeck) {
                        list.get(position).setSelected(true);
                    } else {
                        list.get(position).setSelected(false);
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
