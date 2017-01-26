package com.sorcerer.sorcery.iconpack.net.coolapk;

import com.google.gson.annotations.SerializedName;

import java.util.Iterator;
import java.util.List;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/1/26
 */

public class CoolapkSearchResult{

    @SerializedName("data")
    private List<CoolapkSearchBean> mData;

    public List<CoolapkSearchBean> getData() {
        return mData;
    }

    public void setData(List<CoolapkSearchBean> data) {
        mData = data;
    }

    public static class CoolapkSearchBean {
        /**
         * id : 3834
         * apktype : 1
         * catid : 7
         * title : QQ
         * version : 6.6.9
         * apkname : com.tencent.mobileqq
         * apkname2 :
         * apkmd5 : 32cb7178a596a9bf8a102bfecdf3a521
         * apkversionname : 6.6.9
         * apkversioncode : 482
         * apksize : 37.79M
         * apklength : 39620826
         * sdkversion : 8
         * romversion : 2.2
         * ishp : 1
         * ishot : 0
         * isbiz : 0
         * iscps : 0
         * recommend : 0
         * digest : 0
         * logo : http://image.coolapk.com/apk_logo/2016/1121/0_1479693197_6924.png.t.png
         * shorttitle : QQ
         * hotlabel :
         * shortlabel :
         * keywords : QQ,腾讯,im,即时聊天,装机必备
         * description : QQ各种客户端一堆，总之是轻量和功能难两全，要大而全就装这个标准版
         * developeruid : 0
         * developername : Tencent Technology (Shenzhen) Company Ltd.
         * star : 2
         * score : 2.0
         * adminscore : 4.0
         * downnum : 9700900
         * follownum : 7737
         * votenum : 8320
         * favnum : 274
         * commentnum : 16137
         * replynum : 49
         * pubdate : 1309844093
         * lastupdate : 1484709697
         * status : 1
         * index_name : app
         * entityType : apk
         * packageName : com.tencent.mobileqq
         * shortTags : 腾讯 QQ 聊天 交友
         * apkTypeName : 应用
         * apkTypeUrl : /apk/
         * apkUrl : /apk/com.tencent.mobileqq
         * url : /apk/com.tencent.mobileqq
         * catDir : /apk/sns/
         * catName : 社交聊天
         * downCount : 970万
         * followCount : 7737
         * voteCount : 8320
         * commentCount : 1.6万
         * updateFlag : Update
         * extraFlag :
         * apkRomVersion : 2.2+
         * statusText : 已发布
         * pubStatusText : 首页可见
         */

        @SerializedName("id")
        private String mId;
        @SerializedName("apktype")
        private String mApktype;
        @SerializedName("catid")
        private String mCatid;
        @SerializedName("title")
        private String mTitle;
        @SerializedName("version")
        private String mVersion;
        @SerializedName("apkname")
        private String mApkname;
        @SerializedName("apkname2")
        private String mApkname2;
        @SerializedName("apkmd5")
        private String mApkmd5;
        @SerializedName("apkversionname")
        private String mApkversionname;
        @SerializedName("apkversioncode")
        private String mApkversioncode;
        @SerializedName("apksize")
        private String mApksize;
        @SerializedName("apklength")
        private String mApklength;
        @SerializedName("sdkversion")
        private String mSdkversion;
        @SerializedName("romversion")
        private String mRomversion;
        @SerializedName("ishp")
        private String mIshp;
        @SerializedName("ishot")
        private String mIshot;
        @SerializedName("isbiz")
        private String mIsbiz;
        @SerializedName("iscps")
        private String mIscps;
        @SerializedName("recommend")
        private String mRecommend;
        @SerializedName("digest")
        private String mDigest;
        @SerializedName("logo")
        private String mLogo;
        @SerializedName("shorttitle")
        private String mShorttitle;
        @SerializedName("hotlabel")
        private String mHotlabel;
        @SerializedName("shortlabel")
        private String mShortlabel;
        @SerializedName("keywords")
        private String mKeywords;
        @SerializedName("description")
        private String mDescription;
        @SerializedName("developeruid")
        private String mDeveloperuid;
        @SerializedName("developername")
        private String mDevelopername;
        @SerializedName("star")
        private String mStar;
        @SerializedName("score")
        private String mScore;
        @SerializedName("adminscore")
        private String mAdminscore;
        @SerializedName("downnum")
        private String mDownnum;
        @SerializedName("follownum")
        private String mFollownum;
        @SerializedName("votenum")
        private int mVotenum;
        @SerializedName("favnum")
        private String mFavnum;
        @SerializedName("commentnum")
        private String mCommentnum;
        @SerializedName("replynum")
        private String mReplynum;
        @SerializedName("pubdate")
        private String mPubdate;
        @SerializedName("lastupdate")
        private String mLastupdate;
        @SerializedName("status")
        private String mStatus;
        @SerializedName("index_name")
        private String mIndexName;
        @SerializedName("entityType")
        private String mEntityType;
        @SerializedName("packageName")
        private String mPackageName;
        @SerializedName("shortTags")
        private String mShortTags;
        @SerializedName("apkTypeName")
        private String mApkTypeName;
        @SerializedName("apkTypeUrl")
        private String mApkTypeUrl;
        @SerializedName("apkUrl")
        private String mApkUrl;
        @SerializedName("url")
        private String mUrl;
        @SerializedName("catDir")
        private String mCatDir;
        @SerializedName("catName")
        private String mCatName;
        @SerializedName("downCount")
        private String mDownCount;
        @SerializedName("followCount")
        private String mFollowCount;
        @SerializedName("voteCount")
        private int mVoteCount;
        @SerializedName("commentCount")
        private String mCommentCount;
        @SerializedName("updateFlag")
        private String mUpdateFlag;
        @SerializedName("extraFlag")
        private String mExtraFlag;
        @SerializedName("apkRomVersion")
        private String mApkRomVersion;
        @SerializedName("statusText")
        private String mStatusText;
        @SerializedName("pubStatusText")
        private String mPubStatusText;

        public String getId() {
            return mId;
        }

        public void setId(String id) {
            mId = id;
        }

        public String getApktype() {
            return mApktype;
        }

        public void setApktype(String apktype) {
            mApktype = apktype;
        }

        public String getCatid() {
            return mCatid;
        }

        public void setCatid(String catid) {
            mCatid = catid;
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String title) {
            mTitle = title;
        }

        public String getVersion() {
            return mVersion;
        }

        public void setVersion(String version) {
            mVersion = version;
        }

        public String getApkname() {
            return mApkname;
        }

        public void setApkname(String apkname) {
            mApkname = apkname;
        }

        public String getApkname2() {
            return mApkname2;
        }

        public void setApkname2(String apkname2) {
            mApkname2 = apkname2;
        }

        public String getApkmd5() {
            return mApkmd5;
        }

        public void setApkmd5(String apkmd5) {
            mApkmd5 = apkmd5;
        }

        public String getApkversionname() {
            return mApkversionname;
        }

        public void setApkversionname(String apkversionname) {
            mApkversionname = apkversionname;
        }

        public String getApkversioncode() {
            return mApkversioncode;
        }

        public void setApkversioncode(String apkversioncode) {
            mApkversioncode = apkversioncode;
        }

        public String getApksize() {
            return mApksize;
        }

        public void setApksize(String apksize) {
            mApksize = apksize;
        }

        public String getApklength() {
            return mApklength;
        }

        public void setApklength(String apklength) {
            mApklength = apklength;
        }

        public String getSdkversion() {
            return mSdkversion;
        }

        public void setSdkversion(String sdkversion) {
            mSdkversion = sdkversion;
        }

        public String getRomversion() {
            return mRomversion;
        }

        public void setRomversion(String romversion) {
            mRomversion = romversion;
        }

        public String getIshp() {
            return mIshp;
        }

        public void setIshp(String ishp) {
            mIshp = ishp;
        }

        public String getIshot() {
            return mIshot;
        }

        public void setIshot(String ishot) {
            mIshot = ishot;
        }

        public String getIsbiz() {
            return mIsbiz;
        }

        public void setIsbiz(String isbiz) {
            mIsbiz = isbiz;
        }

        public String getIscps() {
            return mIscps;
        }

        public void setIscps(String iscps) {
            mIscps = iscps;
        }

        public String getRecommend() {
            return mRecommend;
        }

        public void setRecommend(String recommend) {
            mRecommend = recommend;
        }

        public String getDigest() {
            return mDigest;
        }

        public void setDigest(String digest) {
            mDigest = digest;
        }

        public String getLogo() {
            return mLogo;
        }

        public void setLogo(String logo) {
            mLogo = logo;
        }

        public String getShorttitle() {
            return mShorttitle;
        }

        public void setShorttitle(String shorttitle) {
            mShorttitle = shorttitle;
        }

        public String getHotlabel() {
            return mHotlabel;
        }

        public void setHotlabel(String hotlabel) {
            mHotlabel = hotlabel;
        }

        public String getShortlabel() {
            return mShortlabel;
        }

        public void setShortlabel(String shortlabel) {
            mShortlabel = shortlabel;
        }

        public String getKeywords() {
            return mKeywords;
        }

        public void setKeywords(String keywords) {
            mKeywords = keywords;
        }

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String description) {
            mDescription = description;
        }

        public String getDeveloperuid() {
            return mDeveloperuid;
        }

        public void setDeveloperuid(String developeruid) {
            mDeveloperuid = developeruid;
        }

        public String getDevelopername() {
            return mDevelopername;
        }

        public void setDevelopername(String developername) {
            mDevelopername = developername;
        }

        public String getStar() {
            return mStar;
        }

        public void setStar(String star) {
            mStar = star;
        }

        public String getScore() {
            return mScore;
        }

        public void setScore(String score) {
            mScore = score;
        }

        public String getAdminscore() {
            return mAdminscore;
        }

        public void setAdminscore(String adminscore) {
            mAdminscore = adminscore;
        }

        public String getDownnum() {
            return mDownnum;
        }

        public void setDownnum(String downnum) {
            mDownnum = downnum;
        }

        public String getFollownum() {
            return mFollownum;
        }

        public void setFollownum(String follownum) {
            mFollownum = follownum;
        }

        public int getVotenum() {
            return mVotenum;
        }

        public void setVotenum(int votenum) {
            mVotenum = votenum;
        }

        public String getFavnum() {
            return mFavnum;
        }

        public void setFavnum(String favnum) {
            mFavnum = favnum;
        }

        public String getCommentnum() {
            return mCommentnum;
        }

        public void setCommentnum(String commentnum) {
            mCommentnum = commentnum;
        }

        public String getReplynum() {
            return mReplynum;
        }

        public void setReplynum(String replynum) {
            mReplynum = replynum;
        }

        public String getPubdate() {
            return mPubdate;
        }

        public void setPubdate(String pubdate) {
            mPubdate = pubdate;
        }

        public String getLastupdate() {
            return mLastupdate;
        }

        public void setLastupdate(String lastupdate) {
            mLastupdate = lastupdate;
        }

        public String getStatus() {
            return mStatus;
        }

        public void setStatus(String status) {
            mStatus = status;
        }

        public String getIndexName() {
            return mIndexName;
        }

        public void setIndexName(String indexName) {
            mIndexName = indexName;
        }

        public String getEntityType() {
            return mEntityType;
        }

        public void setEntityType(String entityType) {
            mEntityType = entityType;
        }

        public String getPackageName() {
            return mPackageName;
        }

        public void setPackageName(String packageName) {
            mPackageName = packageName;
        }

        public String getShortTags() {
            return mShortTags;
        }

        public void setShortTags(String shortTags) {
            mShortTags = shortTags;
        }

        public String getApkTypeName() {
            return mApkTypeName;
        }

        public void setApkTypeName(String apkTypeName) {
            mApkTypeName = apkTypeName;
        }

        public String getApkTypeUrl() {
            return mApkTypeUrl;
        }

        public void setApkTypeUrl(String apkTypeUrl) {
            mApkTypeUrl = apkTypeUrl;
        }

        public String getApkUrl() {
            return mApkUrl;
        }

        public void setApkUrl(String apkUrl) {
            mApkUrl = apkUrl;
        }

        public String getUrl() {
            return mUrl;
        }

        public void setUrl(String url) {
            mUrl = url;
        }

        public String getCatDir() {
            return mCatDir;
        }

        public void setCatDir(String catDir) {
            mCatDir = catDir;
        }

        public String getCatName() {
            return mCatName;
        }

        public void setCatName(String catName) {
            mCatName = catName;
        }

        public String getDownCount() {
            return mDownCount;
        }

        public void setDownCount(String downCount) {
            mDownCount = downCount;
        }

        public String getFollowCount() {
            return mFollowCount;
        }

        public void setFollowCount(String followCount) {
            mFollowCount = followCount;
        }

        public int getVoteCount() {
            return mVoteCount;
        }

        public void setVoteCount(int voteCount) {
            mVoteCount = voteCount;
        }

        public String getCommentCount() {
            return mCommentCount;
        }

        public void setCommentCount(String commentCount) {
            mCommentCount = commentCount;
        }

        public String getUpdateFlag() {
            return mUpdateFlag;
        }

        public void setUpdateFlag(String updateFlag) {
            mUpdateFlag = updateFlag;
        }

        public String getExtraFlag() {
            return mExtraFlag;
        }

        public void setExtraFlag(String extraFlag) {
            mExtraFlag = extraFlag;
        }

        public String getApkRomVersion() {
            return mApkRomVersion;
        }

        public void setApkRomVersion(String apkRomVersion) {
            mApkRomVersion = apkRomVersion;
        }

        public String getStatusText() {
            return mStatusText;
        }

        public void setStatusText(String statusText) {
            mStatusText = statusText;
        }

        public String getPubStatusText() {
            return mPubStatusText;
        }

        public void setPubStatusText(String pubStatusText) {
            mPubStatusText = pubStatusText;
        }
    }
}
