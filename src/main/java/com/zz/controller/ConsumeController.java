package com.zz.controller;

import com.google.common.collect.Lists;
import com.zz.cache.Cache;
import com.zz.common.CaculateUtils;
import com.zz.common.DateUtils;
import com.zz.common.ExportExcel;
import com.zz.common.StockInfoUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhangzheng on 2019/10/23.
 */
@Controller
@RequestMapping("/stock/consume")
public class ConsumeController {
    private static final Logger log = LoggerFactory.getLogger(ConsumeController.class);
    @Autowired
    StockService stockService;
    @Autowired
    StockInfoUtils stockInfoUtils;
    @RequestMapping("/stockSearch")
    public String stockSearch(Model model,TimeInVo timeInVo){
        log.info("开始时间为："+timeInVo.getBeginDate());
        log.info("结束时间为："+timeInVo.getEndDate());
        List<StockData> list = getStockList(timeInVo);
        model.addAttribute("time", timeInVo);
        model.addAttribute("list", list);
        return "/stockSearch";
    }
    @RequestMapping("/stockRank")
    public String stockRank(@RequestParam String type,Model model){
        TimeInVo time = getTimeByType(type);
        List<StockData> list = getStockList( time);
        model.addAttribute("time", time);
        model.addAttribute("list", list);
        return "/stockSearch";
    }


    private   List<StockData> getStockList(TimeInVo time){
        List<StockData> codeList = stockService.getAllCode();
        List<StockData> list = new ArrayList<>();

        final List<StockData> finalList = list;
        long start = System.currentTimeMillis();
        codeList.parallelStream().forEach(
                stockData->{
                    StockData result = stockInfoUtils.getStockDataBySymbolAndTime(stockData.getSymbol(),time.getBeginDate(),time.getEndDate());
                    result.setName(stockData.getName());
                    result.setCode(stockData.getCode());
                    finalList.add(result);
                }
        );
        log.info(codeList.size()+"个股数据查询共耗时"+(System.currentTimeMillis() - start) + "毫秒。");
        long start2 = System.currentTimeMillis();
        //按上涨幅度排名
        list = finalList.stream().sorted((stockData1,stockData2)->{
            BigDecimal bd1= new BigDecimal(stockData1.getPercent());
            BigDecimal bd2= new BigDecimal(stockData2.getPercent());
            return bd1.compareTo(bd2);
        }).collect(Collectors.toList());
       log.info(finalList.size()+"个股数据排序共耗时"+(System.currentTimeMillis() - start2) + "毫秒。");
        return list;
    }

    /**
     *  type 1 最近一个月
     *  type 2最近三个月
     *  type 3 最近6个月
     *  type 4 最近12个月
     * @param type
     * @return
     */
    private TimeInVo getTimeByType(String type) {
        TimeInVo timeInVo = new TimeInVo();
        Date endTime = DateUtils.getYesterday();
        timeInVo.setEndDate(DateUtils.getDateStr(endTime,"yyyy-MM-dd"));
        Date startTime=DateUtils.subMonth(new Date(),Integer.valueOf(type));
        timeInVo.setBeginDate(DateUtils.getDateStr(startTime,"yyyy-MM-dd"));
        log.info("查询开始时间:"+timeInVo.getBeginDate()+"结束时间："+timeInVo.getEndDate());
       return   timeInVo;
    }


}
