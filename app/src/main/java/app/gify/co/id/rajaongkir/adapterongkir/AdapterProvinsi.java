package app.gify.co.id.rajaongkir.adapterongkir;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.gify.co.id.R;
import app.gify.co.id.rajaongkir.modelongkir.provinsi.ResultOngkir;

/**
 * Created by SUPRIYANTO on 03/25/2020.
 */

public class AdapterProvinsi extends BaseAdapter {

    private Activity activity;
    private List<ResultOngkir> lists;
    private ArrayList<ResultOngkir> ongkirArrayList;
    private LayoutInflater ly;

    public AdapterProvinsi(Activity activity, List<ResultOngkir> lists) {
        this.activity = activity;
        this.lists = lists;

        ongkirArrayList = new ArrayList<>();
        ongkirArrayList.addAll(lists);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (ly == null)
            ly = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = ly.inflate(R.layout.rajaongkir_adapter_city, null);
        TextView tv_category = (TextView) view.findViewById(R.id.tv_category);
        TextView tv_details = (TextView) view.findViewById(R.id.tv_detail);

        ResultOngkir rs = lists.get(i);

        tv_category.setText(rs.getProvince());
        tv_details.setText(rs.getProvinceId());

        return view;
    }

    public void filter(String str){
        str = str.toLowerCase();
        lists.clear();
        if (str.length() == 0){
            lists.addAll(ongkirArrayList);
        }else{
            for (ResultOngkir ro : ongkirArrayList){
                if (ro.getProvince().toLowerCase().contains(str)){
                    lists.add(ro);
                }else{

                }
            }
        }
        notifyDataSetChanged();
    }

    public void setList(List<ResultOngkir> resultOngkirs){
        this.ongkirArrayList.addAll(resultOngkirs);
    }
}
