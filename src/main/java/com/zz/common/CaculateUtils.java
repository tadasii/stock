package com.zz.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by zhangzheng on 2019/10/25.
 */
public class CaculateUtils {

    public static BigDecimal getPercent(BigDecimal dividend,BigDecimal divisor){
        BigDecimal result = dividend.divide(divisor).setScale(2, RoundingMode.HALF_UP);
        return result;
    }
}
