package br.com.arthursena.filmesfamosos;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnClickListener {

    private MovieAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_movies);
        progressBar = findViewById(R.id.pb_loading_indicator);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        progressBar.setVisibility(View.VISIBLE);

        try {
            new MovieDbAsyncTask().execute(new URL("http://api.themoviedb.org/3/movie/popular?api_key=0ce8f1033d6adc7627f96b9841686205"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public class MovieDbAsyncTask extends AsyncTask<URL, Void, MovieDbResponse> {

        @Override
        protected MovieDbResponse doInBackground(URL... urls) {

            InputStreamReader reader = null;
            try {
                reader = new InputStreamReader(urls[0].openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new Gson().fromJson(reader, MovieDbResponse.class);
        }

        @Override
        protected void onPostExecute(MovieDbResponse response) {
            adapter = new MovieAdapter(MainActivity.this, response.getResults(), null);
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.INVISIBLE);
            super.onPostExecute(response);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
