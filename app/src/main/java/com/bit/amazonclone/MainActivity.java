package com.bit.amazonclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bit.amazonclone.Model.Prevalent.Prevalent;
import com.bit.amazonclone.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton,loginButton;
    private ProgressDialog lodingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton = (Button) findViewById(R.id.main_join_now_btn);
        loginButton = (Button) findViewById(R.id.main_login_btn);
        lodingBar=new ProgressDialog(this);

        Paper.init(this);

        loginButton.setOnClickListener(new InnerClass());
        joinNowButton.setOnClickListener(new InnerClass());

        String UserPhoneKey=Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey=Paper.book().read(Prevalent.UserPasswordKey);
        if ( UserPhoneKey !="" && UserPasswordKey !="")
        {
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserPhoneKey,UserPasswordKey);
                lodingBar.setTitle("Already Logged in");
                lodingBar.setMessage("Please Wait ...");
                lodingBar.setCanceledOnTouchOutside(false);
                lodingBar.show();
            }
        }
    }
    private void AllowAccess(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if( (dataSnapshot.child("Users").child(phone).exists())){
                    Users userData=dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    lodingBar.dismiss();
                    if(userData.getPhone().equals(phone))
                    {
                        if (userData.getPassword().equals(password))
                        {
                            Toast.makeText(MainActivity.this, "Access Granted To User", Toast.LENGTH_SHORT).show();
                            lodingBar.dismiss();
                            finish();
                            Intent intent=new Intent(MainActivity.this ,HomeActivity.class);
                            Prevalent.CurrentOnlineUser=userData;
                            startActivity(intent);
                        }
                    }

                }
                else{
                    Toast.makeText(MainActivity.this,"Access Denied Invalid User",Toast.LENGTH_SHORT).show();
                    lodingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }
    class InnerClass implements View.OnClickListener {
            @Override
            public void onClick(View arg0) {
               if(arg0.equals(loginButton)){
                   Intent intent=new Intent(MainActivity.this ,LoginActivity.class);
                   startActivity(intent);
               }
               if(arg0.equals(joinNowButton)){
                   Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                   startActivity(intent);
               }
            }
    }



}





