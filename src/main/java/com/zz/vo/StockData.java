package com.zz.vo;

import java.math.BigDecimal;

/**
 * Created by zhangzheng on 2019/10/24.
 */
public class StockData {
    private int num;
    private String symbol; //代码
    private String code;
    private String name;
    private double beforePrice; //股票之前的价钱
    private String  beforeTime; //之前的时间
    private long  beforeTimeNum;
    private double  nowPrice; //现在的价钱
    private String nowTime; //现在的时间
    private long  nowTimeNum;
    private double  percent;  //上涨幅度
    private int  dayNum; //统计的交易天数


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }



    public void setBeforeTime(String beforeTime) {
        this.beforeTime = beforeTime;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }

    public double getBeforePrice() {
        return beforePrice;
    }

    public void setBeforePrice(double beforePrice) {
        this.beforePrice = beforePrice;
    }

    public String getBeforeTime() {
        return beforeTime;
    }

    public double getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(double nowPrice) {
        this.nowPrice = nowPrice;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public long getBeforeTimeNum() {
        return beforeTimeNum;
    }

    public void setBeforeTimeNum(long beforeTimeNum) {
        this.beforeTimeNum = beforeTimeNum;
    }

    public long getNowTimeNum() {
        return nowTimeNum;
    }

    public void setNowTimeNum(long nowTimeNum) {
        this.nowTimeNum = nowTimeNum;
    }

    public int getDayNum() {
        return dayNum;
    }

    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "StockData{" +
                "symbol='" + symbol + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", beforePrice=" + beforePrice +
                ", beforeTime='" + beforeTime + '\'' +
                ", beforeTimeNum=" + beforeTimeNum +
                ", nowPrice=" + nowPrice +
                ", nowTime='" + nowTime + '\'' +
                ", nowTimeNum=" + nowTimeNum +
                ", percent=" + percent +
                ", dayNum=" + dayNum +
                '}';
    }
}
