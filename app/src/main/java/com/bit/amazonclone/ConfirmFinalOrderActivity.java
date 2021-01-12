package com.bit.amazonclone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bit.amazonclone.Model.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEditText,phoneEdittext,addressEditText,cityEditText;
    private Button confirmOrderBtn;
    private String TotalAmount="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        TotalAmount=getIntent().getStringExtra("Total Price");
        confirmOrderBtn=(Button)findViewById(R.id.confirm_final_order_btn);
        nameEditText=(EditText)findViewById(R.id.shipment_name);
        phoneEdittext=(EditText)findViewById(R.id.shipment_phone);
        addressEditText=(EditText)findViewById(R.id.shipment_address);
        cityEditText=(EditText)findViewById(R.id.shipment_city);


        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

    }

    private void check() {
        if(TextUtils.isEmpty(nameEditText.getText().toString()))
            Toast.makeText(this,"Enter Name",Toast.LENGTH_SHORT).show();
        else if(TextUtils.isEmpty(addressEditText.getText().toString()))
            Toast.makeText(this,"Enter Address",Toast.LENGTH_SHORT).show();
        else if(TextUtils.isEmpty(phoneEdittext.getText().toString()))
            Toast.makeText(this,"Enter Phone Number",Toast.LENGTH_SHORT).show();
        else if(TextUtils.isEmpty(cityEditText.getText().toString()))
            Toast.makeText(this,"Enter City",Toast.LENGTH_SHORT).show();
        else
            confirmOrder();
    }

    private void confirmOrder() {
         final String saveCurrentTime,saveCurrentDate;

        Calendar calForDate =Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());                                  // For Current Date And Time
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime =currentTime.format(calForDate.getTime());

        final DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.CurrentOnlineUser.getPhone());

        HashMap<String, Object>OrderMap=new HashMap<>();
        OrderMap.put("TotalAmount",TotalAmount);
        OrderMap.put("name",nameEditText.getText().toString());
        OrderMap.put("phone",phoneEdittext.getText().toString());
        OrderMap.put("address",addressEditText.getText().toString());
        OrderMap.put("city",cityEditText.getText().toString());
        OrderMap.put("date",saveCurrentDate);
        OrderMap.put("time",saveCurrentTime);
        OrderMap.put("status","Not Shipped");


        orderRef.updateChildren(OrderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(Prevalent.CurrentOnlineUser.getPhone()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ConfirmFinalOrderActivity.this,"Order Placed Successfully",Toast.LENGTH_SHORT).show();
                            }
                            Intent intent=new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });

    }
}