package com.bluewindsolution.kare.parsejsondownload;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Pruxasin on 25/1/2559.
 */
public class GetDownloadData extends AsyncTask<String, Integer, String> {

    private Context context;
    private String[] appId;

    public static String statusMissionPath;
    public static String basePatch = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kare/Mission/";

    public GetDownloadData(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        appId = params[0].split("id=");

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

            return finalJson;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } /*catch (JSONException e) {
            e.printStackTrace();
        }*/ finally {
            if (connection != null)
                connection.disconnect();
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        ArrayList<InfoGameData> infoGameDataArrLs = ParseJson.getInfoGameData(result);

        //String imgTitle = "";

        String[] appIdName = new String[2];
        ArrayList<String> linkImgTitle = new ArrayList<String>();
        ArrayList<String> linkImgItem = new ArrayList<String>();
        ArrayList<String> linkImgLearning = new ArrayList<String>();
        ArrayList<String> linkImgDodont = new ArrayList<String>();
        ArrayList<String> linkImgCommuAnswer = new ArrayList<String>();

        ArrayList<String> linkFileTitle = new ArrayList<String>();
        ArrayList<String> linkFileItem = new ArrayList<String>();
        ArrayList<String> linkFileLearning = new ArrayList<String>();
        ArrayList<String> linkFileDodont = new ArrayList<String>();
        ArrayList<String> linkFileCommu = new ArrayList<String>();
        ArrayList<String> linkFileCommuAnswer = new ArrayList<String>();

        ArrayList<ArrayList<String>> allArrayList = new ArrayList<>();

        if (infoGameDataArrLs != null) {
            for (InfoGameData infg : infoGameDataArrLs) {
                //imgTitle = infg.i;
                appIdName[0] = infg.getT();
                appIdName[1] = infg.getI();
                linkImgTitle.add(";" + infg.getI());
                linkFileTitle.add(";" + infg.getS());
                for (int i = 0; i < infg.infoGameDataItems.size(); i++) {
                    linkImgItem.add(infg.infoGameDataItems.get(i).getNo() + ";" +  infg.infoGameDataItems.get(i).getI());
                    linkFileItem.add(infg.infoGameDataItems.get(i).getNo() + ";" +  infg.infoGameDataItems.get(i).getS());
                }
                for (int i = 0; i < infg.infoGameDataLearnings.size(); i++) {
                    linkImgLearning.add(infg.infoGameDataLearnings.get(i).getNo() + ";" + infg.infoGameDataLearnings.get(i).getI());
                    linkFileLearning.add(infg.infoGameDataLearnings.get(i).getNo() + ";" + infg.infoGameDataLearnings.get(i).getS());
                    linkFileLearning.add(infg.infoGameDataLearnings.get(i).getNo() + ";" + infg.infoGameDataLearnings.get(i).getV());
                }
                for (int i = 0; i < infg.infoGameDataDodonts.size(); i++) {
                    linkImgDodont.add(infg.infoGameDataDodonts.get(i).getAnswer()+ ";" + infg.infoGameDataDodonts.get(i).getI());
                    linkFileDodont.add(infg.infoGameDataDodonts.get(i).getAnswer() + ";" + infg.infoGameDataDodonts.get(i).getS());
                    linkFileDodont.add(infg.infoGameDataDodonts.get(i).getAnswer() + ";" + infg.infoGameDataDodonts.get(i).getV());
                }
                for (int i = 0; i < infg.infoGameDataCommunications.size(); i++) {
                    linkFileCommu.add(infg.infoGameDataCommunications.get(i).getQid() + ";" + infg.infoGameDataCommunications.get(i).getS());
                    for (int j = 0; j < infg.infoGameDataCommunications.get(i).infoGameDataCommunicationAnswers.size(); j++) {
                        linkImgCommuAnswer.add(infg.infoGameDataCommunications.get(i).getQid() + ";" + infg.infoGameDataCommunications.get(i).infoGameDataCommunicationAnswers.get(j).getQid() + ";" + infg.infoGameDataCommunications.get(i).infoGameDataCommunicationAnswers.get(j).getI());
                        linkFileCommuAnswer.add(infg.infoGameDataCommunications.get(i).getQid() + ";" + infg.infoGameDataCommunications.get(i).infoGameDataCommunicationAnswers.get(j).getQid() + ";" + infg.infoGameDataCommunications.get(i).infoGameDataCommunicationAnswers.get(j).getS());
                    }
                }
            }
            allArrayList.add(linkImgTitle);
            allArrayList.add(linkImgItem);
            allArrayList.add(linkImgLearning);
            allArrayList.add(linkImgDodont);
            allArrayList.add(linkImgCommuAnswer);
            allArrayList.add(linkFileTitle);
            allArrayList.add(linkFileItem);
            allArrayList.add(linkFileLearning);
            allArrayList.add(linkFileDodont);
            allArrayList.add(linkFileCommu);
            allArrayList.add(linkFileCommuAnswer);
        }

        String missionPath = basePatch + appId[1];

        File Folder = new File(missionPath);
        if (!Folder.exists()) {
            Folder.mkdirs();
        }

        File nomediaFile = new File(basePatch, ".nomedia");
        if (!nomediaFile.exists()) {
            try {
                nomediaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

/*        try {
            File createTxtFile = new File(basePatch, "Mission.txt");
            FileWriter allPathInApp = new FileWriter(createTxtFile,true);
            allPathInApp.append(appId[1] + "=" + appIdName[0] + "=" + appIdName[1] + "\n");
            allPathInApp.flush();
            allPathInApp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        statusMissionPath = appId[1] + "=" + appIdName[0] + "=" + appIdName[1];

        new DownloadFile(context, missionPath, appId[1], allArrayList).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://genetic-plus.org/presite/kare/");
    }
}
