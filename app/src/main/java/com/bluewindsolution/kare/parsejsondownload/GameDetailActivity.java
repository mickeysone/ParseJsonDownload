package com.bluewindsolution.kare.parsejsondownload;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.sql.Connection;
import java.util.ArrayList;

public class GameDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtAppName, txtAppView, txtAppDownload, txtAppDetail;
    private Button btnDelete, btnDownload, btnDisplay;

    private String appId = "", stToast  = "Please connect to the internet.";;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MissionDetailAdapter missionDetailAdapter;

    //private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialWidget();
        appId = getIntent().getExtras().getString("appID");

        if (!CheckInternetConnection.isInternetAvailable(this)) {
            CheckInternetConnection.showNotifications(this, stToast, Toast.LENGTH_SHORT);
           // pb.setVisibility(View.GONE);
        } else {
            new GetGameDetailData().execute("http://genetic-plus.org/presite/kare/api/getGameDetail.php?id=" + appId);
        }

        File createTxtFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kare/Mission/", "Mission.txt");
        if (createTxtFile.exists()) {
            try {
                String [] allMissionPath = GameDataManager.getStringFromFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kare/Mission/Mission.txt");
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
        btnDisplay = (Button) findViewById(R.id.btn_gameDetail_display);
        btnDisplay.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.rcv_gametail);
        layoutManager = new LinearLayoutManager(GameDetailActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        /*pb = (ProgressBar) findViewById(R.id.pb_gamedetailpage);
        pb.setVisibility(View.VISIBLE);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_gameDetail_download:
                if (!CheckInternetConnection.isInternetAvailable(this)) {
                    CheckInternetConnection.showNotifications(this, stToast, Toast.LENGTH_SHORT);
                } else {
                    new GetDownloadData(GameDetailActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://genetic-plus.org/presite/kare/api/getGame.php?id=" + appId);
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
                        //ลบไฟล์ตาม path ที่มีในไฟล์ .txt
                        File allFilePathInText = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kare/Mission/" + appId);
                        if (allFilePathInText.exists()) {
                            try {
                                File appIdTextFile = new File(allFilePathInText.toString()  + "/" + appId + ".txt");
                                if (appIdTextFile.exists()){
                                    String[] arrAllPath = GameDataManager.getStringFromFile(appIdTextFile.toString());
                                    for (int i = arrAllPath.length - 1; i >= 0; i--) {
                                        File file = new File(arrAllPath[i]);
                                        if (file.exists()) {
                                            file.delete();
                                        }
                                    }
                                    allFilePathInText.delete();
                                } else {
                                    File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kare/Mission/" + appId);
                                    if (path.isDirectory()) {
                                        DeleteFolder(path);
                                        path.delete();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //อัปเดต Mission ในไฟล์ Mission.txt
                            File createMissionTxtFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kare/Mission/", "Mission.txt");
                            if (createMissionTxtFile.exists()) {
                                try {
                                    String[] allMissionPath = GameDataManager.getStringFromFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kare/Mission/Mission.txt");
                                    StringBuffer stBuffer = new StringBuffer();
                                    for (int i = 0; i < allMissionPath.length; i++) {
                                        if (!allMissionPath[i].startsWith(appId) && allMissionPath[i].length() > 0) {
                                            stBuffer.append(allMissionPath[i] + "\n");
                                        }
                                    }
                                    FileWriter allPathInApp = new FileWriter(createMissionTxtFile);
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
                        } else {
                            Toast.makeText(GameDetailActivity.this, "file Not Found.", Toast.LENGTH_SHORT).show();
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

            case R.id.btn_gameDetail_display:
                Intent intent = new Intent(GameDetailActivity.this, DisplayFileActivity.class);
                intent.putExtra("appID", appId);
                startActivity(intent);
                break;
        }
    }

    public class GetGameDetailData extends AsyncTask<String, Integer, ArrayList<DetailData>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pb.setVisibility(View.VISIBLE);
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
//            pb.setVisibility(View.GONE);

            if (result != null) {
                txtAppName.setText(result.get(0).getName());
                txtAppView.setText("View: " + result.get(0).getView());
                txtAppDownload.setText("Download: " + result.get(0).getDownload());
                txtAppDetail.setText(result.get(0).getDetail());

                ArrayList<String> gameDetailImg = new ArrayList<String>();
                gameDetailImg.add(result.get(0).getImg1());
                gameDetailImg.add(result.get(0).getImg2());
                gameDetailImg.add(result.get(0).getImg3());
                gameDetailImg.add(result.get(0).getImg4());
                gameDetailImg.add(result.get(0).getImg5());

                if (gameDetailImg != null){
                    missionDetailAdapter = new MissionDetailAdapter(gameDetailImg, GameDetailActivity.this);
                    recyclerView.setAdapter(missionDetailAdapter);
                } else {
                    missionDetailAdapter = new MissionDetailAdapter(new ArrayList<String>(), GameDetailActivity.this);
                    recyclerView.setAdapter(missionDetailAdapter);
                }
            }
        }
    }

    public void DeleteFolder(File directory) {
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
    }
}



