package zjc.qualitytrackingee.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.activity.GoodsDutyActivity;
import zjc.qualitytrackingee.activity.GoodsFeedBackActivity;
import zjc.qualitytrackingee.activity.ShowGoodsBatchActivity;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.AllGoodsBatchRecyclerItemView;
import zjc.qualitytrackingee.utils.AllGoodsRecyclerItemView;
import zjc.qualitytrackingee.utils.RecyclerUtils;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class ShowGoodsBatchRecyclerViewAdapter extends RecyclerView.Adapter<ShowGoodsBatchRecyclerViewAdapter.SimpleHolder>
        implements AllGoodsBatchRecyclerItemView.onSlidingButtonListener{
    private Context context;
    public String e_phone;
    private List<String> dataImage;    //头像
    private List<String> dataName;     //姓名
    private List<String> dataPrice;     //价格
    private List<String> dataDescribe;//商品描述
    private List<String> dataId;//商品描述
    private List<String> datastatus;
    private ShowGoodsBatchActivity showGoodsBatchActivity;
    private static String co_id;
    private static String co_batch;
    private onSlidingViewClickListener onSvcl;
    private AllGoodsBatchRecyclerItemView recyclers;

    public ShowGoodsBatchRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public ShowGoodsBatchRecyclerViewAdapter(Context context,
                                             List<String> dataImage,
                                             List<String> dataName,List<String> dataPrice,
                                             List<String>dataDescribe,List<String> dataId,List<String> datastatus,ShowGoodsBatchActivity showGoodsBatchActivity
                                       ) {
        this.context = context;
        this.dataImage = dataImage;
        this.dataName = dataName;
        this.dataDescribe=dataDescribe;
        this.dataPrice=dataPrice;
        this.dataId=dataId;
        this.datastatus=datastatus;
        this.showGoodsBatchActivity=showGoodsBatchActivity;
    }

    @Override
    public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_goodsbatch_item, parent, false);
        return new SimpleHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleHolder holder, final int position) {
        //holder.check_image_iv.setImageBitmap(dataImage.get(position));
        Glide.with(MyApplication.getContext())
                .load(dataImage.get(position))
                .asBitmap()
//                .bitmapTransform(new CropCircleTransformation((BitmapPool) this))
//                .crossFade(1000)
                .error(R.drawable.head1)
                .into(holder.goodsbatch_image_iv);
        holder.goodsbatch_name_tv.setText(dataName.get(position));
         holder.goodsbatch_describe_tv.setText(dataDescribe.get(position));
         holder.goodsbatch_id_tv.setText(dataId.get(position));
        holder.goodsbatch_price_tv.setText(dataPrice.get(position));
        holder.allgoodsbatch_ll.getLayoutParams().width = RecyclerUtils.getScreenWidth(context);
        holder.appraise_goodsbatch_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(datastatus.get(position).equals("0")) {
                    Intent intent = new Intent(showGoodsBatchActivity, GoodsFeedBackActivity.class);
                    intent.putExtra("co_id", dataId.get(position));
                    intent.putExtra("co_name", dataName.get(position));
                    showGoodsBatchActivity.startActivity(intent);
                }else{
                    Toast.makeText(showGoodsBatchActivity,"该商品还没有反馈信息",Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(context,"删除了："+position,Toast.LENGTH_SHORT).show();
//                int subscript = holder.getLayoutPosition();
//                onSvcl.onDeleteBtnCilck(view,subscript);

            }
        });
        holder.duty_goodsbatch_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String url= Net.GetGoodsDuty+"?co_id="+holder.goodsbatch_id_tv.getText().toString();
                VolleyRequest.RequestGet(getContext(),url, "selectHeadPerson",
                        new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {
                            @Override
                            public void onMySuccess(String result) {

                                Log.d("zjc",url);
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String  code=jsonObject.getString("selectHeadPerson");
                                    if("no".equals(code)){
                                        Toast.makeText(showGoodsBatchActivity,"该商品没有加工",Toast.LENGTH_LONG).show();
                                    }else {
                                        Intent intent=new Intent(showGoodsBatchActivity,GoodsDutyActivity.class);
                                        intent.putExtra("co_id",dataId.get(position));
                                        showGoodsBatchActivity.startActivity(intent);
                                    }

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


        });
    }

    @Override
    public int getItemCount() {
        return dataImage.size();
    }

    @Override
    public void onMenuIsOpen(View view) {
        recyclers = (AllGoodsBatchRecyclerItemView) view;
    }

    @Override
    public void onDownOrMove(AllGoodsBatchRecyclerItemView recycler) {
        if(menuIsOpen()){
            if(recyclers != recycler){
                closeMenu();
            }
        }
    }



    class SimpleHolder extends  RecyclerView.ViewHolder {

        public ImageView goodsbatch_image_iv;
        public TextView goodsbatch_name_tv;
        public TextView goodsbatch_price_tv;
        public TextView goodsbatch_describe_tv;
        public TextView appraise_goodsbatch_tv;//
        public TextView duty_goodsbatch_tv;
        public LinearLayout allgoodsbatch_ll;
        public TextView goodsbatch_id_tv;
        public SimpleHolder(View view) {
            super(view);

            goodsbatch_image_iv = (ImageView) view.findViewById(R.id.goodsbatch_image_iv);
            goodsbatch_name_tv = (TextView) view.findViewById(R.id.goodsbatch_name_tv);
            goodsbatch_price_tv = (TextView) view.findViewById(R.id.goodsbatch_price_tv);
            goodsbatch_describe_tv = (TextView) view.findViewById(R.id.goodsbatch_describe_tv);
            goodsbatch_id_tv = (TextView) view.findViewById(R.id.goodsbatch_id_tv);
            appraise_goodsbatch_tv = (TextView) view.findViewById(R.id.appraise_goodsbatch_tv);
            allgoodsbatch_ll = (LinearLayout) view.findViewById(R.id.allgoodsbatch_ll);
            duty_goodsbatch_tv=view.findViewById(R.id.duty_goodsbatch_tv);
            ((AllGoodsBatchRecyclerItemView)view).setSlidingButtonListener(ShowGoodsBatchRecyclerViewAdapter.this);
        }
    }

    //删除数据
    public void removeData(int position){
        dataImage.remove(position);
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


}
