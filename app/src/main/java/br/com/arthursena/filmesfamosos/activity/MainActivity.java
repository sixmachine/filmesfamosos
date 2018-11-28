package br.com.arthursena.filmesfamosos.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
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

import br.com.arthursena.filmesfamosos.BuildConfig;
import br.com.arthursena.filmesfamosos.R;
import br.com.arthursena.filmesfamosos.adapter.AbstractMovieAdapter;
import br.com.arthursena.filmesfamosos.adapter.MovieAdapter;
import br.com.arthursena.filmesfamosos.adapter.MovieCursorAdapter;
import br.com.arthursena.filmesfamosos.database.MovieContract;
import br.com.arthursena.filmesfamosos.model.MovieDb;
import br.com.arthursena.filmesfamosos.model.MovieDbResponse;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickListener, LoaderManager.LoaderCallbacks<Cursor>{

    private static final int MOVIE_LOADER_ID = 0;
    private RecyclerView recyclerView;
    private MovieCursorAdapter cursorAdapter;
    private MovieAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvErrorMessage;
    private String link;
    private String apiKey;

    private static final String TAG = MainActivity.class.getSimpleName();

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
        else if(R.id.menu_favoritos == item.getItemId()){
            if(cursorAdapter == null){
                cursorAdapter = new MovieCursorAdapter(this,this);
            }
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
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
        Intent intent = new Intent(this, MovieDetailsActivity.class);
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
            AbstractMovieAdapter adapter = new MovieAdapter(MainActivity.this, response.getResults(), MainActivity.this);
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.INVISIBLE);
            super.onPostExecute(response);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // COMPLETED (5) Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
        recyclerView.setAdapter(cursorAdapter);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
        recyclerView.setAdapter(cursorAdapter);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(recyclerView.getAdapter() instanceof MovieCursorAdapter){
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID,null,MainActivity.this);
        }
    }
}
