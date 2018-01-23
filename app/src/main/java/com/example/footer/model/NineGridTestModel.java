package com.example.footer.model;



import java.io.Serializable;
import java.util.List;

/**
 * Created by 乃军 on 2017/11/7.
 */

public class NineGridTestModel implements Serializable {
   /* public List<String> urlList = new ArrayList<>();

      public List<String> getUrlList() {
        return urlList;
    }*/
    public String spoorId;//足迹id
    public String createTime;
    public String spoorContent;
    public String spoorTagOne;
    public String spoorTagTwo;
    public String spoorTagThree;
    public String benefitCount;//收益多少
    public String isBenefit;//是否有收益
    public String iscollect;//是否收藏
    public String userIcon;//用户头像
    public String nickName;//用户名称
    public String comments;//评论
    public String userPhone;//电话号码
    public List<Picture> spoorPictures;
    public String spoorVideos;
    public List<Comment> spoorComment;

    public void setSpoorId(String spoorId) {
        this.spoorId = spoorId;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setSpoorContent(String spoorContent) {
        this.spoorContent = spoorContent;
    }

    public void setSpoorTagOne(String spoorTagOne) {
        this.spoorTagOne = spoorTagOne;
    }

    public void setSpoorTagTwo(String spoorTagTwo) {
        this.spoorTagTwo = spoorTagTwo;
    }

    public void setSpoorTagThree(String spoorTagThree) {
        this.spoorTagThree = spoorTagThree;
    }

    public void setBenefitCount(String benefitCount) {
        this.benefitCount = benefitCount;
    }

    public void setIsBenefit(String isBenefit) {
        this.isBenefit = isBenefit;
    }

    public void setIscollect(String iscollect) {
        this.iscollect = iscollect;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setSpoorPictures(List<Picture> spoorPictures) {
        this.spoorPictures = spoorPictures;
    }

    public void setSpoorVideos(String spoorVideos) {
        this.spoorVideos = spoorVideos;
    }

    public String getSpoorId() {
        return spoorId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getSpoorContent() {
        return spoorContent;
    }

    public String getSpoorTagOne() {
        return spoorTagOne;
    }

    public String getSpoorTagTwo() {
        return spoorTagTwo;
    }

    public String getSpoorTagThree() {
        return spoorTagThree;
    }

    public String getBenefitCount() {
        return benefitCount;
    }

    public String getIsBenefit() {
        return isBenefit;
    }

    public String getIscollect() {
        return iscollect;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public String getNickName() {
        return nickName;
    }

    public String getComments() {
        return comments;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public List<Picture> getSpoorPictures() {
        return spoorPictures;
    }

    public String getSpoorVideos() {
        return spoorVideos;
    }

    public List<Comment> getSpoorComment() {
        return spoorComment;
    }

    public void setSpoorComment(List<Comment> spoorComment) {
        this.spoorComment = spoorComment;
    }

    @Override
    public String toString() {
        return "NineGridTestModel{" +
                "spoorId='" + spoorId + '\'' +
                ", createTime='" + createTime + '\'' +
                ", spoorContent='" + spoorContent + '\'' +
                ", spoorTagOne='" + spoorTagOne + '\'' +
                ", spoorTagTwo='" + spoorTagTwo + '\'' +
                ", spoorTagThree='" + spoorTagThree + '\'' +
                ", benefitCount='" + benefitCount + '\'' +
                ", isBenefit='" + isBenefit + '\'' +
                ", iscollect='" + iscollect + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", nickName='" + nickName + '\'' +
                ", comments='" + comments + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", spoorPictures=" + spoorPictures +
                ", spoorVideos='" + spoorVideos + '\'' +
                ", spoorComment=" + spoorComment +
                '}';
    }
}
