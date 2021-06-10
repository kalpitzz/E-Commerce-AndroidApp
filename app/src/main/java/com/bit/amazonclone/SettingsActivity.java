package com.bit.amazonclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bit.amazonclone.Model.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText fullNameEditText,userPhoneEditText,addressEditText;
    private TextView profileChangeTextBtn,closeTextBtn,saveTextButton;

    private Uri imageUri;
    private String myUrl="";
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef;
    private String checker="";


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePictureRef= FirebaseStorage.getInstance().getReference().child("Profile pictures");

        profileImageView=(CircleImageView)findViewById(R.id.settings_profile_image);

        fullNameEditText=(EditText)findViewById(R.id.setting_full_name);
        userPhoneEditText=(EditText)findViewById(R.id.setting_phone_number);
        addressEditText=(EditText)findViewById(R.id.setting_adress);
        profileChangeTextBtn=(TextView)findViewById(R.id.profile_image_change_btn);
        closeTextBtn=(TextView)findViewById(R.id.close_settings_btn);
        saveTextButton=(TextView)findViewById(R.id.update_account_settings_btn);

        userInfoDisplay(profileImageView,fullNameEditText,userPhoneEditText,addressEditText);
        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked")){
                    userInfoSaved();
                }else{
                    updateOnlyUserInfo();
                }
            }
        });
        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    checker="clicked";
                CropImage.activity(imageUri).start(SettingsActivity.this);
                    
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null ){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();
            profileImageView.setImageURI(imageUri);

        }else{
            Intent intent=new Intent(SettingsActivity.this, HomeActivity.class);
            Toast.makeText(this,"Error Try Again",Toast.LENGTH_SHORT).show();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            finish();
        }
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> userMap=new HashMap<>();
        userMap.put("name",fullNameEditText.getText().toString());
        userMap.put("address",addressEditText.getText().toString());
        userMap.put("phoneOrder",userPhoneEditText.getText().toString());

        ref.child(Prevalent.CurrentOnlineUser.getPhone()).updateChildren(userMap);
        Intent intent=new Intent(SettingsActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        Toast.makeText(SettingsActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
        finish();

    }

    private void userInfoSaved()
    {
        if(TextUtils.isEmpty(fullNameEditText.getText().toString())){
            Toast.makeText(this,"Name is Mandatory",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
        Toast.makeText(this,"Address is Mandatory",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(userPhoneEditText.getText().toString()))
        {
            Toast.makeText(this,"Phone is Mandatory",Toast.LENGTH_SHORT).show();
        }else if (checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, Updating Account info");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri!=null){
            final StorageReference fileRef=storageProfilePictureRef.child(Prevalent.CurrentOnlineUser.getPhone()+".jpg");
            uploadTask=fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                  if(!task.isSuccessful()) {
                      throw task.getException();
                  }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                   if(task.isSuccessful()){
                       Uri downloadUrl=task.getResult();
                       myUrl=downloadUrl.toString();
                       DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
                       HashMap<String,Object> userMap=new HashMap<>();
                        userMap.put("name",fullNameEditText.getText().toString());
                        userMap.put("address",addressEditText.getText().toString());
                        userMap.put("phoneOrder",userPhoneEditText.getText().toString());
                        userMap.put("image",myUrl);

                       ref.child(Prevalent.CurrentOnlineUser.getPhone()).updateChildren(userMap);
                       progressDialog.dismiss();
                        Intent intent=new Intent(SettingsActivity.this, HomeActivity.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                       startActivity(intent);

                       Toast.makeText(SettingsActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();

                       finish();

                  }else{
                       progressDialog.dismiss();
                       Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();

                   }
                }
            });
        }else{
            Toast.makeText(this,"Image is not selected",Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText addressEditText) {
        DatabaseReference UserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.CurrentOnlineUser.getPhone());
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                            if(dataSnapshot.child("image").exists()){
                                String name=dataSnapshot.child("name").getValue().toString();
                                String phone=dataSnapshot.child("phone").getValue().toString();
                                String address=dataSnapshot.child("address").getValue().toString();
                                //if(!image.equals("https://firebasestorage.googleapis.com/v0/b/clone-8405b.appspot.com/o/Profile%20pictures%2F1.jpg?alt=media&token=b05a35b1-20e5-4bd7-aa27-573f452e3b04"))
                                String image=dataSnapshot.child("image").getValue().toString();
                                Picasso.get().load(image).into(profileImageView);

                                fullNameEditText.setText(name);
                                userPhoneEditText.setText(phone);
                                addressEditText.setText(address);
                            }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
