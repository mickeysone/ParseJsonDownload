package com.bluewindsolution.kare.parsejsondownload;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Pruxasin on 2/2/2559.
 */
public class ParseJson {

    public static ArrayList<MissionData> getMissionData(String json) {
        ArrayList<MissionData> result = new ArrayList<MissionData>();

        try {
            JSONArray parentArray = new JSONArray(json);

            for (int i = 0; i < parentArray.length(); i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);
                MissionData mzData = new MissionData();
                mzData.setName(finalObject.getString("name"));
                mzData.setLang(finalObject.getString("lang"));
                mzData.setId(finalObject.getString("id"));
                mzData.setImg(finalObject.getString("img"));
                mzData.setCategory(finalObject.getString("category"));
                mzData.setDetail(finalObject.getString("detail"));

                result.add(mzData);
            }
            return result;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<DetailData> getDetailData(String json) {
        ArrayList<DetailData> result = new ArrayList<DetailData>();

        try {
            JSONArray parentArray = new JSONArray(json);

            for (int i = 0; i < parentArray.length(); i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);
                DetailData detailData = new DetailData();
                detailData.setName(finalObject.getString("name"));
                detailData.setDetail(finalObject.getString("detail"));
                detailData.setView(finalObject.getString("view"));
                detailData.setDownload(finalObject.getString("download"));
                detailData.setImg1(finalObject.getString("img1"));
                detailData.setImg2(finalObject.getString("img2"));
                detailData.setImg3(finalObject.getString("img3"));
                detailData.setImg4(finalObject.getString("img4"));
                detailData.setImg5(finalObject.getString("img5"));

                result.add(detailData);
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<InfoGameData> getInfoGameData(String json) {
        ArrayList<InfoGameData> result = new ArrayList<InfoGameData>();

        try {
            JSONArray parentArray = new JSONArray(json);

            for (int i = 0; i < parentArray.length(); i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);
                InfoGameData infoGameData = new InfoGameData();
                infoGameData.setId(finalObject.getString("id"));
                infoGameData.setT(finalObject.getString("t"));
                infoGameData.setI(finalObject.getString("i"));
                infoGameData.setS(finalObject.getString("s"));

                JSONArray itemArray = finalObject.getJSONArray("item");
                infoGameData.setInfoGameDataItems(getInfoGameDataItem(itemArray));

                JSONArray learningArray = finalObject.getJSONArray("learning");
                infoGameData.setInfoGameDataLearnings(getInfoGameDataLearning(learningArray));

                JSONArray dodontArray = finalObject.getJSONArray("dodont");
                infoGameData.setInfoGameDataDodonts(getInfoGameDataDodont(dodontArray));

                JSONArray communicationArray = finalObject.getJSONArray("communication");
                infoGameData.setInfoGameDataCommunications(getInfoGameDataCommunication(communicationArray));

                result.add(infoGameData);
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<InfoGameDataItem> getInfoGameDataItem(JSONArray jsonArray) {
        ArrayList<InfoGameDataItem> result = new ArrayList<InfoGameDataItem>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject finalObjectItem = jsonArray.getJSONObject(i);
                InfoGameDataItem infoGameDataItem = new InfoGameDataItem();
                infoGameDataItem.setNo(finalObjectItem.getString("no"));
                infoGameDataItem.setT(finalObjectItem.getString("t"));
                infoGameDataItem.setI(finalObjectItem.getString("i"));
                infoGameDataItem.setS(finalObjectItem.getString("s"));

                result.add(infoGameDataItem);
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<InfoGameDataLearning> getInfoGameDataLearning (JSONArray jsonArray) {
        ArrayList<InfoGameDataLearning> result = new ArrayList<InfoGameDataLearning>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject finalObjectLearning = jsonArray.getJSONObject(i);
                InfoGameDataLearning infoGameDataLearning = new InfoGameDataLearning();
                infoGameDataLearning.setNo(finalObjectLearning.getString("no"));
                infoGameDataLearning.setT(finalObjectLearning.getString("t"));
                infoGameDataLearning.setI(finalObjectLearning.getString("i"));
                infoGameDataLearning.setS(finalObjectLearning.getString("s"));
                infoGameDataLearning.setV(finalObjectLearning.getString("v"));

                result.add(infoGameDataLearning);
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    public static ArrayList<InfoGameDataDodont> getInfoGameDataDodont (JSONArray jsonArray) {
        ArrayList<InfoGameDataDodont> result = new ArrayList<InfoGameDataDodont>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject finalObjectDodont = jsonArray.getJSONObject(i);
                InfoGameDataDodont infoGameDataDodont = new InfoGameDataDodont();
                infoGameDataDodont.setAnswer(finalObjectDodont.getString("answer"));
                infoGameDataDodont.setT(finalObjectDodont.getString("t"));
                infoGameDataDodont.setI(finalObjectDodont.getString("i"));
                infoGameDataDodont.setS(finalObjectDodont.getString("s"));
                infoGameDataDodont.setV(finalObjectDodont.getString("v"));

                result.add(infoGameDataDodont);
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<InfoGameDataCommunication> getInfoGameDataCommunication (JSONArray jsonArray) {
        ArrayList<InfoGameDataCommunication> result = new ArrayList<InfoGameDataCommunication>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject finalObjectCommunication = jsonArray.getJSONObject(i);
                InfoGameDataCommunication infoGameDataCommunication = new InfoGameDataCommunication();
                infoGameDataCommunication.setQid(finalObjectCommunication.getString("qid"));
                infoGameDataCommunication.setT(finalObjectCommunication.getString("t"));
                infoGameDataCommunication.setS(finalObjectCommunication.getString("s"));

                JSONArray communicationAnswerArray = finalObjectCommunication.getJSONArray("answer");
                infoGameDataCommunication.setInfoGameDataCommunicationAnswers(getInfoGameDataCommunicationAnswer(communicationAnswerArray));

                result.add(infoGameDataCommunication);
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<InfoGameDataCommunicationAnswer> getInfoGameDataCommunicationAnswer (JSONArray jsonArray) {
        ArrayList<InfoGameDataCommunicationAnswer> result = new ArrayList<InfoGameDataCommunicationAnswer>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject finalObjectCommunicationAnswer = jsonArray.getJSONObject(i);
                InfoGameDataCommunicationAnswer infoGameDataCommunicationAnswer = new InfoGameDataCommunicationAnswer();
                infoGameDataCommunicationAnswer.setQid(finalObjectCommunicationAnswer.getString("qid"));
                infoGameDataCommunicationAnswer.setAnswer(finalObjectCommunicationAnswer.getString("answer"));
                infoGameDataCommunicationAnswer.setT(finalObjectCommunicationAnswer.getString("t"));
                infoGameDataCommunicationAnswer.setI(finalObjectCommunicationAnswer.getString("i"));
                infoGameDataCommunicationAnswer.setS(finalObjectCommunicationAnswer.getString("s"));

                result.add(infoGameDataCommunicationAnswer);
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
