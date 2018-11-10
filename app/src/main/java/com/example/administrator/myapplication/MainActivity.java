package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView updateBtn;
    private ImageView SelectCityBtn;

    private String updateCityCode;

    // todayWeather
    private TextView tCityName,tCity,tTime,tHumidity,tWeek,tPmData,tPmQuality,tTemperature,tClimate,tWind;
    private ImageView weatherImg,pm25Img;

    // 用handler启动更新
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case 1:
                    updateTodayWeather((TodayWeather)message.obj);
                    break;
                default:
                    break;
            }
        }
    };

    TodayWeather todayWeather = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 设置按钮监听
        updateBtn = findViewById(R.id.nav_update);
        updateBtn.setOnClickListener(this);
        SelectCityBtn = findViewById(R.id.nav_manager);
        SelectCityBtn.setOnClickListener(this);

        initView();

        if (CheckNet.getNetStatus(this) == CheckNet.NET_NONE) {
            Log.d("MyApplication","无网络连接!");
            Toast.makeText(MainActivity.this,"无网络连接!",Toast.LENGTH_LONG).show();
        } else {
            Log.d("MyApplication","网络连通");
            Toast.makeText(MainActivity.this,"网络连通。",Toast.LENGTH_LONG).show();
        }
    }

    // 点击按钮事件
    public void onClick(View v) {
        if (v.getId() == R.id.nav_update) {
            getWeatherDatafromNet("101030100");
        }
        if (v.getId() == R.id.nav_manager) {
            Intent intent = new Intent(this,SelectCity.class);
            startActivity(intent);
        }
    }

    // 初始化
    private void initView() {
        // title
        tCityName = findViewById(R.id.nav_cityName);
        // todayWeather
        tCity = findViewById(R.id.today_info_city);
        tTime = findViewById(R.id.today_info_updateTime);
        tHumidity = findViewById(R.id.today_info_humidity);
        tWeek = findViewById(R.id.today_info_week);
        tPmData = findViewById(R.id.today_info_pm25);
        tPmQuality = findViewById(R.id.today_info_pm25status);
        tTemperature = findViewById(R.id.today_info_temperature);
        tClimate = findViewById(R.id.today_info_weatherStatus);
        tWind = findViewById(R.id.today_info_wind);
        weatherImg = findViewById(R.id.today_info_weatherStatusImg);
        pm25Img = findViewById(R.id.today_info_pm25img);

        tCityName.setText("N/A");
        tCity.setText("N/A");
        tTime.setText("N/A");
        tHumidity.setText("N/A");
        tWeek.setText("N/A");
        tPmData.setText("N/A");
        tPmQuality.setText("N/A");
        tTemperature.setText("N/A");
        tClimate.setText("N/A");
        tWind.setText("N/A");
    }

    // 更新
    private void updateTodayWeather(TodayWeather todayWeather) {
        tCityName.setText(todayWeather.getCity() + "天气");
        tCity.setText(todayWeather.getCity());
        tTime.setText(todayWeather.getUpdatetime());
        tHumidity.setText("湿度:" + todayWeather.getShidu());
        tPmData.setText(todayWeather.getPm25());
        tPmQuality.setText(todayWeather.getQuality());
        tWeek.setText(todayWeather.getDate());
        tTemperature.setText(todayWeather.getLow() + "~" + todayWeather.getHigh());
        tClimate.setText(todayWeather.getType());
        tWind.setText("风力" + todayWeather.getFengli());
        if (todayWeather.getPm25() != null) {
            int pm25 = Integer.parseInt(todayWeather.getPm25());
            if (pm25 <= 50) {
                pm25Img.setImageResource(R.drawable.pm25_0_50);
            } else if (pm25 > 50 && pm25 <= 100) {
                pm25Img.setImageResource(R.drawable.pm25_51_100);
            } else if (pm25 > 100 && pm25 <= 150) {
                pm25Img.setImageResource(R.drawable.pm25_101_150);
            } else if (pm25 > 150 && pm25 <= 200) {
                pm25Img.setImageResource(R.drawable.pm25_151_200);
            } else if (pm25 > 200 && pm25 <= 250) {
                pm25Img.setImageResource(R.drawable.pm25_201_250);
            } else if (pm25 > 250 && pm25 <= 300) {
                pm25Img.setImageResource(R.drawable.pm25_251_300);
            } else if (pm25 > 300) {
                pm25Img.setImageResource(R.drawable.pm25_300);
            }
        }
        if (todayWeather.getType() != null) {
            Log.d("WeatherType",todayWeather.getType());
            switch (todayWeather.getType()) {
                case "晴":
                    weatherImg.setImageResource(R.drawable.weather_qing);
                    break;
                case "阴":
                    weatherImg.setImageResource(R.drawable.weather_yin);
                    break;
                case "雾":
                    weatherImg.setImageResource(R.drawable.weather_wu);
                    break;
                case "多云":
                    weatherImg.setImageResource(R.drawable.weather_duoyun);
                    break;
                case "小雨":
                    weatherImg.setImageResource(R.drawable.weather_xiaoyu);
                    break;
                case "中雨":
                    weatherImg.setImageResource(R.drawable.weather_zhongyu);
                    break;
                case "大雨":
                    weatherImg.setImageResource(R.drawable.weather_dayu);
                    break;
                case "阵雨":
                    weatherImg.setImageResource(R.drawable.weather_zhenyu);
                    break;
                case "雷阵雨":
                    weatherImg.setImageResource(R.drawable.weather_leizhenyu);
                    break;
                case "雷阵雨加暴":
                    weatherImg.setImageResource(R.drawable.weather_leizhenyujiabao);
                    break;
                case "暴雨":
                    weatherImg.setImageResource(R.drawable.weather_baoyu);
                    break;
                case "大暴雨":
                    weatherImg.setImageResource(R.drawable.weather_dabaoyu);
                    break;
                case "特大暴雨":
                    weatherImg.setImageResource(R.drawable.weather_tedabaoyu);
                    break;
                case "阵雪":
                    weatherImg.setImageResource(R.drawable.weather_zhenxue);
                    break;
                case "暴雪":
                    weatherImg.setImageResource(R.drawable.weather_baoxue);
                    break;
                case "大雪":
                    weatherImg.setImageResource(R.drawable.weather_daxue);
                    break;
                case "小雪":
                    weatherImg.setImageResource(R.drawable.weather_xiaoxue);
                    break;
                case "雨夹雪":
                    weatherImg.setImageResource(R.drawable.weather_yujiaxue);
                    break;
                case "中雪":
                    weatherImg.setImageResource(R.drawable.weather_zhongxue);
                    break;
                case "沙尘暴":
                    weatherImg.setImageResource(R.drawable.weather_shachenbao);
                    break;
                default:
                    weatherImg.setImageResource(R.drawable.weather_qing);
                    break;
            }
        }
        Toast.makeText(MainActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
    }

    // 借助HttpUrlConnection（java.net.HttpUrlConnection）,获取Url网页上的数据
    private void getWeatherDatafromNet(String cityKey) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityKey;
        Log.d("Address",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection;
                try {
                    URL url = new URL(address);
                    urlConnection = (HttpURLConnection)url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(8000);
                    urlConnection.setReadTimeout(8000);
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer stringBuffer = new StringBuffer();
                    String string;
                    while((string = bufferedReader.readLine()) != null) {
                        stringBuffer.append(string);
                        Log.d("data from url",string);
                    }
                    String response = stringBuffer.toString();
                    Log.d("response",response);
                    todayWeather = parseXML(response);
                    if (todayWeather != null) {
                        Message message = new Message();
                        message.what = 1;
                        message.obj = todayWeather;
                        mHandler.sendMessage(message);
                    }
                    bufferedReader.close();
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 解析从Url网页上获取的数据
    private TodayWeather parseXML(String xmlData) {
        TodayWeather todayWeather = null;

        int fengliCount = 0;
        int fengxiangCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            Log.d("MyApplication","start parse xml");
            while (eventType != xmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    // 文档开始位置
                    case XmlPullParser.START_DOCUMENT:
                        Log.d("parse","start doc");
                        break;
                    // 标签元素开始位置
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                Log.d("city",xmlPullParser.getText());
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                Log.d("updatetime",xmlPullParser.getText());
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                Log.d("wendu",xmlPullParser.getText());
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("fengli",xmlPullParser.getText());
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                Log.d("shidu",xmlPullParser.getText());
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("fengxiang",xmlPullParser.getText());
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                Log.d("pm25",xmlPullParser.getText());
                                todayWeather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                Log.d("quality",xmlPullParser.getText());
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("date",xmlPullParser.getText());
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("high",xmlPullParser.getText());
                                todayWeather.setHigh(xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("low",xmlPullParser.getText());
                                todayWeather.setLow(xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("type",xmlPullParser.getText());
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return todayWeather;
    }
}
