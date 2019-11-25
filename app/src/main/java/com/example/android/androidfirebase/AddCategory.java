package com.example.android.androidfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class AddCategory extends AppCompatActivity {

    EditText categoryTxt;
    DatabaseReference dbCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);
        dbCategory = FirebaseDatabase.getInstance().getReference("category");
        categoryTxt = findViewById(R.id.categoryTxt);
    }


    public void addCategory(View v) {
        String categoryName = categoryTxt.getText().toString();
        Date date = new Date();
        if (!TextUtils.isEmpty(categoryName)) {
            String id = dbCategory.push().getKey();
            Category category = new Category(id, categoryName, date);
            dbCategory.child(id).setValue(category);

            categoryTxt.setText("");

            Toast.makeText(this, "Category added", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,AddPost.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, "Please enter a category name", Toast.LENGTH_LONG).show();
        }
    }

    public void Back(View v){
        Intent intent = new Intent(this,AddPost.class);
        startActivity(intent);
        finish();
    }
}
