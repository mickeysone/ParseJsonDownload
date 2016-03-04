package com.bluewindsolution.kare.parsejsondownload;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Pruxasin on 29/1/2559.
 */
public class DownloadFile extends AsyncTask<String, Integer, String> {
    private Context context;
    private ProgressDialog pd;

    private ArrayList<ArrayList<String>> allArrayList = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> allLinksImg = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> allLinksFile = new ArrayList<ArrayList<String>>();

    private String appId = "";
    private String missionPatch = "";

    boolean resultCheckConn = true;
    private String stToast = "Lost internet connection. Please download again.";

    private Bitmap bitmap = null;
    private InputStream inputStream = null;
    private String realURl = "";

    private int countProgress = 0;
    private int sumLinks = 0;
    private StringBuffer stPath = new StringBuffer();

    File createTxtFile;

    public DownloadFile(Context context, String missionPatch, String appId, ArrayList<ArrayList<String>> allArrayList) {
        this.context = context;
        this.allArrayList = allArrayList;
        this.missionPatch = missionPatch;
        this.appId = appId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        for (int i = 0; i < allArrayList.size(); i++) {
            if (i <= 4) {
                allLinksImg.add(allArrayList.get(i));
                sumLinks += allArrayList.get(i).size();
            } else {
                allLinksFile.add(allArrayList.get(i));
                sumLinks += allArrayList.get(i).size();
            }
        }

        File createFolder = new File(missionPatch);
        if (!createFolder.exists()) {
            createFolder.mkdirs();
            stPath.append(createFolder.toString() + "\n");
        }
        createFolder = new File(missionPatch + "/Items");
        if (!createFolder.exists()) {
            createFolder.mkdirs();
            stPath.append(createFolder.toString() + "\n");
        }
        createFolder = new File(missionPatch + "/Learning");
        if (!createFolder.exists()) {
            createFolder.mkdirs();
            stPath.append(createFolder.toString() + "\n");
        }
        createFolder = new File(missionPatch + "/Dodont");
        if (!createFolder.exists()) {
            createFolder.mkdirs();
            stPath.append(createFolder.toString() + "\n");
        }
        createFolder = new File(missionPatch + "/Communication");
        if (!createFolder.exists()) {
            createFolder.mkdirs();
            stPath.append(createFolder.toString() + "\n");
        }

        createTxtFile = new File(missionPatch, appId + ".txt");
        if (!createTxtFile.exists()) {
            stPath.append(createTxtFile.toString() + "\n");
        }

        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setTitle("Loading...");
        pd.setMessage("Loading files...");
        pd.setCancelable(false);
        pd.setIndeterminate(false);
        pd.setMax(sumLinks);
        pd.setProgress(0);
        pd.show();
    }

    @Override
    protected String doInBackground(String... params) {
        for (int i = 0; i < allLinksImg.size(); i++) {
            if (!CheckInternetConnection.isInternetAvailable(context)) {
                resultCheckConn = false;
                break;
            } else {
                String addPath = "";
                if (i == 0)
                    addPath = missionPatch;
                else if (i == 1)
                    addPath = missionPatch + "/Items";
                else if (i == 2)
                    addPath = missionPatch + "/Learning";
                else if (i == 3)
                    addPath = missionPatch + "/Dodont";
                else if (i == 4)
                    addPath = missionPatch + "/Communication";

                LoadImage(allLinksImg.get(i), addPath, params[0]);
            }
        }
        for (int i = 0; i < allLinksFile.size(); i++) {
            if (!CheckInternetConnection.isInternetAvailable(context)) {
                resultCheckConn = false;
                break;
            } else {
                String addPath = "";
                if (i == 0)
                    addPath = missionPatch;
                else if (i == 1)
                    addPath = missionPatch + "/Items";
                else if (i == 2)
                    addPath = missionPatch + "/Learning";
                else if (i == 3)
                    addPath = missionPatch + "/Dodont";
                else if (i == 4)
                    addPath = missionPatch + "/Communication/";
                else if (i == 5)
                    addPath = missionPatch + "/Communication";

                LoadFiles(allLinksFile.get(i), addPath, params[0]);
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
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        pd.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pd.dismiss();
        Button btnDownload = (Button) ((GameDetailActivity) context).findViewById(R.id.btn_gameDetail_download);

        if (!resultCheckConn) {
            CheckInternetConnection.showNotifications(context, stToast, Toast.LENGTH_LONG);
            btnDownload.setEnabled(true);
        } else if (resultCheckConn) {
            AddPathMissionToTextFile();
            btnDownload.setEnabled(false);
            Toast.makeText(context, "Download Complete.", Toast.LENGTH_SHORT).show();
        }
        AddFilePathToTextFile();
    }

    private InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        if (!(connection instanceof HttpURLConnection)) {
            throw new IOException("Not an HTTP connection");
        }

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            response = httpConnection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConnection.getInputStream();
            }
        } catch (Exception ex) {
            throw new IOException("Error connecting");
        }
        return in;
    }

    private void LoadImage(ArrayList<String> LinksImg, String addPath, String params) {
        String filename = "", linkDownload = "";
        File dir;
        String[] arrFilename;

        File folder = new File(addPath);
        if (!folder.exists()) {
            folder.mkdirs();
            stPath.append(folder.toString() + "\n");
        }

        for (int i = 0; i < LinksImg.size(); i++) {
            String[] stTemp = LinksImg.get(i).split(";");
            if (stTemp.length <= 2 && stTemp[0].length() > 0) {
                folder = new File(addPath + "/" + stTemp[0]);
                if (!folder.exists()) {
                    folder.mkdirs();
                    stPath.append(folder.toString() + "\n");
                }
                linkDownload = stTemp[1];
                arrFilename = linkDownload.split("/");
                filename = arrFilename[2];
            } else if (stTemp.length == 3) {
                folder = new File(addPath + "/" + stTemp[0]);
                if (!folder.exists()) {
                    folder.mkdirs();
                    stPath.append(folder.toString() + "\n");
                }
                folder = new File(addPath + "/" + stTemp[0] + "/Answer");
                if (!folder.exists()) {
                    folder.mkdirs();
                    stPath.append(folder.toString() + "\n");
                }
                folder = new File(addPath + "/" + stTemp[0] + "/Answer/" + stTemp[1]);
                if (!folder.exists()) {
                    folder.mkdirs();
                    stPath.append(folder.toString() + "\n");
                }
                linkDownload = stTemp[2];
                arrFilename = linkDownload.split("/");
                filename = arrFilename[2];
            } else {
                linkDownload = stTemp[1];
                arrFilename = linkDownload.split("/");
                filename = arrFilename[2];
            }
            dir = new File(folder, filename);

            if (!CheckInternetConnection.isInternetAvailable(context)) {
                resultCheckConn = false;
                break;
            } else if (CheckInternetConnection.isInternetAvailable(context) && dir.exists()) {
                countProgress++;
                publishProgress(countProgress);
            } else if (CheckInternetConnection.isInternetAvailable(context) && !dir.exists()) {
                stPath.append(dir.toString() + "\n");
                try {
                    realURl = params + linkDownload;
                    inputStream = OpenHttpConnection(realURl);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    FileOutputStream out = new FileOutputStream(dir);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    if (bitmap != null)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    out.write(bos.toByteArray());
                    out.close();
                    countProgress++;
                    publishProgress(countProgress);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void LoadFiles(ArrayList<String> LinksFile, String addPath, String params) {
        String filename = "", linkDownload = "";
        File dir;
        String[] arrFilename;
        File folder = new File(addPath);
        if (!folder.exists()) {
            folder.mkdirs();
            stPath.append(folder.toString() + "\n");
        }

        URL u = null;
        InputStream is = null;

        for (int i = 0; i < LinksFile.size(); i++) {
            String[] stTemp = LinksFile.get(i).split(";");
            if (stTemp.length <= 2 && stTemp[0].length() > 0) {
                folder = new File(addPath + "/" + stTemp[0]);
                if (!folder.exists()) {
                    folder.mkdirs();
                    stPath.append(folder.toString() + "\n");
                }
                linkDownload = stTemp[1];
                arrFilename = linkDownload.split("/");
                filename = arrFilename[2];
            } else if (stTemp.length == 3 ) {
                folder = new File(addPath + "/" + stTemp[0]);
                if (!folder.exists()) {
                    folder.mkdirs();
                    stPath.append(folder.toString() + "\n");
                }
                folder = new File(addPath + "/" + stTemp[0] + "/Answer");
                if (!folder.exists()) {
                    folder.mkdirs();
                    stPath.append(folder.toString() + "\n");
                }
                folder = new File(addPath + "/" + stTemp[0] + "/Answer/" + stTemp[1]);
                if (!folder.exists()) {
                    folder.mkdirs();
                    stPath.append(folder.toString() + "\n");
                }
                linkDownload = stTemp[2];
                arrFilename = linkDownload.split("/");
                filename = arrFilename[2];
            } else {
                linkDownload = stTemp[1];
                arrFilename = linkDownload.split("/");
                filename = arrFilename[2];
            }
            dir = new File(folder, filename);
            stPath.append(dir.toString() + "\n");

            if (!CheckInternetConnection.isInternetAvailable(context)) {
                resultCheckConn = false;
                break;
            } else if (CheckInternetConnection.isInternetAvailable(context) && dir.exists()){
                countProgress++;
                publishProgress(countProgress);
            } else if (CheckInternetConnection.isInternetAvailable(context) && !dir.exists()) {
                try {
                    u = new URL(params + linkDownload);
                    is = u.openStream();
                    HttpURLConnection huc = (HttpURLConnection) u.openConnection();//to know the size of video
                    int size = huc.getContentLength();

                    if (huc != null) {
                        FileOutputStream fos = new FileOutputStream(dir);
                        byte[] buffer = new byte[1024];
                        //long total = 0;
                        int len1 = 0;
                        if (is != null) {
                            while ((len1 = is.read(buffer)) > 0) {
                            /*total += len1;
                            publishProgress((int)((total*100)/size));*/
                                fos.write(buffer, 0, len1);
                            }
                            countProgress++;
                            publishProgress(countProgress);
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        }
/*        try {
            File createTxtFile = new File(basePatch, appId + ".txt");
            if (!createTxtFile.exists()) {
                stPath.append(createTxtFile.toString() + "\n");
            }
            FileWriter allPathInApp = new FileWriter(createTxtFile, true);
            allPathInApp.append(stPath.toString());
            allPathInApp.flush();
            allPathInApp.close();
            stPath = new StringBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void AddPathMissionToTextFile() {
        try {
            File createMissionTxtFile = new File(GetDownloadData.basePatch, "Mission.txt");
            FileWriter missionTextFile = new FileWriter(createMissionTxtFile, true);
            missionTextFile.append(GetDownloadData.statusMissionPath + "\n");
            missionTextFile.flush();
            missionTextFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AddFilePathToTextFile() {
        try {
            FileWriter allPathInApp = new FileWriter(createTxtFile, true);
            allPathInApp.append(stPath.toString());
            allPathInApp.flush();
            allPathInApp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}