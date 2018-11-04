package com.xcd.www.internet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.xcd.www.internet.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;

public class CountryCodesActivity extends SimpleTopbarActivity implements TextWatcher {

    private ListView listView;    //定义ListView用来获取到，布局文件中的ListView控件
    private ArrayAdapter<String> arrayAdapter;    //定义一个数组适配器对象
    private EditText etSearch;
    String[] rl;
    List<String> contactSearch;//模糊搜索集合
    String[] array;
    @Override
    protected Object getTopbarTitle() {
        return "国家";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_codes);


        GetCountryZipCode(this);
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        etSearch = findViewById(R.id.et_Search);
        etSearch.addTextChangedListener(this);

        listView = findViewById(R.id.listView);

    }

    public void GetCountryZipCode(Context Context) {//   HashMap<Integer, String> countryCodeMap = new HashMap<>();
        String CountryID = "";
        String CountryZipCode = "";
        TelephonyManager manager = (TelephonyManager) Context.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        rl = Context.getResources().getStringArray(R.array.CountryCodes);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rl);
        //把数组适配器加载到ListView控件中
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] g;
               if ( array != null&&array.length >0){
                   g = array[i].split(",");
               }else {
                   g = rl[i].split(",");
               }
                Intent intent = getIntent();
                intent.putExtra("CountryZipCode", g[0]);
                intent.putExtra("CountryZipName", g[1]);
                setResult(1, intent);
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //如果长度为0
        if (rl != null && rl.length > 0) {

            String trim = etSearch.getText().toString().trim();
            if (!TextUtils.isEmpty(trim)) {
                contactSearch = new ArrayList<>();//模糊搜索集合
                for (int j = 0, k = rl.length; j < k; j++) {
                    String countryCodeArr = rl[j];
                    String[] g = countryCodeArr.split(",");
                    String country = g[0];
                    String code = g[1];
                    if (country.indexOf(trim) != -1 || code.indexOf(trim) != -1) {
                        contactSearch.add(countryCodeArr);
                    }
                }
                array = new String[contactSearch.size()];
                contactSearch.toArray(array);
                arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
                //把数组适配器加载到ListView控件中
                listView.setAdapter(arrayAdapter);
                Log.e("TAG_搜索", "contactSearch=" + contactSearch.toString());
            }else {
                array = null;
                arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rl);
                //把数组适配器加载到ListView控件中
                listView.setAdapter(arrayAdapter);
            }

        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

}
