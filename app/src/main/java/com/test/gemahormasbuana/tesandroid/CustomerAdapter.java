package com.test.gemahormasbuana.tesandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomerAdapter extends BaseAdapter {
    Context context;
    String kodeBank[];
    String namaBank[];
    LayoutInflater inflater;
    public CustomerAdapter(Context applicationContext, String[] kodeBank, String[] namaBank){
        this.context = context;
        this.kodeBank = kodeBank;
        this.namaBank = namaBank;
        inflater = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return kodeBank.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i){
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.list_row,null);
        TextView nama = (TextView) view.findViewById(R.id.nama);
        TextView kode = (TextView) view.findViewById(R.id.kode);
        nama.setText(namaBank[i]);
        kode.setText(kodeBank[i]);
        return view;
    }



}
