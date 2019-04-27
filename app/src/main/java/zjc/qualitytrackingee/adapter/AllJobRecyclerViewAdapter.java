package zjc.qualitytrackingee.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.activity.AddJobActivity;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.AllJobRecyclerItemView;
import zjc.qualitytrackingee.utils.RecyclerItemView;
import zjc.qualitytrackingee.utils.RecyclerUtils;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class AllJobRecyclerViewAdapter extends RecyclerView.Adapter<AllJobRecyclerViewAdapter.SimpleHolder>
        implements AllJobRecyclerItemView.onSlidingButtonListener{
    private Context context;
    public String e_phone;
    private AddJobActivity addJobActivity;
    private List<String> dataJob;     //姓名
    public String j_id;

    private onSlidingViewClickListener onSvcl;

    private AllJobRecyclerItemView recyclers;

    public AllJobRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public AllJobRecyclerViewAdapter(Context context,
                                     List<String> dataJob) {
        this.context = context;
        this.dataJob = dataJob;
    }

    @Override
    public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.alljob_item, parent, false);
        return new SimpleHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleHolder holder, final int position) {
        //holder.check_image_iv.setImageBitmap(dataImage.get(position));

        holder.job_name_tv.setText(dataJob.get(position));

        holder.job_ll.getLayoutParams().width = RecyclerUtils.getScreenWidth(context);

        holder.job_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Toast.makeText(context,"做出操作，进入新的界面或弹框",Toast.LENGTH_SHORT).show();
                //判断是否有删除菜单打开
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                } else {
                    //获得布局下标（点的哪一个）
                    int subscript = holder.getLayoutPosition();
                    onSvcl.onItemClick(view, subscript);
                }
            }
        });

        holder.delete_job_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FailedCheck();
              //  e_phone=holder.job_name_tv.getText().toString();
                j_id=addJobActivity.dataJobid.get(position);
                Toast.makeText(context,"删除了："+position,Toast.LENGTH_SHORT).show();
                int subscript = holder.getLayoutPosition();
                onSvcl.onDeleteBtnCilck(view,subscript);
                FailedCheck();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataJob.size();
    }

    @Override
    public void onMenuIsOpen(View view) {
        recyclers = (AllJobRecyclerItemView) view;
    }

    @Override
    public void onDownOrMove(AllJobRecyclerItemView recycler) {
        if(menuIsOpen()){
            if(recyclers != recycler){
                closeMenu();
            }
        }
    }

    class SimpleHolder extends  RecyclerView.ViewHolder {

        public TextView job_name_tv;

        public TextView delete_job_tv;
        public LinearLayout job_ll;
        public SimpleHolder(View view) {
            super(view);


            job_name_tv = (TextView) view.findViewById(R.id.job_name_tv);
            delete_job_tv = (TextView) view.findViewById(R.id.delete_job_tv);
            job_ll = (LinearLayout) view.findViewById(R.id.job_ll);

            ((AllJobRecyclerItemView)view).setSlidingButtonListener(AllJobRecyclerViewAdapter.this);
        }
    }

    //删除数据
    public void removeData(int position){
        dataJob.remove(position);
//        notifyDataSetChanged();
        notifyItemRemoved(position);

    }

    //关闭菜单
    public void closeMenu() {
        recyclers.closeMenu();
        recyclers = null;

    }

    // 判断是否有菜单打开
    public Boolean menuIsOpen() {
        if(recyclers != null){
            return true;
        }
        return false;
    }

    //设置在滑动侦听器上
    public void setOnSlidListener(onSlidingViewClickListener listener) {
        onSvcl = listener;
    }

    // 在滑动视图上单击侦听器
    public interface onSlidingViewClickListener {
        void onItemClick(View view, int position);
        void onDeleteBtnCilck(View view, int position);
    }

    public void FailedCheck(){


        String url= Net.DeleteJob+"?j_id="+j_id;
        VolleyRequest.RequestGet(getContext(),url, "deleteJob",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            String code=jsonObject.getString("deleteJob");
                            if(code.equals("yes")){
                                Toast.makeText(getContext(),"成功删除",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getContext(),"删除失败",Toast.LENGTH_LONG).show();
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
