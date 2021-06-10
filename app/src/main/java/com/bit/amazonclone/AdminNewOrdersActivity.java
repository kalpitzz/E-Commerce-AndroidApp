package com.bit.amazonclone;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bit.amazonclone.Model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference orderRef,AdminOrderRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        orderRef= FirebaseDatabase.getInstance().getReference().child("Orders");
        ordersList=findViewById(R.id.order_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrders> options=new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(orderRef,AdminOrders.class).build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter=new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull AdminOrders model) {
                holder.userName.setText("Name: "+model.getName());
                holder.userphoneNumber.setText("Phone: "+model.getPhone());
                holder.userTotalprice.setText("Total Amount: "+model.getTotalAmount());
                holder.userDateTime.setText("Order On: "+model.getDate()+" "+model.getTime());
                holder.userShippingAddress.setText("ShippingAddress: "+model.getAddress()+","+model.getCity());

                holder.ShoworderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        String uID=getRef(position).getKey();
                        Intent intent=new Intent(AdminNewOrdersActivity.this,AdminUserProductsActivity.class);
                        intent.putExtra("uid",uID);
                        startActivity(intent);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] =new CharSequence[]{
                                "YES",
                                "NO"
                        };
                        AlertDialog.Builder builder=new AlertDialog.Builder(AdminNewOrdersActivity.this);
                        builder.setTitle("Order Shipped ?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if(i==0){
                                    String uID=getRef(position).getKey();
                                    RemoveOrder(uID);
                                }else{
                                    finish();
                                }
                            }
                        });

                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                return new AdminOrdersViewHolder(view);
            }
        };


        ordersList.setAdapter(adapter);
        adapter.startListening();
    }



    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder {
        public TextView userName,userphoneNumber,userTotalprice,userDateTime,userShippingAddress;
        public Button ShoworderBtn;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName=itemView.findViewById(R.id.order_user_name);
            userphoneNumber=itemView.findViewById(R.id.order_phone_number);
            userTotalprice=itemView.findViewById(R.id.order_total_price);
            userDateTime=itemView.findViewById(R.id.order_date_time);
            userShippingAddress=itemView.findViewById(R.id.order_address_city);
            ShoworderBtn=itemView.findViewById(R.id.show_all_products_btn);

        }
    }
    private void RemoveOrder(String uID) {
        AdminOrderRef=FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(uID);
        AdminOrderRef.removeValue();
        orderRef.child(uID).removeValue();
    }
}
