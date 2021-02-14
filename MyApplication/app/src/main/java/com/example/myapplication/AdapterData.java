package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.MyHolder> {
    Context context;
    List<ModelData> list;

    public AdapterData(Context context, List<ModelData> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.data,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final ModelData data=list.get(position);
        String Txt_date=list.get(position).getDate();
        String Txt_name=list.get(position).getAudio_name();
        String Txt_duration=list.get(position).getDuration();
//
        holder.Audio_name.setText(Txt_name);
        holder.Duration.setText("Duration:-" + Txt_duration);
        holder.Date.setText("Date:-" + Txt_date);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        TextView Date,Audio_name,Duration;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            Date=itemView.findViewById(R.id.date);
            Duration=itemView.findViewById(R.id.duration);
            Audio_name=itemView.findViewById(R.id.name);
        }
    }
}