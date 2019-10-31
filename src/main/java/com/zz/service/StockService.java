package com.zz.service;

import com.google.common.collect.Lists;
import com.zz.cache.Cache;
import com.zz.common.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangzheng on 2019/10/25.
 */
@Service
@Transactional
public class StockService {
    @Autowired
    RestTemplate restTemplate;

    private String  xueqiuUrl ="https://xueqiu.com/stock/forchartk/stocklist.json";

    private String xueqiuCookieUrl ="https://xueqiu.com";

    public List<String> getAllCode(){
        List<String> codeList = (List<String>) Cache.get("codeList");
        if(codeList==null){
            codeList = getAllCodeFromNet();
            Cache.put("codeList",codeList);
        }
        return codeList;
    }

    public List<String> getAllShCode(){
        List<String> codeList = (List<String>) Cache.get("shCodeList");
        if(codeList==null){
            codeList = getAllShCodeFromNet();
            Cache.put("shCodeList",codeList);
        }
        return codeList;
    }

    public List<String> getAllSzCode(){
        List<String> codeList = (List<String>) Cache.get("szCodeList");
        if(codeList==null){
            codeList = getAllSzCodeFromNet();
            Cache.put("szCodeList",codeList);
        }
        return codeList;
    }


    public List<String> getAllCodeFromNet(){
        List<String> codeList = new ArrayList<>();
        codeList.add("002001");
        codeList.add("002206");
        return codeList;
    }

    public List<String> getAllShCodeFromNet(){
        List<String> codeList = new ArrayList<>();
        codeList.add("002001");
        codeList.add("002206");
        return codeList;
    }

    public List<String> getAllSzCodeFromNet(){
        List<String> codeList = new ArrayList<>();
        codeList.add("002001");
        codeList.add("002206");
        return codeList;
    }



    public void getSingleStockInfo(String startDateStr,String endDateStr,String symbol){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long startDateNum = DateUtils.parseForNumber(startDateStr);
        long endDateNum = DateUtils.parseForNumber(endDateStr);
        String url = xueqiuUrl;


    }
}
