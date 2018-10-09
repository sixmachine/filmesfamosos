package br.com.arthursena.filmesfamosos;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickListener {

    private MovieAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv_movies);
        progressBar = findViewById(R.id.pb_loading_indicator);

        if(savedInstanceState == null){
            link = String.format("%s%s",getString(R.string.link_popular), getString(R.string.api_key));
        }
        else {
            link = savedInstanceState.getString("apiKey");
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        progressBar.setVisibility(View.VISIBLE);
        executarBuscaPorFilmes();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("apiKey", link);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(R.id.menu_melhor_avaliacao == item.getItemId()){
            link = String.format("%s%s",getString(R.string.link_top_rated), getString(R.string.api_key));
            executarBuscaPorFilmes();
            return true;
        }
        else if(R.id.menu_popular == item.getItemId()){
            link = String.format("%s%s",getString(R.string.link_popular), getString(R.string.api_key));
            executarBuscaPorFilmes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void executarBuscaPorFilmes(){
        try {
            new MovieDbAsyncTask().execute(new URL(link));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickFilme(MovieDb filme) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("filme", filme);
        startActivity(intent);
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
            adapter = new MovieAdapter(MainActivity.this, response.getResults(), MainActivity.this);
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.INVISIBLE);
            super.onPostExecute(response);
        }
    }
}
