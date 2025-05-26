package com.example.mewa9350_a1;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ListItemsActivity extends AppCompatActivity {


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                android.graphics.Bitmap imageBitmap = (android.graphics.Bitmap) extras.get("data");
                ImageButton imgButton = findViewById(R.id.imageButton);
                imgButton.setImageBitmap(imageBitmap);
            } else {
                Toast.makeText(this, "No image captured", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.i(TAG, "inside onCreate") ;
        ImageButton imgButton = findViewById(R.id.imageButton);

        imgButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        10);
            } else {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 10);
                } else {
                    Toast.makeText(this, "No camera app found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RadioButton radioButton = findViewById(R.id.radioButton);
        radioButton.setOnClickListener(v -> {
            Log.i(TAG,"RadioButton clicked");
            print("RadioButton clicked: " + radioButton.getText());
        });


        Switch mySwitch = findViewById(R.id.mySwitch);

        mySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            CharSequence text;
            int duration;

            if (isChecked) {
                text = "Switch is On";
                duration = Toast.LENGTH_SHORT;
            } else {
                text = "Switch is Off";
                duration = Toast.LENGTH_LONG;
            }

            Toast toast = Toast.makeText(ListItemsActivity.this, text, duration);
            toast.show();
//                print("Switch toggled: " + text);
        });

        CheckBox myCheckbox = findViewById(R.id.myCheckbox);

        myCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);

                builder.setTitle(R.string.dialog_title)
                        .setMessage(R.string.dialog_message)
                        .setPositiveButton(R.string.ok, (dialog, id) -> {
                            // ✅ Return intent and finish activity
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("Response", "Here is my response");
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        })
                        .setNegativeButton(R.string.cancel, (dialog, id) -> {
                            myCheckbox.setChecked(false);
                            dialog.dismiss();
                        })
                        .show();
            }
        });


    }
    public void print(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "inside onResume") ;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "inside onStart") ;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "inside onPause") ;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "inside onStop") ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "inside onDestroy") ;
    }
}