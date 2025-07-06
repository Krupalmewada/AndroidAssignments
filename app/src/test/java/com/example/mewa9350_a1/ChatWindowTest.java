package com.example.mewa9350_a1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ChatWindowTest {

    private Context context;
    private ChatDatabaseHelper dbHelper;
    private SQLiteDatabase database;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        context.deleteDatabase("Messages.db");

        dbHelper = new ChatDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    @After
    public void tearDown() {
        if (database != null && database.isOpen()) database.close();
        if (dbHelper != null) dbHelper.close();
        context.deleteDatabase("Messages.db");
    }

    @Test
    public void testDatabaseCreation() {
        assertNotNull(database);
        assertTrue(database.isOpen());
        assertFalse(database.isReadOnly());
    }

    @Test
    public void testTableExists() {
        Cursor cursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                new String[]{ChatDatabaseHelper.TABLE_NAME});
        assertTrue(cursor.moveToFirst());
        assertEquals(ChatDatabaseHelper.TABLE_NAME, cursor.getString(0));
        cursor.close();
    }

    @Test
    public void testTableSchema() {
        Cursor cursor = database.rawQuery("PRAGMA table_info(" + ChatDatabaseHelper.TABLE_NAME + ")", null);

        boolean hasId = false;
        boolean hasMessage = false;

        while (cursor.moveToNext()) {
            String column = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            if (ChatDatabaseHelper.KEY_ID.equals(column)) hasId = true;
            if (ChatDatabaseHelper.KEY_MESSAGE.equals(column)) hasMessage = true;
        }

        assertTrue("Table should have _id column", hasId);
        assertTrue("Table should have message column", hasMessage);

        cursor.close();
    }

    @Test
    public void testInsertAndRetrieveMessage() {
        String testMessage = "Test message for DB";

        ContentValues values = new ContentValues();
        values.put(ChatDatabaseHelper.KEY_MESSAGE, testMessage);

        long id = database.insert(ChatDatabaseHelper.TABLE_NAME, null, values);
        assertTrue(id > 0);

        Cursor cursor = database.query(ChatDatabaseHelper.TABLE_NAME,
                new String[]{ChatDatabaseHelper.KEY_MESSAGE},
                ChatDatabaseHelper.KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        assertTrue(cursor.moveToFirst());
        String messageFromDb = cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_MESSAGE));
        assertEquals(testMessage, messageFromDb);

        cursor.close();
    }

    @Test
    public void testInsertEmptyMessage() {
        ContentValues values = new ContentValues();
        values.put(ChatDatabaseHelper.KEY_MESSAGE, "");

        long id = database.insert(ChatDatabaseHelper.TABLE_NAME, null, values);
        assertTrue(id > 0);
    }

    @Test
    public void testInsertNullMessage() {
        ContentValues values = new ContentValues();
        values.putNull(ChatDatabaseHelper.KEY_MESSAGE);

        long id = database.insert(ChatDatabaseHelper.TABLE_NAME, null, values);
        assertTrue(id > 0);
    }

    @Test
    public void testAutoIncrementId() {
        ContentValues values1 = new ContentValues();
        values1.put(ChatDatabaseHelper.KEY_MESSAGE, "Message one");
        long id1 = database.insert(ChatDatabaseHelper.TABLE_NAME, null, values1);

        ContentValues values2 = new ContentValues();
        values2.put(ChatDatabaseHelper.KEY_MESSAGE, "Message two");
        long id2 = database.insert(ChatDatabaseHelper.TABLE_NAME, null, values2);

        assertTrue(id2 > id1);
    }

    @Test
    public void testDatabaseUpgradeClearsData() {
        ContentValues values = new ContentValues();
        values.put(ChatDatabaseHelper.KEY_MESSAGE, "Before upgrade");
        database.insert(ChatDatabaseHelper.TABLE_NAME, null, values);

        dbHelper.onUpgrade(database, 1, 2);

        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + ChatDatabaseHelper.TABLE_NAME, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        assertEquals(0, count);
    }

    @Test
    public void testCursorLogsColumnInfo() {
        // Insert some data
        ContentValues values = new ContentValues();
        values.put(ChatDatabaseHelper.KEY_MESSAGE, "Check cursor");
        database.insert(ChatDatabaseHelper.TABLE_NAME, null, values);

        Cursor cursor = database.query(ChatDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

        // Just check that column count is > 0 and columns have expected names
        assertTrue(cursor.getColumnCount() >= 2);
        boolean hasId = false, hasMessage = false;
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            String colName = cursor.getColumnName(i);
            if (colName.equals(ChatDatabaseHelper.KEY_ID)) hasId = true;
            if (colName.equals(ChatDatabaseHelper.KEY_MESSAGE)) hasMessage = true;
        }
        assertTrue(hasId);
        assertTrue(hasMessage);

        cursor.close();
    }

    @Test
    public void testChatDatabaseHelperConstants() {
        assertEquals("messages", ChatDatabaseHelper.TABLE_NAME);
        assertEquals("_id", ChatDatabaseHelper.KEY_ID);
        assertEquals("message", ChatDatabaseHelper.KEY_MESSAGE);
    }

    @Test
    public void testChatDatabaseHelperConstructor() {
        ChatDatabaseHelper helper = new ChatDatabaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        assertTrue(db.isOpen());
        db.close();
        helper.close();
    }
}
