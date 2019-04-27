package zjc.qualitytrackingee.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.activity.EnrollActivity;
import zjc.qualitytrackingee.beans.SearchBean;

public class SearchCompanyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SearchBean.search> searchList;
    private EnrollActivity enrollActivity;
    public SearchCompanyAdapter(List<SearchBean.search> searchList, EnrollActivity enrollActivity) {
        this.searchList=searchList;
        this.enrollActivity=enrollActivity;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.searchcompany_item,parent,false);
        final MyViewHolder holder=new MyViewHolder(view);
        holder.searchcompany_item_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enrollActivity.er_company_et.setText(holder.searchcompany_tv.getText());

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((MyViewHolder)viewHolder).searchcompany_tv.setText(searchList.get(i).getC_name());
       // Log.d("name",searchList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView searchcompany_tv;
        LinearLayout searchcompany_item_ll;
        public MyViewHolder(View itemView) {
            super(itemView);
            searchcompany_tv= itemView.findViewById(R.id.searchcompany_tv);
            searchcompany_item_ll= itemView.findViewById(R.id.searchcompany_item_ll);
        }
    }
}
