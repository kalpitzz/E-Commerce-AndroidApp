package com.bit.amazonclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bit.amazonclone.Model.Prevalent.Prevalent;
import com.bit.amazonclone.Model.Products;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addToCartButton;
    private ImageView productImage;

    private ElegantNumberButton numberButton;
    private TextView productPrice,productDescription,productName;
    private String productID="";
    private String Display_price="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID=getIntent().getStringExtra("pid");


        addToCartButton=(Button)findViewById(R.id.pd_add_to_cart_button);
        numberButton=(ElegantNumberButton)findViewById(R.id.number_btn);
        productImage=(ImageView)findViewById(R.id.product_image_details);
        productName=(TextView)findViewById(R.id.product_name_details);
        productDescription=(TextView)findViewById(R.id.product_description_details);
        productPrice=(TextView)findViewById(R.id.product_price_details);



        getProductDetails(productID);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });

    }


    private void addingToCartList() {
        String saveCurrentTime,saveCurrentDate;


        Calendar calForDate =Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());                                  // For Current Date And Time
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime =currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef=FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String,Object> cartMap=new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",Display_price);
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");

        cartListRef.child("User View").child(Prevalent.CurrentOnlineUser.getPhone()).child("Products").child(productID)
                .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    cartListRef.child("Admin View").child(Prevalent.CurrentOnlineUser.getPhone()).child("Products").child(productID)
                            .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                             if(task.isSuccessful()){
                                 Toast.makeText(ProductDetailsActivity.this,"Added To Cart",Toast.LENGTH_SHORT).show();
                                 Intent intent=new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                 startActivity(intent);
                                 finish();
                             }
                        }
                    });
                }
            }
        });

    }


    private void getProductDetails(String productID) {

        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Products products=dataSnapshot.getValue(Products.class);
                        productName.setText(products.getPname());
                        Display_price=products.getPrice();
                        productPrice.setText("Rs "+Display_price);
                        productDescription.setText(products.getDescription());
                        Picasso.get().load(products.getImage()).into(productImage);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}