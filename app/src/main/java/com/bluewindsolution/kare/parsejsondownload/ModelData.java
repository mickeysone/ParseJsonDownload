package com.bluewindsolution.kare.parsejsondownload;

import java.util.ArrayList;

/**
 * Created by Pruxasin on 3/2/2559.
 */
class DetailData {
    public String name, detail, view, download, img1, img2, img3, img4, img5;
}

class MissionData {
    public String name, lang, id, img, category, detail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}

class InfoGameData {
    public String id, t, i, s;
    public ArrayList<InfoGameDataItem> infoGameDataItems;
    public ArrayList<InfoGameDataLearning> infoGameDataLearnings;
    public ArrayList<InfoGameDataDodont> infoGameDataDodonts;
    public ArrayList<InfoGameDataCommunication> infoGameDataCommunications;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public ArrayList<InfoGameDataItem> getInfoGameDataItems() {
        return infoGameDataItems;
    }

    public void setInfoGameDataItems(ArrayList<InfoGameDataItem> infoGameDataItems) {
        this.infoGameDataItems = infoGameDataItems;
    }

    public ArrayList<InfoGameDataLearning> getInfoGameDataLearnings() {
        return infoGameDataLearnings;
    }

    public void setInfoGameDataLearnings(ArrayList<InfoGameDataLearning> infoGameDataLearnings) {
        this.infoGameDataLearnings = infoGameDataLearnings;
    }

    public ArrayList<InfoGameDataDodont> getInfoGameDataDodonts() {
        return infoGameDataDodonts;
    }

    public void setInfoGameDataDodonts(ArrayList<InfoGameDataDodont> infoGameDataDodonts) {
        this.infoGameDataDodonts = infoGameDataDodonts;
    }

    public ArrayList<InfoGameDataCommunication> getInfoGameDataCommunications() {
        return infoGameDataCommunications;
    }

    public void setInfoGameDataCommunications(ArrayList<InfoGameDataCommunication> infoGameDataCommunications) {
        this.infoGameDataCommunications = infoGameDataCommunications;
    }
}

class InfoGameDataItem {
    public String no, t, i, s;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
}

class InfoGameDataLearning {
    public String no, t, i, s, v;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }
}

class InfoGameDataDodont {
    public String answer, t, i, s, v;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }
}

class InfoGameDataCommunication {
    public String qid, t, s;
    public ArrayList<InfoGameDataCommunicationAnswer> infoGameDataCommunicationAnswers;

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public ArrayList<InfoGameDataCommunicationAnswer> getInfoGameDataCommunicationAnswers() {
        return infoGameDataCommunicationAnswers;
    }

    public void setInfoGameDataCommunicationAnswers(ArrayList<InfoGameDataCommunicationAnswer> infoGameDataCommunicationAnswers) {
        this.infoGameDataCommunicationAnswers = infoGameDataCommunicationAnswers;
    }
}

class InfoGameDataCommunicationAnswer {
    public String qid, answer, t, i, s;

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
}

