package com.zz.service;

import com.google.common.collect.Lists;
import com.zz.cache.Cache;
import com.zz.common.DateUtils;
import com.zz.vo.StockData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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

    public static  String  shAllUrl = "http://web.juhe.cn:8080/finance/stock/shall" ;
    public static  String  szAllUrl = "http://web.juhe.cn:8080/finance/stock/szall" ;

    public static  int type = 4; //分页方式

    @Value("juhe.key")
    String key;

    private String xueqiuCookieUrl ="https://xueqiu.com";

    /**
     * 获得所有股票数据
     * @return
     */
    public  List<StockData> getAllCode(){
        List<StockData> codeList = (List<StockData>) Cache.get("codeList");
        if(codeList==null){
            List<StockData> shAll = getAllShCode();
            List<StockData> szAll = getAllSzCode();
            if(shAll.addAll(szAll)){
                codeList = shAll;
            }
            Cache.put("codeList",codeList);
        }
        return codeList;
    }

    /**
     * 获得上海的所有股票信息
     * @return
     */
    public List<StockData> getAllShCode(){
        List<StockData> codeList = (List<StockData>) Cache.get("shCodeList");
        if(codeList==null){
            codeList = getAllCodeFromNet(shAllUrl);
            Cache.put("shCodeList",codeList);
        }
        return codeList;
    }

    /**
     * 获得深圳所有股票信息
     * @return
     */
    public List<StockData> getAllSzCode(){
        List<StockData> codeList = (List<StockData>) Cache.get("szCodeList");
        if(codeList==null){
            codeList = getAllCodeFromNet(szAllUrl);
            Cache.put("szCodeList",codeList);
        }
        return codeList;
    }




    public List<StockData> getAllCodeFromNet(String requstUrl){
        String url = "";
        int pageNum = 1;
        List<StockData> list = new ArrayList<>();
        int totalNum = 0;  //累计查询总数
        int totalCount  = 0; //股票总数
        do {
            url=requstUrl+ getAllParm(pageNum++,key,type);
            String  pageInfo = restTemplate.getForEntity(url,String.class).getBody();
            JSONObject stockDataJson = new JSONObject(pageInfo);
            JSONObject stockResult = (JSONObject) stockDataJson.get("result");
            String totalCountStr = (String) stockResult.get("totalCount");
            totalCount = Integer.valueOf(totalCountStr);
            String num = (String) stockResult.get("num");
            totalNum =  totalNum + Integer.valueOf(num);
            JSONArray dataArray = (JSONArray) stockResult.get("data");
            for (int i = 0; i <dataArray.length() ; i++) {
                StockData stockData = new StockData();
                JSONObject  stockObject = (JSONObject) dataArray.get(i);
                stockData.setSymbol((String) stockObject.get("symbol"));
                stockData.setCode((String) stockObject.get("code"));
                stockData.setName((String) stockObject.get("name"));
                list.add(stockData);
            }
        }while(totalNum <totalCount);

        return list;
    }





    public void getSingleStockInfo(String startDateStr,String endDateStr,String symbol){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long startDateNum = DateUtils.parseForNumber(startDateStr);
        long endDateNum = DateUtils.parseForNumber(endDateStr);
        String url = xueqiuUrl;
    }

    public static String getAllParm(int pageNum,String key,int type){
        return  "?page=" +pageNum+
                "&key="+key+
                "&type="+type+
                "&stock=a";
    }
}
