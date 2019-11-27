import com.zz.cache.Cache;
import com.zz.common.CaculateUtils;
import com.zz.common.DateUtils;
import com.zz.configure.RestTemplateConfig;
import com.zz.configure.ThrowErrorHandler;
import com.zz.vo.StockCharItem;
import com.zz.vo.StockData;
import org.apache.commons.collections.map.HashedMap;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;


/**
 * Created by zhangzheng on 2019/10/25.
 */
public class RestTempleteTest {
   public static  RestTemplate getRestTemplate(){
       RestTemplateConfig restTemplateConfig = new RestTemplateConfig();
       RestTemplate restTemplate = new RestTemplate();
       try {
           restTemplate = restTemplateConfig.restTemplate();
       } catch (KeyStoreException e) {
           e.printStackTrace();
       } catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
       } catch (KeyManagementException e) {
           e.printStackTrace();
       }
       return  restTemplate;
   }

    /**
     * 获取雪球的cookie
     * @return 雪球的cookie
     */
   public static List<String> getCookieList(){
       if(Cache.get("cookieList")!=null){
           return (List<String>) Cache.get("cookieList");
       }else{
           RestTemplate restTemplate = getRestTemplate();
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

    /**
     *  根据股票代码和查询日期，获得股票涨跌情况
     * @param symbol 股票代码
     * @param beginDateStr 查询开始日期 格式 yyyy-MM-dd
     * @param endDateStr 查询结束日期  格式 yyyy-MM-dd
     * @return
     */
   public static StockData getStockDataBySymbolAndTime(String symbol,String beginDateStr,String endDateStr){
       long begin =  DateUtils.parseForNumber(beginDateStr);
       long end =  DateUtils.parseForNumber(endDateStr);
       List<String> cookieList = getCookieList();
       HttpHeaders requestHeaders = new HttpHeaders();
       requestHeaders.add("Content-Type", "application/json; charset=UTF-8");
       requestHeaders.put(HttpHeaders.COOKIE, cookieList);
       HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
       RestTemplate restTemplate = getRestTemplate();
       String url = "https://xueqiu.com/stock/forchartk/stocklist.json";
       url=url+"?symbol=" +symbol+
               "&period=1day&type=before&begin="+begin+
               "&end="+end;
       System.out.println("url:"+url);
       String  stockInfo = restTemplate.postForEntity(url,requestEntity,String.class).getBody();
       JSONObject stockDataJson = new JSONObject(stockInfo);

       JSONArray stockList = (JSONArray) stockDataJson.get("chartlist");
       StockData stockData = new StockData();
       stockData.setSymbol(symbol);
       if(stockList!=null&&stockList.length()>0){
           JSONObject  stockItemFirst = (JSONObject) stockList.get(0);
           Double beforePrice = (Double) stockItemFirst.get("close");
           long  beforeTimestamp = (long) stockItemFirst.get("timestamp");
           stockData.setBeforeTimeNum(beforeTimestamp);
           Date beforeTime = new Date(beforeTimestamp);
           String beforeTimeStr =  DateUtils.getDateStr(beforeTime,"yyyy-MM-dd");
           stockData.setBeforePrice(beforePrice);
           stockData.setBeforeTime(beforeTimeStr);

           JSONObject  stockItemLast = (JSONObject) stockList.get(stockList.length()-1);
           Double endPrice = (Double) stockItemLast.get("close");
           long  endTimestamp = (long) stockItemLast.get("timestamp");
           stockData.setNowTimeNum(endTimestamp);
           Date  endTime = new Date(endTimestamp);
           String endTimeStr =  DateUtils.getDateStr(endTime,"yyyy-MM-dd");
           stockData.setNowPrice(endPrice);
           stockData.setNowTime(endTimeStr);

           stockData.setDayNum(stockList.length());

           double percent = (stockData.getNowPrice()-stockData.getBeforePrice())/stockData.getBeforePrice() ;
           stockData.setPercent(percent);
       }

        return  stockData;
   }





    public static void main(String[] args) {
//        StockData stockData = getStockDataBySymbolAndTime("SH600756","2018-09-29","2019-10-31");
//        StockData stockData2 = getStockDataBySymbolAndTime("SZ002001","2018-09-29","2019-10-31");
//        System.out.println(stockData);
//        System.out.println(stockData2);
        List<StockData> list =  getAllShCodeFromNet();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    public static  String  shAllUrl = "http://web.juhe.cn:8080/finance/stock/shall" ;
    public static String key = "d61b577ac3737b7faeabbd4766a2b53d";
    public static  int type = 4; //分页方式
    public static int pageSize= type*20; //每页最大数
    //获得上海股票所有的代码
    public static List<StockData> getAllShCodeFromNet(){
        RestTemplate restTemplate = getRestTemplate();
        String url = "";
        int pageNum = 1;
        List<StockData> list = new ArrayList<>();
        int totalNum = 0;  //累计查询总数
        int totalCount  = 0; //股票总数
        do {
            url=shAllUrl+ getAllParm(pageNum++,key,type);
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

    public static String getAllParm(int pageNum,String key,int type){
        return  "?page=" +pageNum+
                "&key="+key+
                "&type="+type+
                "&stock=a";
    }
}
