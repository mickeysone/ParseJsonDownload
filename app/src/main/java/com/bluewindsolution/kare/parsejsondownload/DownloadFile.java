package com.bluewindsolution.kare.parsejsondownload;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
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
    Context context;
    ProgressDialog pd;
    ArrayList<ArrayList<String>> allArrayList = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> allLinksImg = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> allLinksFile = new ArrayList<ArrayList<String>>();
    String appId = "";
    String basePatch = "";
    CheckInternetConnection checkInternetConnection;
    boolean resultCheckConn = true;
    String stToast = "Lost internet connection. Please download again.";
    Bitmap bitmap = null;
    InputStream inputStream = null;
    String realURl = "";
    int countProgress = 0;
    int sumLinks = 0;
    StringBuffer stPath = new StringBuffer();

    public DownloadFile(Context context, String basePatch, String appId, ArrayList<ArrayList<String>> allArrayList) {
        this.context = context;
        this.allArrayList = allArrayList;
        this.basePatch = basePatch;
        this.appId = appId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        checkInternetConnection = new CheckInternetConnection(context);

        for (int i = 0; i < allArrayList.size(); i++) {
            if (i <= 4) {
                allLinksImg.add(allArrayList.get(i));
                sumLinks += allArrayList.get(i).size();
            } else {
                allLinksFile.add(allArrayList.get(i));
                sumLinks += allArrayList.get(i).size();
            }
        }

        File createFolder = new File(basePatch);
        if (!createFolder.exists()) {
            createFolder.mkdirs();
            stPath.append(createFolder.toString() + "\n");
        }
        createFolder = new File(basePatch + "/Items");
        if (!createFolder.exists()) {
            createFolder.mkdirs();
            stPath.append(createFolder.toString() + "\n");
        }
        createFolder = new File(basePatch + "/Learning");
        if (!createFolder.exists()) {
            createFolder.mkdirs();
            stPath.append(createFolder.toString() + "\n");
        }
        createFolder = new File(basePatch + "/Dodont");
        if (!createFolder.exists()) {
            createFolder.mkdirs();
            stPath.append(createFolder.toString() + "\n");
        }
        createFolder = new File(basePatch + "/Communication");
        if (!createFolder.exists()) {
            createFolder.mkdirs();
            stPath.append(createFolder.toString() + "\n");
        }
        createFolder = new File(basePatch + "/Communication/Answer");
        if (!createFolder.exists()) {
            createFolder.mkdirs();
            stPath.append(createFolder.toString() + "\n");
        }

        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setTitle("Loading...");
        pd.setMessage("Loading images...");
        pd.setCancelable(false);
        pd.setIndeterminate(false);
        pd.setMax(sumLinks);
        pd.setProgress(0);
        pd.show();
    }

    @Override
    protected String doInBackground(String... params) {
        for (int i = 0; i < allLinksImg.size(); i++) {
            if (!checkInternetConnection.isNetworkConnected()) {
                resultCheckConn = false;
                break;
            } else {
                String addPath = "";
                if (i == 0)
                    addPath = basePatch;
                else if (i == 1)
                    addPath = basePatch + "/Items";
                else if (i == 2)
                    addPath = basePatch + "/Learning";
                else if (i == 3)
                    addPath = basePatch + "/Dodont";
                else if (i == 4)
                    addPath = basePatch + "/Communication/Answer";

               LoadImage(allLinksImg.get(i), addPath, params[0]);
            }
        }
        for (int i = 0; i < allLinksFile.size(); i++) {
            if (!checkInternetConnection.isNetworkConnected()) {
                resultCheckConn = false;
                break;
            } else {
                String addPath = "";
                if (i == 0)
                    addPath = basePatch;
                else if (i == 1)
                    addPath = basePatch + "/Items";
                else if (i == 2)
                    addPath = basePatch + "/Learning";
                else if (i == 3)
                    addPath = basePatch + "/Dodont";
                else if (i == 4)
                    addPath = basePatch + "/Communication/";
                else if (i == 5)
                    addPath = basePatch + "/Communication/Answer";

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

        if (!resultCheckConn) {
            checkInternetConnection.showNotifications(stToast, Toast.LENGTH_LONG);
        } else if (resultCheckConn) {
            Button btnDownload = (Button) ((GameDetailActivity) context).findViewById(R.id.btn_gameDetail_download);
            btnDownload.setEnabled(false);
            Toast.makeText(context, "Download Complete.", Toast.LENGTH_SHORT).show();
        }
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
        File folder = new File(addPath);
        if (!folder.exists()) {
            folder.mkdirs();
            stPath.append(folder.toString() + "\n");
        }

        for (int i = 0; i < LinksImg.size(); i++) {
            if (!checkInternetConnection.isNetworkConnected()) {
                resultCheckConn = false;
                break;
            } else {
                String[] stTemp = LinksImg.get(i).split(";");
                if (stTemp[0].length() > 0) {
                    folder = new File(addPath + "/" + stTemp[0]);
                    if (!folder.exists()) {
                        folder.mkdirs();
                        stPath.append(folder.toString() + "\n");
                    }
                }
                try {
                    realURl = params + stTemp[1];
                    inputStream = OpenHttpConnection(realURl);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    String[] arrFilename = stTemp[1].split("/");
                    String filename = arrFilename[2];
                    File dir = new File(folder, filename);
                    FileOutputStream out = new FileOutputStream(dir);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    if (bitmap != null)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    out.write(bos.toByteArray());
                    out.close();

                    countProgress++;
                    stPath.append(dir.toString() + "\n");
                    publishProgress(countProgress);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
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
        }
    }

    public void LoadFiles(ArrayList<String> LinksFile, String addPath, String params) {
        File folder = new File(addPath);
        if (!folder.exists()) {
            folder.mkdirs();
            stPath.append(folder.toString() + "\n");
        }

        URL u = null;
        InputStream is = null;

        for (int i = 0; i < LinksFile.size(); i++) {
            if (!checkInternetConnection.isNetworkConnected()) {
                resultCheckConn = false;
                break;
            } else {
                String[] stTemp = LinksFile.get(i).split(";");
                if (stTemp[0].length() > 0) {
                    folder = new File(addPath + "/" + stTemp[0]);
                    if (!folder.exists()) {
                        folder.mkdirs();
                        stPath.append(folder.toString() + "\n");
                    }
                }
                try {
                    u = new URL(params + LinksFile.get(i).split(";")[1]);
                    is = u.openStream();
                    HttpURLConnection huc = (HttpURLConnection) u.openConnection();//to know the size of video
                    int size = huc.getContentLength();

                    if (huc != null) {
                        String[] arrFilename = stTemp[1].split("/");
                        String fileName = arrFilename[2];
                        File dir = new File(folder, fileName);

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
                            stPath.append(dir.toString() + "\n");
                            publishProgress(countProgress);
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    }
                } catch (MalformedURLException mue) {
                    mue.printStackTrace();
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

        try {
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
        }
    }
}
