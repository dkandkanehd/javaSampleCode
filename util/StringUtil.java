package com.iwi.comm.util;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;

/**
 * @Since 2021. 3. 15.
 * @Author LGH
 * @FileName : StringUtil.java
 * <pre>
 * ---------------------------------------------------------
 * 개정이력
 * 2021. 3. 15. LGH : 최초작성
 * </pre>
 */
public class StringUtil {


	/**
	 * <pre>값이 비어있는지 여부를 체크 비어있을경우 true 리턴한다.</pre>
	 * 값이 비어있는지 여부를 체크 비어있을경우 true 리턴
	 * @param str 문자열<br>
	 * @return<br>
	 */
	public static boolean isEmpty(String str) {
		boolean rtn = false;

		if ( str == null || str.trim().equals("") || str.trim().equals("null") ) {
			rtn = true;
		}

		return rtn;
	}

	/**
	 * <pre>인자로 받은 String이 null일 경우 대체 값을 리턴한다.</pre>
	 * null이거나 ""이면 ""반환한다.
	 * @param str 문자열<br>
	 * @return<br>
	 */
	public static boolean isEquals(String orgStr, String comStr) {
		boolean rtn = false;

		if ( orgStr == null || orgStr.trim().equals("") || orgStr.trim().equals("null") ) {
			orgStr = "";
		}

		if ( comStr == null || comStr.trim().equals("") || comStr.trim().equals("null") ) {
			comStr = "";
		}

		if(orgStr.equals(comStr)) {
			rtn = true;
		}

		return rtn;
	}

	public static boolean isEquals(Object orgObj, String comStr) {
		boolean rtn = false;
		String orgStr = "";

		if ( orgObj == null) {
			orgStr = "";
		}

		if ( comStr == null || comStr.trim().equals("") || comStr.trim().equals("null") ) {
			comStr = "";
		}

		if(orgStr.equals(comStr)) {
			rtn = true;
		}

		return rtn;
	}

	/**
	 * <pre>인자로 받은 String이 null일 경우 대체 값을 리턴한다.</pre>
	 * null이거나 ""이면 ""반환한다.
	 * @param str 문자열<br>
	 * @return<br>
	 */
	public static String nullToBlank(String str) {
		String rtnStr = str;

		if ( str == null || str.trim().equals("") || str.trim().equals("null") ) {
			rtnStr = "";
		}

		return rtnStr;
	}

	/**
	 * 해당 문자열에 값이 있는지 확인한다.<br>
	 * null이거나 ""이면 ""반환한다.
	 * @param str 문자열<br>
	 * @return<br>
	 */
	public static String nullToBlank(Object obj) {
		String rtnStr = "";

		if (obj == null) {
			rtnStr = "";
		}
		else if(obj.toString().trim().equals("") || obj.toString().trim().equals("null")) {
			rtnStr = "";
		}
		else {
			rtnStr = obj.toString();
		}

		return rtnStr;
	}

	/**
	 * <pre>인자로 받은 String이 null일 경우 대체 값을 리턴한다.</pre>
	 * @param  비교 값
	 * @param  대체 값
	 * @return String
	 */
	public static String nullToCustom(String str, String replaceStr) {
		String rtnStr = str;

		if ( str == null || str.trim().equals("") || str.trim().equals("null") ) {
			rtnStr = replaceStr;
		}

		return rtnStr;
	}

	/**
	 * <pre>인자로 받은 String이 null일 경우 대체 값을 리턴한다.</pre>
	 * @param  비교 값
	 * @param  대체 값
	 * @return String
	 */
	public static String nullToCustom(Object obj, String replaceStr) {

		String str 		= nullToBlank(obj);

		if ( str.trim().equals("") || str.trim().equals("null") ) {
			str = replaceStr;
		}

		return str;
	}

	/**
	 * <pre>인자로 받은 String이 null일 경우 대체 값을 리턴한다.</pre>
	 * null이거나 ""이면 ""반환한다.
	 * @param str 문자열<br>
	 * @return<br>
	 */
	public static String nullToZero(String str) {
		String rtnStr = str;

		if ( str == null || str.trim().equals("") || str.trim().equals("null") ) {
			rtnStr = "0";
		}

		return rtnStr;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private static void copyMapToDto(HashMap<String, ?> map, Object dto) throws Exception {
		Iterator<?> iterator = map.keySet().iterator();
		
		while(iterator.hasNext()) {
			String key = (String) iterator.next();

			HashMap<String, ?> resultMap = (HashMap<String, ?>) map.get(key);
			Class<?> type = (Class<?>) resultMap.get("type");
			Object value = resultMap.get("value");

			String methodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);

			Class<?> clazz = dto.getClass();
			Method method = null;
			try {
				method = clazz.getMethod(methodName, type);
			} catch (NoSuchMethodException e) {
				continue;
			}

			method.invoke(dto, value);
		}
		
		/*for (int i = 0; iterator.hasNext(); i++) {
			String key = (String) iterator.next();

			HashMap resultMap = (HashMap) map.get(key);
			Class type = (Class) resultMap.get("type");
			Object value = resultMap.get("value");

			String methodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);

			Class clazz = dto.getClass();
			Method method = null;
			try {
				method = clazz.getMethod(methodName, type);
			} catch (NoSuchMethodException e) {
				continue;
			}

			method.invoke(dto, value);
		}*/
	}

	/**
	 * 본인인증 모듈에서 사용
	 * @param paramValue
	 * @param gubun
	 * @return
	 */
	public static String requestReplace(String paramValue, String gubun) {
		String result = "";

		if (paramValue != null) {

			paramValue = paramValue.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

			paramValue = paramValue.replaceAll("\\*", "");
			paramValue = paramValue.replaceAll("\\?", "");
			paramValue = paramValue.replaceAll("\\[", "");
			paramValue = paramValue.replaceAll("\\{", "");
			paramValue = paramValue.replaceAll("\\(", "");
			paramValue = paramValue.replaceAll("\\)", "");
			paramValue = paramValue.replaceAll("\\^", "");
			paramValue = paramValue.replaceAll("\\$", "");
			paramValue = paramValue.replaceAll("'", "");
			paramValue = paramValue.replaceAll("@", "");
			paramValue = paramValue.replaceAll("%", "");
			paramValue = paramValue.replaceAll(";", "");
			paramValue = paramValue.replaceAll(":", "");
			paramValue = paramValue.replaceAll("-", "");
			paramValue = paramValue.replaceAll("#", "");
			paramValue = paramValue.replaceAll("--", "");
			paramValue = paramValue.replaceAll("-", "");
			paramValue = paramValue.replaceAll(",", "");

			if(gubun != "encodeData"){
				paramValue = paramValue.replaceAll("\\+", "");
				paramValue = paramValue.replaceAll("/", "");
				paramValue = paramValue.replaceAll("=", "");
			}

			result = paramValue;

		}
		return result;
	}

	public static void strToVo(String strParam, Object vo) {
		
		String[] arrStrParam1	= strParam.split("&");
		String[] arrStrParam2	= null;
		String[] arrParamKey	= new String[arrStrParam1.length];
		String[] arrParamVal	= new String[arrStrParam1.length];

		for(int i=0; i<arrStrParam1.length; i++) {
			arrStrParam2 = arrStrParam1[i].split("=");

			if(arrStrParam2 != null && arrStrParam2.length == 2) {
				arrParamKey[i] = arrStrParam2[0];
				arrParamVal[i] = arrStrParam2[1];
			}
		}


		Class<?> c = vo.getClass();

		try{			
			for (int i = 0; i < arrParamKey.length; i++) {

				Object value = arrParamVal[i];

				String methodName = "set" + arrParamKey[i].substring(0, 1).toUpperCase() + arrParamKey[i].substring(1);

				Class<?> type = arrParamKey[i].getClass();

				Method method = null;
				try {
					method = c.getMethod(methodName, type);
				} catch (NoSuchMethodException e) {
					continue;
				}

				method.invoke(vo, value);
			}

		}catch(Exception ex){

		}
	}

	/**
	 * 코드 이외의 값 포맷 확인
	 * 한건이라고 false이면 N
	 * @param checkMap 체크 항목
	 * @param dataMap 체크 데이터
	 * @return
	 */
	public static String allFormatCheck(Map<String, String> checkMap, Map<String, String> dataMap) {
		
		String result = "Y";
		
		
		
		return result;
	}

	/**
	 * 정규식 확인
	 * @param checkType
	 * @param checkData
	 * @return
	 */
	public static boolean formatCheck(String checkType, String checkData) {

		String regExp = "";
		
		if("rrn".equals(checkType)) {
//			regExp = "^(?:[0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[1,2][0-9]|3[0,1]))-[1-4][0-9]{6}$";

			if(checkData.length() != 13) {
				return false;
			}
			
			return true;
		}
		
		if ("hp".equals(checkType)) {
			regExp = "^\\d{3}-\\d{3,4}-\\d{4}$";
		}
		
		if ("tel".equals(checkType)) {
			regExp = "^\\d{2,3}-\\d{3,4}-\\d{4}$";
		}
		
		return checkData.matches(regExp);
	}

	
	/**
	 * 숫자만 추출
	 * @param str
	 * @return
	 */
	public static String getOnlyNumberString(String str) {
		return str.replaceAll("[^0-9]", "");
	}
	
	/**
	 * 특정 encoding을 byte단위로 문자열 자르기
	 * @param str
	 * @param length
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static String getSubStrByte(String str, int length, String type)throws Exception {
		if(!isEmpty(str)) {
			str = str.trim();
		}
		if(str.getBytes(type).length <= length) {
			return str;
		}
		StringBuffer sb = new StringBuffer(length);
		int cnt = 0;
		for(char ch : str.toCharArray()) {
			cnt += String.valueOf(ch).getBytes(type).length;
			if(cnt > length) break;
			sb.append(ch);
		}
		return sb.toString();
	}

	/**
	 * 인코딩 확인
	 * @param str
	 */
	public static void getEncodeingCheck(String str) {
		String[] charSet = {"utf-8","euc-kr","ksc5601","iso-8859-1","x-windows-949"};
		for(String str1 : charSet) {
			for(String str2 : charSet) {
				try {
					System.out.println("[str1] : " + str1 + ", [str2] : " + str2 + ", [text] : " + new String(str.getBytes(str1), str2));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 인코딩 변환
	 * @param str - 입력된값
	 * @param charSet - 이전 인코딩
	 * @param changeCharSet - 변경할 인코딩
	 * @return
	 */
	public static String getEncodingChange(String str, String charSet, String changeCharSet)throws Exception {
		return new String(str.getBytes(charSet),changeCharSet);
	}

	/**
	 * 데이터 구분자 붙이기
	 * @param str : 기존 데이터
	 * @param addStr : 추가할 데이터
	 * @param separator : 구분자
	 */
	public static String setDataAttachSeparator(String str, String addStr, String separator) {
		
		if(StringUtil.isEmpty(str)) {
			str = addStr;
		}else {
			str += separator + addStr;
		}
		
		return str;
	}
	

	/**
	 * 파일 구분자 통일
	 * @param fileFullPath
	 * @return
	 */
	public static String filePathSeparatorChange(String fileFullPath) {		
		fileFullPath = fileFullPath.replaceAll("/", Matcher.quoteReplacement(File.separator));
		fileFullPath = fileFullPath.replaceAll(Matcher.quoteReplacement(File.separator), "/");
		return fileFullPath;
	}

	/**
	 * 숫자, 문자 등 조합하여 랜덤값 뽑기
	 * @param type
	 * @param cnt
	 * @return
	 */
	public static String getRandomValue(String type, int cnt) {
		StringBuffer strPwd = new StringBuffer();
		char str[] = new char[1];
		
		Random radom = new Random();
		
		// 특수기호 포함
		if (type.equals("P")) {
			for (int i = 0; i < cnt; i++) {
				str[0] = (char) ((radom.nextDouble() * 94) + 33);
				strPwd.append(str);
		}
		// 대문자로만
		} else if (type.equals("A")) {
			for (int i = 0; i < cnt; i++) {
				str[0] = (char) ((radom.nextDouble() * 26) + 65);
				strPwd.append(str);
			}
		// 소문자로만
		} else if (type.equals("S")) {
			for (int i = 0; i < cnt; i++) {
				str[0] = (char) ((radom.nextDouble() * 26) + 97);
				strPwd.append(str);
			}
		// 숫자형으로
		} else if (type.equals("I")) {
			int strs[] = new int[1];
			for (int i = 0; i < cnt; i++) {
				strs[0] = (int) (radom.nextDouble() * 9);
				strPwd.append(strs[0]);
			}
		// 소문자, 숫자형
		} else if (type.equals("C")) {
			Random rnd = new Random();
			for (int i = 0; i < cnt; i++) {
				if (rnd.nextBoolean()) {
					strPwd.append((char) ((int) (rnd.nextInt(26)) + 97));
				} else {
					strPwd.append((rnd.nextInt(10)));
				}
			}
		}
		
		return strPwd.toString();
	}
	

}
