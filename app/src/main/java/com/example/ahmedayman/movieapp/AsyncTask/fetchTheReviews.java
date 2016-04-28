package com.example.ahmedayman.movieapp.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ahmedayman.movieapp.BuildConfig;
import com.example.ahmedayman.movieapp.Items.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eng AhmedAyman on 4/12/2016.
 */
public class fetchTheReviews extends AsyncTask<String, Void, String > {



    Context context ;
    List<String> reviews = new ArrayList<String>();
    String JsonResponse ;
    List<Review> bothArrays =new ArrayList<>();
    String [] data ;

    private StringTransfer tr ;

    public fetchTheReviews(StringTransfer ia )
    {
        this.tr=ia;
    }



    @Override
    protected String doInBackground(String... params) {

        if (params.length == 0) {
         Log.v ("No" ,"ID");
            return null;
        }
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;




        try {
            String baseUrl = "http://api.themoviedb.org/3/movie/";
            String id = params[0];
            String typeOfData = "/reviews";
            String apiKey = "?api_key=" + BuildConfig.MyApi;
            String link = baseUrl + id + typeOfData + apiKey;
            URL url = new URL(link);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream stream = urlConnection.getInputStream();
            StringBuffer json = new StringBuffer();


            if (stream == null) {
                  return null;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(stream));

            String line ;
            while ((line = bufferedReader.readLine()) !=null)
            {
                json.append(line+"\n");
            }

            if (json.length() == 0)
                return null ;


            JsonResponse = json.toString() ;
            Log.v("REVEIWS",JsonResponse);

        } catch (IOException e) {
            Log.e ("Error with Reviews","------>Error");
        }
        finally {
            if (urlConnection!= null)
            {
                urlConnection.disconnect();
            }
            if(bufferedReader!= null)
            {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Error with Reviews", "Error closing stream", e);

                }
            }
        }//finally check if not null to close

     return  JsonResponse ;
      }
 @Override
    protected void onPostExecute(String  json) {
        super.onPostExecute(json);
     List<Review>  rev = null ;
     try {
         rev = getJson(json);
tr.transferReviews(rev);
     } catch (JSONException e) {
         e.printStackTrace();
     }
 }
    private List<Review> getJson (String JsonResponse) throws JSONException {


        JSONObject data = new JSONObject(JsonResponse);
        JSONArray results    = data.getJSONArray("results");
        for (int i = 0 ; i < results.length(); i++)
        {
            JSONObject review = results.getJSONObject(i);
            Review review1 = new Review();
            review1.setAuthor(review.getString("author"));
            review1.setContent(review.getString("content"));
            bothArrays.add(review1);

        }

        return bothArrays ;
    }


}
