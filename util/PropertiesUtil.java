package com.iwi.comm.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @Since 2022. 6. 7.
 * @Author KMS
 * @FileName : PropertiesUtil.java
 * <pre>
 * ---------------------------------------------------------
 * 2022. 6. 7. KMS : 최초 생성
 * </pre>
 */
public class PropertiesUtil extends PropertyPlaceholderConfigurer {
	
	private static Map<String, String> propertiesMap;

	private static Logger log = LogManager.getLogger(PropertiesUtil.class);
	
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		
		super.processProperties(beanFactoryToProcess, props);
		
		propertiesMap = new HashMap<String, String>();
		
		for(Object key : props.keySet()) {
			
			try {
				propertiesMap.put((String) key, StringUtil.getEncodingChange(props.getProperty((String) key), "iso-8859-1", "utf-8"));
			} catch (Exception e) {
				e.printStackTrace();
				log.error("{}",e.getMessage());
			}
		}
		log.info("\n\n There are loaded properties.\n{}\n\n",propertiesMap.toString().replace(",", ",\n"));
	}
	
	public static int getInt(String name) {
		int re_val = 0;
		try {
			re_val = Integer.parseInt( String.valueOf(propertiesMap.get(name)));
		} catch (Exception e) {
			re_val = 0;
		}
		
		return re_val;
	}
	
	public static String getString(String name) {
		return String.valueOf(propertiesMap.get(name));
	}
	
	
}
