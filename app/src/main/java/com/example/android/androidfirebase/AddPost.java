package com.example.android.androidfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddPost extends AppCompatActivity {


    DatabaseReference dbPost, dbCategory;

    String categoriesSelected = "";
    EditText titleTxt, contentTxt;
    List<CheckBox> categoryCheckBox = new ArrayList<>();

    ArrayList<Category> categories = new ArrayList<>();
    LinearLayout checkBoxContainer;
    ScrollView scrollView;

    CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);

        titleTxt = findViewById(R.id.titleTxt);
        contentTxt = findViewById(R.id.contentTxt);
        checkBoxContainer = findViewById(R.id.checkBoxContainer);
        scrollView = findViewById(R.id.scrollView);

        dbCategory = FirebaseDatabase.getInstance().getReference("category");
        dbPost = FirebaseDatabase.getInstance().getReference("post");

    }

    @Override
    protected void onStart() {
        super.onStart();

        dbCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categories.clear();
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    Category category = categorySnapshot.getValue(Category.class);
                    categories.add(category);
                }
                onCheckboxClicked();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

    }

    public void onCheckboxClicked() {
        /*Creates Checkbox dynamically*/
        for(int i = 0; i<categories.size(); i++){
            checkBox = new CheckBox(getApplicationContext());
            checkBox.setText(categories.get(i).getCname());
            checkBoxContainer.addView(checkBox);
            categoryCheckBox.add(checkBox);

        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoriesSelected = "";
                for (int j = 0; j <categories.size(); j++){
                    if(categoryCheckBox.get(j).isChecked())
                        categoriesSelected += categories.get(j).getCname() + ",";
                }
            }
        });
    }

    public void addCategory(View v){
        Intent intent = new Intent(this, AddCategory.class);
        startActivityForResult(intent, 2);
    }

    public void addPost(View v) {
        String title = titleTxt.getText().toString();
        String content = contentTxt.getText().toString();

        if (!(TextUtils.isEmpty(title) && TextUtils.isEmpty(content))) {
            String id = dbPost.push().getKey();


            Post post = new Post(id, title, categoriesSelected, new Date(), content);
            dbPost.child(id).setValue(post);

            titleTxt.setText("");
            contentTxt.setText("");

            Toast.makeText(this, "Post Added", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Title and content are required", Toast.LENGTH_LONG).show();
        }
    }

    public void Back(View v){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}

