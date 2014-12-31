package com.roc.http;

import android.content.Context;
import android.os.AsyncTask;

import com.roc.common.DebugLog;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * AsyncTask异步处理类
 *
 * @author Mr.Zheng
 * @date 2014年8月2日23:36:48
 */
public class CustomAsyncTask extends AsyncTask<String, Integer, String> {
    private CustomAsyncTaskCallback mCallback = null;
    private List<NameValuePair> listNameValuePairs;
    private Mode mMode;
    private int statusCode = -1;
    private Context mContext;

    public static enum Mode {
        GET, POST
    }

    /**
     * 构造方法,默认通过POST方式获取HttpResponsse
     */
    public CustomAsyncTask(Context context) {
        this(context, null, Mode.POST);
    }

    /**
     * @param mode http请求模式 {@link Mode}
     */
    public CustomAsyncTask(Context context, Mode mode) {
        this(context, null, mode);
    }

    /**
     * 构造方法,默认通过POST方式获取HttpResponsse
     *
     * @param listNameValuePairs NameValuePair参数
     */
    public CustomAsyncTask(Context context, List<NameValuePair> listNameValuePairs) {
        this(context, listNameValuePairs, Mode.POST);
    }

    /**
     * 构造方法
     *
     * @param listNameValuePairs NameValuePair参数
     * @param mode               设置获取HttpResponse的模式 <br>
     */
    public CustomAsyncTask(Context context, List<NameValuePair> listNameValuePairs, Mode mode) {
        if (listNameValuePairs != null)
            this.listNameValuePairs = listNameValuePairs;
        else
            this.listNameValuePairs = new ArrayList<NameValuePair>();
        // 默认post模式
        this.mMode = mode;
        this.mContext = context;
    }

    /**
     * 设置回调
     *
     * @param callback
     */
    public CustomAsyncTask setCallBack(CustomAsyncTaskCallback callback) {
        this.mCallback = callback;
        return this;
    }

    @Override
    protected void onPreExecute() {
        if (mCallback != null)
            mCallback.onPreExecute();
    }

    @Override
    public String doInBackground(String... params) {

        HttpResponse response = null;
        String responseText = "";
        try {
            switch (mMode) {
                case GET:
                    NameValuePair[] nameValuePairs = new NameValuePair[listNameValuePairs.size()];
                    for (int i = 0; i < listNameValuePairs.size(); i++) {
                        nameValuePairs[i] = listNameValuePairs.get(i);
                    }
                    response = CustomHttpClient.getHttpResponseByGET(mContext, params[0], nameValuePairs);
                    break;
                case POST:
                    response = CustomHttpClient.getHttpResponseByPOST(mContext, params[0], listNameValuePairs);
                    break;
                default:
                    break;
            }

            statusCode = response.getStatusLine().getStatusCode();
            responseText = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
        } catch (Exception e) {
            DebugLog.e(e.getMessage());
            e.printStackTrace();
        }

        return responseText;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (mCallback != null)
            mCallback.onProgressUpdate();
    }

    @Override
    protected void onPostExecute(String responseText) {
        if (mCallback != null)
            mCallback.onPostExecute(statusCode, responseText);
    }

    /**
     * CustomAsyncTask回调接口
     */
    public interface CustomAsyncTaskCallback {
        void onPreExecute();

        void onProgressUpdate();

        /**
         * @param statusCode   状态码
         * @param responseText 请求返回的responseText
         */
        void onPostExecute(int statusCode, String responseText);

    }

    /**
     * 根据需要重写3个回调方法
     *
     * @author Mr.Zheng
     * @date 2014年9月15日 下午6:08:49
     */
    public static class CustomAsyncTaskBaseCallback implements CustomAsyncTaskCallback {
        @Override
        public void onPreExecute() {

        }

        @Override
        public void onProgressUpdate() {

        }

        @Override
        public void onPostExecute(int statusCode, String responseText) {

        }
    }
}
