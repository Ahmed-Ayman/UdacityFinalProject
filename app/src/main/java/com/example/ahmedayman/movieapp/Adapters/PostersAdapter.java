package com.example.ahmedayman.movieapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.ahmedayman.movieapp.Items.Movie;
import com.example.ahmedayman.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ahmed Ayman on 27-Apr-16.
 */

public class
PostersAdapter extends BaseAdapter {

    private final Context mContext;
  //  private final LayoutInflater inflater;

    private final Movie mMovie = new Movie();

    private List<Movie> mObjects;

    public PostersAdapter(Context context, List<Movie> objects) {
        mContext = context;
      //  inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mObjects = objects;
    }

    public Context getContext() {
        return mContext;
    }

    public void add(Movie object) {
        synchronized (mMovie) {
            mObjects.add(object);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mMovie) {
            mObjects.clear();
        }
        notifyDataSetChanged();
    }

    public void setData(List<Movie> data) {
        clear();
        for (Movie movie : data) {
            add(movie);
        }
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Movie getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_item_movie, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        final Movie movie = getItem(position);
        String image_url = "http://image.tmdb.org/t/p/w500" + movie.getImage();
        viewHolder = (ViewHolder) view.getTag();
        Picasso.with(getContext()).load(image_url).into(viewHolder.imageView)  ;

        return view;
    }
    public static class ViewHolder {
        public final ImageView imageView;
     public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.grid_item_image);
        }
    }
}


