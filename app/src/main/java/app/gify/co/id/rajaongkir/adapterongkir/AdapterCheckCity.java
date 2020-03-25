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
import app.gify.co.id.rajaongkir.modelongkir.kota.Result;


/**
 * Created by SUPRIYANTO on 03/25/2020.
 */

public class AdapterCheckCity extends BaseAdapter {

    private Activity activity;
    private List<Result> resultList;
    private ArrayList<Result> resultArrayList;
    private LayoutInflater ly;

    public AdapterCheckCity(Activity activity, List<Result> resultList){
        this.activity = activity;
        this.resultList = resultList;
        resultArrayList = new ArrayList<>();
        resultArrayList.addAll(resultList);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int i) {
        return resultList.get(i);
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

        TextView tv_cateogry = (TextView) view.findViewById(R.id.tv_category);
        TextView tv_details = (TextView) view.findViewById(R.id.tv_detail);

        Result result = resultList.get(i);
        tv_cateogry.setText(result.getCityName());
        tv_details.setText(result.getCityId());

        return view;
    }

    public void filter(String text){
        text = text.toLowerCase();
        resultList.clear();
        if (text.length() == 0){
            resultList.addAll(resultArrayList);
        }else{
            for (Result kota : resultArrayList){
                if (kota.getCityName().toLowerCase().contains(text)){
                    resultList.add(kota);
                }else{

                }
            }
        }
        notifyDataSetChanged();
    }

    public void setList(List<Result> list){
        this.resultArrayList.addAll(list);
    }
}
