package com.example.mewa9350_a1;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class TestToolbar extends AppCompatActivity {

    private String snackbarMessage = "You selected item 1";
    private Toolbar toolbar; // Declare globally

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        toolbar = findViewById(R.id.toolbar); // Initialize globally declared toolbar
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                Snackbar.make(view, "Custom FAB Message", Snackbar.LENGTH_LONG).show()
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_one) {
            Snackbar.make(toolbar, snackbarMessage, Snackbar.LENGTH_LONG).show();
            return true;

        } else if (id == R.id.action_two) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dialog_title)
                    .setPositiveButton(R.string.ok, (dialog, which) -> finish())
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
            builder.create().show();
            return true;

        } else if (id == R.id.action_three) {
            AlertDialog.Builder customDialog = new AlertDialog.Builder(this);
            customDialog.setTitle("New Message");
            final EditText input = new EditText(this);
            customDialog.setView(input);
            customDialog.setPositiveButton("OK", (dialog, which) -> snackbarMessage = input.getText().toString());
            customDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            customDialog.show();
            return true;

        } else if (id == R.id.about) {
            Toast.makeText(this, "Version 1.0, by Krupal Mewada", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
