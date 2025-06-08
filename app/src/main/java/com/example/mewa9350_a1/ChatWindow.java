package com.example.mewa9350_a1;
//
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
//

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    private EditText textInput;
    private ArrayList<String> messages;
    private ChatAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
            getSupportActionBar().setTitle("Chat Window"); // set title
        }


        ListView listView = findViewById(R.id.list_view);
        textInput = findViewById(R.id.message_input);
        Button sendButton = findViewById(R.id.button_send);
        messages = new ArrayList<>();

        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(v -> {
            String text = textInput.getText().toString();
            if (!text.isEmpty()) {
                messages.add(text);
                messageAdapter.notifyDataSetChanged();
                textInput.setText("");
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // go back to MainActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public String getItem(int position) {
            return messages.get(position);
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
