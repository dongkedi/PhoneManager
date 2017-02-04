package com.zhuoxin.phone.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuoxin.phone.R;
import com.zhuoxin.phone.base.MyBaseAdapter;
import com.zhuoxin.phone.entity.TelNumberInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/11/7.
 */

public class TelNumberAdapter extends MyBaseAdapter<TelNumberInfo> {
    public TelNumberAdapter(List<TelNumberInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_numberlist, null);
            holder = new ViewHolder();
            holder.tv_telnumbername = (TextView) convertView.findViewById(R.id.tv_telnumbername);
            holder.tv_telnumber = (TextView) convertView.findViewById(R.id.tv_telnumber);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TelNumberInfo info = (TelNumberInfo) getItem(position);
        holder.tv_telnumbername.setText(info.name);
        holder.tv_telnumber.setText(info.number);
        return convertView;
    }

    private static class ViewHolder {
        TextView tv_telnumbername;
        TextView tv_telnumber;
    }
}
