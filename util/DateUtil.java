package com.iwi.comm.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Since 2022. 4. 05.
 * @Author kms
 * <pre>
 * ---------------------------------------------------------
 * 날짜 관련 유틸
 * 2022. 4. 05. kms : 최초 생성
 * </pre>
 */
public class DateUtil {

	
	/**
	 * 현재 날짜를 포맷에 맞게 가져온다.
	 * @param format
	 * @return
	 */
	public static String getCurrentDateWithFormat(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}
}
