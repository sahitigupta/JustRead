package com.example.justread.BookActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.justread.Database.DataBaseHelper;
import com.example.justread.Datamodels.BookModel;
import com.example.justread.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Pattern;

public class AddBooksActivity extends AppCompatActivity {
    Button add_btn;
    EditText add_bookname_et, add_bookauthor_et, add_bookcategory_et;
    RadioButton add_bookread_rb, add_booknotread_rb;
    RadioGroup add_bookread_rg;

    String bookname, bookauthor, bookcategory;
    int notreadthebook = 1, userid;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        Bundle bundle = getIntent().getBundleExtra("data");
        userid = bundle.getInt("userid");
        initViews();
    }

    void initViews(){
        add_btn = findViewById(R.id.add_btn);
        add_bookname_et = findViewById(R.id.add_bookname_et);
        add_bookauthor_et = findViewById(R.id.add_bookauthor_et);
        add_bookcategory_et = findViewById(R.id.add_bookcategory_et);
        add_bookread_rb = findViewById(R.id.add_bookread_rb);
        add_booknotread_rb = findViewById(R.id.add_booknotread_rb);
        add_bookread_rg = findViewById(R.id.add_bookread_rg);

        add_bookread_rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notreadthebook = 0;
                System.out.println("AddBooksActivity: read");
            }
        });

        add_booknotread_rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("AddBooksActivity: not read");
                notreadthebook = 1;
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(validate()){
                        boolean success = addbook();
                        if(success){
                            Bundle bundle = new Bundle();
                            bundle.putInt("userid", userid);
                            Intent mainpage = new Intent(AddBooksActivity.this, ListBooksActivity.class);
                            mainpage.putExtra("data", bundle);
                            startActivity(mainpage);
                            finish();
                        }else{
                            Snackbar.make(AddBooksActivity.this, v, "Insert failed.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Snackbar.make(AddBooksActivity.this, v, "Please enter valid details", Snackbar.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Snackbar.make(AddBooksActivity.this, v, "Internal error",Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean addbook(){
        BookModel bookitem = new BookModel(-1, bookname, bookauthor, bookcategory, notreadthebook, userid);
        boolean success = dataBaseHelper.addItem(bookitem);
        return success;
    }

    private boolean validate(){
        bookname = add_bookname_et.getText().toString();
        bookauthor = add_bookauthor_et.getText().toString();
        bookcategory = add_bookcategory_et.getText().toString();
        boolean isvalid = false;

        try{
            dataBaseHelper = new DataBaseHelper(AddBooksActivity.this);
        }catch (Exception e){
            System.out.println("Exception in AddBooksActivity: " + e);
        }

        String regex_book = "^[A-Za-z0-9_.+@!* ]+$";
        Pattern pattern_book = Pattern.compile(regex_book);
        if(!pattern_book.matcher(bookname).matches()){
            isvalid = false;
            add_bookname_et.setError("Enter valid characters");
        }else if(dataBaseHelper.findbook(bookname, userid)){
            add_bookname_et.setError("Book already exists");
        }else if(!pattern_book.matcher(bookauthor).matches()){
            isvalid = false;
            add_bookauthor_et.setError("Enter valid characters");
        }else if(!pattern_book.matcher(bookcategory).matches()){
            isvalid = false;
            add_bookcategory_et.setError("Enter valid characters");
        }else if(!add_booknotread_rb.isChecked() && !add_bookread_rb.isChecked()){
            isvalid = false;
            Toast.makeText(this, "Have you read the book?", Toast.LENGTH_SHORT).show();
        }else{
            isvalid = true;
        }
        return isvalid;
    }
}
