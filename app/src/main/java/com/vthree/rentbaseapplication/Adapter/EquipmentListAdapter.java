package com.vthree.rentbaseapplication.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vthree.rentbaseapplication.Activity.BookEquipementActivity;
import com.vthree.rentbaseapplication.ModelClass.EquipmentModel;
import com.vthree.rentbaseapplication.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class EquipmentListAdapter extends RecyclerView.Adapter<EquipmentListAdapter.ViewHolder> implements Filterable {
    List<EquipmentModel> mFilterLIst = new ArrayList<EquipmentModel>();
    List<EquipmentModel> userModels = new ArrayList<EquipmentModel>();
    private EquipmentModel[] listdata;
    Context context;
    static OnArrayItemClick mOnArrayItemClick;
    byte[] imageAsBytes;

    // RecyclerView recyclerView;
    public EquipmentListAdapter(EquipmentModel[] listdata) {
        this.listdata = listdata;
    }

    public EquipmentListAdapter(List<EquipmentModel> userModels, Context context) {
        this.userModels = userModels;
        this.context = context;
        this.mFilterLIst = userModels;
        setHasStableIds(true);
    }

    public EquipmentListAdapter(List<EquipmentModel> userModels) {
        this.userModels = userModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final EquipmentModel equipmentModel = mFilterLIst.get(position);
        try {
            // final SellerDataModel myListData = listdata[position];
            holder.txt_name.setText(mFilterLIst.get(position).getEquipment_name());
            holder.txt_contact.setText(mFilterLIst.get(position).getContact());

            String base = "Base64 string values of some image";
            try {
               imageAsBytes = Base64.decode(mFilterLIst.get(position).getImage().getBytes(), Base64.DEFAULT);

                holder.image_equipment.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
            } catch (Exception e) {

            }
        } catch (Exception e) {

        }

       /* holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "click on item: " + myListData.getDescription(), Toast.LENGTH_LONG).show();
            }
        });*/
        holder.img_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeleteDialoge(position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookEquipementActivity.class);
                intent.putExtra("equipmentData", equipmentModel);
              //  intent.putExtra("imageAsBytes",imageAsBytes);
                context.startActivity(intent);
            }
        });
    }

    void confirmDeleteDialoge(final int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("Are you sure you want to delete?");
// Add the buttons
        builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mOnArrayItemClick.setOnArrayItemClickListener(position, mFilterLIst.get(position).getEquipment_id());            }
        });
// Set other dialog properties

// Create the AlertDialog
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return mFilterLIst.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {

                    mFilterLIst = userModels;

                } else {

                    List<EquipmentModel> filterdLIst = new ArrayList<>();
                    for (EquipmentModel phoneModel : mFilterLIst) {

                        if (phoneModel.getEquipment_name().toLowerCase().contains(charString)) {

                            filterdLIst.add(phoneModel);
                        }
                    }

                    mFilterLIst = filterdLIst;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterLIst;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mFilterLIst = ((List<EquipmentModel>) results.values);
                notifyDataSetChanged();
            }
        };

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // public ImageView imageView;
        public TextView txt_name, txt_contact;
        public RelativeLayout relativeLayout;
        ImageView img_admin, image_equipment;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_name = (TextView) itemView.findViewById(R.id.name);
            this.txt_contact = (TextView) itemView.findViewById(R.id.contact);
            this.img_admin = (ImageView) itemView.findViewById(R.id.img_admin);
            this.image_equipment = (ImageView) itemView.findViewById(R.id.image_equipment);
            // relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);


        }
    }

    public interface OnArrayItemClick {
        void setOnArrayItemClickListener(int position, String sellerID);

    }

    public void OnItemClickArrayElement(OnArrayItemClick onArrayItemClick) {

        mOnArrayItemClick = onArrayItemClick;
    }
}
