package com.example.android.androidfirebase;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

public class PostList extends ArrayAdapter<Post> implements Filterable {
    private Activity context;
    List<Post> posts;
    List<Post> postsResults;
    final Format dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                posts = (List<Post>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < posts.size(); i++) {
                    Post post = posts.get(i);
                    if (post.getCategory().toLowerCase().contains(constraint.toString()) || dateFormat.format(post.getDate()).contains(constraint.toString()))  {
                        postsResults.add(post);
                    }
                }

                results.count = postsResults.size();
                results.values = postsResults;

                return results;
            }
        };

        return filter;
    }
}
