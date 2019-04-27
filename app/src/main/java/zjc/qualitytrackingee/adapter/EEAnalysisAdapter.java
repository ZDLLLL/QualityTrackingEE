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

import org.w3c.dom.Text;

import java.util.List;

import zjc.qualitytrackingee.EEactivity.EEAnalysisActivity;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.activity.StaffManagementActivity;
import zjc.qualitytrackingee.activity.StaffManagementInfoActivity;

public class EEAnalysisAdapter extends RecyclerView.Adapter{
    private EEAnalysisActivity eeAnalysisActivity;
    public class EEAnalysisViewHolder extends  RecyclerView.ViewHolder{
        private ImageView analysis_item_processpeople_iv ;
        private ImageView analysis_item_inspectorimage_iv;
//        private LinearLayout staff_item_ll;
        private TextView analysis_item_gxname_tv;
        private TextView analysis_item_goodsid_tv;
        private TextView analysis_item_goodsname_tv;
        private TextView analysis_item_processpeople_name_tv;
        private TextView analysis_item_inspectorname_tv;
        private TextView analysis_item_processpeople_time_tv;
        private TextView analysis_item_inspectortime_tv;
        //参数itemView就是country_item.xml中的大窗口View对象
        public EEAnalysisViewHolder(View itemView) {
            super(itemView);
            analysis_item_processpeople_iv=itemView.findViewById(R.id.analysis_item_processpeople_iv);
            analysis_item_inspectorimage_iv=itemView.findViewById(R.id.analysis_item_inspectorimage_iv);
            analysis_item_gxname_tv=itemView.findViewById(R.id.analysis_item_gxname_tv);
            analysis_item_goodsid_tv=itemView.findViewById(R.id.analysis_item_goodsid_tv);
            analysis_item_goodsname_tv=itemView.findViewById(R.id.analysis_item_goodsname_tv);
            analysis_item_processpeople_name_tv=itemView.findViewById(R.id.analysis_item_processpeople_name_tv);
            analysis_item_inspectorname_tv=itemView.findViewById(R.id.analysis_item_inspectorname_tv);
            analysis_item_processpeople_time_tv=itemView.findViewById(R.id.analysis_item_processpeople_time_tv);
            analysis_item_inspectortime_tv=itemView.findViewById(R.id.analysis_item_inspectortime_tv);
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




    public EEAnalysisAdapter(Context context, List<String> processpeopletimeList, List<String> processpeoplenameList
            , List<String> processpeopleimageList,  List<String> inspectortimeList,List<String> inspectornameList
    ,List<String> inspectorimageList, List<String> goodsidList, List<String> goodsnameList,List<String> gxnameList,EEAnalysisActivity eeAnalysisActivity) {
        this.eeAnalysisActivity=eeAnalysisActivity;
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
        View view = LayoutInflater.from(context).inflate(R.layout.eeanalysis_item,viewGroup,false);
        return new EEAnalysisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        EEAnalysisViewHolder eeAnalysisViewHolder=(EEAnalysisViewHolder)holder;
        Glide.with(MyApplication.getContext())
                .load(inspectorimageList.get(position))
                .asBitmap()
                .error(R.drawable.head1)
                .into(eeAnalysisViewHolder.analysis_item_inspectorimage_iv);
        Glide.with(MyApplication.getContext())
                .load(processpeopleimageList.get(position))
                .asBitmap()
                .error(R.drawable.head1)
                .into(eeAnalysisViewHolder.analysis_item_processpeople_iv);
      //  final String phone=staffManagementActivity.PhoneList.get(position);
        eeAnalysisViewHolder.analysis_item_gxname_tv.setText(gxnameList.get(position));
        eeAnalysisViewHolder.analysis_item_goodsname_tv.setText(goodsnameList.get(position));
        eeAnalysisViewHolder.analysis_item_processpeople_name_tv.setText(processpeoplenameList.get(position));
        eeAnalysisViewHolder.analysis_item_inspectorname_tv.setText(inspectornameList.get(position));
        eeAnalysisViewHolder.analysis_item_processpeople_time_tv.setText(processpeopletimeList.get(position));
        eeAnalysisViewHolder.analysis_item_inspectortime_tv.setText(inspectortimeList.get(position));

        eeAnalysisViewHolder.analysis_item_goodsid_tv.setText(goodsidList.get(position));

    }

    @Override
    public int getItemCount() {
        return goodsidList.size();
    }
}
