package com.vthree.rentbaseapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vthree.rentbaseapplication.Adapter.Constants;
import com.vthree.rentbaseapplication.ModelClass.BookingModel;
import com.vthree.rentbaseapplication.ModelClass.EquipmentModel;
import com.vthree.rentbaseapplication.ModelClass.UserModel;
import com.vthree.rentbaseapplication.R;
import com.vthree.rentbaseapplication.preferences.PrefManager;
import com.vthree.rentbaseapplication.service.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookEquipementActivity extends AppCompatActivity {
    EquipmentModel equipmentModel;
    ImageView book_image;
    TextView book_equipment_name, book_equipment_price, book_equipment_dipost, book_equipment_address, book_equipment_contact, select_date,
            select_to_time, select_from_time, total_houre;

    Calendar myCalendar;
    byte[] imageAsBytes;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Button place_order;
    String date, to_time, from_time = "";
    String booking_id;
    DatabaseReference databaseReference;
    PrefManager prefManager;
    String user_id, address, taluka, city, mobile, user_name = null;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_equipement);
        myCalendar = Calendar.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        prefManager = new PrefManager(this);
        //  imageAsBytes=intent.getByteArrayExtra("imageAsBytes");
        equipmentModel = (EquipmentModel) intent.getSerializableExtra("equipmentData");
        getSupportActionBar().setTitle(equipmentModel.getEquipment_name());
        book_image = findViewById(R.id.book_image);
        book_equipment_name = findViewById(R.id.book_equipment_name);
        book_equipment_price = findViewById(R.id.book_equipment_price);
        book_equipment_dipost = findViewById(R.id.book_equipment_dipost);
        book_equipment_address = findViewById(R.id.book_equipment_address);
        book_equipment_contact = findViewById(R.id.book_equipment_contact);
        select_date = findViewById(R.id.select_date);
        select_to_time = findViewById(R.id.select_to_time);
        select_from_time = findViewById(R.id.select_from_time);
        total_houre = findViewById(R.id.total_houre);
        place_order = findViewById(R.id.place_order);

        user_id = prefManager.getString("user_id");
        address = prefManager.getString("address");
        taluka = prefManager.getString("taluka");
        city = prefManager.getString("city");
        mobile = prefManager.getString("mobile");
        user_name = prefManager.getString("user_name");
        databaseReference = FirebaseDatabase.getInstance().getReference("equipmentBooking").child("data");

        // byte[] imageAsBytes = Base64.decode(equipmentModel.getImage().getBytes(), Base64.DEFAULT);

        //  book_image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

        book_equipment_name.setText(equipmentModel.getEquipment_name());
        book_equipment_price.setText("Rs." + equipmentModel.getPriseinhr() + "/-");
        book_equipment_dipost.setText("Deposit Amount :Rs." + equipmentModel.getDeposite() + "/-");
        book_equipment_address.setText("Address :" + equipmentModel.getAddress() + ", " + equipmentModel.getCity() + ", " + equipmentModel.getTaluka());
        book_equipment_contact.setText("Contact :" + equipmentModel.getContact());
        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mYear = myCalendar.get(Calendar.YEAR);
                mMonth = myCalendar.get(Calendar.MONTH);
                mDay = myCalendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(BookEquipementActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                select_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        select_from_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(BookEquipementActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        select_from_time.setText(selectedHour + ":" + selectedMinute);
                        from_time = selectedHour + ":" + selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        select_to_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(BookEquipementActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        select_to_time.setText(selectedHour + ":" + selectedMinute);
                        to_time = selectedHour + ":" + selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (date.isEmpty()) {
                    Toast.makeText(BookEquipementActivity.this, "Please select date", Toast.LENGTH_SHORT).show();
                } else if (from_time.isEmpty()) {
                    Toast.makeText(BookEquipementActivity.this, "Please select from time", Toast.LENGTH_SHORT).show();
                } else if (to_time.isEmpty()) {
                    Toast.makeText(BookEquipementActivity.this, "Please select to time", Toast.LENGTH_SHORT).show();
                } else {
                    booking_id = databaseReference.push().getKey();
                    databaseReference.child(booking_id).setValue(new BookingModel(booking_id, equipmentModel.getEquipment_id(), equipmentModel.getEquipment_name(),
                            equipmentModel.getDescription(), equipmentModel.getPriseinhr(), equipmentModel.getDeposite(), equipmentModel.getUser_id(), date, from_time,
                            to_time, equipmentModel.getContact(), "5", "500", user_id, mobile, address, city, taluka,"Pending"))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    Query key = ref.child("RentBase").child("user").orderByChild("user_id").equalTo(equipmentModel.getUser_id());
                                    key.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                                UserModel model = appleSnapshot.getValue(UserModel.class);
                                                token = model.getToken();
                                                Log.d("tokenss", "" + model.getToken());

                                            ///notification code
                                                JSONObject notification = new JSONObject();
                                                JSONObject notifcationBody = new JSONObject();
                                                try {
                                                    notifcationBody.put("title", "Equipment Booking");
                                                    notifcationBody.put("message", "your equipment book by " + user_name);
                                                    notification.put("to", token);
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

                                    Intent intent = new Intent(BookEquipementActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(BookEquipementActivity.this, "Order Added Successful", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BookEquipementActivity.this, "Order Not Added Successful", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        Log.d("datass", equipmentModel.getEquipment_name());
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
                        Toast.makeText(BookEquipementActivity.this, "Request error", Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


}
