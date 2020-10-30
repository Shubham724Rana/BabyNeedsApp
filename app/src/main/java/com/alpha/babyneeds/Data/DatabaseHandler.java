package com.alpha.babyneeds.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

import androidx.annotation.Nullable;

import com.alpha.babyneeds.Model.Item;
import com.alpha.babyneeds.Util.Util;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BABY_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "("
                + Util.KEY_Id + " INTEGER PRIMARY KEY,"
                + Util.KEY_itemName + " INTEGER,"
                + Util.KEY_itemColor + " TEXT,"
                + Util.KEY_itemQuantity + " INTEGER,"
                + Util.KEY_itemSize + " INTEGER,"
                + Util.KEY_dateItemAdded + " LONG);";

        db.execSQL(CREATE_BABY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_NAME);

        onCreate(db);
    }

    // CRUD operations
    public void addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_itemName, item.getItemName());
        values.put(Util.KEY_itemColor, item.getItemColor());
        values.put(Util.KEY_itemQuantity, item.getItemQuantity());
        values.put(Util.KEY_itemSize, item.getItemSize());
        values.put(Util.KEY_dateItemAdded, java.lang.System.currentTimeMillis());//timestamp of the system

        //Inset the row
        db.insert(Util.TABLE_NAME, null, values);

        Log.d("DBHandler", "added Item: ");
    }

    //Get an Item
    public Item getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TABLE_NAME,
                new String[]{Util.KEY_Id,
                        Util.KEY_itemName,
                        Util.KEY_itemColor,
                        Util.KEY_itemQuantity,
                        Util.KEY_itemSize,
//                        Util.KEY_dateItemAdded
                      },
                Util.KEY_Id + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Item item = new Item();
        if (cursor != null) {
            item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_Id))));
            item.setItemName(cursor.getString(cursor.getColumnIndex(Util.KEY_itemName)));
            item.setItemColor(cursor.getString(cursor.getColumnIndex(Util.KEY_itemColor)));
            item.setItemQuantity(cursor.getInt(cursor.getColumnIndex(Util.KEY_itemQuantity)));
            item.setItemSize(cursor.getInt(cursor.getColumnIndex(Util.KEY_itemSize)));

//            //convert Timestamp to something readable
//            DateFormat dateFormat = DateFormat.getDateInstance();
//            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Util.KEY_dateItemAdded)))
//                    .getTime()); // Feb 23, 2020

//            item.setDataItemAdded(formattedDate);


        }

        return item;
    }

    //Get all Items
    public List<Item> getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Item> itemList = new ArrayList<>();

        Cursor cursor = db.query(Util.TABLE_NAME,
                new String[]{Util.KEY_Id,
                        Util.KEY_itemName,
                        Util.KEY_itemColor,
                        Util.KEY_itemQuantity,
                        Util.KEY_itemSize,
                        Util.KEY_dateItemAdded},
                null, null, null, null,
                Util.KEY_dateItemAdded + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_Id))));
                item.setItemName(cursor.getString(cursor.getColumnIndex(Util.KEY_itemName)));
                item.setItemColor(cursor.getString(cursor.getColumnIndex(Util.KEY_itemColor)));
                item.setItemQuantity(cursor.getInt(cursor.getColumnIndex(Util.KEY_itemQuantity)));
                item.setItemSize(cursor.getInt(cursor.getColumnIndex(Util.KEY_itemSize)));

                //convert Timestamp to something readable
                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Util.KEY_dateItemAdded)))
                        .getTime()); // Feb 23, 2020
                item.setDataItemAdded(formattedDate);

                //Add to arraylist
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        return itemList;

    }

    //Todo: Add updateItem
    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_itemName, item.getItemName());
        values.put(Util.KEY_itemColor, item.getItemColor());
        values.put(Util.KEY_itemQuantity, item.getItemQuantity());
        values.put(Util.KEY_itemSize, item.getItemSize());
        values.put(Util.KEY_dateItemAdded, java.lang.System.currentTimeMillis());//timestamp of the system

        //update row
        return db.update(Util.TABLE_NAME, values,
                Util.KEY_Id + "=?",
                new String[]{String.valueOf(item.getId())});

    }

    //Todo: Add Delete Item
    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Util.TABLE_NAME,
                Util.KEY_Id + "=?",
                new String[]{String.valueOf(id)});

        //close
        db.close();

    }

    //Todo: getItemCount
    public int getItemsCount() {
        String countQuery = "SELECT * FROM " + Util.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();

    }

}