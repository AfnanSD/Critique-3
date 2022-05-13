package com.example.critique;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance = null;
    private static final String DatabaseName = "Critique.db";

    public static DBHelper getInstance(Context ctx) {
        if (instance == null) {
            instance = new DBHelper(ctx.getApplicationContext());
        }
        return instance;
    }

    private DBHelper(Context context) {
        super(context, DatabaseName, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + usersTable + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT, TYPE TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE storesTable (SID INTEGER PRIMARY KEY AUTOINCREMENT, RETAILERID INTEGER, STORENAME TEXT, FOREIGN KEY (RETAILERID) REFERENCES USERS (ID));");//afnan
        sqLiteDatabase.execSQL("CREATE TABLE "+ reviewsTable + "(RID INTEGER PRIMARY KEY AUTOINCREMENT, STOREID INTEGER, REVIEW TEXT, RATING FLOAT, FOREIGN KEY (STOREID) REFERENCES STORETABLE(SID));");
        sqLiteDatabase.execSQL("CREATE TABLE " + invitationTable   + "(NotificationID TEXT PRIMARY KEY, StoreID INTEGER, Title TEXT, notificationText TEXT, Latitude DOUBLE, Longitude DOUBLE, FOREIGN KEY (StoreID) REFERENCES STORETABLE(SID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + usersTable);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + invitationTable);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + storesTable);//afnan
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + reviewsTable);//?afnan
        onCreate(sqLiteDatabase);
    }

    public Cursor getRow(String Table, String condition, String[] conditionArgs){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Table + " WHERE " + condition, conditionArgs);
        cursor.moveToFirst();
        return cursor;
    }

    //Users Table #1
    private static final String usersTable = "Users";
    //    private static final String col11 = "ID";
    private static final String col12 = "USERNAME";
    private static final String col13 = "PASSWORD";
    private static final String col14 = "TYPE";

    public boolean insertUsersData(String username, String password, String type){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col12, username);
        cv.put(col13, password);
        cv.put(col14, type);
        long result = db.insert(usersTable, null, cv);

        return (result != -1);
    }

    public boolean validateInput(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] conditionArgs = {username, password};
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", conditionArgs);

        boolean valid = cursor.getCount() > 0;
        cursor.close();

        return valid;
    }

    //return retailer id or 0
    public int validateInput2(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] conditionArgs = {username, password};
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE username = ? AND password = ?", conditionArgs);
        int id;
        if(cursor.getCount()!=0){
            cursor.moveToFirst();
            id = cursor.getInt(0);
        }
        else
            id = 0;
        cursor.close();

        return id;
    }

    public Cursor getUserName(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] conditionArgs = {Integer.toString(id)};
        Cursor cursor = db.rawQuery("select username from users where id = ?",conditionArgs);
        return cursor;
    }


    //Invitation Table
    private static final String invitationTable = "Invitation";
    private static final String col21 = "NotificationID", col22 = "StoreID", col23 = "Title", col24 = "notificationText", col25 = "Latitude", col26 = "Longitude";

    public boolean insertInvitationData(String NotificationID, String StoreID, String title, String note, double lat, double lng) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col21, NotificationID);
        cv.put(col22, StoreID);
        cv.put(col23, title);
        cv.put(col24, note);
        cv.put(col25, lat);
        cv.put(col26, lng);
        long result = db.insert(invitationTable, null, cv);

        return (result != -1);
    }


    //Stores Table
    private static final String storesTable = "storesTable";//table for retailer's stores
    private static final String colRetailerID = "retailerID";//FK
    private static final String colStoresName = "storeName";

    public boolean insertDataIntoStoresTable(int retielerID , String storeName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(colRetailerID,retielerID);
        contentValues.put(colStoresName,storeName);
        long result = db.insert(storesTable,null,contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public ArrayList<String> readStores() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorStores = db.rawQuery("SELECT STORENAME FROM " + storesTable, null);
        Cursor cursorReviews;
        ArrayList<String> storesArrayList = new ArrayList<>();
        if (cursorStores.moveToFirst()) {
            do {
//                ArrayList<String> reviews = new ArrayList<>();
//                cursorReviews = getReviews(cursorStores.getInt(0));
//                if(cursorReviews.moveToFirst()){
//                    do{
//                        reviews.add(cursorReviews.getString(2));
//                    }while(cursorReviews.moveToNext());
//                }
//
//                storesArrayList.add(new Store(cursorStores.getString(3), reviews, );
                storesArrayList.add(cursorStores.getString(0));
            } while (cursorStores.moveToNext());
        }
        return storesArrayList;
    }

    public Cursor getStores (int retailerID){
        SQLiteDatabase db = this.getWritableDatabase();//close in ondestroy()//change xxx.this
        String[] conditionArgs = {Integer.toString(retailerID)};
        Cursor c = db.rawQuery("select * from "+storesTable+" where retailerid=?",conditionArgs);
        return c;
    }

    public Cursor getAllStores(){
        SQLiteDatabase db = this.getWritableDatabase();//close in ondestroy()//change xxx.this
        //String[] conditionArgs = {Integer.toString()};
        Cursor c = db.rawQuery("select * from "+storesTable,null);
        return c;
    }

    //Reviews Table
    private static final String reviewsTable = "reviewsTable";//table for store's reviews
    private static final String colStoreID = "storeID";//FK
    private static final String colReviews = "review";
    private static final String colReviewsRating = "rating";//out of 5?

    public boolean insertDataIntoReviewsTable(int storeID , String review , float rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(colStoreID,storeID);
        contentValues.put(colReviews,review);
        contentValues.put(colReviewsRating,rating);
        long result = db.insert(reviewsTable,null,contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getReviews(int storeID){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] conditionArgs = {Integer.toString(storeID)};
        Cursor c = db.rawQuery("select * from "+reviewsTable+" where STOREID=?",conditionArgs);
        return c;
    }


}
