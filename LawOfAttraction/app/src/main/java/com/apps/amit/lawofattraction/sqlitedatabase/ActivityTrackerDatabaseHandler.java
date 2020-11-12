package com.apps.amit.lawofattraction.sqlitedatabase;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apps.amit.lawofattraction.utils.ManifestationTrackerUtils;

public class ActivityTrackerDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";

    public ActivityTrackerDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createContactsTable = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";
        db.execSQL(createContactsTable);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new manifestationTrackerUtils
    public void addContact(ManifestationTrackerUtils manifestationTrackerUtils) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, manifestationTrackerUtils.getName()); // ManifestationTrackerUtils Name
        values.put(KEY_PH_NO, manifestationTrackerUtils.getPhoneNumber()); // ManifestationTrackerUtils Phone

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // Getting single contact
    ManifestationTrackerUtils getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return new ManifestationTrackerUtils(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
    }

    // Getting All Contacts
    public List<ManifestationTrackerUtils> getAllContacts() {
        List<ManifestationTrackerUtils> manifestationTrackerUtilsList = new ArrayList<>();


        // Select All Query
        String selectQuery = " SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ManifestationTrackerUtils manifestationTrackerUtils = new ManifestationTrackerUtils();
                manifestationTrackerUtils.setID(Integer.parseInt(cursor.getString(0)));
                manifestationTrackerUtils.setName(cursor.getString(1));
                manifestationTrackerUtils.setPhoneNumber(cursor.getString(2));
                // Adding manifestationTrackerUtils to list
                manifestationTrackerUtilsList.add(manifestationTrackerUtils);
            } while (cursor.moveToNext());
        }

        // return contact list
        return manifestationTrackerUtilsList;
    }

    // Updating single manifestationTrackerUtils
    public int updateContact(ManifestationTrackerUtils manifestationTrackerUtils) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, manifestationTrackerUtils.getName());
        values.put(KEY_PH_NO, manifestationTrackerUtils.getPhoneNumber());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(manifestationTrackerUtils.getID()) });
    }

    // Deleting single manifestationTrackerUtils
    public void deleteContact(ManifestationTrackerUtils manifestationTrackerUtils) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(manifestationTrackerUtils.getID()) });
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void removeDuplicates() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "DELETE FROM "+TABLE_CONTACTS+" WHERE "
                +KEY_ID+" NOT IN ( SELECT MIN("+KEY_ID+") FROM "+TABLE_CONTACTS+" GROUP BY "+KEY_NAME+", "+KEY_PH_NO+")";

        db.execSQL(selectQuery);

    }
}