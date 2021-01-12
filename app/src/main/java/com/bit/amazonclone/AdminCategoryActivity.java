package com.bit.amazonclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView TShirts,SportsTShirts,FemaleDresses,Sweathers;
    private ImageView Glasses,HatsCaps,WalletsBagsPurses,Shoes;
    private ImageView HeadPhonesHandFree,Laptops,Watches,MobilePhones;
    private Button LogoutBtn,CheckOrdersBtn,maintainProductsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        TShirts=(ImageView)findViewById(R.id.t_shirts);
        SportsTShirts=(ImageView)findViewById(R.id.sports_t_shirts);
        FemaleDresses=(ImageView)findViewById(R.id.female_dresses);
        Sweathers=(ImageView)findViewById(R.id.sweathers);
        Glasses=(ImageView)findViewById(R.id.glasses);
        HatsCaps=(ImageView)findViewById(R.id.hats_caps);
        WalletsBagsPurses=(ImageView)findViewById(R.id.purses_bags_wallets);
        Shoes=(ImageView)findViewById(R.id.shoes);
        HeadPhonesHandFree=(ImageView)findViewById(R.id.headphones_handfree);
        Laptops=(ImageView)findViewById(R.id.laptop_pc);
        Watches=(ImageView)findViewById(R.id.watches);
        MobilePhones=(ImageView)findViewById(R.id.mobilephones);

        LogoutBtn=(Button) findViewById(R.id.admin_logout_btn);
        CheckOrdersBtn=(Button) findViewById(R.id.check_orders_btn);
        maintainProductsBtn=(Button) findViewById(R.id.maintain_btn);
        maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(AdminCategoryActivity.this,HomeActivity.class);
                intent2.putExtra("Admin","Admin");
                startActivity(intent2);
            }
        });
        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(AdminCategoryActivity.this,MainActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);
                finish();
            }
        });
        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(AdminCategoryActivity.this,AdminNewOrdersActivity.class);
                startActivity(intent2);
            }
        });



        TShirts.setOnClickListener(new InnerClass());
        SportsTShirts.setOnClickListener(new InnerClass());
        FemaleDresses.setOnClickListener(new InnerClass());
        Sweathers.setOnClickListener(new InnerClass());
        Glasses.setOnClickListener(new InnerClass());
        HatsCaps.setOnClickListener(new InnerClass());
        WalletsBagsPurses.setOnClickListener(new InnerClass());
        Shoes.setOnClickListener(new InnerClass());
        HeadPhonesHandFree.setOnClickListener(new InnerClass());
        Laptops.setOnClickListener(new InnerClass());
        Watches.setOnClickListener(new InnerClass());
        MobilePhones.setOnClickListener(new InnerClass());


    }
    class InnerClass implements View.OnClickListener{
        @Override
    public void onClick(View v)
        {
            Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
           if(v.equals(TShirts))
           {
               intent.putExtra("category","TShirts");
           }
           else if(v.equals(SportsTShirts))
            {
                intent.putExtra("category","SportsTShirts");
            }
            else if(v.equals(FemaleDresses))
            {
                intent.putExtra("category","FemaleDresses");
            }
           else if(v.equals(Sweathers))
           {
               intent.putExtra("category","Sweathers");
           }
           else if(v.equals(Glasses))
           {
               intent.putExtra("category","Glasses");
           }
           else if(v.equals(HatsCaps))
           {
               intent.putExtra("category","HatsCaps");
           }
           else if(v.equals(WalletsBagsPurses))
           {
               intent.putExtra("category","WalletsBagsPurses");
           }
           else if(v.equals(Shoes))
           {
               intent.putExtra("category","Shoes");
           }
           else if(v.equals(HeadPhonesHandFree))
           {
               intent.putExtra("category","HeadPhonesHandFree");
           }
           else if(v.equals(Watches))
           {
               intent.putExtra("category","Watches");
           }
           else if(v.equals(Laptops))
           {
               intent.putExtra("category","Laptops");
           }
           else if(v.equals(MobilePhones))
           {
               intent.putExtra("category","MobilePhones");
           }

            startActivity(intent);
        }

    }

}
