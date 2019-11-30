package com.zz.common;

import com.zz.cache.Cache;
import com.zz.configure.RestTemplateConfig;
import com.zz.vo.StockData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangzheng on 2019/11/28.
 */
@Component
public class StockInfoUtils {

    @Autowired
    RestTemplate restTemplate;
    /**
     *  根据股票代码和查询日期，获得股票涨跌情况
     * @param symbol 股票代码
     * @param beginDateStr 查询开始日期 格式 yyyy-MM-dd
     * @param endDateStr 查询结束日期  格式 yyyy-MM-dd
     * @return
     */
    public  StockData getStockDataBySymbolAndTime(String symbol, String beginDateStr, String endDateStr){
        long begin =  DateUtils.parseForNumber(beginDateStr);
        long end =  DateUtils.parseForNumber(endDateStr);
        List<String> cookieList = getCookieList();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", "application/json; charset=UTF-8");
        requestHeaders.put(HttpHeaders.COOKIE, cookieList);
        String url = "https://stock.xueqiu.com/v5/stock/chart/kline.json";

        url=url+"?symbol=" +symbol.toUpperCase()+
                "&period=day&indicator=kline&type=before&begin="+begin+
                "&end="+end;
        System.out.println("url:"+url);
        String stockInfo = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<String>(requestHeaders),
                String.class).getBody();;
        JSONObject stockDataJson = new JSONObject(stockInfo);
        JSONArray stockList;
        StockData stockData = new StockData();
        stockData.setSymbol(symbol);
        try {
            JSONObject data = (JSONObject) stockDataJson.get("data");
            stockList = (JSONArray) data.get("item");
            if(stockList!=null&&stockList.length()>0){
                JSONArray  stockItemFirst = (JSONArray) stockList.get(0);
                long  beforeTimestamp = (long) stockItemFirst.get(0);
                stockData.setBeforeTimeNum(beforeTimestamp);
                Double beforePrice = (Double) stockItemFirst.get(5);

                Date beforeTime = new Date(beforeTimestamp);
                String beforeTimeStr =  DateUtils.getDateStr(beforeTime,"yyyy-MM-dd");
                stockData.setBeforePrice(beforePrice);
                stockData.setBeforeTime(beforeTimeStr);

                JSONArray  stockItemLast = (JSONArray) stockList.get(stockList.length()-1);
                long  endTimestamp = (long) stockItemLast.get(0);
                Double endPrice = (Double) stockItemLast.get(5);
                stockData.setNowTimeNum(endTimestamp);
                Date  endTime = new Date(endTimestamp);
                String endTimeStr =  DateUtils.getDateStr(endTime,"yyyy-MM-dd");
                stockData.setNowPrice(endPrice);
                stockData.setNowTime(endTimeStr);

                stockData.setDayNum(stockList.length());

                double percent = (stockData.getNowPrice()-stockData.getBeforePrice())/stockData.getBeforePrice() ;
                stockData.setPercent(percent);
            }
        }catch (Exception e){
            //异常数据
            stockData.setBeforePrice(0);
            stockData.setBeforeTime(beginDateStr);
            stockData.setNowPrice(0);
            stockData.setNowTime(endDateStr);
            stockData.setDayNum(0);
            stockData.setPercent(0);
        }


        return  stockData;
    }

    /**
     * 获取雪球的cookie
     * @return 雪球的cookie
     */
    public  List<String> getCookieList(){
        if(Cache.get("cookieList")!=null){
            return (List<String>) Cache.get("cookieList");
        }else{
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json; charset=UTF-8");
            MultiValueMap<String, String> requestParam= new LinkedMultiValueMap<String, String>();
            HttpEntity<Map> requestEntity = new HttpEntity<Map>(requestParam, headers);
            String accessTokenUrl ="https://xueqiu.com";
            ResponseEntity<String> response=restTemplate.postForEntity(accessTokenUrl,requestEntity, String.class);
            //获取返回的header
            List<String> val = response.getHeaders().get("Set-Cookie");
            Cache.put("cookieList",val);
            return val;
        }
    }


}
