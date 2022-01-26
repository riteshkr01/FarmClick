package com.vthree.rentbaseapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.vthree.rentbaseapplication.Activity.MapActivity;

import com.vthree.rentbaseapplication.ModelClass.EquipmentModel;
import com.vthree.rentbaseapplication.ModelClass.MyListData;
import com.vthree.rentbaseapplication.ModelClass.UserModel;
import com.vthree.rentbaseapplication.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
    List<UserModel> userModels=new ArrayList<UserModel>();
    List<EquipmentModel> equipmentModels=new ArrayList<EquipmentModel>();

    private MyListData[] listdata;
public Context context;
    // RecyclerView recyclerView;


    public MyListAdapter(List<EquipmentModel> equipmentModels, Context context) {
        this.equipmentModels = equipmentModels;
        this.context = context;
    }

    public MyListAdapter(MyListData[] listdata) {
        this.listdata = listdata;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       try {
           holder.textView.setText(equipmentModels.get(position).getEquipment_name());
           //  holder.imageView.setImageResource(listdata[position].getImgId());
       }catch (Exception e){

       }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MapActivity.class);
                view.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return equipmentModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public Context context;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.txt_addresss);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
