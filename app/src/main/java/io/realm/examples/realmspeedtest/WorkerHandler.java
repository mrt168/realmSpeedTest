/*
 * Copyright 2014 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.realm.examples.realmspeedtest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.List;

import io.realm.Realm;
import io.realm.examples.realmspeedtest.models.PostData;

public class WorkerHandler extends Handler {

    public static final int ADD_TIMESTAMP = 1;
    public static final int REMOVE_TIMESTAMP = 2;
    public static final int ADD_DATA = 3;

    public static final String ACTION = "action";
    public static final String TIMESTAMP = "timestamp";


    private Realm realm;
    private List<String> postAddressLists;

    long startTime;
    long endTime;

    public WorkerHandler(Realm realm) {
        this.realm = realm;
    }

    public void setList(List<String> lists) {
        this.postAddressLists = lists;
    }

    @Override
    public void handleMessage(Message msg) {
        final Bundle bundle = msg.getData();

        final int action = bundle.getInt(ACTION);
        final String timestamp = bundle.getString(TIMESTAMP);
        startTime = System.currentTimeMillis();

        switch (action) {
            case ADD_DATA:

                realm.beginTransaction();
                for (String data : postAddressLists) {
                    String[] str = data.split(",");

                    // Create a new object
                    PostData post = realm.createObject(PostData.class);
                    post.setPostCode(str[2]);
                    post.setAddress(str[6] + str[7] + str[8]);

                }
                realm.commitTransaction();
                endTime = System.currentTimeMillis();
                Log.d("time", (endTime - startTime) + "m秒");
                break;

        }
    }
}
