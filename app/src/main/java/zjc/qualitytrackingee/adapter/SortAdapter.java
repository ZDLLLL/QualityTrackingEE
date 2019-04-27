package zjc.qualitytrackingee.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import zjc.qualitytrackingee.MyApplication;
import zjc.qualitytrackingee.R;
import zjc.qualitytrackingee.activity.CompanyActivity;
import zjc.qualitytrackingee.activity.EnrollActivity;
import zjc.qualitytrackingee.beans.CompanyBean;

public class SortAdapter extends BaseAdapter{

    private List<CompanyBean> list = null;
    private Context mContext;
    private CompanyActivity companyActivity;

    public SortAdapter(Context mContext, List<CompanyBean> list, CompanyActivity companyActivity) {
        this.mContext = mContext;
        this.list = list;
        this.companyActivity=companyActivity;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        final ViewHolder viewHolder;
        final CompanyBean companyBean = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.company_item, null);
            viewHolder.name =  view.findViewById(R.id.company_item_name);
            viewHolder.catalog =  view.findViewById(R.id.company_item_catalog);
//            viewHolder.img=view.findViewById(R.id.img);
            view.setTag(viewHolder);

            viewHolder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String cname=viewHolder.name.getText().toString();
                    Intent intent=new Intent(companyActivity,EnrollActivity.class);
                    intent.putExtra("name",cname);
                    intent.putExtra("page",1);
                    intent.putExtra("e_name",companyActivity.e_name);
                    intent.putExtra("e_password",companyActivity.e_password);
                    intent.putExtra("e_passwordagain",companyActivity.e_passwordagain);
                    intent.putExtra("e_phone",companyActivity.e_phone);
                    intent.putExtra("e_yzm",companyActivity.e_yzm);
                    MyApplication.destoryActivity("EnrollActivity");
                    companyActivity.startActivity(intent);
                    companyActivity.finish();
                }
            });
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //根据position获取首字母作为目录catalog
        String catalog = list.get(position).getFirstLetter();

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(catalog)){
            viewHolder.catalog.setVisibility(View.VISIBLE);
            viewHolder.catalog.setText(companyBean.getFirstLetter().toUpperCase());
        }else{
            viewHolder.catalog.setVisibility(View.GONE);
        }

        viewHolder.name.setText(this.list.get(position).getC_name());
//        Glide.with(MyApplication.getContext()).load(infoBean.getTea_pic())
//                .into(event_tea_pic_iv);
//        viewHolder.img.setImageDrawable(this.list.get(position).getC_url());

        return view;

    }

    final static class ViewHolder {
        TextView catalog;
        TextView name;
        ImageView img;
    }

    /**
     * 获取catalog首次出现位置
     */
    public int getPositionForSection(String catalog) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getFirstLetter();
            if (catalog.equalsIgnoreCase(sortStr)) {
                return i;
            }
        }
        return -1;
    }

}