package com.zz.controller;

import com.google.common.collect.Lists;
import com.zz.cache.Cache;
import com.zz.common.CaculateUtils;
import com.zz.common.ExportExcel;
import com.zz.service.StockService;
import com.zz.vo.StockData;
import com.zz.vo.TimeInVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzheng on 2019/10/23.
 */
@Controller
@RequestMapping("/stock/consume")
public class ConsumeController {
    private static final Logger log = LoggerFactory.getLogger(ConsumeController.class);
    @Autowired
    StockService stockService;
    @RequestMapping("/stockSearch")
    public String stockSearch(Model model,TimeInVo timeInVo){
        log.info("开始时间为："+timeInVo.getBeginDate());
        log.info("结束时间为："+timeInVo.getEndDate());
        List<String> codeList = stockService.getAllCode();
        List<StockData> list = new ArrayList<StockData>();
        codeList.forEach(code->{
            StockData stockData = new StockData();
            stockData.setSymbol(code);
            double nowPrice = 10;
            stockData.setNowPrice(nowPrice);
            double  beforePrice = 20;
            stockData.setBeforePrice(beforePrice);
            double percent = (nowPrice-beforePrice)/beforePrice ;
            stockData.setPercent(percent);
            list.add(stockData);
        });
        model.addAttribute("list", list);
        return "/stockSearch";
    }




}
