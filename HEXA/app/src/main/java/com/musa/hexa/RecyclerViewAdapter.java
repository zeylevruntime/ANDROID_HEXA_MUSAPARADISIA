package com.musa.hexa;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Mylist> item;
    Context context ;

    public RecyclerViewAdapter(Context context, List<Mylist> item ) {
        this.item = item;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_row, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(item.get(position).getName());
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        holder.tv_salary.setText(formatRupiah.format(item.get(position).getSalary()) );
        Glide.with(context)
                .load("http://"+item.get(position).getImage())
                .centerCrop()
                .crossFade()
                .override(120,120)
                .into(holder.iv_img);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, tv_salary;
        ImageView iv_img;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_nama);
            tv_salary = itemView.findViewById(R.id.tv_salary);
            iv_img = itemView.findViewById(R.id.iv_img);
        }
    }
}