package com.tc.dataType;

import java.util.ArrayList;

import com.tc.XML.IO_xml;
import com.tc.util.Method;

public class DataType {

	public static void testData(String xmlfilename,String element,double percent,String elementValue) throws Exception{
		long begintime = System.currentTimeMillis();
		ArrayList<String> idlists=IO_xml.getElementID(xmlfilename, element);
		int range=idlists.size();
		ArrayList<String> set = Method.getRandomNumbers(percent, range);
		for(int i=0;i<set.size();i++){
			//System.out.println(set.get(i));
			IO_xml.modifyElementValueByID(xmlfilename, set.get(i), element, elementValue);
		}
		System.out.println("已完成测试集自动挑选！");
		long endtime = System.currentTimeMillis();
		long costTime = (endtime - begintime) / 1000;
		System.out.println("耗时：" + costTime + "秒！");
	}
	
	
	
	public static void main(String[] args) throws Exception{
		DataType.testData("3","classfication",0.1,"0");
	}
}
