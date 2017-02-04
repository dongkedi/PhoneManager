package com.zhuoxin.phone.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuoxin.phone.R;
import com.zhuoxin.phone.base.MyBaseAdapter;
import com.zhuoxin.phone.entity.TelClassInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/11/4.
 */

public class TelClassListAdapter extends MyBaseAdapter<TelClassInfo> {
    public TelClassListAdapter(List<TelClassInfo> dataList, Context context) {
        super(dataList, context);
    }

    /*
        //1、需要一个List和LayoutInflater
        List<TelClassInfo> telClassInfosList = new ArrayList<TelClassInfo>();
        LayoutInflater inflater;

        public TelClassListAdapter(Context context, List<TelClassInfo> list) {
            inflater = LayoutInflater.from(context);
            telClassInfosList.addAll(list);
        }

        public void addAllDate(List<TelClassInfo> list) {
            telClassInfosList.addAll(list);
        }

        @Override
        public int getCount() {
            return telClassInfosList.size();
        }

        @Override
        public Object getItem(int position) {
            return telClassInfosList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            //1、填充布局到convertView中
            convertView = inflater.inflate(R.layout.item_classlist, null);
            //2、找到holder对应的控件
            holder = new ViewHolder();
            holder.tv_telclassname = (TextView) convertView.findViewById(R.id.tv_telclassname);
            //3、把holder保存到convertView中
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //获取电话信息，并且设置到textView中
        TelClassInfo info = (TelClassInfo) getItem(position);
        holder.tv_telclassname.setText(info.name);
        //把convertView返回
        return convertView;
    }

    public static class ViewHolder {
        TextView tv_telclassname;
    }
}
