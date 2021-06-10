package com.bit.amazonclone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bit.amazonclone.Model.Prevalent.Prevalent;
import com.bit.amazonclone.Model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import ViewHolder.ProductViewHolder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;



public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private Button SearchBtn;
    private EditText inputText;
    private String SearchInput;
    private String type="";


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        SearchBtn=(Button) findViewById(R.id.search_btn);
        inputText=(EditText) findViewById(R.id.search_product_name);
        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchInput=inputText.getText().toString();
                onStart();
            }
        });

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
        {
            type=getIntent().getExtras().get("Admin").toString();
        }


        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");


        Paper.init(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(type.equals("Admin"))
            fab.setVisibility(View.GONE);

        fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(HomeActivity.this,CartActivity.class);

                    startActivity(intent);
                }
            });




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            if(type.equals("Admin"))
                navigationView.setVisibility(View.GONE);
            else{

                navigationView.setNavigationItemSelectedListener(this);
                View headerView = navigationView.getHeaderView(0);
                TextView userNameTextView = headerView.findViewById(R.id.User_profile_name);
                CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);
                userNameTextView.setText(Prevalent.CurrentOnlineUser.getName());
                Picasso.get().load(Prevalent.CurrentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);

            }


        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options;
        if(inputText.getText().toString().isEmpty())
           options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(ProductsRef, Products.class).build();
        else
            options=new FirebaseRecyclerOptions.Builder<Products>().setQuery(ProductsRef.orderByChild("pname").startAt(SearchInput),Products.class).build();

    try{
            FirebaseRecyclerAdapter<Products,ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products,ProductViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                {
                    holder.txtProductName.setText(model.getPname());
                    holder.txtProductPrice.setText("Rs "+ model.getPrice());
                     Picasso.get().load(model.getImage()).into(holder.imageView);



                         holder.itemView.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 if(type.equals("Admin"))
                                 {
                                     Intent intent=new Intent(HomeActivity.this,AdminMaintainProductActivity.class);
                                     intent.putExtra("pid",model.getPid());
                                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                     startActivity(intent);

                                 }
                                 else
                                     {
                                         Intent intent=new Intent(HomeActivity.this,ProductDetailsActivity.class);
                                         intent.putExtra("pid",model.getPid());
                                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                         startActivity(intent);

                                     }
                                 }
                             });
                }
                @NonNull
                @Override
                public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                    ProductViewHolder holder = new ProductViewHolder(view);
                    return holder;
                }
            };
            recyclerView.setAdapter(adapter);
            adapter.startListening();

    }catch(Exception e){

}


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_cart)
        {

            Intent intent=new Intent(HomeActivity.this,CartActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_search)
        {
            Intent intent=new Intent(HomeActivity.this,YoutubeActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_category)
        {

        }
        else if (id == R.id.nav_settings)
        {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);

            startActivity(intent);



        }
        else if (id == R.id.nav_logout)
        {
            Paper.book().destroy();

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}