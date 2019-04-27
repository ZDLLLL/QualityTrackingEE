package zjc.qualitytrackingee.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.activity.AddGoodsClassActivity;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.AllGoodsClassRecyclerItemView;
import zjc.qualitytrackingee.utils.RecyclerUtils;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class AllGoodsClassRecyclerViewAdapter extends RecyclerView.Adapter<AllGoodsClassRecyclerViewAdapter.SimpleHolder>
        implements AllGoodsClassRecyclerItemView.onSlidingButtonListener{
    private Context context;
    public String e_phone;
    private String coc_id;
    private List<String> dataName;     //姓名
    private List<String> dataid;

    private onSlidingViewClickListener onSvcl;

    private AllGoodsClassRecyclerItemView recyclers;
    private AddGoodsClassActivity addGoodsClassActivity;
    public AllGoodsClassRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public AllGoodsClassRecyclerViewAdapter(Context context,
                                            List<String> dataName,List<String> dataid) {
        this.context = context;
        this.dataName = dataName;
        this.dataid=dataid;
    }

    @Override
    public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.allgoods_class_item, parent, false);
        return new SimpleHolder((AllGoodsClassRecyclerItemView) view);
    }

    @Override
    public void onBindViewHolder(final SimpleHolder holder, final int position) {
        //holder.check_image_iv.setImageBitmap(dataImage.get(position));

        holder.goodsclass_name_tv.setText(dataName.get(position));
        holder.goodsclass_id_tv.setText(dataid.get(position));

        holder.allgoodsclass_ll.getLayoutParams().width = RecyclerUtils.getScreenWidth(context);

        holder.allgoodsclass_ll.setOnClickListener(new View.OnClickListener() {
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

        holder.delete_goodsclass_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // FailedCheck();
               // e_phone=holder.goodsclass_name_tv.getText().toString();
                coc_id=addGoodsClassActivity.dataid.get(position);
                Toast.makeText(context,"删除了："+position,Toast.LENGTH_SHORT).show();
                int subscript = holder.getLayoutPosition();
                onSvcl.onDeleteBtnCilck(view,subscript);
                FailedCheck();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataName.size();
    }

    @Override
    public void onMenuIsOpen(View view) {
        recyclers = (AllGoodsClassRecyclerItemView) view;
    }

    @Override
    public void onDownOrMove(AllGoodsClassRecyclerItemView recycler) {
        if(menuIsOpen()){
            if(recyclers != recycler){
                closeMenu();
            }
        }
    }





    class SimpleHolder extends  RecyclerView.ViewHolder {

        public TextView goodsclass_name_tv;
        public TextView goodsclass_id_tv;
        public TextView delete_goodsclass_tv;
        public LinearLayout allgoodsclass_ll;
        public SimpleHolder(AllGoodsClassRecyclerItemView view) {
            super(view);
            goodsclass_name_tv = (TextView) view.findViewById(R.id.goodsclass_name_tv);
            delete_goodsclass_tv = (TextView) view.findViewById(R.id.delete_goodsclass_tv);
            allgoodsclass_ll = (LinearLayout) view.findViewById(R.id.allgoodsclass_ll);
            goodsclass_id_tv = (TextView) view.findViewById(R.id.goodsclass_id_tv);

            view.setSlidingButtonListener(AllGoodsClassRecyclerViewAdapter.this);
        }
    }

    //删除数据
    public void removeData(int position){
        dataName.remove(position);
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


        String url= Net.DeleteGoodsClass+"?coc_id="+coc_id;
        VolleyRequest.RequestGet(getContext(),url, "DisAgreeCheck",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            String code=jsonObject.getString("getAllCommodityClassByCid");
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
