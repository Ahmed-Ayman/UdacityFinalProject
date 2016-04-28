package com.example.ahmedayman.movieapp.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ahmedayman.movieapp.BuildConfig;

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

/**
 * Created by Eng AhmedAyman on 4/12/2016.
 */
public class fetchTheTrailers extends AsyncTask<String , Void , String >{

    ArrayList<String> keys = new ArrayList<>();
    private interfaceAsApreclient interfac ;

    public fetchTheTrailers(interfaceAsApreclient ia )
    {
        this.interfac=ia;
    }

    @Override
    protected String doInBackground(String... params) {

        if (params.length==0)
            return null;

        HttpURLConnection urlConnection= null ;
        BufferedReader bufferedReader = null ;
        String JsonResponse = null ;

       String baseUrl = "http://api.themoviedb.org/3/movie/";
        String id = params[0];
        String theRemaining ="/videos?api_key="+ BuildConfig.MyApi;
        try
        {
        String finalUrl = baseUrl+id+theRemaining;
            URL url = new URL(finalUrl);
            urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream read = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (read == null)
                return  null ;

            bufferedReader = new BufferedReader(new InputStreamReader(read));

            String line = bufferedReader.readLine() ;
            if (line!=null)
            {
                buffer.append(line+"\n");
            }

            JsonResponse = buffer.toString();

        }
 catch (IOException e) {
            e.printStackTrace();
        }
finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (bufferedReader != null)
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return JsonResponse;
    }
    @Override
    protected void onPostExecute(String jsonResponse) {
        super.onPostExecute(jsonResponse);
      Log.e("JSON1",jsonResponse);
        ArrayList<String> tr = null;
        try {
           tr=  getTrailers(jsonResponse);
            interfac.transfer(tr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private ArrayList<String> getTrailers (String jsonResp) throws JSONException {

        JSONObject object = new JSONObject(jsonResp);
        JSONArray results = object.getJSONArray("results");
        for (int i = 0 ; i < results.length();i++)
        {
            JSONObject trail = results.getJSONObject(i);
            String key =trail.getString("key");
            keys.add(key);
        }
        return keys;
    }
}
