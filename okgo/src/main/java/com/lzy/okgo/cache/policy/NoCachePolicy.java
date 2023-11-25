/*
 * Copyright 2016 jeasonlzy(廖子尧)
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
package com.lzy.okgo.cache.policy;

import android.util.Log;

import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.json.JSONObject;

import java.io.File;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2017/5/25
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class NoCachePolicy<T> extends BaseCachePolicy<T> {
    private int code = 1;
    public NoCachePolicy(Request<T, ? extends Request> request) {
        super(request);
    }

    @Override
    public void onSuccess(final Response<T> success) {
        if(success.body() instanceof File) {
            mCallback.onSuccess("");
            mCallback.onFinish();
        }else
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = (String) success.body();
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.optInt("code");
                    String data = jsonObject.optString("data");
                    String msg = jsonObject.optString("msg");
                    String tag = (String) success.getRawResponse().request().tag();
                    if (code == 200){
                       /* if(data == null||data.equals("null")&&!isReturn(tag)){
//                            mCallback.onError("接口返回数据异常！");
                        }else*/
                            mCallback.onSuccess(data);
                    }else{
                        Log.e("wyy","dddddddddddddd");
                        mCallback.onError(code,msg);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                mCallback.onFinish();
            }
        });
    }

    private boolean isReturn(String tag) {
        if(tag.equals("heartBeat"))
            return true;
        return false;
    }

    @Override
    public void onError(final Response<T> error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCallback.onError(-1,"网络异常！");
                mCallback.onFinish();
            }
        });
    }

    @Override
    public Response<T> requestSync(CacheEntity<T> cacheEntity) {
        try {
            prepareRawCall();
        } catch (Throwable throwable) {
            return Response.error(false, rawCall, null, throwable);
        }
        return requestNetworkSync();
    }

    @Override
    public void requestAsync(CacheEntity<T> cacheEntity, Callback<T> callback) {
        mCallback = callback;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCallback.onStart(request);

                try {
                    prepareRawCall();
                } catch (Throwable throwable) {
                    Response<T> error = Response.error(false, rawCall, null, throwable);
                    mCallback.onError(-1,"网络异常！");
                    mCallback.onFinish();
                    return;
                }
                requestNetworkAsync();
            }
        });
    }
}
