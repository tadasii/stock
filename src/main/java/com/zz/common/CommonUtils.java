/**   
 * @Title: CreateORGCod.java
 * @Package com.ccb.xydjgyggl.service.util
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 王烽光
 * @date 2013-1-31 下午4:09:30
 * @version V1.0   
 */
package com.zz.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class CommonUtils {
    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);
    /**
     * 返回7随机数 为当前毫秒数＋x位随即数字
     * @Description: 生成7位key
     * @return
     */
    public static String create7Key() {
        String keyText = System.currentTimeMillis() + "";
        String keyRandom = String.valueOf(Math.random());
        // Debug.debug(keyRandom );
        if (keyRandom.length() < 15)
            keyRandom += String.valueOf(Math.random()).substring(2, 6);
        try {
            keyRandom = keyRandom.substring(2, (24 - keyText.length() - 2));
        } catch (StringIndexOutOfBoundsException e) {
            logger.error(e.getMessage(), e);
        }
        return keyRandom.toString();
    }

    /**
     * 返回20位随机数 为当前毫秒数＋7位随即数字
     * @Description: 生成20位key
     * @return
     */
    public static String create20Key() {
        StringBuffer keyText = new StringBuffer(String.valueOf(System
                .currentTimeMillis()));
        String keyRandom = String.valueOf(Math.random());
        if (keyRandom.length() < 15)
            keyRandom += String.valueOf(Math.random()).substring(2, 6);
        try {
            keyRandom = keyRandom.substring(2, (20 - keyText.length() + 2));
        } catch (StringIndexOutOfBoundsException e) {
            logger.error(e.getMessage(), e);
        }
        keyText.append(keyRandom);
        return keyText.toString();
    }


	/**
	 * 返回24位随机数 为当前毫秒数＋11位随即数字
     * @Description: 生成24位key
	 * @return
	 */
	public static String create24Key() {
		StringBuffer keyText = new StringBuffer(String.valueOf(System
				.currentTimeMillis()));
		String keyRandom = String.valueOf(Math.random());
		if (keyRandom.length() < 15)
			keyRandom += String.valueOf(Math.random()).substring(2, 6);
		try {
			keyRandom = keyRandom.substring(2, (24 - keyText.length() + 2));
		} catch (StringIndexOutOfBoundsException e) {
            logger.error(e.getMessage(), e);
		}
		keyText.append(keyRandom);
		return keyText.toString();

	}


    /**
     * @Title: random6Key
     * @Description: 生成6位随机数
     * @return
     */
	public static String random6Key() {
		int[] array = { 0, 1, 2, 3, 4, 5 };
		Random rand = new Random();
		for (int i = 6; i > 1; i--) {
			int index = rand.nextInt(i);
			int tmp = array[index];
			array[index] = array[i - 1];
			array[i - 1] = tmp;
		}
		String code = "";
		for (int i = 0; i < 6; i++) {
			code += String.valueOf(array[i]);
		}
		return code;
	}


    /**
     * @Title: random10Key
     * @Description: 生成10位随机数
     * @return
     */
    public static String random10Key() {
        int[] array = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        Random rand = new Random();
        for (int i = 10; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        String code = "";
        for (int i = 0; i < 10; i++) {
            code += String.valueOf(result * 10 + array[i]);
        }
        return code;
    }

	/**
	 * 生成32位随机数
	 * 
	 * @return
	 */
	public static String create32Key() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * @Title: getStartIndex
	 * @Description: 多页查询每页笔数、多页查询跳转页码返回开始条数
	 * @return int
	 * @author fengqingzhi
	 */
	public static int getStartIndex(int recInPage, int pageJump) {
		int startIndex = 0;
		if (pageJump > 0 && recInPage > 0) {
			startIndex = (pageJump - 1) * recInPage;
		}
		return startIndex;
	}

	@SuppressWarnings("rawtypes")
	public static String generateWord() {
		String[] beforeShuffle = new String[] { "1", "2", "3", "4", "5", "6",
				"7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
				"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
				"V", "W", "X", "Y", "Z" };
		List list = Arrays.asList(beforeShuffle);
		Collections.shuffle(list);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
		}
		String afterShuffle = sb.toString();
		String result = afterShuffle.substring(5, 9);
		return result;
	}

	
	public static String getLoaclMachineNm(){
		try {
			InetAddress addr = InetAddress.getLocalHost();
			return addr.getHostName().toString();
		} catch (UnknownHostException e) {
            logger.error(e.getMessage(), e);
			return "";
		}
	}
	public static String getLoaclMachineIP(){
		try {
			InetAddress addr = InetAddress.getLocalHost();
			return addr.getHostAddress().toString();
		} catch (UnknownHostException e) {
            logger.error(e.getMessage(), e);
			return "";
		}
	}
	

}
