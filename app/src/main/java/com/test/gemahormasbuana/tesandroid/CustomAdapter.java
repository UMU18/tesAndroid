package com.test.gemahormasbuana.tesandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class CustomAdapter extends BaseAdapter {
    Context context;
    String namaKereta[];
    String jamBerangkat[];
    String jamSampai[];
    String seatAvail[];
    String kelas[];
    LayoutInflater inflater;
    public CustomAdapter(Context applicationContext, String[] namaKereta, String[] jamBerangkat, String[] jamSampai, String[] seatAvail, String[] kelas){
        this.context = context;
        this.namaKereta = namaKereta;
        this.jamBerangkat = jamBerangkat;
        this.jamSampai=jamSampai;
        this.seatAvail=seatAvail;
        this.kelas=kelas;
        inflater = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return namaKereta.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.list_kereta,null);
        TextView nama_kereta = (TextView) view.findViewById(R.id.namaKereta);
        TextView jam_berangkat = (TextView) view.findViewById(R.id.jamBerangkat);
        TextView jam_sampai = (TextView) view.findViewById(R.id.jamSampai);
        TextView seat_avail = (TextView) view.findViewById(R.id.seatAvail);
        TextView kelasview = (TextView) view.findViewById(R.id.kelas);
        nama_kereta.setText(namaKereta[i]);
        jam_berangkat.setText(jamBerangkat[i]);
        jam_sampai.setText(jamSampai[i]);
        seat_avail.setText(seatAvail[i]);
        kelasview.setText(kelas[i]);
        return view;
    }
}
