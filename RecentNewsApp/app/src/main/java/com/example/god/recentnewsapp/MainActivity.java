package com.example.god.recentnewsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.god.recentnewsapp.R.string.no_internet;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final String NEWS_REQUEST_URL =
            "http://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date=2014-01-01&api-key=test&page-size=20&show-tags=contributor";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int NEWS_LOADER_ID = 1;
    private NewsAdapter mNewsAdapters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView NewsListView = (ListView) findViewById(R.id.list);
        mNewsAdapters = new NewsAdapter(this, new ArrayList<News>());
        NewsListView.setAdapter(mNewsAdapters);
        NewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = mNewsAdapters.getItem(position);
                Uri earthquakeUri = Uri.parse(currentNews.getWebUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);
            }
        });
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        }
    }
    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, NEWS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        mNewsAdapters.clear();
        if (data != null && !data.isEmpty()) {
            mNewsAdapters.addAll(data);
        }
    }
    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mNewsAdapters.clear();
    }
}

