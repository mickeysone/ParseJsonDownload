package com.bluewindsolution.kare.parsejsondownload;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private String lang, stToast  = "Please connect to the internet.";

    private CheckInternetConnection checkInternetConnection = new CheckInternetConnection(this);

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MissionAdapter missionAdapter;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pd = new ProgressDialog(this);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.setMessage("Loading. Please wait...");

        if (!checkInternetConnection.isInternetAvailable()) {
            checkInternetConnection.showNotifications(stToast, Toast.LENGTH_SHORT);
        } else {
            lang = "TH";
            new GetMainPageData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://genetic-plus.org/presite/kare/api/getStore.php?lang=" + lang);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (!checkInternetConnection.isInternetAvailable()) {
                    checkInternetConnection.showNotifications(stToast, Toast.LENGTH_SHORT);
                } else {
                    recreate();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!checkInternetConnection.isInternetAvailable()) {
            checkInternetConnection.showNotifications(stToast, Toast.LENGTH_SHORT);
        }
    }

    public class GetMainPageData extends AsyncTask<String, Integer, ArrayList<MissionData>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected ArrayList<MissionData> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = "";
                finalJson = buffer.toString();

                return ParseJson.getMissionData(finalJson);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(ArrayList<MissionData> result) {
            super.onPostExecute(result);
            pd.dismiss();

            recyclerView = (RecyclerView) findViewById(R.id.rcv_mainpage);
            layoutManager = new LinearLayoutManager(MainActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            missionAdapter = new MissionAdapter(result, MainActivity.this);
            recyclerView.setAdapter(missionAdapter);

            missionAdapter.SetOnItemClickListener(new MissionAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(String appId, String appName, View view) {
                    if (!checkInternetConnection.isInternetAvailable()) {
                        checkInternetConnection.showNotifications(stToast, Toast.LENGTH_SHORT);
                    } else {
                        Intent intent = new Intent(MainActivity.this, GameDetailActivity.class);
                        intent.putExtra("appID", appId);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
