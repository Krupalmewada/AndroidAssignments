package com.example.mewa9350_a1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    // Static variables for database configuration
    private static final String DATABASE_NAME = "Messages.db";
    private static final int VERSION_NUM = 2;

    // Table and column names (making TABLE_NAME public as required in Step 2)
    public static final String TABLE_NAME = "messages";
    public static final String KEY_ID = "_id";
    public static final String KEY_MESSAGE = "message";

    // Constructor that opens the database file "Messages.db"
    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    // Step 3: onCreate with required logging message
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Step 3: Add Log.i() message as specified
        Log.i("ChatDatabaseHelper", "Calling onCreate");

        // Create table with id column (auto-increment integers) and MESSAGE column (strings)
        String createTableSQL = "CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_MESSAGE + " TEXT)";

        db.execSQL(createTableSQL);
    }

    // Step 3: onUpgrade with required logging message
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Step 3: Add Log.i() message with exact format as specified
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);

        // Execute SQL statement to drop table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Recreate database by calling onCreate(db)
        onCreate(db);
    }
}