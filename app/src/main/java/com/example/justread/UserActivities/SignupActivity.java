package com.example.justread.UserActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.justread.BookActivities.HomeActivity;
import com.example.justread.Database.DataBaseHelper;
import com.example.justread.Datamodels.UserModel;
import com.example.justread.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    EditText email_et, password_et, username_et, phone_et, confirm_password_et;
    Button signup_btn;
    TextView login_page_tv;
    String email, password, username, phone, confirm_password;
    UserModel userCreated;
    DataBaseHelper userDataBaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initViews();
    }

    void initViews(){
        username_et = findViewById(R.id.username_et);
        phone_et = findViewById(R.id.phone_et);
        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        confirm_password_et = findViewById(R.id.confirm_password_et);
        signup_btn = findViewById(R.id.signup_btn);
        login_page_tv = findViewById(R.id.login_page_tv);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    userCreated = new UserModel(username, email, password, Long.parseLong(phone), -1);
                    boolean adduser = userDataBaseHelper.adduser(userCreated);
                    userCreated = userDataBaseHelper.finduser(userCreated.getUseremail());
                    System.out.println("User created: " + userCreated.getUseremail());
                    if(!adduser){
                        Toast.makeText(SignupActivity.this, "Database error: Try after some time", Toast.LENGTH_SHORT).show();
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putString("username", userCreated.getUsername());
                        bundle.putString("useremail", userCreated.getUseremail());
//                        bundle.putString("userpassword", userCreated.getUserpassword());
//                        bundle.putString("username", userCreated.getUsername());
                        bundle.putInt("userid", userCreated.getUserid());
                        System.out.println("SignupActivity - userid: " + userCreated.getUserid());
                        Intent signup = new Intent(SignupActivity.this, HomeActivity.class);
                        signup.putExtra("data", bundle);
                        startActivity(signup);
                        finish();
                    }
                }else
                    Toast.makeText(SignupActivity.this, "Enter valid details", Toast.LENGTH_SHORT).show();
            }
        });

        login_page_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        });
    }

    private void getDataFromViews(){
        username = username_et.getText().toString();
        email = email_et.getText().toString();
        phone = phone_et.getText().toString();
        password = password_et.getText().toString();
        confirm_password = confirm_password_et.getText().toString();
    }

    private boolean validate() {
        getDataFromViews();

        boolean isValid = false;
        String regex_username = "^[A-Za-z0-9 ]+$";
        Pattern pattern_username = Pattern.compile(regex_username);
        Matcher matcher_username = pattern_username.matcher(username);

        String regex_email = "^[A-Za-z0-9_.+]+@[a-z.]+$";
        Pattern pattern_email = Pattern.compile(regex_email);
        Matcher matcher_email = pattern_email.matcher(email);

        String regex_password = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!])[A-Za-z\\d@$!]{8,}$";
        Pattern pattern_password = Pattern.compile(regex_password);
        Matcher matcher_password = pattern_password.matcher(password);


        try {
            userDataBaseHelper = new DataBaseHelper(SignupActivity.this);
        }catch (Exception e){
            System.out.println("Exception in signup activity: " + e);
        }

        if(!matcher_username.matches()){
            username_et.setError("Enter valid name");
            isValid = false;
        }else if(!matcher_email.matches()){
            email_et.setError("Enter valid email");
            isValid = false;
        }else if(userDataBaseHelper.finduser(email) != null){
            email_et.setError("Email already exists");
            isValid = false;
        }else if(phone.length()!=10){
            phone_et.setError("Enter 10 digit phone number");
        }else if(!matcher_password.matches()){
            password_et.setError("Enter valid password");
            isValid = false;
        }else if(!password.equals(confirm_password)){
            confirm_password_et.setError("Passwords don't match");
            isValid = false;
        }else{
            isValid = true;
        }
        return isValid;
    }
}
