package com.example.justread.UserActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.justread.Database.DataBaseHelper;
import com.example.justread.Datamodels.UserModel;
import com.example.justread.R;
import com.example.justread.SplashActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordChangeActivity extends AppCompatActivity {
    EditText cp_userpassword_et, cp_username_et, cp_userconfirmpassword_et;
    TextView cp_useremail_tv;
    Button cp_changepassword_btn;

    String username, password, useremail, confirmpassword;
    int userid;
    DataBaseHelper userDataBaseHelper;
    UserModel userfound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        Bundle bundle = getIntent().getBundleExtra("data");
        useremail = bundle.getString("useremail");
        userid = bundle.getInt("userid");

        initViews();

    }

    public void initViews(){
        cp_useremail_tv = findViewById(R.id.cp_useremail_tv);
        cp_username_et = findViewById(R.id.cp_username_et);
        cp_userpassword_et = findViewById(R.id.cp_userpassword_et);
        cp_userconfirmpassword_et = findViewById(R.id.cp_userconfirmpassword_et);
        cp_changepassword_btn = findViewById(R.id.cp_changepassword_btn);

        cp_useremail_tv.setText(useremail);
        cp_changepassword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = cp_username_et.getText().toString();
                password = cp_userpassword_et.getText().toString();
                confirmpassword = cp_userconfirmpassword_et.getText().toString();

                if(validate()){
                    boolean success = userDataBaseHelper.changePassword(userid, password);
                    if(success){
                        Toast.makeText(PasswordChangeActivity.this, "Password changed succesfully. Login now", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(PasswordChangeActivity.this, "Database error. Try after some time", Toast.LENGTH_SHORT).show();
                    }
                    Intent splash = new Intent(PasswordChangeActivity.this, SplashActivity.class);
                    startActivity(splash);
                    finish();
                }else{
                    Snackbar.make(PasswordChangeActivity.this, v, "Invalid details", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    public boolean validate(){
        boolean isValid = false;
        String regex_username = "^[A-Za-z0-9 ]+$";
        Pattern pattern_username = Pattern.compile(regex_username);
        Matcher matcher_username = pattern_username.matcher(username);

        String regex_password = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!])[A-Za-z\\d@$!]{8,}$";
        Pattern pattern_password = Pattern.compile(regex_password);
        Matcher matcher_password = pattern_password.matcher(password);

        try {
            userDataBaseHelper = new DataBaseHelper(PasswordChangeActivity.this);
            userfound = userDataBaseHelper.finduser(useremail);
        }catch (Exception e){
            System.out.println("Exception in PasswordChangeActivity activity: " + e);
        }

        if(!matcher_username.matches()){
            cp_username_et.setError("Enter valid name");
            isValid = false;
        }else if(!matcher_password.matches()){
            cp_userpassword_et.setError("Enter valid password");
            isValid = false;
        }else if(!password.equals(confirmpassword)){
            System.out.println("PasswordChangeActivity: passwords not match ");
            cp_userconfirmpassword_et.setError("Passwords don't match");
            isValid = false;
        }else if(userfound != null){
            if(! userfound.getUsername().equals(username)){
                cp_username_et.setError("User name doesn't match");
                isValid = false;
            }else{
                isValid = true;
            }
        }else{
            isValid = false;
        }

        return isValid;
    }
}
