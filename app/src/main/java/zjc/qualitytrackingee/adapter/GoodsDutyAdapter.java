package zjc.qualitytrackingee.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import zjc.qualitytrackingee.EEactivity.EEAnalysisActivity;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.activity.GoodsDutyActivity;

public class GoodsDutyAdapter extends RecyclerView.Adapter{
    private GoodsDutyActivity goodsDutyActivity;
    public class GoodsDutyViewHolder extends  RecyclerView.ViewHolder{
        private ImageView goodsduty_item_processpeople_iv ;
        private ImageView goodsduty_item_inspectorimage_iv;
//        private LinearLayout staff_item_ll;
        private TextView goodsduty_item_gxname_tv;
        private TextView goodsduty_item_goodsid_tv;
        private TextView goodsduty_item_goodsname_tv;
        private TextView goodsduty_item_processpeople_name_tv;
        private TextView goodsduty_item_inspectorname_tv;
        private TextView goodsduty_item_processpeople_time_tv;
        private TextView goodsduty_item_inspectortime_tv;
        //参数itemView就是country_item.xml中的大窗口View对象
        public GoodsDutyViewHolder(View itemView) {
            super(itemView);
            goodsduty_item_processpeople_iv=itemView.findViewById(R.id.goodsduty_item_processpeople_iv);
            goodsduty_item_inspectorimage_iv=itemView.findViewById(R.id.goodsduty_item_inspectorimage_iv);
            goodsduty_item_gxname_tv=itemView.findViewById(R.id.goodsduty_item_gxname_tv);
            goodsduty_item_goodsid_tv=itemView.findViewById(R.id.goodsduty_item_goodsid_tv);
            goodsduty_item_goodsname_tv=itemView.findViewById(R.id.goodsduty_item_goodsname_tv);
            goodsduty_item_processpeople_name_tv=itemView.findViewById(R.id.goodsduty_item_processpeople_name_tv);
            goodsduty_item_inspectorname_tv=itemView.findViewById(R.id.goodsduty_item_inspectorname_tv);
            goodsduty_item_processpeople_time_tv=itemView.findViewById(R.id.goodsduty_item_processpeople_time_tv);
            goodsduty_item_inspectortime_tv=itemView.findViewById(R.id.goodsduty_item_inspectortime_tv);
        }



    }
    private Context context;
    private List<String> processpeopletimeList;
    private List<String> processpeoplenameList;
    private List<String> processpeopleimageList;
    private List<String> inspectortimeList;
    private List<String> inspectornameList;
    private List<String> inspectorimageList;
    private  List<String> goodsidList;
    private  List<String> goodsnameList;
    private List<String> gxnameList;




    public GoodsDutyAdapter(Context context, List<String> processpeopletimeList, List<String> processpeoplenameList
            , List<String> processpeopleimageList, List<String> inspectortimeList, List<String> inspectornameList
    , List<String> inspectorimageList, List<String> goodsidList, List<String> goodsnameList, List<String> gxnameList, GoodsDutyActivity goodsDutyActivity) {
        this.goodsDutyActivity=goodsDutyActivity;
        this.context = context;
        this.processpeopletimeList = processpeopletimeList;
        this.processpeoplenameList = processpeoplenameList;
        this.processpeopleimageList = processpeopleimageList;
        this.inspectorimageList = inspectorimageList;
        this.inspectornameList = inspectornameList;
        this.goodsidList = goodsidList;
        this.goodsnameList = goodsnameList;
        this.inspectortimeList = inspectortimeList;
        this.gxnameList = gxnameList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.goodsduty_item,viewGroup,false);
        return new GoodsDutyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        GoodsDutyViewHolder goodsDutyViewHolder=(GoodsDutyViewHolder)holder;
        Glide.with(MyApplication.getContext())
                .load(inspectorimageList.get(position))
                .asBitmap()
                .error(R.drawable.head1)
                .into(goodsDutyViewHolder.goodsduty_item_inspectorimage_iv);
        Glide.with(MyApplication.getContext())
                .load(processpeopleimageList.get(position))
                .asBitmap()
                .error(R.drawable.head1)
                .into(goodsDutyViewHolder.goodsduty_item_processpeople_iv);
      //  final String phone=staffManagementActivity.PhoneList.get(position);
        goodsDutyViewHolder.goodsduty_item_gxname_tv.setText(gxnameList.get(position));
        goodsDutyViewHolder.goodsduty_item_goodsname_tv.setText(goodsnameList.get(position));
        goodsDutyViewHolder.goodsduty_item_processpeople_name_tv.setText(processpeoplenameList.get(position));
        goodsDutyViewHolder.goodsduty_item_inspectorname_tv.setText(inspectornameList.get(position));
        goodsDutyViewHolder.goodsduty_item_processpeople_time_tv.setText(processpeopletimeList.get(position));
        goodsDutyViewHolder.goodsduty_item_inspectortime_tv.setText(inspectortimeList.get(position));

        goodsDutyViewHolder.goodsduty_item_goodsid_tv.setText(goodsidList.get(position));
//        staffManageViewHolder.staff_item_ll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(staffManagementActivity,StaffManagementInfoActivity.class);
//                intent.putExtra("cimage",ImageList.get(position));
//                intent.putExtra("cname",NameList.get(position));
//                intent.putExtra("cjob",JobList.get(position));
//                intent.putExtra("c_phone",phone);
//                staffManagementActivity.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return goodsidList.size();
    }
}
