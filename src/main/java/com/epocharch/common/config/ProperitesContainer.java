/**
 * 
 */
package com.epocharch.common.config;

import com.epocharch.common.constants.InternalConstants;
import com.epocharch.common.constants.PropKey;
import com.epocharch.common.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * @author Archer Jiang
 * 
 */
public class ProperitesContainer {
	private static Logger logger = LoggerFactory.getLogger(ProperitesContainer.class);
	public Map<String,Properties> pMap;
	public Properties eaProperties;
	private static ProperitesContainer pContainer = new ProperitesContainer();
	private static String fileName = InternalConstants.PROPERITIES_FILE_NAME;

	public static synchronized ProperitesContainer getInstance() {
		return pContainer;
	}


	private ProperitesContainer() {
		pMap = new HashMap<String, Properties>();
		eaProperties = new Properties();
		eaProperties.put(PropKey.JVM_PID, SystemUtil.getJvmPid());
		eaProperties.put(PropKey.HOST_IP, SystemUtil.getLocalhostIp());
		pMap.put(InternalConstants.NAMESPACE_EPOCHARCH, eaProperties);
		load();
	}


	public void loadFromClassPath() {
		InputStream input = null;
		Properties p = new Properties();
		String path = System.getProperty(InternalConstants.PROPERITIES_PATH_KEY);
		try {
			if (path != null) {
				File file = new File(path);
				if (file.exists()) {
					input = new FileInputStream(file);
				} else {
					input = loadFileFromClasspath();
				}
			} else {
				input = loadFileFromClasspath();
			}
			if (input != null) {
				p.load(input);
				if (!p.isEmpty()) {
					eaProperties.putAll(p);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Load properties file:" + fileName + " failed!!!");
					}
				}
			}
		} catch (IOException e) {
			logger.error("Load properties file:" + fileName + " failed!!!", e);
			System.exit(1);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
			}
		}
	}

	private InputStream loadFileFromClasspath() throws IOException {
		InputStream input = null;
		ClassLoader clzLoader = this.getClass().getClassLoader();
		URL url = clzLoader.getSystemResource(fileName);

		if (url != null) {
			input = url.openStream();
		} else {
			input = clzLoader.getSystemResourceAsStream(fileName);

			/*
			 * Classpath based resource could be loaded by this way in j2ee environment.
			 */
			if (input == null) {
				input = clzLoader.getResourceAsStream(fileName);
			}
		}
		return input;
	}

	public void load() {
		loadFromClassPath();
		loadFromExternal();
	}

	private void loadFromExternal() {
	}

	public String getPropertyByNameSpace(String nameSpace,String key){
		String value = null;
		Properties nsp = pMap.get(nameSpace);
		if(nsp!=null){
			value = nsp.getProperty(key);
		}
		return value;
	}

	public String getPropertyByNameSpace(String nameSpace,String key,String defaultValue){
		String value = null;
		Properties nsp = pMap.get(nameSpace);
		if(nsp!=null){
			value = nsp.getProperty(key,defaultValue);
		}
		return value;
	}

	public String getProperty(String key) {
		return eaProperties.getProperty(key);
	}

	public String getProperty(String key, String defValue) {
		String value = getProperty(key);
		return value == null ? defValue : value.trim();
	}

	public int getIntProperty(String key, int defValue) {
		int v = defValue;
		String value = getProperty(key);
		if (value != null) {
			try {
				v = Integer.valueOf(value.trim());
			} catch (Exception e) {
			}
		}
		return v;
	}

	public long getLongProperty(String key, long defValue) {
		long v = defValue;
		String value = getProperty(key);
		if (value != null) {
			try {
				v = Long.valueOf(value.trim());
			} catch (Exception e) {
			}
		}
		return v;
	}

	public boolean getBoolean(String key, boolean defValue) {
		boolean value = defValue;
		String strValue = getProperty(key);
		if (strValue != null) {
			strValue = strValue.trim();
			if (strValue.equalsIgnoreCase("true")) {
				value = true;
			}
		}
		return value;
	}


}
