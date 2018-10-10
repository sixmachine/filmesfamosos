package br.com.arthursena.filmesfamosos;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickListener {

    private MovieAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvErrorMessage;
    private String link;
    private String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv_movies);
        progressBar = findViewById(R.id.pb_loading_indicator);
        tvErrorMessage = findViewById(R.id.tv_error_message_display);
        apiKey = BuildConfig.API_KEY;

        if (savedInstanceState == null) {
            link = String.format("%s%s", getString(R.string.link_popular), apiKey);
        } else {
            link = savedInstanceState.getString("apiKey");
        }
        int posterWidth = 500;
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, calculateBestSpanCount(posterWidth));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        progressBar.setVisibility(View.VISIBLE);
        executarBuscaPorFilmes();
    }

    private int calculateBestSpanCount(int posterWidth) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
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
        if (R.id.menu_melhor_avaliacao == item.getItemId()) {
            link = String.format("%s%s", getString(R.string.link_top_rated), apiKey);
            executarBuscaPorFilmes();
            return true;
        } else if (R.id.menu_popular == item.getItemId()) {
            link = String.format("%s%s", getString(R.string.link_popular), apiKey);
            executarBuscaPorFilmes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void executarBuscaPorFilmes() {
        if (!exibirMessagemErro()) {
            try {
                new MovieDbAsyncTask().execute(new URL(link));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean exibirMessagemErro() {
        if (!isConnected()) {
            tvErrorMessage.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            return true;
        }

        tvErrorMessage.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        return false;
    }

    private Boolean isConnected() {
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else
            connected = false;

        return connected;
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
