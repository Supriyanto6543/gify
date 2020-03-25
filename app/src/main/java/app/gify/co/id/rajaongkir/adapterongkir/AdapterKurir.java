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
import app.gify.co.id.rajaongkir.modelongkir.kurir.ItemExpedisi;

/**
 * Created by SUPRIYANTO on 03/25/2020.
 */

public class AdapterKurir extends BaseAdapter {

    private Activity activity;
    private List<ItemExpedisi> itemExpedisis;
    private ArrayList<ItemExpedisi> itemExpedisiArrayList;
    private LayoutInflater ly;

    public AdapterKurir(Activity activity, List<ItemExpedisi> itemExpedisis) {
        this.activity = activity;
        this.itemExpedisis = itemExpedisis;

        itemExpedisiArrayList = new ArrayList<>();
        itemExpedisiArrayList.addAll(itemExpedisis);
    }

    @Override
    public int getCount() {
        return itemExpedisis.size();
    }

    @Override
    public Object getItem(int i) {
        return itemExpedisis.get(i);
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

        ItemExpedisi itemExpedisi = itemExpedisis.get(i);
        tv_category.setText(itemExpedisi.getName());
        tv_details.setText(itemExpedisi.getId());

        return view;
    }

    public void filter(String string){
        string = string.toLowerCase();
        itemExpedisis.clear();
        if (string.length() == 0){
            itemExpedisis.addAll(itemExpedisiArrayList);
        }else{
            for (ItemExpedisi k : itemExpedisiArrayList){
                if (k.getName().toLowerCase().contains(string)){
                    itemExpedisis.add(k);
                }else{

                }
            }
        }
        notifyDataSetChanged();
    }

    public void setList(List<ItemExpedisi> param){
        this.itemExpedisiArrayList.addAll(param);
    }
}
