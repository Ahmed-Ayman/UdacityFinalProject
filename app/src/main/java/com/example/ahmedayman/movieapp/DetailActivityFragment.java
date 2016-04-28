package com.example.ahmedayman.movieapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedayman.movieapp.AsyncTask.StringTransfer;
import com.example.ahmedayman.movieapp.AsyncTask.fetchTheReviews;
import com.example.ahmedayman.movieapp.AsyncTask.fetchTheTrailers;
import com.example.ahmedayman.movieapp.AsyncTask.interfaceAsApreclient;
import com.example.ahmedayman.movieapp.DataBase.MovieContract;
import com.example.ahmedayman.movieapp.Items.Movie;
import com.example.ahmedayman.movieapp.Items.Review;
import com.example.ahmedayman.movieapp.Items.Trailer;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements interfaceAsApreclient,StringTransfer {
    public static final String TAG = DetailActivityFragment.class.getSimpleName();
    static final String DETAIL_MOVIE = "DETAIL_MOVIE";
    private Movie mMovie;
    private ImageView secondImage;
    private TextView MovieName;
    private TextView overView;
    private TextView releasedDate;
    private TextView voting;
    private ScrollView detailsLayout;
    private Toast mToast;
    private Trailer trailer;
        String [] data ;
    Button reviews ;
    Button trailers ;
    ArrayList<String> links = new ArrayList<>();

    String [] linksArray  ,  titles;

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mMovie != null) {
            inflater.inflate(R.menu.menu_fragment_detail, menu);

            final MenuItem favorite = menu.findItem(R.id.action_favorite);

            new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... params) {
                    return Utility.isFavorited(getActivity(), mMovie.getId());
                }

                @Override
                protected void onPostExecute(Integer isFavorited) {
                    favorite.setIcon(isFavorited == 1 ? R.drawable.abc_btn_rating_star_on_mtrl_alpha : R.drawable.abc_btn_rating_star_off_mtrl_alpha);
                }
            }.execute();
        }

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_favorite:
                if (mMovie != null) {
                    // check if movie is in favorites or not
                    new AsyncTask<Void, Void, Integer>() {
                        @Override
                        protected Integer doInBackground(Void... params) {
                            return Utility.isFavorited(getActivity(), mMovie.getId());
                        }

                        @Override
                        protected void onPostExecute(Integer isFavorited) {
                            // if it is in favorites
                            if (isFavorited == 1) {
                                // delete from favorites
                                new AsyncTask<Void, Void, Integer>() {
                                    @Override
                                    protected Integer doInBackground(Void... params) {
                                        return getActivity().getContentResolver().delete(
                                                MovieContract.MovieData.CONTENT_URI,
                                                MovieContract.MovieData.COLUMN_MOVIE_ID + " = ?",
                                                new String[]{Integer.toString(mMovie.getId())}
                                        );
                                    }
                               @Override
                                    protected void onPostExecute(Integer rowsDeleted) {
                                        item.setIcon(R.drawable.abc_btn_rating_star_off_mtrl_alpha);
                                        if (mToast != null) {
                                            mToast.cancel();
                                        }
                                        mToast = Toast.makeText(getActivity(),"Removed From Favorites", Toast.LENGTH_SHORT);
                                        mToast.show();
                                    }
                                }.execute();
                            } else {
                                new AsyncTask<Void, Void, Uri>() {
                                    @Override
                                    protected Uri doInBackground(Void... params) {
                                        ContentValues values = new ContentValues();
                                        values.put(MovieContract.MovieData.COLUMN_MOVIE_ID, mMovie.getId());
                                        values.put(MovieContract.MovieData.COLUMN_TITLE, mMovie.getTitle());
                                        values.put(MovieContract.MovieData.COLUMN_IMAGE, mMovie.getImage());
                                        values.put(MovieContract.MovieData.COLUMN_IMAGE_COPY, mMovie.getImage2());
                                        values.put(MovieContract.MovieData.COLUMN_OVERVIEW, mMovie.getOverview());
                                        values.put(MovieContract.MovieData.COLUMN_RATING, mMovie.getRating());
                                        values.put(MovieContract.MovieData.COLUMN_DATE, mMovie.getDate());
                                        return getActivity().getContentResolver().insert(MovieContract.MovieData.CONTENT_URI,
                                                values);
                                    }

                                    @Override
                                    protected void onPostExecute(Uri returnUri) {
                                        item.setIcon(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
                                        if (mToast != null) {
                                            mToast.cancel();
                                        }
                                        mToast = Toast.makeText(getActivity(), "Added to Favorites", Toast.LENGTH_SHORT);
                                        mToast.show();
                                    }
                                }.execute();
                            }
                        }
                    }.execute();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovie = arguments.getParcelable(DetailActivityFragment.DETAIL_MOVIE);
        }
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        detailsLayout = (ScrollView) view.findViewById(R.id.detail_layout);
        if (mMovie != null) {
            detailsLayout.setVisibility(View.VISIBLE);
        } else {
            detailsLayout.setVisibility(View.INVISIBLE);
        }
        secondImage = (ImageView) view.findViewById(R.id.movie_sec_image);
        MovieName = (TextView) view.findViewById(R.id.movie_name);
        overView = (TextView) view.findViewById(R.id.describtion);
        releasedDate = (TextView) view.findViewById(R.id.released_date);
        voting = (TextView) view.findViewById(R.id.voting);
        reviews = (Button)view.findViewById(R.id.go_to_review);
        trailers = (Button)view.findViewById(R.id.trailers);

        if (mMovie != null) {

            String image_url = Utility.buildImageUrl(342, mMovie.getImage());
            Picasso.with(getContext()).load(image_url).into(secondImage)  ;
            MovieName.setText(mMovie.getTitle());
            overView.setText(mMovie.getOverview());

            String movie_date = mMovie.getDate();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            try {
                String date = DateUtils.formatDateTime(getActivity(),
                        formatter.parse(movie_date).getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
                releasedDate.setText(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            fetchTheTrailers videos = new fetchTheTrailers(this);
            videos.execute(Integer.toString(mMovie.getId()));
            fetchTheReviews review = new  fetchTheReviews(this);
            review.execute(Integer.toString(mMovie.getId()));

            reviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle("Reviews");
                    dialog.setItems(data, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.create().show();
                }
            });

            trailers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    dialogBuilder.setTitle("Available Trailers");
                    dialogBuilder.setItems(titles,new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(linksArray[which])));

                        }
                    });
                    dialogBuilder.create().show();

                }
            });

            voting.setText(Integer.toString(mMovie.getRating()));
        }

        return view;
    }

    @Override
    public void transferReviews(List<Review> reviews) {
        data = new String [reviews.size()] ;
        for ( int i = 0 ; i < reviews.size() ; i++)
        {
            data[i] =(i+1)+" - "+reviews.get(i).getAuthor() +"\n " +reviews.get(i).getContent() +"\n \n ---------------------------------------------------------- \n";
        }
    }
    @Override
    public void transfer(ArrayList<String> arr) {
        titles = new String[arr.size()];
        for (int i = 0 ; i <arr.size();i++)
        {
            links .add( "https://www.youtube.com/watch?v="+arr.get(i));
            titles[i] ="Trailer #"+(i+1);
        }
        linksArray = new String[links.size()];
        links.toArray(linksArray);
    }
}
