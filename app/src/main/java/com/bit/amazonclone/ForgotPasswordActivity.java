package com.bit.amazonclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class ForgotPasswordActivity extends AppCompatActivity {

    private ProgressDialog lodingBar;
    private EditText userName,userPhone;
    private Button sendBtn;
    private String username,userphone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        userName=(EditText)findViewById(R.id.verify_forgot_user_name);
        userPhone=(EditText)findViewById(R.id.verify_forgot_phone);
        sendBtn=(Button)findViewById(R.id.send_pass_btn);
        lodingBar=new ProgressDialog(this);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=userName.getText().toString();
                userphone=userPhone.getText().toString();
                if(username.equals(""))
                {
                    Toast.makeText(ForgotPasswordActivity.this,"Enter UserName",Toast.LENGTH_SHORT).show();
                }
                else if(userphone.equals(""))
                {
                    Toast.makeText(ForgotPasswordActivity.this,"Enter Registered Phone number",Toast.LENGTH_SHORT).show();
                }
                else {
                    lodingBar.setTitle("VALIDATING INFORMATION");
                    lodingBar.setMessage("Please Wait While We are Checking Credentials");
                    lodingBar.setCanceledOnTouchOutside(false);
                    lodingBar.show();
                    sendPassword();
                }
            }
        });


    }

    private void sendPassword() {
        DatabaseReference RootRef= FirebaseDatabase.getInstance().getReference().child("Users");

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot)
            {

                if( (dataSnapshot.child(userphone).exists()))
                {

                    if(dataSnapshot.child(userphone).child("name").getValue().equals(username))
                    {
                        final smsgateway sending=new smsgateway();
                            lodingBar.dismiss();

                            Thread t1 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                  sending.customerUserName=dataSnapshot.child(userphone).child("name").getValue().toString();
                                        sending.customerPhoneNumber=userphone;
                                        sending.customerPassword=dataSnapshot.child(userphone).child("password").getValue().toString();
                                        sending.sendMessage();

                            }
                        });
                        t1.start();


                    Toast.makeText(ForgotPasswordActivity.this,"Password is :"+dataSnapshot.child(userphone).child("password").getValue(),Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {

                            Toast.makeText(ForgotPasswordActivity.this,"Invalid UserName",Toast.LENGTH_SHORT).show();
                            lodingBar.dismiss();
                    }

                }
                else
                 {
                        Toast.makeText(ForgotPasswordActivity.this,"Phone number Not Registered",Toast.LENGTH_SHORT).show();
                        lodingBar.dismiss();
                 }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }


}