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

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.examples.realmspeedtest.models.PostData;


public class RealmTestActivity extends Activity {

    private Realm realm;
    private WorkerThread workerThread;
    private List<String> postAddressLists;
    private Button realmStartBtn;
    private Button sqLiteStartBtn;
    private TextView text;

    SQLiteOpenHelper mHelper;
    SQLiteDatabase mWritableDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        Realm.deleteRealmFile(this);


        realm = Realm.getInstance(this);

        realmStartBtn = (Button) findViewById(R.id.realm_start_btn);
        realmStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = buildMessage(WorkerHandler.ADD_DATA, Long.toString(System.currentTimeMillis()));
                workerThread.workerHandler.sendMessage(message);
            }
        });
        Button realmReadStartBtn = (Button) findViewById(R.id.realm_read_start_btn);
        realmReadStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.refresh();
                long startTime = System.currentTimeMillis();

                RealmResults<PostData> postDatas = realm.where(PostData.class).findAll();

                long endTime = System.currentTimeMillis();
                Log.d("time", (endTime - startTime) + "m秒");
            }
        });
        Button sqLiteReadStartBtn = (Button) findViewById(R.id.sqlite_read_start_btn);
        sqLiteReadStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mWritableDb == null || mWritableDb.isOpen() == false) {
                    try {
                        // mHelper.close();
                        mWritableDb = mHelper.getWritableDatabase();
                        // String text = mWritableDb.isOpen() ? "open": "not open" ;
                        // Toast.makeText(this, text,
                        // Toast.LENGTH_LONG).show();
                    } catch (SQLiteException e) {

                        return;
                    }
                }

                List<PostData> posts = new ArrayList<PostData>();
                long startTime = System.currentTimeMillis();
                try {
                    //queryメソッドでデータを取得
                    String[] cols = null;
                    String selection = "postCode = ?";
                    String[] selectionArgs = {"1"};
                    String groupBy = null;
                    String having = null;
                    String orderBy = null;
                    // データベース処理開始
                    mWritableDb.beginTransaction();

                    Cursor cursor = mWritableDb.query("TABLE_NAME", new String[]{"*"}, null, null, null, null, "ID DESC");

                    if(cursor.moveToFirst()) {
                        while (cursor.moveToNext()){
                            PostData post = new PostData();
                            post.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                            post.setPostCode(cursor.getString(cursor.getColumnIndex("postCode")));
                            posts.add(post);
                        }
                    }
                    mWritableDb.setTransactionSuccessful();
                } finally {
                    mWritableDb.endTransaction();
                }
                long endTime = System.currentTimeMillis();
                Log.d("time", (endTime - startTime) + "m秒"+posts.get(0).getAddress());
            }
        });
        Button realmCountStartBtn = (Button) findViewById(R.id.realm_count_start_btn);
        realmCountStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.refresh();
                long startTime = System.currentTimeMillis();
                long count = realm.where(PostData.class).count();
                long endTime = System.currentTimeMillis();
                Log.d("time", (endTime - startTime) + "m秒"+"count"+count);
            }
        });
        Button sqLiteCountStartBtn = (Button) findViewById(R.id.sqlite_count_start_btn);
        sqLiteCountStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWritableDb == null || mWritableDb.isOpen() == false) {
                    try {
                        // mHelper.close();
                        mWritableDb = mHelper.getWritableDatabase();
                        // String text = mWritableDb.isOpen() ? "open": "not open" ;
                        // Toast.makeText(this, text,
                        // Toast.LENGTH_LONG).show();
                    } catch (SQLiteException e) {

                        return;
                    }
                }
                int count = 0;

                long startTime = System.currentTimeMillis();
                try {
                    mWritableDb.beginTransaction();
                    Cursor cursor = mWritableDb.rawQuery(
                            String.format("SELECT COUNT(*) FROM %s ", "TABLE_NAME"), null);

                    if(cursor.moveToNext()){
                        count = cursor.getInt(0);
                    }
                    mWritableDb.setTransactionSuccessful();
                } finally {
                    mWritableDb.endTransaction();
                }
                long endTime = System.currentTimeMillis();
                Log.d("time", (endTime - startTime) + "m秒"+"count"+count);
            }
        });
        sqLiteStartBtn = (Button) findViewById(R.id.sqlite_start_btn);
        sqLiteStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // SQLiteOpenHelper#getWritableDatabase() で
                // データベースを開いて、書き込み用の SQLiteDatabase# を取得
                // じゃなかった。こいつは読み書き両方できる
                // データベースが無ければ作成するが、この場合
                // SQLiteOpenHelper#onCreate(SQLiteDatabase db)
                // がコールされる
                if (mWritableDb == null || mWritableDb.isOpen() == false) {
                    try {
                        // mHelper.close();
                        mWritableDb = mHelper.getWritableDatabase();
                        // String text = mWritableDb.isOpen() ? "open": "not open" ;
                        // Toast.makeText(this, text,
                        // Toast.LENGTH_LONG).show();
                    } catch (SQLiteException e) {

                        return;
                    }
                }

                // データベース処理開始

                long startTime = System.currentTimeMillis();
                mWritableDb.beginTransaction();
                ContentValues values = new ContentValues();
                for (String data : postAddressLists) {
                    String[] str = postAddressLists.get(0).split(",");
                    // Create a new object

                    values.put("postCode", str[2]);
                    values.put("address", str[6] + str[7] + str[8]);
                    mWritableDb.insert("TABLE_NAME", null, values);

                }

                mWritableDb.setTransactionSuccessful();
                mWritableDb.endTransaction();
                long endTime = System.currentTimeMillis();
                Log.d("time", (endTime - startTime) + "m秒");


            }
        });
//        text = (TextView) findViewById(R.id.result_time);
        loadCSV();
//        final MyAdapter adapter = new MyAdapter(this, R.id.listView, timeStamps, true);
//        ListView listView = (ListView) findViewById(R.id.listView);
//        listView.setAdapter(adapter);
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                TimeStamp timeStamp = adapter.getRealmResults().get(i);
//                Message message = buildMessage(WorkerHandler.REMOVE_TIMESTAMP, timeStamp.getTimeStamp());
//
//                workerThread.workerHandler.sendMessage(message);
//                return true;
//            }
//        });
    }

    private void loadCSV() {
        CsvLoad csv = new CsvLoad(getResources().getString(R.string.csv_name), getAssets(), this);
        csv.execute(getResources().getString(R.string.csv_name));
    }

    public void saveList(List<String> postAddressLists) {
        this.postAddressLists = postAddressLists;
        workerThread.workerHandler.setList(postAddressLists);
        realmStartBtn.setVisibility(View.VISIBLE);
        sqLiteStartBtn.setVisibility(View.VISIBLE);
    }

    public void setTime(String time) {
        text.setText(time);
    }

    @Override
    protected void onPause() {
        super.onPause();
        workerThread.workerHandler.getLooper().quit();
        mHelper.close();

        if (mWritableDb != null) {
            Toast.makeText(this, "close mWritableDb", Toast.LENGTH_LONG).show();
            mWritableDb.close();
            // mReadableDb.close();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        workerThread = new WorkerThread(this);
        workerThread.start();

        // SQLiteOpenHelperインスタンスを取得するための情報
        // コンテキスト
        Context CONTEXT = this;
        // データベース名 / メモリ上で使用する場合は null
        String DATABASE_NAME = "mydatabase.sqlite3";
        // CursorFactory : Cursorを作成するためのインスタンス
        SQLiteDatabase.CursorFactory CURSOR_FACTORY = null;
        // バージョン、1から始まる。
        // バージョンが変更されたときにコールされる
        // onUpgrade と onDowngrade の引数に渡される
        int DATABASE_VERSION = 1;

        // MySQLiteOpenHelperインスタンスを生成
        mHelper = new MySQLiteOpenHelper(CONTEXT, DATABASE_NAME,
                CURSOR_FACTORY, DATABASE_VERSION);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }


    private static Message buildMessage(int action, String timeStamp) {
        Bundle bundle = new Bundle(2);
        bundle.putInt(WorkerHandler.ACTION, action);
        bundle.putString(WorkerHandler.TIMESTAMP, timeStamp);
        Message message = new Message();
        message.setData(bundle);
        return message;
    }


    // SQLiteOpenHelper は抽象クラスであり、
    // 2つの抽象メソッド onCreate, onUpgrade を持つため
    // これらを実装しなければならない。
    private class MySQLiteOpenHelper extends SQLiteOpenHelper {

        Context mContext;

        // 引数の無いコンストラクタが無いため作成しなければならない
        // コンストラクタで接続するデータベースを指定する
        public MySQLiteOpenHelper(Context context, String name,
                                  SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // データベース作成時（
            // SQLDatabase#getWritableDatabase()
            // SQLDatabase#getReadableDatabase()
            // が成功したとき）にコールされる

            Toast.makeText(mContext, "onCreate", Toast.LENGTH_LONG).show();

            // データベース処理開始
            db.beginTransaction();
            try {
                // テーブル作成を実行
                db.execSQL("CREATE TABLE TABLE_NAME ( ID INTEGER PRIMARY KEY AUTOINCREMENT, postCode TEXT , address TEXT);");
                db.setTransactionSuccessful();
            } finally {
                // データベース終了処理
                db.endTransaction();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // バージョンが上がったときにコールされる
            Toast.makeText(mContext, "onUpgrade", Toast.LENGTH_LONG).show();

            db.execSQL("DROP TABLE IF EXISTS test_table");
            onCreate(db);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            // データベースが開かれたときに実行される
            // これの実装は任意
            super.onOpen(db);
            Toast.makeText(mContext, "onOpen", Toast.LENGTH_LONG).show();
        }

    }

}
