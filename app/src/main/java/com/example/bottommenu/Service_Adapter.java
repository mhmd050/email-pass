package com.example.bottommenu;

import android.view.ViewGroup;

import android.content.Context;
import android.provider.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Service_Adapter extends RecyclerView.Adapter<Service_Adapter.ViewHolder>{
    private ArrayList<Service> services;
    ItemSelected activity;

    public interface ItemSelected {
        void onItemSelected(int index);

        void onItemClicked(int Index);
    }
    public Service_Adapter(Context context, ArrayList<Service> list){
        this.services=list;
        activity=(ItemSelected)context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            imageView=itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onItemSelected(services.indexOf(v.getTag()));
                }
            });
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(services.get(position));
        holder.tvName.setText(services.get(position).getName());
        holder.imageView.setImageResource(services.get(position).getStyle_img());
    }

    @Override
    public int getItemCount() {
        return services.size();
    }
}
