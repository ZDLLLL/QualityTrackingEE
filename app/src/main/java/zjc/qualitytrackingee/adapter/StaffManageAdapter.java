package zjc.qualitytrackingee.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.activity.StaffManagementActivity;
import zjc.qualitytrackingee.activity.StaffManagementInfoActivity;

public class StaffManageAdapter extends RecyclerView.Adapter{
    private StaffManagementActivity staffManagementActivity;
    public class StaffManageViewHolder extends  RecyclerView.ViewHolder{
        private ImageView staff_item_iv;
        private LinearLayout staff_item_ll;
        private TextView staff_name_tv;
        private TextView staff_job_tv;
        //参数itemView就是country_item.xml中的大窗口View对象
        public StaffManageViewHolder(View itemView) {
            super(itemView);
            staff_item_iv=itemView.findViewById(R.id.staff_item_iv);
            staff_item_ll=itemView.findViewById(R.id.staff_item_ll);
            staff_name_tv=itemView.findViewById(R.id.staff_name_tv);
            staff_job_tv=itemView.findViewById(R.id.staff_job_tv);
        }



    }
    private Context context;
    private List<String> ImageList;
    private List<String> NameList;
    private List<String> JobList;

    public StaffManageAdapter(Context context, List<String> imageList, List<String> nameList, List<String> jobList,StaffManagementActivity staffManagementActivity) {
        this.staffManagementActivity=staffManagementActivity;
        this.context = context;
        this.ImageList = imageList;
        this.NameList = nameList;
        this.JobList = jobList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.staff_item,viewGroup,false);
        return new StaffManageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        StaffManageViewHolder staffManageViewHolder=(StaffManageViewHolder)holder;
        Glide.with(MyApplication.getContext())
                .load(ImageList.get(position))
                .asBitmap()
                .error(R.drawable.head1)
                .into(staffManageViewHolder.staff_item_iv);
        final String phone=staffManagementActivity.PhoneList.get(position);
        staffManageViewHolder.staff_job_tv.setText(JobList.get(position));
        staffManageViewHolder.staff_name_tv.setText(NameList.get(position));
        staffManageViewHolder.staff_item_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(staffManagementActivity,StaffManagementInfoActivity.class);
                intent.putExtra("cimage",ImageList.get(position));
                intent.putExtra("cname",NameList.get(position));
                intent.putExtra("cjob",JobList.get(position));
                intent.putExtra("c_phone",phone);
                staffManagementActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ImageList.size();
    }
}
