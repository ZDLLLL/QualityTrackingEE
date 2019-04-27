package zjc.qualitytrackingee.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import zjc.qualitytrackingee.R;

/**
 * Created by cold on 2017/4/20.
 */

public class MessageAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private  List<String> list;
    private Context context;
    public MessageAdapter(Context context,List<String> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,parent,false);
        final MyViewHolder holder=new MyViewHolder(view);
        return  holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder)holder).message_tv.setText(list.get(position));

}
    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView message_tv;
        public MyViewHolder(View itemView) {
            super(itemView);
            message_tv=(TextView) itemView.findViewById(R.id.message_tv);
        }
    }
}
