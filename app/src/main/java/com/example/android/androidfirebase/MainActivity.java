package com.example.android.androidfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String POST_ID= "POST_ID";
    public static final String POST_CATEGORY = "POST_CATEGORY";
    public static final String POST_CONTENT = "POST_CONTENT";
    public static final String POST_TIME = "POST_TIME";


    ListView listViewPosts;
    DatabaseReference dbPost, dbCategory;
    EditText filterTxt;

    final Format dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    List<Post> posts;

    Spinner spinner;
    PostList postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewPosts = findViewById(R.id.postsLv);
        filterTxt = findViewById(R.id.filterTxt);

        dbCategory = FirebaseDatabase.getInstance().getReference("category");
        dbPost = FirebaseDatabase.getInstance().getReference("post");

        posts = new ArrayList<>();

        spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filter_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        listViewPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Post post = posts.get(i);
                String timestamp_string = dateFormat.format(post.getDate());
                Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                intent.putExtra(POST_ID,post.getId());
                intent.putExtra("POST_TITLE", post.getTitle());
                intent.putExtra(POST_CONTENT,post.getContent());
                intent.putExtra(POST_CATEGORY,post.getCategory());
                intent.putExtra(POST_TIME, timestamp_string);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        dbPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    posts.add(post);
                }

                postAdapter = new PostList(MainActivity.this, posts);
                listViewPosts.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Date"))
                {
                    filterTxt.setHint("yyyy-MM-dd");
                    filterTxt.setOnKeyListener(new View.OnKeyListener() {
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            // If the event is a key-down event on the "enter" button
                            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                // Perform action on key press
                                postAdapter.getFilter().filter(filterTxt.getText());
                                return true;
                            }
                            return false;
                        }
                    });
                }
                if(selectedItem.equals("Category"))
                {
                    filterTxt.setHint("Enter Category");
                    filterTxt.setOnKeyListener(new View.OnKeyListener() {
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            // If the event is a key-down event on the "enter" button
                            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                postAdapter.getFilter().filter(filterTxt.getText());
                                return true;
                            }
                            return false;
                        }
                    });
                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }
    public void addPost(View v){
        Intent intent = new Intent(this, AddPost.class);
        startActivity(intent);
    }

    public class TimeStampComparator implements Comparator<Post> {
        @Override
        public int compare(Post earlypost, Post latepost) {
            return latepost.getDate().compareTo(earlypost.getDate());
        }

    }
    public void sort(View v){

    }
}
