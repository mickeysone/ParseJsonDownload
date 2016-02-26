package com.bluewindsolution.kare.parsejsondownload;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GameDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtAppName, txtAppView, txtAppDownload, txtAppDetail;
    private Button btnDelete, btnDownload;

    private String appId = "", stToast  = "Please connect to the internet.";;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MissionDetailAdapter missionDetailAdapter;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        pd = new ProgressDialog(this);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.setMessage("Loading. Please wait...");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appId = getIntent().getExtras().getString("appID");

        initialWidget();

        new GetGameDetailData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://genetic-plus.org/presite/kare/api/getGameDetail.php?id=" + appId);

        File createTxtFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kare/Mission/", "Mission.txt");
        if (!createTxtFile.exists()) {

        } else {
            try {
                String [] allMissionPath = getStringFromFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kare/Mission/Mission.txt");
                for (int i = 0; i < allMissionPath.length; i++) {
                    if (allMissionPath[i].startsWith(appId)) {
                        btnDownload.setEnabled(false);
                        btnDelete.setEnabled(true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initialWidget() {
        txtAppName = (TextView) findViewById(R.id.txt_gameDetail_missionName);
        txtAppView = (TextView) findViewById(R.id.txt_gameDetail_missionView);
        txtAppDownload = (TextView) findViewById(R.id.txt_gameDetail_missionDownload);
        txtAppDetail = (TextView) findViewById(R.id.txt_gameDetail_missionDetail);

        btnDownload = (Button) findViewById(R.id.btn_gameDetail_download);
        btnDownload.setOnClickListener(this);
        btnDelete = (Button) findViewById(R.id.btn_gameDetail_delete);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_gameDetail_download:
                CheckInternetConnection checkInternetConnection = new CheckInternetConnection(this);
                if (!checkInternetConnection.isNetworkConnected()) {
                    checkInternetConnection.showNotifications(stToast, Toast.LENGTH_SHORT);
                } else {
                    new GetDownloadPage(GameDetailActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://genetic-plus.org/presite/kare/api/getGame.php?id=" + appId);
                    btnDelete.setEnabled(true);
                    btnDownload.setEnabled(false);
                }
                break;
            case R.id.btn_gameDetail_delete:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Delete files");
                dialog.setCancelable(true);
                dialog.setMessage("Do you want to delete?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        File allFilePathInText = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kare/Mission/" + appId);
                        if (allFilePathInText.exists()) {
                            try {
                                String[] arrAllPath = getStringFromFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kare/Mission/" + appId + "/" + appId + ".txt");
                                for (int i = arrAllPath.length - 1; i >= 0; i--) {
                                    File file = new File(arrAllPath[i]);
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                }
                                allFilePathInText.delete();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            File createTxtFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kare/Mission/", "Mission.txt");
                            if (createTxtFile.exists()) {
                                try {
                                    String[] allMissionPath = getStringFromFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kare/Mission/Mission.txt");
                                    StringBuffer stBuffer = new StringBuffer();
                                    for (int i = 0; i < allMissionPath.length; i++) {
                                        if (!allMissionPath[i].startsWith(appId)) {
                                            stBuffer.append(allMissionPath[i] + "\n");
                                        }
                                    }
                                    FileWriter allPathInApp = new FileWriter(createTxtFile);
                                    allPathInApp.append(stBuffer);
                                    allPathInApp.flush();
                                    allPathInApp.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            btnDownload.setEnabled(true);
                            btnDelete.setEnabled(false);
                            dialog.cancel();
                            Toast.makeText(GameDetailActivity.this, "Delete files complete.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialog.show();

/*                File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kare/Mission/" + appId);
                if (path.isDirectory()) {
                    DeleteFolder(path);
                    path.delete();

                    File createTxtFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kare/Mission/", "Mission.txt");
                    if (createTxtFile.exists()) {
                        try {
                            String [] allMissionPath = getStringFromFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kare/Mission/Mission.txt");
                            StringBuffer stBuffer = new StringBuffer();
                            for (int i = 0; i < allMissionPath.length; i++) {
                                if (!allMissionPath[i].startsWith(appId)) {
                                    stBuffer.append(allMissionPath[i] + "\n");
                                }
                            }
                            FileWriter allPathInApp = new FileWriter(createTxtFile);
                            allPathInApp.append(stBuffer);
                            allPathInApp.flush();
                            allPathInApp.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    btnDownload.setEnabled(true);
                    btnDelete.setEnabled(false);
                    Toast.makeText(this, "Delete files complete.", Toast.LENGTH_SHORT).show();
                }*/


                break;
        }
    }

    public class GetGameDetailData extends AsyncTask<String, Integer, ArrayList<DetailData>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected ArrayList<DetailData> doInBackground(String... params) {
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

                //ArrayList<DetailData> arrLsDetailData = ParseJson.getDetailData(finalJson);

                return ParseJson.getDetailData(finalJson);

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
        protected void onPostExecute(ArrayList<DetailData> result) {
            super.onPostExecute(result);
            pd.dismiss();

            if (result != null) {
                txtAppName.setText(result.get(0).name);
                txtAppView.setText("View: " + "\n" + result.get(0).view);
                txtAppDownload.setText("Download: "+ "\n" +result.get(0).download);
                txtAppDetail.setText(result.get(0).detail);

                ArrayList<String> gameDetailImg = new ArrayList<String>();
                gameDetailImg.add(result.get(0).img1);
                gameDetailImg.add(result.get(0).img2);
                gameDetailImg.add(result.get(0).img3);
                gameDetailImg.add(result.get(0).img4);
                gameDetailImg.add(result.get(0).img5);

                recyclerView = (RecyclerView) findViewById(R.id.rcv_gametail);
                layoutManager = new LinearLayoutManager(GameDetailActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                missionDetailAdapter = new MissionDetailAdapter(gameDetailImg, GameDetailActivity.this);
                recyclerView.setAdapter(missionDetailAdapter);
            }
        }
    }

    public static String[] getStringFromFile (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        fin.close();
        String[] arrAllMissionPath = ret.split("\\r?\\n");
        return arrAllMissionPath;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

/*    public void DeleteFolder(File directory) {
        if (directory.isDirectory()) {
            File[] subFolder = directory.listFiles();
            if (subFolder != null) {
                for (int i = 0; i < subFolder.length; i++) {
                    File tempFile = subFolder[i];
                    if (tempFile.isDirectory()) {
                        DeleteFolder(tempFile);
                    }
                    tempFile.delete();
                }
            }
        }
    }*/
}



