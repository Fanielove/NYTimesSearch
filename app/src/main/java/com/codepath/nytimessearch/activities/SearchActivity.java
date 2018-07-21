package com.codepath.nytimessearch.activities;
//import com.codepath.nytimessearch.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.codepath.nytimessearch.Article;
import com.codepath.nytimessearch.ArticleArrayAdapter;
import com.codepath.nytimessearch.AsyncHttpClient;
import com.codepath.nytimessearch.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.cert.LDAPCertStoreParameters;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {
Toolbar toolbar;
EditText etquery;
GridView gvResults;
Button btButton;
ArrayList<Article>articles;
ArticleArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();


    }

    public void setupViews(){
        etquery = (EditText) findViewById(R.id.etQuery);
        gvResults =(GridView) findViewById(R.id.gvResults);
        btButton =(Button) findViewById(R.id.btButton);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i =new Intent(getApplicationContext(),ArticleActivity.class);
                Article article = articles.get(position);
                i.putExtra("article",article);
                startActivity(i);
            }

            });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onArticleSearch (View view){
String query = etquery.getText().toString();
        //Toast.makeText(this,"searching for" + query,Toast.LENGTH_LONG).show();

AsyncHttpClient client =new AsyncHttpClient();
    String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.put("api-key","f06fd4510a7a480bb1ab442916068464");
        params.put("page",0);
        params.put("q",query);
        client.get(url, params , new JsonHttpResponseHandler(){
            @Override
                    public void  onSuccess(int statusCode, Header[] headers, JSONObject Response) {
                LDAPCertStoreParameters response = null;
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;
                try {
                   articleJsonResults = Response.getJSONObject("response").getJSONArray("docs");
                   // Log.d("DEBUG", articleJsonResults.toString());
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }


        });

}

}