package com.iwi.comm.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * 웹의 request 및 response 객체 제어를 위한 유틸
 * @author kms
 *
 */
public class WebUtil {

	/**
	 * AJAX ALERT 처리
	 * @param response
	 * @param msg : 출력메시지
	 * @param type : B=뒤로가기, L=링크이동, C=부모창 링크이동 및 현재창 닫기
	 * @param url : 링크이동 URL 
	 * @throws Exception
	 */
	public static void ajaxPrintMsg(HttpServletResponse response, String msg, String type, String url) throws Exception{
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		StringBuffer sb = new StringBuffer();
		sb.append("<html lang=\"ko\" xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"ko\">");
		sb.append("<title>알림</title>");
		sb.append("<script language='javascript'>");
		
		if(msg != null && !"".equals(msg)) {
			sb.append("alert('"+msg+"');");
		}
		
		if(type != null && !"".equals(type)) {
			if("B".equals(type)) {
				sb.append("history.back();");
			}else if("L".equals(type)) {
				sb.append("window.location.href='"+url+"';");
			}else if("C".equals(type)) {
				sb.append("opener.location.href='"+url+"';");
				sb.append("window.close();");
			}
		}
		sb.append("</script>");
		sb.append("</html>");
		out.println(sb.toString());
		out.flush();
		out.close();
	}
}
