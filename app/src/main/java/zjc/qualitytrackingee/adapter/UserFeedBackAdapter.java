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
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.activity.StaffManagementActivity;
import zjc.qualitytrackingee.activity.StaffManagementInfoActivity;
import zjc.qualitytrackingee.activity.UserFeedBackActivity;
import zjc.qualitytrackingee.activity.UserFeedBackInfoActivity;

public class UserFeedBackAdapter extends RecyclerView.Adapter{
    private UserFeedBackActivity userFeedBackActivity;
    public class UserFeedBackViewHolder extends  RecyclerView.ViewHolder{
        private ImageView user_feedback_item_iv;
        private LinearLayout user_feedback_item_ll;
        private TextView feedback_item_score_tv;
        private TextView feedback_item_username_tv;
        private TextView feedback_item_time_tv;
        private ImageView user_feedback_item_read_iv;
        //参数itemView就是country_item.xml中的大窗口View对象
        public UserFeedBackViewHolder(View itemView) {
            super(itemView);
            user_feedback_item_iv=itemView.findViewById(R.id.user_feedback_item_iv);
            user_feedback_item_ll=itemView.findViewById(R.id.user_feedback_item_ll);
            feedback_item_username_tv=itemView.findViewById(R.id.feedback_item_username_tv);
            feedback_item_time_tv=itemView.findViewById(R.id.feedback_item_time_tv);
            feedback_item_score_tv=itemView.findViewById(R.id.feedback_item_score_tv);
            user_feedback_item_read_iv=itemView.findViewById(R.id.user_feedback_item_read_iv);
        }



    }
    private Context context;
    private List<String> UserImageList;
    private List<String> UsernameList;
    private List<String> ScoreList;
    private List<String> TimeList;
    private List<String> MessageList;//反馈信息
    public List<String> ConameList;//商品名
    public List<String> ImageList1;//反馈图片
    public List<String> ImageList2;//反馈图片
    public List<String> ImageList3;//反馈图片
    private List<String> StatusList;
    private List<String> FidList;


    public UserFeedBackAdapter(Context context, List<String> UserImageList, List<String> UsernameList, List<String> ScoreList,List<String> TimeList,
                               List<String> MessageList,List<String> ConameList, List<String> ImageList1,
                               List<String> ImageList2,List<String> ImageList3,List<String> StatusList,List<String> FidList,UserFeedBackActivity userFeedBackActivity) {
        this.userFeedBackActivity=userFeedBackActivity;
        this.context = context;
        this.UserImageList = UserImageList;
        this.UsernameList = UsernameList;
        this.ScoreList = ScoreList;
        this.TimeList=TimeList;
        this.ImageList1=ImageList1;
        this.MessageList=MessageList;
        this.ImageList2=ImageList2;
        this.ImageList3=ImageList3;
        this.ConameList=ConameList;
        this.StatusList=StatusList;
        this.FidList=FidList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_feedback_item,viewGroup,false);
        return new UserFeedBackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final UserFeedBackViewHolder userFeedBackViewHolder=(UserFeedBackViewHolder)holder;
        if("0".equals(StatusList.get(position))){
            Glide.with(MyApplication.getContext())
                    .load(R.drawable.yidu)
                    .asBitmap()
                    .error(R.drawable.head1)
                    .into(userFeedBackViewHolder.user_feedback_item_read_iv);
        }else{
            Glide.with(MyApplication.getContext())
                    .load(R.drawable.dontread)
                    .asBitmap()
                    .error(R.drawable.head1)
                    .into(userFeedBackViewHolder.user_feedback_item_read_iv);
        }
        Glide.with(MyApplication.getContext())
                .load(UserImageList.get(position))
                .asBitmap()
                .error(R.drawable.head1)
                .into(userFeedBackViewHolder.user_feedback_item_iv);
        userFeedBackViewHolder.feedback_item_time_tv.setText(TimeList.get(position));
        userFeedBackViewHolder.feedback_item_score_tv.setText(ScoreList.get(position));
        userFeedBackViewHolder.feedback_item_username_tv.setText(UsernameList.get(position));
        userFeedBackViewHolder.user_feedback_item_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(userFeedBackActivity,UserFeedBackInfoActivity.class);
                intent.putExtra("time",TimeList.get(position));
                intent.putExtra("userImage",UserImageList.get(position));
                intent.putExtra("score",ScoreList.get(position));
                intent.putExtra("username",UsernameList.get(position));
                intent.putExtra("message",MessageList.get(position));
                intent.putExtra("image1",ImageList1.get(position));
                intent.putExtra("image2",ImageList2.get(position));
                intent.putExtra("image3",ImageList3.get(position));
                intent.putExtra("coname",ConameList.get(position));
                intent.putExtra("status",StatusList.get(position));
                intent.putExtra("f_id",FidList.get(position));
                userFeedBackActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return UsernameList.size();
    }
}
