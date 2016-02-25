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
            JSONObject finalObject;

            for (int i = 0; i < parentArray.length(); i++) {
                finalObject = parentArray.getJSONObject(i);
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
            JSONObject finalObject;

            for (int i = 0; i < parentArray.length(); i++) {
                finalObject = parentArray.getJSONObject(i);
                DetailData detailData = new DetailData();
                detailData.name = finalObject.getString("name");
                detailData.detail = finalObject.getString("detail");
                detailData.view = finalObject.getString("view");
                detailData.download = finalObject.getString("download");
                detailData.img1 = finalObject.getString("img1");
                detailData.img2 = finalObject.getString("img2");
                detailData.img3 = finalObject.getString("img3");
                detailData.img4 = finalObject.getString("img4");
                detailData.img5 = finalObject.getString("img5");

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
            JSONObject finalObject = null;

            for (int i = 0; i < parentArray.length(); i++) {
                finalObject = parentArray.getJSONObject(i);
                InfoGameData infoGameData = new InfoGameData();
                infoGameData.id = finalObject.getString("id");
                infoGameData.t = finalObject.getString("t");
                infoGameData.i = finalObject.getString("i");
                infoGameData.s = finalObject.getString("s");

                JSONArray itemArray = finalObject.getJSONArray("item");
                infoGameData.infoGameDataItems = getInfoGameDataItem(itemArray);

                JSONArray learningArray = finalObject.getJSONArray("learning");
                infoGameData.infoGameDataLearnings = getInfoGameDataLearning(learningArray);

                JSONArray dodontArray = finalObject.getJSONArray("dodont");
                infoGameData.infoGameDataDodonts = getInfoGameDataDodont(dodontArray);

                JSONArray communicationArray = finalObject.getJSONArray("communication");
                infoGameData.infoGameDataCommunications = getInfoGameDataCommunication(communicationArray);

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
                infoGameDataItem.no = finalObjectItem.getString("no");
                infoGameDataItem.t = finalObjectItem.getString("t");
                infoGameDataItem.i = finalObjectItem.getString("i");
                infoGameDataItem.s = finalObjectItem.getString("s");

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
                infoGameDataLearning.no = finalObjectLearning.getString("no");
                infoGameDataLearning.t = finalObjectLearning.getString("t");
                infoGameDataLearning.i = finalObjectLearning.getString("i");
                infoGameDataLearning.s = finalObjectLearning.getString("s");
                infoGameDataLearning.v = finalObjectLearning.getString("v");

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
                infoGameDataDodont.answer = finalObjectDodont.getString("answer");
                infoGameDataDodont.t = finalObjectDodont.getString("t");
                infoGameDataDodont.i = finalObjectDodont.getString("i");
                infoGameDataDodont.s = finalObjectDodont.getString("s");
                infoGameDataDodont.v = finalObjectDodont.getString("v");

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
                infoGameDataCommunication.qid = finalObjectCommunication.getString("qid");
                infoGameDataCommunication.t = finalObjectCommunication.getString("t");
                infoGameDataCommunication.s = finalObjectCommunication.getString("s");

                JSONArray communicationAnswerArray = finalObjectCommunication.getJSONArray("answer");
                infoGameDataCommunication.infoGameDataCommunicationAnswers = getInfoGameDataCommunicationAnswer(communicationAnswerArray);

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
                infoGameDataCommunicationAnswer.qid = finalObjectCommunicationAnswer.getString("qid");
                infoGameDataCommunicationAnswer.answer = finalObjectCommunicationAnswer.getString("answer");
                infoGameDataCommunicationAnswer.t = finalObjectCommunicationAnswer.getString("t");
                infoGameDataCommunicationAnswer.i = finalObjectCommunicationAnswer.getString("i");
                infoGameDataCommunicationAnswer.s = finalObjectCommunicationAnswer.getString("s");

                result.add(infoGameDataCommunicationAnswer);
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
