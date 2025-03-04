/*
 * Copyright © Yan Zhenjie. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ninggc.trade.address;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ninggc.trade.DAO.Province;
import com.yanzhenjie.loading.dialog.LoadingDialog;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * <p>请求区域列表，这里是从asset读取，实际上可以从服务器读取。</p>
 * Created by YanZhenjie on 2017/6/1.
 */
public class RequestCampusListTask extends AsyncTask<Void, Void, List<Province>> {

    private Context mContext;
    private Callback mCallback;
    private LoadingDialog mLoadingDialog;

    public static final String TAG = "CAMPUS";

    public RequestCampusListTask(Context context, Callback callback) {
        this.mContext = context;
        this.mCallback = callback;
        mLoadingDialog = new LoadingDialog(context);
    }

    @Override
    protected void onPreExecute() {
        if (!mLoadingDialog.isShowing())
            mLoadingDialog.show();
    }

    @Override
    protected void onPostExecute(List<Province> provinces) {
        if (mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
        if (mCallback != null)
            mCallback.callback(provinces);
    }

    @Override
    protected List<Province> doInBackground(Void... params) {
        try {
            InputStream inputStream = mContext.getAssets().open("campus_list.json");
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                arrayOutputStream.write(buffer, 0, len);
            }
            arrayOutputStream.flush();
            arrayOutputStream.close();
            inputStream.close();

            String json = new String(arrayOutputStream.toByteArray());
//            Log.e(TAG, "doInBackground: " + json);
            List<Province> provinces = new Gson().fromJson(json, new TypeToken<List<Province>>(){}.getType());
//            List<Province> provinces = JSON.parseArray(json, Province.class);
            Log.e(TAG, "doInBackground: " + provinces.size());
            Log.e(TAG, "doInBackground: " + new Gson().toJson(provinces.get(0)));
            return provinces;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public interface Callback {
        void callback(List<Province> provinces);
    }
}
