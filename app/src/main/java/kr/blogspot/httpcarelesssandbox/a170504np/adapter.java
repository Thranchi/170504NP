package kr.blogspot.httpcarelesssandbox.a170504np;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 윤현하 on 2017-05-10.
 */

public class adapter extends BaseAdapter{
    ArrayList<data> datachest=new ArrayList<data>();
    boolean gotcha;

    public adapter(ArrayList<data> datachest) {
        this.datachest = datachest;
    }

    @Override
    public int getCount() {
        return datachest.size();
    }

    @Override
    public Object getItem(int position) {
        return datachest.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int key = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.textitem, parent, false);
        }

        TextView nametext = (TextView) convertView.findViewById(R.id.nametext);
        data tempdatachest = datachest.get(position);

        nametext.setText("<" + tempdatachest.getName()+">"+" " + tempdatachest.getUrl());

        return convertView;
    }

    public void deleteitem(int position) {
        datachest.remove(position);
        this.notifyDataSetChanged();
    }

    public void addItem(String name, String url) {
        data item = new data(name, url);
        datachest.add(item);
        this.notifyDataSetChanged();

    }

    public boolean findItem(String url) {
        for(int i = 0; i< datachest.size(); i ++){
            if(datachest.get(i).getUrl().equals(url)){
                gotcha = true;
                return true;
            }
        }
        return false;
    }
}
