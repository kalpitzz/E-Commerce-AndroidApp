package com.bit.amazonclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
   private EditText InputPhoneNumber,InputPassword;
   private Button Login;
   private String ParentDBName="Users";
   private ProgressDialog lodingBar;
   private CheckBox chkBoxRememberMe;
   private TextView AdminLink,NotAdminLink,forgetPasswordLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InputPhoneNumber=(EditText) findViewById(R.id.login_phone_number_input);
        InputPassword=(EditText) findViewById(R.id.login_password_input);
        AdminLink=(TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink=(TextView) findViewById(R.id.not_admin_panel_link);
        forgetPasswordLink=(TextView)findViewById(R.id.forget_password_link);
        Login=(Button) findViewById(R.id.login_btn);
        lodingBar=new ProgressDialog(this);
        chkBoxRememberMe=(CheckBox) findViewById(R.id.remember_me_chkb);

        Paper.init(this);

        Login.setOnClickListener(new InnerClass());
        AdminLink.setOnClickListener(new InnerClass());
        NotAdminLink.setOnClickListener(new InnerClass());
        forgetPasswordLink.setOnClickListener(new InnerClass());

    }

    class InnerClass implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v.equals(Login))
            {
                UserLogin();

            }
            else if(v.equals(AdminLink))
            {
                    Login.setText("Login Admin");
                    AdminLink.setVisibility(v.INVISIBLE);
                    NotAdminLink.setVisibility(v.VISIBLE);
                    chkBoxRememberMe.setChecked(false);
                    chkBoxRememberMe.setVisibility(v.INVISIBLE);
                    ParentDBName="Admins";

            }
            else if(v.equals(NotAdminLink))
            {
                Login.setText("Login");
                AdminLink.setVisibility(v.VISIBLE);
                NotAdminLink.setVisibility(v.INVISIBLE);
                chkBoxRememberMe.setVisibility(v.VISIBLE);
                ParentDBName="Users";

            }
            else if(v.equals(forgetPasswordLink))
            {
                Intent intent=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);

            }
        }





        private void UserLogin() {

            String phone=InputPhoneNumber.getText().toString();
            String password=InputPassword.getText().toString();


            if(TextUtils.isEmpty(phone)){
                Toast.makeText(LoginActivity.this,"Please Enter Your Phone Number",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(password)){
                Toast.makeText(LoginActivity.this,"Please Enter Your Password",Toast.LENGTH_SHORT).show();
            }
            else{
                lodingBar.setTitle("Creating Account");
                lodingBar.setMessage("Please Wait While We are Checking Credentials");
                lodingBar.setCanceledOnTouchOutside(false);
                lodingBar.show();

                LoginAccess(phone,password);

            }
        }


        private void LoginAccess(final String phone, final String password) {

            if(chkBoxRememberMe.isChecked() ){
                Paper.book().write(Prevalent.UserPhoneKey,phone);
                Paper.book().write(Prevalent.UserPasswordKey,password);
            }

            final DatabaseReference RootRef;
            RootRef= FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if( (dataSnapshot.child(ParentDBName).child(phone).exists())){
                        Users userData=dataSnapshot.child(ParentDBName).child(phone).getValue(Users.class);
                        lodingBar.dismiss();
                        if(userData.getPhone().equals(phone)) {
                            if (userData.getPassword().equals(password)) {
                               if(ParentDBName.equals("Admins")){
                                   Toast.makeText(LoginActivity.this, "Admin You Logged In Successfully...", Toast.LENGTH_SHORT).show();
                                   lodingBar.dismiss();
                                  Intent intent=new Intent(LoginActivity.this ,AdminCategoryActivity.class);
                                   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                   finish();
                                  startActivity(intent);

                               }else if(ParentDBName.equals("Users")){

                                   Toast.makeText(LoginActivity.this, "Logged In Successfully...", Toast.LENGTH_SHORT).show();
                                   lodingBar.dismiss();
                                   finish();
                                   Intent intent=new Intent(LoginActivity.this ,HomeActivity.class);
                                       Prevalent.CurrentOnlineUser=userData;
                                   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                   startActivity(intent);

                               }
                            }else{
                                Toast.makeText(LoginActivity.this,"Invalid Password",Toast.LENGTH_SHORT).show();
                                lodingBar.dismiss();
                            }
                       }

                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Access Denied Invalid User",Toast.LENGTH_SHORT).show();
                        lodingBar.dismiss();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

}
