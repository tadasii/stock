package com.zz.controller;

import com.google.common.collect.Lists;
import com.zz.cache.Cache;
import com.zz.common.ExportExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zhangzheng on 2019/10/23.
 */
@RestController
@RequestMapping("/stock/ini")
public class InitialzationController {
    private static final Logger log = LoggerFactory.getLogger(InitialzationController.class);
    @GetMapping("/getAllCode")
    public List<String> getAllCode(){
        log.info("正在获得所有股票代码");
        List<String> codeList = Lists.newArrayList();
        codeList.add("002001");
        codeList.add("002206");
        Cache.put("codeList",codeList);
        log.info("获得所有股票代码结束");
        return codeList;
    }




}
