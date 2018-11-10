package com.example.administrator.myapplication;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

public class MyApplication extends Application {
    private static Application mApp;
    List<City> cityList;
    CityDB mCityDB;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("InnerMyApplication", "OnCreate");
        mApp = this;
        mCityDB = openCityDB();
        initCityList();
    }

    public static Application getInstance() {
        return mApp;
    }

    public CityDB openCityDB() {
        String path = "/data" + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + getPackageName()
                + File.separator + "databases"
                + File.separator + CityDB.CITY_DB_NAME;
        Log.d("filePath",path);
        File db = new File(path);
        Log.d("db",path);

        try {
            InputStream inputStream = getAssets().open("city.db");
            FileOutputStream fileOutputStream = new FileOutputStream(db);
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
//                Log.d("writeBuffer",buffer.toString());
                fileOutputStream.flush();
            }
            fileOutputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return new CityDB(this,path);
    }

    private void initCityList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareCityList();
            }
        }).start();
    }

    private boolean prepareCityList() {
        cityList = mCityDB.getCityList();
        for (City city:cityList) {
            String cityName = city.getCity();
            Log.d("CityDB",cityName);
        }
        return true;
    }

    public List<City> getCityList() {
        return cityList;
    }
}
