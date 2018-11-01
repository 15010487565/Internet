package com.xcd.www.internet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xcd.www.internet.R;

import java.io.IOException;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;

public class CountryCodesActivity extends SimpleTopbarActivity {

    private ListView listView;    //定义ListView用来获取到，布局文件中的ListView控件
    private ArrayAdapter<String> arrayAdapter;    //定义一个数组适配器对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_codes);
        listView = findViewById(R.id.listView);

        GetCountryZipCode(this);
    }

    public void GetCountryZipCode(Context Context) {//   HashMap<Integer, String> countryCodeMap = new HashMap<>();
        String CountryID = "";
        String CountryZipCode = "";
        TelephonyManager manager = (TelephonyManager) Context.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        final String[] rl = Context.getResources().getStringArray(R.array.CountryCodes);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rl);
        //把数组适配器加载到ListView控件中
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] g = rl[i].split(",");
                Intent intent = getIntent();
                Log.e("TAG_国家编码","g[0]="+g[0]);
                intent.putExtra("CountryZipCode",g[0]);
                intent.putExtra("CountryZipName",g[2]);
                setResult(1,intent);
                finish();
            }
        });
    }



    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {

    }

    @Override
    public void onCancelResult() {

    }

    @Override
    public void onErrorResult(int errorCode, IOException errorExcep) {

    }

    @Override
    public void onParseErrorResult(int errorCode) {

    }

    @Override
    public void onFinishResult() {

    }
}
