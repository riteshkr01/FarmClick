package com.vthree.rentbaseapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vthree.rentbaseapplication.ModelClass.BookingModel;
import com.vthree.rentbaseapplication.R;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {
    List<BookingModel> bookingModelsl;
    Context context;

    public MyOrderAdapter(List<BookingModel> bookingModelsl, Context context) {
        this.bookingModelsl = bookingModelsl;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_my_order, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.row_my_order_name.setText("Equipment  : "+bookingModelsl.get(position).getEquipment_name());
        holder.row_booking_date.setText(bookingModelsl.get(position).getBook_date());
        holder.row_booking_time.setText(bookingModelsl.get(position).getBook_from_time() + "    To     " + bookingModelsl.get(position).getBook_to_time());
        holder.row_booking_status.setText(bookingModelsl.get(position).getStatus());
        if (bookingModelsl.get(position).getStatus().equals("Pending")){
            holder.row_booking_status.setTextColor(context.getResources().getColor(R.color.yellow));
        }else if (bookingModelsl.get(position).getStatus().equals("Approved")){
            holder.row_booking_status.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }else {
            holder.row_booking_status.setTextColor(context.getResources().getColor(R.color.reject));
        }

    }

    @Override
    public int getItemCount() {
        return bookingModelsl.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView row_my_order_name, row_booking_date, row_booking_time, row_booking_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            row_my_order_name = itemView.findViewById(R.id.row_my_order_name);
            row_booking_date = itemView.findViewById(R.id.row_booking_date);
            row_booking_time = itemView.findViewById(R.id.row_booking_time);
            row_booking_status = itemView.findViewById(R.id.row_booking_status);
        }
    }
}
