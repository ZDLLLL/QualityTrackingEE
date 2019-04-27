package zjc.qualitytrackingee.adapter;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;

import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.activity.ShowGoodsBatchActivity;
import zjc.qualitytrackingee.activity.ShowGoodsClassActivity;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.AllGoodsRecyclerItemView;
import zjc.qualitytrackingee.utils.RecyclerUtils;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class AllGoodsRecyclerViewAdapter extends RecyclerView.Adapter<AllGoodsRecyclerViewAdapter.SimpleHolder>
        implements AllGoodsRecyclerItemView.onSlidingButtonListener{
    private Context context;
    public String e_phone;
    private List<String> dataclassid;    //商品分类编号
    private List<String> dataName;     //姓名
    private List<String> dataid;
    private ShowGoodsClassActivity showGoodsClassActivity;
//    private List<String> dataPrice;     //商品名
//    private List<String> dataDescribe;//商品描述


    private onSlidingViewClickListener onSvcl;

    private AllGoodsRecyclerItemView recyclers;

    public AllGoodsRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public AllGoodsRecyclerViewAdapter(Context context,
                                       List<String> dataclassid,
                                       List<String>dataid,
                                       List<String> dataName,ShowGoodsClassActivity showGoodsClassActivity
                                       ) {
        this.context = context;
        this.dataid=dataid;
        this.dataclassid = dataclassid;
        this.dataName = dataName;
        this.showGoodsClassActivity=showGoodsClassActivity;

    }

    @Override
    public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_goodsclass_item, parent, false);
        return new SimpleHolder((AllGoodsRecyclerItemView)view);
    }

    @Override
    public void onBindViewHolder(final SimpleHolder holder, final int position) {
        //holder.check_image_iv.setImageBitmap(dataImage.get(position));
//        Glide.with(MyApplication.getContext())
//                .load(dataImage.get(position))
//                .asBitmap()
//                .bitmapTransform(new CropCircleTransformation((BitmapPool) this))
//                .crossFade(1000)
//                .error(R.drawable.head1)
//                .into(holder.goods_image_iv);
        holder.goodsclass_id_tv.setText(dataclassid.get(position));
        holder.goods_name_tv.setText(dataName.get(position));
//        holder.goods_describe_tv.setText(dataDescribe.get(position));
//        holder.goods_price_tv.setText(dataPrice.get(position));
        holder.allgoods_ll.getLayoutParams().width = RecyclerUtils.getScreenWidth(context);

        holder.allgoods_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Toast.makeText(context,"做出操作，进入新的界面或弹框",Toast.LENGTH_SHORT).show();
                //判断是否有删除菜单打开
                Intent intent=new Intent(showGoodsClassActivity, ShowGoodsBatchActivity.class);
                intent.putExtra("coc_id",holder.goodsclass_id_tv.getText().toString());
                intent.putExtra("co_id",dataid.get(position));
                intent.putExtra("gname",holder.goods_name_tv.getText().toString());
                showGoodsClassActivity.startActivity(intent);
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                } else {
                    //获得布局下标（点的哪一个）
                    int subscript = holder.getLayoutPosition();
                    onSvcl.onItemClick(view, subscript);
                }
            }
        });

        holder.delete_goods_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FailedCheck();
                //e_phone=holder.goods_price_tv.getText().toString();
                Toast.makeText(context,"删除了："+holder.goods_name_tv.getText().toString(),Toast.LENGTH_SHORT).show();
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
        recyclers = (AllGoodsRecyclerItemView) view;
    }

    @Override
    public void onDownOrMove(AllGoodsRecyclerItemView recycler) {
        if(menuIsOpen()){
            if(recyclers != recycler){
                closeMenu();
            }
        }
    }



    class SimpleHolder extends  RecyclerView.ViewHolder {

        public ImageView goods_image_iv;
        public TextView goods_name_tv;
        public TextView goodsclass_id_tv;
        public TextView goods_number_tv;
        public TextView delete_goods_tv;
        public LinearLayout allgoods_ll;
        public SimpleHolder(View view) {
            super(view);

            //goods_image_iv = (ImageView) view.findViewById(R.id.goods_image_iv);
            goods_name_tv = (TextView) view.findViewById(R.id.goods_name_tv);
            goodsclass_id_tv = (TextView) view.findViewById(R.id.goodsclass_id_tv);
//            goods_price_tv = (TextView) view.findViewById(R.id.goods_price_tv);
          //  check_agree_tv = (TextView) view.findViewById(R.id.check_agree_tv);
            delete_goods_tv = (TextView) view.findViewById(R.id.delete_goods_tv);
            allgoods_ll = (LinearLayout) view.findViewById(R.id.allgoods_ll);

            ((AllGoodsRecyclerItemView)view).setSlidingButtonListener(AllGoodsRecyclerViewAdapter.this);
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
        String e_status="-1";

        String url= Net.DisAgreeCheckEmployee+"?e_phone="+e_phone+"&e_status="+e_status;
        VolleyRequest.RequestGet(getContext(),url, "DisAgreeCheck",
                new VolleyInterface(getContext(), VolleyInterface.mListener,VolleyInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(String result) {
                        Toast.makeText(getContext(),"操作成功",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onMyError(VolleyError error) {

                    }
                });
    }
}
