package com.bit.amazonclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    private Button CreateAccountButton;
    private EditText InputName , InputPhoneNumber,InputPassword;
    private ProgressDialog lodingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        CreateAccountButton=(Button) findViewById(R.id.register_btn);
        InputName=(EditText) findViewById(R.id.register_username_input);
        InputPhoneNumber=(EditText) findViewById(R.id.register_phone_number_input);
        InputPassword=(EditText) findViewById(R.id.register_password_input);
        lodingBar=new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });


    }

    private void CreateAccount() {

    String name=InputName.getText().toString();
    String phone=InputPhoneNumber.getText().toString();
    String password=InputPassword.getText().toString();

    if(TextUtils.isEmpty(name)){
        Toast.makeText(this,"Please Enter Your Name",Toast.LENGTH_SHORT).show();
    }else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please Enter Your Phone Number",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please Enter Your Password",Toast.LENGTH_SHORT).show();
        }
    else{
            lodingBar.setTitle("Creating Account");
            lodingBar.setMessage("Please Wait While We are Checking Credentials");
            lodingBar.setCanceledOnTouchOutside(false);
            lodingBar.show();

            ValidatePhoneNumber(name,phone,password);

    }

    }

    private void ValidatePhoneNumber(final String name, final String phone, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String,Object> UserDataMap=new HashMap<>();

                    UserDataMap.put("phone",phone);
                    UserDataMap.put("name",name);
                    UserDataMap.put("password",password);

                    RootRef.child("Users").child(phone).updateChildren(UserDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                lodingBar.dismiss();
                                Toast.makeText(RegisterActivity.this,"Congratulations Your Account Has Been Created",Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent=new Intent(RegisterActivity.this ,LoginActivity.class);
                                startActivity(intent);
                            }
                            else{
                                lodingBar.dismiss();
                                Toast.makeText(RegisterActivity.this,"Error Occured",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                Toast.makeText(RegisterActivity.this,"This number "+phone+" Already Exists",Toast.LENGTH_SHORT).show();
                lodingBar.dismiss();
                    Intent intent=new Intent(RegisterActivity.this ,MainActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
