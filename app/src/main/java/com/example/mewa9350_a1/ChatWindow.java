package com.example.mewa9350_a1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    // Step 1: Add ACTIVITY_NAME constant for logging
    private static final String ACTIVITY_NAME = "ChatWindow";

    // Step 1: Add database instance variables
    private SQLiteDatabase database; // Store as instance variable as required

    private EditText textInput;
    private ArrayList<String> messages;
    private ChatAdapter messageAdapter;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chat Window");
        }

        ListView listView = findViewById(R.id.list_view);
        textInput = findViewById(R.id.message_input);
        Button sendButton = findViewById(R.id.button_send);
        messages = new ArrayList<>();

        // Step 1: Create temporary ChatDatabaseHelper object and get writable database
        ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(this);
        database = dbHelper.getWritableDatabase(); // Store as instance variable

        // Step 1: Execute query for existing chat messages and add to ArrayList
        // Option 1: Using query() method
        Cursor cursor = database.query(
                ChatDatabaseHelper.TABLE_NAME,
                new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},
                null, null, null, null, null
        );

        // Alternative Option 2: Using rawQuery() method
        // Cursor cursor = database.rawQuery("SELECT * FROM " + ChatDatabaseHelper.TABLE_NAME, null);

        // Step 1: Print cursor information
        Log.i(ACTIVITY_NAME, "Cursor's column count = " + cursor.getColumnCount());

        // Step 1: Use for loop to print column names
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, "Column " + i + ": " + cursor.getColumnName(i));
        }

        // Step 1: Move to first position and loop through messages
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // Step 1: Add Log.i() message for each retrieved message (exact format as required)
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));

            // Add message to ArrayList
            String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
            messages.add(message);

            cursor.moveToNext();
        }
        cursor.close();

        messageAdapter = new ChatAdapter(this, messages);
        listView.setAdapter(messageAdapter);

        // Step 2: Modify sendButton's onClickListener to insert into database
        sendButton.setOnClickListener(v -> {
            String text = textInput.getText().toString();
            if (!text.isEmpty()) {
                // Add message to ArrayList (existing functionality)
                messages.add(text);

                // Step 2: Insert new message into database using ContentValues
                ContentValues values = new ContentValues();
                values.put(ChatDatabaseHelper.KEY_MESSAGE, text);

                // Insert into database (ID will auto-increment, so we don't insert it)
                long newRowId = database.insert(ChatDatabaseHelper.TABLE_NAME, null, values);
                Log.i(ACTIVITY_NAME, "Message inserted with row ID: " + newRowId);

                messageAdapter.notifyDataSetChanged();
                textInput.setText("");
            }
        });
    }

    // Step 4: Implement onDestroy() function
    @Override
    protected void onDestroy() {
        // Close the database that was opened in onCreate()
        if (database != null && database.isOpen()) {
            database.close();
            Log.i(ACTIVITY_NAME, "Database closed in onDestroy");
        }

        // Call the super() version of the function
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ChatAdapter extends ArrayAdapter<String> {
        private final ArrayList<String> localMessages;

        public ChatAdapter(Context ctx, ArrayList<String> messages) {
            super(ctx, 0, messages);
            this.localMessages = messages;
        }

        @Override
        public int getCount() {
            return localMessages.size();
        }

        @Override
        public String getItem(int position) {
            return localMessages.get(position);
        }

        @SuppressLint("InflateParams")
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result;

            if (position % 2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView messageText = result.findViewById(R.id.message_text);
            messageText.setText(getItem(position));

            return result;
        }
    }
}