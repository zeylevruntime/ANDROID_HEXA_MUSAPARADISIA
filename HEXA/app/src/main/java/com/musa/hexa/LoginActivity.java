package com.musa.hexa;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.READ_CONTACTS;


public class LoginActivity extends AppCompatActivity {


    private EditText username,password;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username =  findViewById(R.id.uname);
        password =  findViewById(R.id.password);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Log in...");
        progressDialog.setCancelable(false);
        Button login = findViewById(R.id.email_sign_in_button);
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                username.setError(null);
                password.setError(null);

                String user = username.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                if (user.length()<1){
                    username.setError("Tidak boleh kosong");
                    return;
                }
                if (pwd.length()<1){
                    password.setError("Tidak boleh kosong");
                    return;
                }
                if (pwd.length()<6){
                    password.setError("Minimal 6 character");
                    return;
                }
                progressDialog.show();
                login(user,pwd);
            }
        });
    }

    private void login(String user, String pwd) {
        RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"), user);
        RequestBody password = RequestBody.create(MediaType.parse("multipart/form-data"), pwd);
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiServ.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ApiServ api = retrofit.create(ApiServ.class);
        Call<MyResponse> call = api.login(username, password);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                progressDialog.dismiss();
                try{
                    MyResponse res = response.body();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("token", res.getToken());
                        intent.putExtra("username", res.getUsername());
                        intent.putExtra("email", res.getEmail());
                        intent.putExtra("fullname", res.getFullname());
                        intent.putExtra("address", res.getAddress());
                        intent.putExtra("photo", res.getPhoto());
                        startActivity(intent);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                System.out.println("onFailure= "+t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}

