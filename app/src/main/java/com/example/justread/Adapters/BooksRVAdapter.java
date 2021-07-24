package com.example.justread.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.justread.Database.DataBaseHelper;
import com.example.justread.Datamodels.BookModel;
import com.example.justread.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class BooksRVAdapter extends RecyclerView.Adapter<BooksRVAdapter.ViewHolder>{
    private List<BookModel> bookslist;

    public BooksRVAdapter(List<BookModel> bookslist) {
        this.bookslist = bookslist;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View bookview = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_book_view, parent, false);
        return new ViewHolder(bookview);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull BooksRVAdapter.ViewHolder holder, int position) {
        if(getItemCount()>0){
            holder.bookname.setText(bookslist.get(position).getBookname());
            holder.bookauthor.setText(bookslist.get(position).getBookauthor());
            holder.bookcategory.setText(bookslist.get(position).getBookcategory());

            if(( bookslist.get(position).getNotRead()) == 1){
                holder.notread.setText("Not completed");
                holder.itemView.setBackgroundColor(Color.rgb(255, 173, 173));
            }else{
                holder.notread.setText("Completed");
                holder.itemView.setBackgroundColor(Color.rgb(173, 255, 173));
            }
            holder.clear_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int bookid = bookslist.get(position).getBookId();
                    DataBaseHelper bookDataBaseHelper = new DataBaseHelper(v.getContext());
                    boolean success = bookDataBaseHelper.deletebook(bookid);
                    System.out.println("Success- BooksRVAdapter: " + success);
                    // TODO: Stuck here. But found a kind of temporary solution - Should find a way to update 'position' variable
//                    bookslist.remove(holder.getAdapterPosition());
//                    System.out.println("BooksRVAdapter 1: " + bookslist.get(holder.getAdapterPosition()).getBookname());
//                    BooksRVAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
//                    holder.itemView.getRootView().postInvalidate();
                    holder.itemView.setVisibility(View.GONE);
//                    BooksRVAdapter.this.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(bookslist!=null)
            return bookslist.size();
        else return 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView bookcategory, bookname, bookauthor, notread;
        FloatingActionButton clear_fab;

        public ViewHolder(View bookview) {
            super(bookview);
            bookname = bookview.findViewById(R.id.one_book_name);
            bookauthor = bookview.findViewById(R.id.one_book_author);
            bookcategory = bookview.findViewById(R.id.one_book_category);
            notread = bookview.findViewById(R.id.one_book_read);
            clear_fab = bookview.findViewById(R.id.one_book_clear_fab);
        }
    }
}
