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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String POST_ID= "POST_ID";
    public static final String POST_CATEGORY = "POST_CATEGORY";
    public static final String POST_CONTENT = "POST_CONTENT";
    public static final String POST_TIME = "POST_TIME";


    ListView listViewPosts;
    DatabaseReference dbPost, dbCategory;

    final Format dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    List<Post> posts;
    List<Category> categories;

    Spinner spinner, spinCats;
    PostList postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewPosts = findViewById(R.id.postsLv);

        dbCategory = FirebaseDatabase.getInstance().getReference("category");
        dbPost = FirebaseDatabase.getInstance().getReference("post");

        posts = new ArrayList<>();
        categories = new ArrayList<>();


        spinner = (Spinner) findViewById(R.id.spinner);
        spinCats = findViewById(R.id.spinCats);
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

        dbCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categories.clear();
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    Category category = categorySnapshot.getValue(Category.class);
                    categories.add(category);
                }

                ArrayList<String> names = new ArrayList<>();
                for(Category category: categories)
                    names.add(category.getCname());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, names);
                adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                spinCats.setAdapter(adapter);
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
                    spinCats.setEnabled(false);
                    listViewPosts.setAdapter(postAdapter);
                }
                if(selectedItem.equals("Category"))
                {
                    spinCats.setEnabled(true);
                    spinCats.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedItem = parent.getItemAtPosition(position).toString();
                            postAdapter.getFilter().filter(selectedItem);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

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
