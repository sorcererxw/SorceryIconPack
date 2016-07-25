package com.sorcerer.sorcery.iconpack.net.leancloud;

import android.os.AsyncTask;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;

import java.util.List;

/**
 * Created by Sorcerer on 2016/7/22.
 */
public class RequestManager {
    private final String TAG = getClass().getSimpleName();

//    private class requestTask extends AsyncTask {
//
//        private List<RequestBean> mRequestList;
//
//        public requestTask(List<RequestBean> requestList) {
//            mRequestList = requestList;
//        }

//        @Override
//        protected Void doInBackground(Object[] p) {
//            AVObject queryRequest = findByPackage(mRequestBean.getAppPackage());
//            if (queryRequest == null) {
//                Log.d(TAG, "null object");
//                AVObject avRequest = new AVObject(TABLE_NAME);
//                avRequest.put(COLUMN_APP_PACKAGE, mRequestBean.getAppPackage());
//                avRequest.put(COLUMN_APP_DEFAULT_NAME, mRequestBean.getAppDefaultName());
//                avRequest.saveInBackground();
//            } else {
//                updateTimes(queryRequest);
//            }
//            return null;
//        }
//    }

    private class requestTask extends AsyncTask {

        private List<RequestBean> mRequestBeanList;

        public requestTask(List<RequestBean> requestBeanList) {
            mRequestBeanList = requestBeanList;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            for (int i = 0; i < mRequestBeanList.size(); i++) {
                try {
                    RequestBean tmp = findRequest(mRequestBeanList.get(i).getAppPackage());
                    if (tmp != null) {
//                        mRequestBeanList.get(i).setRequestTimes(tmp.getRequestTimes() + 1);
                        mRequestBeanList.get(i).setObjectId(tmp.getObjectId());
                        tmp.delete();
                        mRequestBeanList.get(i).save();
                    } else {
                        mRequestBeanList.get(i).save();
                    }
                } catch (AVException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        RequestBean findRequest(String packageName) throws AVException {
            AVQuery<RequestBean> query = AVQuery.getQuery(RequestBean.class);
            query.whereEqualTo(RequestBean.COLUMN_APP_PACKAGE, packageName);
            List<RequestBean> list = query.find();
            if (list.size() > 0) {
                return list.get(0);
            } else {
                return null;
            }
        }
    }

    public void saveRequest(List<RequestBean> requestList) {
        new requestTask(requestList).execute();
    }

}
