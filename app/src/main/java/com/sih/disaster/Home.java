package com.sih.disaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.Objects;

public class Home extends AppCompatActivity {

    ImageView red_blink;
    Boolean disastermode=false;
    FirebaseDatabase root;

    DatabaseReference reference,data;
    DrawerLayout drawerLayout;
    TextView ngo, deaths, disaster,ranger,user;
    Button dismode;
    TextView news;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        root=FirebaseDatabase.getInstance();



        reference=root.getReference("disaster");
        livedataget();
        hook();
        loadpage();
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                44);
        reference.child("mode").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(Boolean.class)){
                    //Disaster mode is ON
                    dismode.setAlpha(1f);

                }
                else {
                    dismode.setAlpha(0f);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("news").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                news.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        news = (TextView)findViewById(R.id.news);
        news.setSelected(true);



        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id){
                    case R.id.faq_menu:{
                        Intent intent=new Intent(Home.this,
                                Faq.class);
                        startActivity(intent);

                        break;
                    }
                    case R.id.Home_menu: {

                        break;
                    }
                    case R.id.ngo_list:{
                        Intent intent=new Intent(Home.this,
                                Ngo_list.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Contactus_menu: {
                        Intent intent=new Intent(Home.this,
                                Contact_Us.class);
                        startActivity(intent);

                        //Toast.makeText(Home.this, "hey/.....", Toast.LENGTH_SHORT).show();

                        break;
                    }
                    case R.id.developer_menu: {
                        Intent intent=new Intent(Home.this,
                                About_Us.class);
                        startActivity(intent);

      

                        break;
                    }
                    case R.id.privacy_policy_menu: {

                        break;
                    }
                    case R.id.TnC_menu: {

                        break;
                    }
                    case R.id.share_app_menu: {
                        
                        break;
                    }
                    case R.id.rate: {
                        

                        break;
                    }
                    default: return true;

                }

                return true;
            }
        });


    }

    public void livedataget() {
        user = (TextView) findViewById(R.id.user_total);
        deaths = (TextView) findViewById(R.id.deaths_total);
        ranger = (TextView) findViewById(R.id.ranger_total);
        ngo = (TextView) findViewById(R.id.ngo_total);
        disaster = (TextView) findViewById(R.id.total_disaster);
        data = root.getReference("data");
        try {
            data.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        if (Objects.equals(snapshot1.getKey(), "deaths")) {
                            deaths.setText(snapshot1.getValue(Integer.class).toString());
                        }
                        else if (Objects.equals(snapshot1.getKey(), "disasters")) {
                            disaster.setText(snapshot1.getValue(Integer.class).toString());
                        }
                        else if (Objects.equals(snapshot1.getKey(), "ngo")) {
                            ngo.setText(snapshot1.getValue(Integer.class).toString());
                        }
                        else if (Objects.equals(snapshot1.getKey(), "rangers")) {
                            ranger.setText(snapshot1.getValue(Integer.class).toString());
                        }
                        else if (Objects.equals(snapshot1.getKey(), "users")) {
                            user.setText(snapshot1.getValue(Integer.class).toString());
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        catch (Exception e){

        }
    }

    public void openillus(View view){
        CardView cclicked = (CardView) view;
        String tag = cclicked.getTag().toString();
        Intent intent=new Intent(Home.this, MainActivity.class);
        if(tag.equals("earthquake")){
            intent.putExtra("mode",1);
            startActivity(intent);

        }
        if(tag.equals("flood")){
            intent.putExtra("mode",3);
            startActivity(intent);

        }
        if(tag.equals("landslide")){
            intent.putExtra("mode",2);
            startActivity(intent);

        }
        if(tag.equals("thunderstorm")){
            intent.putExtra("mode",4);
            startActivity(intent);

        }
        //Toast.makeText(this, ""+tag, Toast.LENGTH_SHORT).show();
    }

    public void dismodeclick(View view){
        Intent intent=new Intent(Home.this,
                MapsActivity.class);
        startActivity(intent);

    }
    private void loadpage() {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(Home.this);
        View loading = getLayoutInflater().inflate(R.layout.loading,null);
        mbuilder.setView(loading);
        AlertDialog loading_dialog = mbuilder.create();
        loading_dialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        loading_dialog.show();
        ImageView loading_image= (ImageView) loading_dialog.findViewById(R.id.loading_image);
        loading_image.animate().rotationBy(1800).setDuration(5000).start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading_dialog.cancel();
            }
        },5000);
    }

    public void refresh(View view){
        ImageView refresh_image= (ImageView) view;
        refresh_image.animate().setDuration(1500).rotation(360).start();
        //Toast.makeText(this, "refreshed", Toast.LENGTH_SHORT).show();
        loadpage();
    }
    public void opendrawer(View view){
        drawerLayout.openDrawer(GravityCompat.START);

    }

    private void hook() {

        red_blink= (ImageView) findViewById(R.id.red_blink);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
        red_blink.startAnimation(animation1);
         dismode = (Button)findViewById(R.id.dismode);
        drawerLayout =(DrawerLayout)findViewById(R.id.drawerlayout);
        navigationView= (NavigationView)findViewById(R.id.navigationlayout);
        


    }
}