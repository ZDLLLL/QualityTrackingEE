package zjc.qualitytrackingee.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
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
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.activity.CheckActivity;
import zjc.qualitytrackingee.internet.Net;
import zjc.qualitytrackingee.utils.RecyclerItemView;
import zjc.qualitytrackingee.utils.RecyclerUtils;
import zjc.qualitytrackingee.utils.VolleyInterface;
import zjc.qualitytrackingee.utils.VolleyRequest;

import static zjc.qualitytrackingee.MyApplication.getContext;

public class CheckRecyclerViewAdapter  extends RecyclerView.Adapter<CheckRecyclerViewAdapter.SimpleHolder>
        implements RecyclerItemView.onSlidingButtonListener{
    private Context context;
    public String e_phone;
    private List<String> dataImage;    //头像
    private List<String> dataName;     //姓名
    private List<String> dataPost;  //职务
    private List<String> dataPhone;     //电话

    private onSlidingViewClickListener onSvcl;

    private RecyclerItemView recyclers;

    public CheckRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public CheckRecyclerViewAdapter(Context context,
                               List<String> dataImage,
                               List<String> dataName,
                               List<String> dataPost,
                               List<String> dataPhone) {
        this.context = context;
        this.dataImage = dataImage;
        this.dataName = dataName;
        this.dataPost = dataPost;
        this.dataPhone = dataPhone;
    }

    @Override
    public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.check_item, parent, false);
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
                .into(holder.check_image_iv);
        holder.check_name_tv.setText(dataName.get(position));
        holder.check_post_tv.setText(dataPost.get(position));
        holder.check_phone_tv.setText(dataPhone.get(position));
        holder.check_user_ll.getLayoutParams().width = RecyclerUtils.getScreenWidth(context);

        holder.check_user_ll.setOnClickListener(new View.OnClickListener() {
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
        holder.check_agree_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //审核不通过 -1
               // Toast.makeText(context,"其他："+position,Toast.LENGTH_SHORT).show();
                e_phone=holder.check_phone_tv.getText().toString();
                holder.check_agree_tv.setText("已同意");
                AgreeSuccess();
            }
        });
        holder.check_disagree_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FailedCheck();
                e_phone=holder.check_phone_tv.getText().toString();
                Toast.makeText(context,"删除了："+position,Toast.LENGTH_SHORT).show();
                int subscript = holder.getLayoutPosition();
                onSvcl.onDeleteBtnCilck(view,subscript);
                FailedCheck();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataImage.size();
    }

    @Override
    public void onMenuIsOpen(View view) {
        recyclers = (RecyclerItemView) view;
    }

    @Override
    public void onDownOrMove(RecyclerItemView recycler) {
        if(menuIsOpen()){
            if(recyclers != recycler){
                closeMenu();
            }
        }
    }

    class SimpleHolder extends  RecyclerView.ViewHolder {

        public ImageView check_image_iv;
        public TextView check_name_tv;
        public TextView check_post_tv;
        public TextView check_phone_tv;
        public TextView check_agree_tv;
        public TextView check_disagree_tv;
        public LinearLayout check_user_ll;
        public SimpleHolder(View view) {
            super(view);

            check_image_iv = (ImageView) view.findViewById(R.id.check_image_iv);
            check_name_tv = (TextView) view.findViewById(R.id.check_name_tv);
            check_post_tv = (TextView) view.findViewById(R.id.check_post_tv);
            check_phone_tv = (TextView) view.findViewById(R.id.check_phone_tv);
            check_agree_tv = (TextView) view.findViewById(R.id.check_agree_tv);
            check_disagree_tv = (TextView) view.findViewById(R.id.check_disagree_tv);
            check_user_ll = (LinearLayout) view.findViewById(R.id.check_user_ll);

            ((RecyclerItemView)view).setSlidingButtonListener(CheckRecyclerViewAdapter.this);
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
    public void AgreeSuccess(){
        String e_status="0";

        String url= Net.AgreeCheckEmployee+"?e_phone="+e_phone+"&e_status="+e_status;
        VolleyRequest.RequestGet(getContext(),url, "AgreeCheck",
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
