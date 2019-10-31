package com.zz.common;


import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by zhangzheng on 2019/3/13.
 */
@Component
public class HttpAndHttpsUtils {

    @Autowired
    RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(HttpAndHttpsUtils.class);

    public JSONObject postForJSONObject(String url, Object request, Object... uriVariables){
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,request,String.class,uriVariables);
        if(!responseEntity.getStatusCode().is2xxSuccessful()){
            logger.info("-----HttpAndHttpsUtils.postForJSONObject -errorBody:{} ",responseEntity.getBody());
            throw new RestClientException(responseEntity.getBody());
        }
        if(responseEntity.getBody()!=null){
            return new JSONObject(responseEntity.getBody());
        }else{
            return new JSONObject();
        }
    }

    public JSONObject getForJSONObject(String url,Object... uriVariables){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url,String.class,uriVariables);
        if(!responseEntity.getStatusCode().is2xxSuccessful()){
            logger.info("-----HttpAndHttpsUtils.getForEntity -errorBody:{} ",responseEntity.getBody());
            throw new RestClientException(responseEntity.getBody());
        }
        if(responseEntity.getBody()!=null){
            return new JSONObject(responseEntity.getBody());
        }else{
            return new JSONObject();
        }
    }
}
