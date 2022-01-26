package com.vthree.rentbaseapplication.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vthree.rentbaseapplication.Activity.BookEquipementActivity;
import com.vthree.rentbaseapplication.Activity.LoginActivity;
import com.vthree.rentbaseapplication.Activity.MainActivity;
import com.vthree.rentbaseapplication.ModelClass.BookingModel;
import com.vthree.rentbaseapplication.ModelClass.UserModel;
import com.vthree.rentbaseapplication.R;
import com.vthree.rentbaseapplication.service.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookOrderAdapter extends RecyclerView.Adapter<BookOrderAdapter.ViewHolder> {
    List<BookingModel> bookingModelsl;
    Context context;

    public BookOrderAdapter(List<BookingModel> bookingModelsl, Context context) {
        this.bookingModelsl = bookingModelsl;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_book_order, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final BookingModel model=bookingModelsl.get(position);
        holder.row_book_order_name.setText("Equipment  : " + bookingModelsl.get(position).getEquipment_name());
        holder.row_booking_date.setText(bookingModelsl.get(position).getBook_date());
        holder.row_booking_time.setText(bookingModelsl.get(position).getBook_from_time() + "    To     " + bookingModelsl.get(position).getBook_to_time());


        if (holder.row_booking_status.getText().equals("Approved")){
            holder.pendding.setVisibility(View.GONE);
            holder.row_booking_status.setText("Approved");
            holder.row_show_details.setVisibility(View.VISIBLE);
            holder.row_booking_status.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else if (holder.row_booking_status.getText().equals("Disapproved")) {
            holder.pendding.setVisibility(View.GONE);
            holder.row_booking_status.setText("Disapproved");
            holder.row_booking_status.setTextColor(context.getResources().getColor(R.color.reject));
        }else {
            holder.row_booking_status.setText(bookingModelsl.get(position).getStatus());
            holder.row_booking_status.setTextColor(context.getResources().getColor(R.color.yellow));
        }

        if (bookingModelsl.get(position).getStatus().equals("Pending")) {
            holder.row_booking_status.setTextColor(context.getResources().getColor(R.color.yellow));
           // holder.pendding.setVisibility(View.VISIBLE);
        } else if (bookingModelsl.get(position).getStatus().equals("Approved")) {
            holder.row_booking_status.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.row_show_details.setVisibility(View.VISIBLE);
            holder.pendding.setVisibility(View.GONE);
            holder.row_booking_status.setText("Approved");
        } else {
            holder.row_booking_status.setTextColor(context.getResources().getColor(R.color.reject));
            holder.pendding.setVisibility(View.GONE);
            holder.row_booking_status.setText("Disapproved");
        }

        holder.row_show_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog builder=new AlertDialog.Builder(context).create();
                View view1= LayoutInflater.from(context).inflate(R.layout.dailog_show_detaols,null);
                TextView book_order_name=view1.findViewById(R.id.book_order_name);
                TextView row_booking_date=view1.findViewById(R.id.row_booking_date);
                TextView row_booking_time=view1.findViewById(R.id.row_booking_time);
                TextView booking_status=view1.findViewById(R.id.booking_status);
                TextView txt_contact=view1.findViewById(R.id.txt_contact);
                TextView txt_amount=view1.findViewById(R.id.txt_amount);
                TextView txt_deposit=view1.findViewById(R.id.txt_deposit);
                TextView txt_address=view1.findViewById(R.id.txt_address);
                book_order_name.setText(model.getEquipment_name());
                row_booking_date.setText(model.getBook_date());
                row_booking_time.setText(model.getBook_from_time() + "    To     " + model.getBook_to_time());
                booking_status.setText(model.getStatus());
                txt_contact.setText(model.getBook_contact());
                txt_amount.setText(model.getPriceinhr());
                txt_deposit.setText(model.getDeposit());
                txt_address.setText(model.getUser_address());
                Button dailog_ok=(Button)view1.findViewById(R.id.dailog_ok);
                dailog_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });

                builder.setView(view1);
                builder.setCanceledOnTouchOutside(true);
                builder.show();
            }
        });

        holder.row_btn_approve.setOnClickListener
                (new View.OnClickListener() {
                             @Override
                             public void onClick(View view) {

                                 holder.row_booking_status.setText("Approved");
                                 holder.row_booking_status.setTextColor(context.getResources().getColor(R.color.colorAccent));
                                 holder.pendding.setVisibility(View.GONE);
                                 DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                 Query key = ref.child("equipmentBooking").child("data").orderByChild("b_id").equalTo(model.getB_id());
                                 key.addListenerForSingleValueEvent(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                         for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                             try {
                                                 appleSnapshot.getRef().child("status").setValue("Approved");

                                                 notifyDataSetChanged();
                                                 DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                                 Query key = ref.child("RentBase").child("user").orderByChild("user_id").equalTo(model.getUser_id());
                                                 key.addListenerForSingleValueEvent(new ValueEventListener() {
                                                     @Override
                                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                         for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                                             UserModel model = appleSnapshot.getValue(UserModel.class);

                                                             Log.d("tokenss", "" + model.getToken());

                                                             ///notification code
                                                             JSONObject notification = new JSONObject();
                                                             JSONObject notifcationBody = new JSONObject();
                                                             try {
                                                                 notifcationBody.put("title", "Equipment Booking");
                                                                 notifcationBody.put("message", "Approved your equipment order " + model.getUser_name());
                                                                 notification.put("to", model.getToken());
                                                                 notification.put("data", notifcationBody);
                                                             } catch (JSONException e) {
                                                                 Log.e("error", "onCreate: " + e.getMessage());
                                                             }
                                                             sendNotification(notification);
                                                         }
                                                     }

                                                     @Override
                                                     public void onCancelled(@NonNull DatabaseError databaseError) {
                                                         Log.e("position", "onCancelled", databaseError.toException());
                                                     }
                                                 });

                                             } catch (Exception e) {
                                                 e.printStackTrace();
                                             }
                                             Log.d("position", "" + appleSnapshot.getKey());

                                         }
                                     }

                                     @Override
                                     public void onCancelled(@NonNull DatabaseError databaseError) {
                                         Log.e("position", "onCancelled", databaseError.toException());
                                     }
                                 });
                             }
                         }
                );

        holder.row_btn_Disapproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.row_booking_status.setText("Disapproved");
                holder.row_booking_status.setTextColor(context.getResources().getColor(R.color.reject));
                holder.pendding.setVisibility(View.GONE);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query key = ref.child("equipmentBooking").child("data").orderByChild("b_id").equalTo(model.getB_id());
                key.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            try {
                                appleSnapshot.getRef().child("status").setValue("Disapproved");

                                notifyDataSetChanged();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                Query key = ref.child("RentBase").child("user").orderByChild("user_id").equalTo(model.getUser_id());
                                key.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                            UserModel model = appleSnapshot.getValue(UserModel.class);

                                            Log.d("tokenss", "" + model.getToken());

                                            ///notification code
                                            JSONObject notification = new JSONObject();
                                            JSONObject notifcationBody = new JSONObject();
                                            try {
                                                notifcationBody.put("title", "Equipment Booking");
                                                notifcationBody.put("message", "Disapproved your equipment order " + model.getUser_name());
                                                notification.put("to", model.getToken());
                                                notification.put("data", notifcationBody);
                                            } catch (JSONException e) {
                                                Log.e("error", "onCreate: " + e.getMessage());
                                            }
                                            sendNotification(notification);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.e("position", "onCancelled", databaseError.toException());
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.d("position", "" + appleSnapshot.getKey());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("position", "onCancelled", databaseError.toException());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingModelsl.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView row_book_order_name, row_booking_date, row_booking_time, row_booking_status,row_show_details;
        Button row_btn_approve, row_btn_Disapproved;
        LinearLayout pendding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            row_book_order_name = itemView.findViewById(R.id.row_book_order_name);
            row_booking_date = itemView.findViewById(R.id.row_booking_date);
            row_booking_time = itemView.findViewById(R.id.row_booking_time);
            row_booking_status = itemView.findViewById(R.id.row_booking_status);
            row_show_details=itemView.findViewById(R.id.row_show_details);
            row_btn_approve = itemView.findViewById(R.id.row_btn_approve);
            row_btn_Disapproved = itemView.findViewById(R.id.row_btn_Disapproved);
            pendding = itemView.findViewById(R.id.pendding);
        }
    }
    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("responce", "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Request error", Toast.LENGTH_LONG).show();
                        Log.i("error", "onErrorResponse: Didn't work  " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", Constants.serverkey);
                params.put("Content-Type", Constants.contentType);
                return params;
            }
        };
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


}
