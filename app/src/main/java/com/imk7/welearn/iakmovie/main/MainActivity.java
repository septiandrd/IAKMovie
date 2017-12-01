package com.imk7.welearn.iakmovie.main;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.imk7.welearn.iakmovie.R;
import com.imk7.welearn.iakmovie.data.ApiClient;
import com.imk7.welearn.iakmovie.data.MovieResponseDao;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerMain;
    private List<MainDao> mData = new ArrayList<>();
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    for (MovieResponseDao.MovieData data : response.body().getResults()) {
                        mData.add(new MainDao(data.getTitle(),"https://image.tmdb.org/t/p/w185/"+data.getPoster_path()));
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

}
