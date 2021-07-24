package com.example.justread.BookActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.justread.Database.DataBaseHelper;
import com.example.justread.R;
import com.example.justread.SplashActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {
    FloatingActionButton home_addbook_fab;
    Button home_view_btn, signout_btn;
    TextView home_username_tv, home_read_tv, home_notread_tv;

    int userid;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle bundle = getIntent().getBundleExtra("data");
        userid = bundle.getInt("userid");
        username = bundle.getString("username");
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(HomeActivity.this);
        home_read_tv.setText("Books read: " + dataBaseHelper.getBooksCount(0, userid));
        home_notread_tv.setText("Books to read: " + dataBaseHelper.getBooksCount(1, userid));

        System.out.println("In HomeActivity: notread count-- " + dataBaseHelper.getBooksCount(1, userid) );
        System.out.println("In HomeActivity: read count -- " + dataBaseHelper.getBooksCount(0, userid) );
    }

    void initViews(){

        home_username_tv = findViewById(R.id.home_username_tv);
        home_addbook_fab = findViewById(R.id.home_addbook_fab);
        home_view_btn = findViewById(R.id.home_view_btn);
        signout_btn = findViewById(R.id.home_signout_btn);
        home_read_tv = findViewById(R.id.home_read_tv);
        home_notread_tv = findViewById(R.id.home_notread_tv);


        home_username_tv.setText("Welcome " + username);
        System.out.println("HomeActivity - userid: " + userid);

        home_addbook_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("userid", userid);
                Intent addbookspage = new Intent(HomeActivity.this, AddBooksActivity.class);
                addbookspage.putExtra("data", bundle);
                startActivity(addbookspage);
            }
        });

        home_view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("userid", userid);
                Intent listbookspage = new Intent(HomeActivity.this, ListBooksActivity.class);
                listbookspage.putExtra("data", bundle);
                startActivity(listbookspage);

            }
        });

        signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signout = new Intent(HomeActivity.this, SplashActivity.class);
                startActivity(signout);
                finish();
            }
        });


    }
}
