package com.easymorse.parser;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

import com.easymorse.bean.Category;
import com.easymorse.bean.CompAppInfo;

public class ParserBySAX {
	public List<Category> getCategories(InputSource inputStream) throws Throwable {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		ExampleHandler handler = new ExampleHandler();
		saxParser.parse(inputStream, handler);
		List<Category> categoryList = handler.getCategories();
		return categoryList;
	}

	public List<CompAppInfo> getCompAppInfos(InputSource inputStream)
			throws Throwable {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		ExampleHandler handler = new ExampleHandler();
		saxParser.parse(inputStream, handler);
		List<CompAppInfo> compAppInfoList = handler.getCompAppInfos();
		return compAppInfoList;
	}
	
	public String getDeviceStatus(InputSource inputStream) throws Throwable {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		ExampleHandler handler = new ExampleHandler();
		saxParser.parse(inputStream, handler);
		String deviceStatus = handler.getDeviceStatus();
		return deviceStatus;
	}
}
