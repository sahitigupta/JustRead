package com.example.justread.UserActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.justread.BookActivities.HomeActivity;
import com.example.justread.Database.DataBaseHelper;
import com.example.justread.Datamodels.UserModel;
import com.example.justread.R;
import com.example.justread.SplashActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    EditText email_et, password_et;
    Button login_btn;
    TextView signup_page_tv;
    UserModel userfound;
    AlertDialog.Builder alertDialog;

    DataBaseHelper userDataBaseHelper;
    int remainingattempts = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }


    void initViews(){
        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        signup_page_tv = findViewById(R.id.signup_page_tv);
        login_btn = findViewById(R.id.login_btn);

        login_btn.setOnClickListener(v -> {
            onClickLoginButton();
        });

        signup_page_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signup);
                finish();
            }
        });
    }

    private void onClickLoginButton(){
        if(validate()){
            Bundle bundle = new Bundle();
            bundle.putString("username", userfound.getUsername());
            bundle.putString("useremail", userfound.getUseremail());
            bundle.putInt("userid", userfound.getUserid());
            System.out.println("LoginActivity - userid: " + userfound.getUserid());
            Intent login = new Intent(LoginActivity.this, HomeActivity.class);
            login.putExtra("data", bundle);
            startActivity(login);
            finish();
        }else{
            remainingattempts--;
            if(remainingattempts <= 0){
                login_btn.setEnabled(false);
                callAlert();
            }else
                Toast.makeText(LoginActivity.this, "Invalid details. Remaining attempts: "+remainingattempts, Toast.LENGTH_SHORT).show();
        }
    }

    private void callAlert() {
        alertDialog = new AlertDialog.Builder(LoginActivity.this);
        alertDialog.setTitle("Invalid login");
        alertDialog.setCancelable(false);

        if(userfound!=null){
            alertDialog.setMessage("No attempts left. Forgot password? Reset now?");
            alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(LoginActivity.this, "Logic to change password", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("useremail", userfound.getUseremail());
                    bundle.putInt("userid", userfound.getUserid());
                    Intent login = new Intent(LoginActivity.this, PasswordChangeActivity.class);
                    login.putExtra("data", bundle);
                    startActivity(login);
                    finish();
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent splash = new Intent(LoginActivity.this, SplashActivity.class);
                    startActivity(splash);
                    finish();
                }
            });
        }else{
            alertDialog.setMessage("No attempts left. User not found");
            alertDialog.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent splash = new Intent(LoginActivity.this, SplashActivity.class);
                    startActivity(splash);
                    finish();
                }
            });
        }
        alertDialog.show();
    }

    private boolean validate() {
        String email, password;
        email = email_et.getText().toString();
        password = password_et.getText().toString();
        boolean isValid = false;

        String regexemail = "^[A-Za-z0-9_.+]+@[a-z.]+$";
        Pattern pattern = Pattern.compile(regexemail);
        Matcher matcher = pattern.matcher(email);

        try {
            userDataBaseHelper = new DataBaseHelper(LoginActivity.this);
            userfound = userDataBaseHelper.finduser(email);
            System.out.println("User found in LoginActivity: " + userfound);
        }catch (Exception e){
            System.out.println("Exception in LoginActivity: " + e);
            return false;
        }
        if(!matcher.matches()){
            email_et.setError("Enter valid email");
            isValid = false;
        }else if(userfound == null) {
            email_et.setError("Email doesn't exist");
            isValid = false;
        }else if(! password.equals(userfound.getUserpassword())){
            password_et.setError("Invalid password");
            isValid = false;
        }else{
            isValid = true;
        }
        return isValid;
    }
}
