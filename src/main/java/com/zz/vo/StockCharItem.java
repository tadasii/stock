package com.zz.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhangzheng on 2019/10/28.
 */
public class StockCharItem {
    private long  volume;  //成交量 9477705,
    private BigDecimal open; // 开盘价       open; // 27.03,
    private BigDecimal  high; //最高价 27.05,
    private BigDecimal close; // 26.56,
    private BigDecimal low; // 26.18,
    private BigDecimal chg; // -0.55,
    private BigDecimal percent; // -2.03,
    private BigDecimal turnrate; // 3.4,
    private BigDecimal ma5; // 26.794,
    private BigDecimal ma10; // 26.734,
    private BigDecimal ma20; // 26.805,
    private BigDecimal ma30; // 26.721,
    private BigDecimal dif; // -0.09,
    private BigDecimal dea; // -0.12,
    private BigDecimal macd; // 0.07,
    private BigDecimal lot_volume; // 94777,
    private long  timestamp; // 1478620800000,
    private Date time; // Wed Nov 09 00; //00; //00 +0800 2016

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getChg() {
        return chg;
    }

    public void setChg(BigDecimal chg) {
        this.chg = chg;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    public BigDecimal getTurnrate() {
        return turnrate;
    }

    public void setTurnrate(BigDecimal turnrate) {
        this.turnrate = turnrate;
    }

    public BigDecimal getMa5() {
        return ma5;
    }

    public void setMa5(BigDecimal ma5) {
        this.ma5 = ma5;
    }

    public BigDecimal getMa10() {
        return ma10;
    }

    public void setMa10(BigDecimal ma10) {
        this.ma10 = ma10;
    }

    public BigDecimal getMa20() {
        return ma20;
    }

    public void setMa20(BigDecimal ma20) {
        this.ma20 = ma20;
    }

    public BigDecimal getMa30() {
        return ma30;
    }

    public void setMa30(BigDecimal ma30) {
        this.ma30 = ma30;
    }

    public BigDecimal getDif() {
        return dif;
    }

    public void setDif(BigDecimal dif) {
        this.dif = dif;
    }

    public BigDecimal getDea() {
        return dea;
    }

    public void setDea(BigDecimal dea) {
        this.dea = dea;
    }

    public BigDecimal getMacd() {
        return macd;
    }

    public void setMacd(BigDecimal macd) {
        this.macd = macd;
    }

    public BigDecimal getLot_volume() {
        return lot_volume;
    }

    public void setLot_volume(BigDecimal lot_volume) {
        this.lot_volume = lot_volume;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "StockCharItem{" +
                "volume=" + volume +
                ", open=" + open +
                ", high=" + high +
                ", close=" + close +
                ", low=" + low +
                ", chg=" + chg +
                ", percent=" + percent +
                ", turnrate=" + turnrate +
                ", ma5=" + ma5 +
                ", ma10=" + ma10 +
                ", ma20=" + ma20 +
                ", ma30=" + ma30 +
                ", dif=" + dif +
                ", dea=" + dea +
                ", macd=" + macd +
                ", lot_volume=" + lot_volume +
                ", timestamp=" + timestamp +
                ", time=" + time +
                '}';
    }
}
