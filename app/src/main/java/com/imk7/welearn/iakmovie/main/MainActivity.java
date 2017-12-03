package com.imk7.welearn.iakmovie.main;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.imk7.welearn.iakmovie.R;
import com.imk7.welearn.iakmovie.data.ApiClient;
import com.imk7.welearn.iakmovie.data.dao.MovieResponseDao;
import com.imk7.welearn.iakmovie.data.offline.MovieContract;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRecyclerMain;
    private List<MainDao> mData = new ArrayList<>();
    private MainAdapter mAdapter;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportLoaderManager().initLoader(0,null,this);

        mAdapter = new MainAdapter(mData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);

        mRecyclerMain = findViewById(R.id.recyclerView);
        mRecyclerMain.setAdapter(mAdapter);
        mRecyclerMain.setLayoutManager(gridLayoutManager);

        ApiClient.service().getMovieList("5ad962a0bbb9c60a45de13a45c8028c7").enqueue(new Callback<MovieResponseDao>() {
            @Override
            public void onResponse(Call<MovieResponseDao> call, Response<MovieResponseDao> response) {
                if (response.isSuccessful()) {

                    Uri deleteUri = MovieContract.MovieEntry.CONTENT_URI;
                    getContentResolver().delete(deleteUri,null,null);
                    for (MovieResponseDao.MovieData data : response.body().getResults()) {
//                        mData.add(new MainDao(data.getTitle(),"https://image.tmdb.org/t/p/w185/"+data.getPoster_path()));
                        ContentValues contentValues = new ContentValues();

                        contentValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE_IDS,data.getId());
                        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE,data.getTitle());
                        contentValues.put(MovieContract.MovieEntry.COLUMN_ORI_TITLE,data.getOriginal_title());
                        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT,data.getVote_count());
                        contentValues.put(MovieContract.MovieEntry.COLUMN_VIDEO,data.isVideo() ? 1 : 0 );
                        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVG,data.getVote_average());
                        contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY,data.getPopularity());
                        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH,data.getPoster_path());
                        contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANG,data.getOriginal_language());
                        contentValues.put(MovieContract.MovieEntry.COLUMN_GENRE,"");
                        contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,data.getBackdrop_path());
                        contentValues.put(MovieContract.MovieEntry.COLUMN_ADULT,data.isAdult() ? 1 : 0 );
                        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,data.getOverview());
                        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,data.getRelease_date());

                        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,contentValues);

                        if (uri != null){
                            Log.d("onResponse", "INSERT DATA SUCCESS");
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MovieResponseDao> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mData.add(new MainDao("Fight Club","https://i.pinimg.com/736x/fd/5e/66/fd5e662dce1a3a8cd192a5952fa64f02--classic-poster-classic-movies-posters.jpg"));
//                mData.add(new MainDao("Titanic", "http://img.moviepostershop.com/titanic-movie-poster-1997-1020339699.jpg"));
//                mData.add(new MainDao("Richard Parker","https://marketplace.canva.com/MACFQTmLl08/2/0/thumbnail_large/canva-tiger-minimalist-movie-poster-MACFQTmLl08.jpg"));
//                mData.add(new MainDao("Jaws","http://static.adweek.com/adweek.com-prod/wp-content/uploads/2017/09/stranger-things-originals-jaws-683x1024.jpg"));mData.add(new MainDao("Fight Club","https://i.pinimg.com/736x/fd/5e/66/fd5e662dce1a3a8cd192a5952fa64f02--classic-poster-classic-movies-posters.jpg"));
//                mData.add(new MainDao("Titanic", "http://img.moviepostershop.com/titanic-movie-poster-1997-1020339699.jpg"));
//                mData.add(new MainDao("Richard Parker","https://marketplace.canva.com/MACFQTmLl08/2/0/thumbnail_large/canva-tiger-minimalist-movie-poster-MACFQTmLl08.jpg"));
//                mData.add(new MainDao("Jaws","http://static.adweek.com/adweek.com-prod/wp-content/uploads/2017/09/stranger-things-originals-jaws-683x1024.jpg"));
//
//                mAdapter.notifyDataSetChanged();
//            }
//        },2000);

        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getApplicationContext()) {
            Cursor mMovieData = null;
            @Override
            protected void onStartLoading() {
                if(mMovieData!=null) {
                    deliverResult(mMovieData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getApplicationContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MovieContract.MovieEntry._ID);
                } catch (Exception e) {
                    Log.e(TAG,"Failed to asynchronously load data \n" + e.getMessage());
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
//        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("onLoadFinished : ", String.valueOf(data.getCount()));
        mData.clear();

        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);

            mData.add(new MainDao(
                    data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)),
                    "https://image.tmdb.org/t/p/w185/" + data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH))
            ));
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
