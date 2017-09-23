package com.example.god.bookapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String REQUEST_URL="https://www.googleapis.com/books/v1/volumes?q=";
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    BookAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView bookListView = (ListView) findViewById(R.id.list);
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(mAdapter);
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Book currentBook = mAdapter.getItem(position);
                Uri BookUri = Uri.parse(currentBook.getlink());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, BookUri);
                startActivity(websiteIntent);
            }
        });
    }
    public void Search(View view){
    BookAsyncTask task = new BookAsyncTask();
        task.execute(REQUEST_URL);
    }
    private class BookAsyncTask extends AsyncTask<String,Void,List<Book>> {
        EditText searchBox = (EditText) findViewById(R.id.Search);
        private String search = searchBox.getText().toString();
        @Override
        protected List<Book> doInBackground(String... urls) {
            if(search.length() == 0) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.enter_search), Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }
            search = search.replace(" ", "+");
            URL url = createUrl(REQUEST_URL + search);
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException", e);
            }
            List<Book> books = extractBookInfoFromJson(jsonResponse);
            return books;
        }
        @Override
        protected void onPostExecute(List<Book> data) {
            mAdapter.clear();
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
        private List<Book> extractBookInfoFromJson(String bookJSON) {
            if (TextUtils.isEmpty(bookJSON)) {
                return null;
            }
            List<Book> books = new ArrayList<>();
            try {
                JSONObject baseJsonResponse = new JSONObject(bookJSON);
                if (baseJsonResponse.getInt("totalItems") == 0) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.Result_not_found), Toast.LENGTH_SHORT).show();
                        }
                    });
                    return null;
                }
                JSONArray itemArray = baseJsonResponse.getJSONArray("items");
                for (int i = 0; i < itemArray.length(); i++) {
                    JSONObject currentItem = itemArray.getJSONObject(i);
                    JSONObject bookInfo = currentItem.getJSONObject("volumeInfo");
                    String title = bookInfo.getString("title");
                    String[] authors = new String[]{};
                    JSONArray authorJsonArray = bookInfo.optJSONArray("authors");
                    if (authorJsonArray != null) {
                        ArrayList<String> authorList = new ArrayList<String>();
                        for (int j = 0; j < authorJsonArray.length(); j++) {
                            authorList.add(authorJsonArray.get(j).toString());
                        }
                        authors = authorList.toArray(new String[authorList.size()]);
                    }
                    String description = "";
                    if (bookInfo.optString("description") != null)
                        description = bookInfo.optString("description");
                    String Link = "";
                    if (bookInfo.optString("infoLink") != null)
                        Link = bookInfo.optString("infoLink");
                    books.add(new Book(title, authors, description, Link));
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the Book JSON results", e);
            }
            return books;
        }
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }
                else {
                    Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the Book JSON results.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }
    }
}
