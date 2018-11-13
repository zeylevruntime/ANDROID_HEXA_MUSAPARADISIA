package com.musa.hexa;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {
    CircleImageView image_prof;
    RecyclerView rv_list;
    TextView tv_fullname,tv_username,tv_email,tv_address;
    String fullname,username,email,address,token,photo;
    ProgressBar pb;
    List<Mylist> jsonlist =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        pb = findViewById(R.id.pb);
        image_prof = findViewById(R.id.image_prof);
        tv_fullname = findViewById(R.id.tv_fullname);
        tv_username = findViewById(R.id.tv_username);
        tv_email = findViewById(R.id.tv_email);
        tv_address = findViewById(R.id.tv_address);
        rv_list = findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_list.setLayoutManager(layoutManager);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
        } else {
            token= extras.getString("token");
            username= extras.getString("username");
            email= extras.getString("email");
            fullname= extras.getString("fullname");
            address= extras.getString("address");
            photo= extras.getString("photo");
            myToolbar.setTitle(username);

            tv_username.setText(username);
            tv_email.setText(email);
            tv_fullname.setText(fullname);
            tv_address.setText(address);
            Glide.with(getApplicationContext())
                    .load("http://"+photo)
                    .centerCrop()
                    .crossFade()
                    .override(120,120)
                    .into(image_prof);
        }
        getmylist(token);
    }

    private void getmylist(final String token) {
        pb.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiServ.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiServ api = retrofit.create(ApiServ.class);
        Call<List<Mylist>> call = api.getList(token);
        call.enqueue(new Callback<List<Mylist>>() {
            @Override
            public void onResponse(Call<List<Mylist>> call, Response<List<Mylist>> response) {
                pb.setVisibility(View.GONE);
                try{
                    jsonlist = response.body();
                    System.out.println("name= "+jsonlist.get(1).getName());
                    System.out.println("getName= "+response.body().get(0).getName());
                    RecyclerViewAdapter recyclerViewAdapter =new RecyclerViewAdapter(getApplicationContext(), jsonlist);
                    rv_list.setAdapter(recyclerViewAdapter);

                }catch (Exception e){
                    e.printStackTrace();
                    System.out.print("err= "+e.toString());
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Mylist>> call, Throwable t) {
                pb.setVisibility(View.GONE);
                System.out.println("onFailure= "+t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
