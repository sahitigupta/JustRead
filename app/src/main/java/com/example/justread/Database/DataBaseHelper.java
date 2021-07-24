package com.example.justread.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.justread.Datamodels.BookModel;
import com.example.justread.Datamodels.UserModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_BOOKS = "BOOKS";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_BOOKNAME = "BOOKNAME";
    public static final String COLUMN_BOOKAUTHOR = "BOOKAUTHOR";
    public static final String COLUMN_BOOKCATEGORY = "BOOKCATEGORY";
    public static final String COLUMN_BOOKNOTREAD = "BOOKNOTREAD";
    public static final String COLUMN_USERID = "USERID";
    public static final String CONSTRAINT_USER_FK = "UserFK";
    public static final String TABLE_USERS = "USERS";

    public static final String COLUMN_USERNAME = "USERNAME";
    public static final String COLUMN_USEREMAIL = "USEREMAIL";
    public static final String COLUMN_USERPASSWORD = "USERPASSWORD";
    public static final String COLUMN_USERPHONE = "USERPHONE";
    public static final String CONSTRAINT_UNIQUE = "CONSTRAINT_UNIQUE";

    public DataBaseHelper(@Nullable Context context) {
        // justread --> appname, one --> first db
        super(context, "justtestfour.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createtableuser = "CREATE TABLE " + TABLE_USERS + " ( " +
                COLUMN_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, " +
                COLUMN_USEREMAIL + " TEXT NOT NULL UNIQUE, " +
                COLUMN_USERPHONE + " INTEGER NOT NULL , " +
                COLUMN_USERPASSWORD + " TEXT NOT NULL )";
        db.execSQL(createtableuser);

        String createTableItem = "CREATE TABLE " + TABLE_BOOKS + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BOOKNAME + " TEXT NOT NULL, " +
                COLUMN_BOOKAUTHOR + " TEXT NOT NULL, " +
                COLUMN_BOOKCATEGORY + " TEXT NOT NULL, " +
                COLUMN_BOOKNOTREAD + " INTEGER NOT NULL, " +
                COLUMN_USERID + " INTEGER NOT NULL," +
                " CONSTRAINT " + CONSTRAINT_USER_FK + " FOREIGN KEY (" + COLUMN_USERID + ") REFERENCES " + TABLE_USERS  + ", " +
                " CONSTRAINT " + CONSTRAINT_UNIQUE + " UNIQUE ( " + COLUMN_BOOKNAME + ", " + COLUMN_USERID + " ) " +
                ")";
        db.execSQL(createTableItem);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean adduser(UserModel userModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, userModel.getUsername());
        cv.put(COLUMN_USEREMAIL, userModel.getUseremail());
        cv.put(COLUMN_USERPHONE, userModel.getUserphone());
        cv.put(COLUMN_USERPASSWORD, userModel.getUserpassword());

        long insert = db.insert(TABLE_USERS, null, cv);
        db.close();
        return insert != -1;
    }

    public UserModel finduser(String user_email){
        SQLiteDatabase db = this.getReadableDatabase();
        String finduserquery = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USEREMAIL + " = '" + user_email + "' ";
        Cursor cursor = db.rawQuery(finduserquery, null);
        System.out.println("User found : " + cursor);
        if(cursor.moveToFirst()){
            int userid = cursor.getInt(0);
            String username = cursor.getString(1);
            String useremail = cursor.getString(2);
            long userphone = cursor.getLong(3);
            String userpassword = cursor.getString(4);
            UserModel userModel = new UserModel(username, useremail, userpassword, userphone, userid);

            db.close();
            return userModel;
        }
        db.close();
        return null;
    }

    public boolean addItem(BookModel unitItemModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_BOOKNAME, unitItemModel.getBookname());
        cv.put(COLUMN_BOOKAUTHOR, unitItemModel.getBookauthor());
        cv.put(COLUMN_BOOKCATEGORY, unitItemModel.getBookcategory());
        cv.put(COLUMN_BOOKNOTREAD, unitItemModel.getNotRead());
        cv.put(COLUMN_USERID, unitItemModel.getUserId());

        long insert = db.insert(TABLE_BOOKS, null, cv);
        db.close();
        if(insert == -1)
            return false;
        else return true;
    }

    public List<BookModel> getBooks(int user_id){
        List<BookModel> booklist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String getbooksquery = "SELECT * FROM " + TABLE_BOOKS + " WHERE " + COLUMN_USERID + " = " + user_id + " ";
//        String getbooksquery = "SELECT * FROM " + TABLE_BOOKS ;
        try (Cursor cursor = db.rawQuery(getbooksquery, null)){
            if (cursor.moveToFirst()) {
                do {
                    int bookid = cursor.getInt(0);
                    String bookname = cursor.getString(1);
                    String bookauthor = cursor.getString(2);
                    String bookcategory = cursor.getString(3);
                    int notread = cursor.getInt(4);
                    int userid = cursor.getInt(5);
                    BookModel bookModel = new BookModel(bookid, bookname, bookauthor, bookcategory, notread, userid);
                    booklist.add(bookModel);
                } while (cursor.moveToNext());
            }
            System.out.println("DatabaseHelper : " + booklist);
        }
        db.close();
        return booklist;
    }

    public int getBooksCount(int type, int userid){
        SQLiteDatabase db = this.getReadableDatabase();
        String getBooksCountQuery = "SELECT COUNT(*) FROM " + TABLE_BOOKS + " WHERE " +
                COLUMN_BOOKNOTREAD + " = " + type + " AND " +
                COLUMN_USERID + " = " + userid ;
        Cursor cursor = db.rawQuery(getBooksCountQuery, null);
        if(cursor.moveToFirst()){
            return cursor.getInt(0);
        }
        return 0;
    }

    public boolean changePassword(int userid, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERPASSWORD, password);
        int update = db.update(TABLE_USERS, cv,  COLUMN_USERID + " = " + userid, null);
        return update != -1;
    }

    public boolean deletebook(int bookid){
        SQLiteDatabase db = this.getReadableDatabase();
        int delete = db.delete(TABLE_BOOKS, COLUMN_ID + " = " + bookid, null);
        return delete != -1;
    }

    public boolean findbook(String bookname, int userid){
        SQLiteDatabase db = this.getReadableDatabase();
        String findbookquery = "SELECT * FROM " + TABLE_BOOKS + " WHERE " +
                COLUMN_BOOKNAME + " = '" + bookname + "' AND " +
                COLUMN_USERID + " = " + userid;
        Cursor cursor = db.rawQuery(findbookquery, null);
        System.out.println("Book found : " + cursor);
        if(cursor.moveToFirst()){
            int bookid = cursor.getInt(0);
            db.close();
            return true;
        }
        db.close();
        return false;
    }
}
