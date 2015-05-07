package io.realm.examples.realmspeedtest;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;

public class CsvLoad extends AsyncTask<String, Integer, ArrayList<String>> {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String mPass;
    AssetManager mAssets;
    Context mContext;

    public CsvLoad(String pass, AssetManager assets,Context context
    ) {
        mPass = pass;
        mAssets = assets;
        mContext = context;
    }

    // ?�?�̓t?�@?�C?�?�?�?�?�A?�?�,CSV?�̓ǂݍ�?�?�
    public ArrayList<String> CSV(String pass, AssetManager assets) {
        ArrayList<String> kaomoji_List = new ArrayList<String>();
        try {

            InputStream fin = null;

            fin = assets.open(pass);// MainActivity.class.getResourceAsStream(pass);
            BufferedReader br = new BufferedReader(new InputStreamReader(fin,"SJIS"));
            String line = "";
            while ((line = br.readLine()) != null) {
                kaomoji_List.add(line);
            }

            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return kaomoji_List;
    }

    @Override
    protected ArrayList<String> doInBackground(String... filepath) {
        return CSV(mPass, mAssets);

    }

    // ?�?�?�C?�?�?�X?�?�?�b?�h?�Ŏ�?�s?�?�?�鏈�?�
    @Override
    protected void onPostExecute(ArrayList<String> result) {
        ((RealmTestActivity)mContext).saveList(result);
    }

}