package com.example.justread.BookActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.justread.Adapters.BooksRVAdapter;
import com.example.justread.Database.DataBaseHelper;
import com.example.justread.Datamodels.BookModel;
import com.example.justread.R;

import java.util.List;

public class ListBooksActivity extends AppCompatActivity {
    RecyclerView bookslist_rv;
    List<BookModel> itemslist;
    TextView bookslistempty_tv;

    int userid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_books);

        Bundle bundle = getIntent().getBundleExtra("data");
        userid = bundle.getInt("userid");

    }

    protected void onResume() {
        super.onResume();
        getData();
        initViews();
    }

    private void initViews() {
        bookslist_rv = findViewById(R.id.bookslist_rv);
        BooksRVAdapter booksRVAdapter = new BooksRVAdapter(itemslist);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        bookslist_rv.setLayoutManager(layoutManager);
        bookslist_rv.setAdapter(booksRVAdapter);
        booksRVAdapter.notifyDataSetChanged();

    }

    private void getData() {
        try {
            DataBaseHelper databaseHelper = new DataBaseHelper(ListBooksActivity.this);
            itemslist = databaseHelper.getBooks(userid);
            if (itemslist.isEmpty()) {
                System.out.println(" in ListBooksActivity: No books ");
                bookslistempty_tv = findViewById(R.id.bookslist_empty_tv);
                bookslistempty_tv.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            System.out.println("Exception in ListBooksActivity: " + e);
        }

    }
}
