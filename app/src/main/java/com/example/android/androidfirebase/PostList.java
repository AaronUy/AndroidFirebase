package com.example.android.androidfirebase;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

public class PostList extends ArrayAdapter<Post> {
    private Activity context;
    List<Post> posts;
    final Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public PostList(Activity context, List<Post> posts) {
        super(context, R.layout.post_list, posts);
        this.context = context;
        this.posts= posts;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.post_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.titleTxt);
        TextView textViewCategory = (TextView) listViewItem.findViewById(R.id.categoryTxt);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.dateTxt);

        Post post = posts.get(position);

        textViewName.setText(post.getTitle());
        textViewCategory.setText(post.getCategory());
        textViewDate.setText(formatter.format(post.getDate()));

        return listViewItem;
    }
}
