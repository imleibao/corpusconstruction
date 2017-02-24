package com.tc.XML;

import java.util.ArrayList;
import java.util.List;

import com.tc.util.IO;

public class XmlFile {
	
	//将某一文档的词转化为数组
	public static ArrayList<String> documentToTermList(String xmlfileName,String idValue,String element) throws Exception{
		String document = IO_xml.getElementValueByID(xmlfileName, idValue, element);
		String[] wordlists = document.split("\\s+");
		ArrayList<String>  termlists=new ArrayList();
		for(int i=0;i<wordlists.length;i++){
			String word=wordlists[i];
			if(!termlists.contains(word)){
				termlists.add(word);
			}
		}
		return termlists;
	}
	
	//找到xml文档中某个子节点中所含有的所有词（去重）
	public static ArrayList<String> getAllTerm(String xmlfileName,String element) throws Exception{
		ArrayList<String>  termlists=new ArrayList();
		ArrayList<String> documents= IO_xml.getElementValue(xmlfileName, element);
		for(int i=0;i<documents.size();i++)
		{
			String document=documents.get(i);
			String[] wordlists = document.split("\\s+");
			for(int j=0;j<wordlists.length;j++){
				String word=wordlists[j];
				if(!termlists.contains(word)){
					termlists.add(word);
				}
			}
		}
		return termlists;
	}
	
	//计算某个字段的所有可能内容（去除重复）,即类型
	public static ArrayList<String> typeByElement(String xmlfileName,String element) throws Exception{
		//基于datatypeElement来识别出训练集，进行操作
		ArrayList<String> typetags = new ArrayList<String>();
		List<String> documents=IO_xml.getElementValue(xmlfileName, element);
		for(int d=0;d<documents.size();d++){
			String datatypetag=documents.get(d);
			if(!typetags.contains(datatypetag)){
				typetags.add(datatypetag);
			}
		}
		return  typetags;
	}
	
	//基于某个字段的类型，生成对应类型的txt文档作为中间文件(带id)
	public static void newfile(String xmlfileName,String typeelement,String writeelement,String allfileID) throws  Exception{
		ArrayList<String> typetags=XmlFile.typeByElement(xmlfileName, typeelement);
		ArrayList<String> documenttags=IO_xml.getElementValue(xmlfileName, typeelement);	
		ArrayList<String> idlists=IO_xml.getElementID(xmlfileName, typeelement);
		for(int i=0;i<documenttags.size();i++){
			String documenttag=documenttags.get(i).toString();
			//System.out.println(idlists.get(i)+";"+documenttag);
			for(int j=0;j<typetags.size();j++){
				String type=typetags.get(j).toString();
				if(documenttag.equals(type)){
					//System.out.println(IO_xml.getElementValueByID(xmlfileName, idlists.get(i), writeelement));
					IO.newFileDir(allfileID);
					IO.writeFile(allfileID+"//"+type+".txt", idlists.get(i)+";"+IO_xml.getElementValueByID(xmlfileName, idlists.get(i), writeelement));
				}
			}
		}
	}
	
	//基于某个字段的类型，生成对应类型的txt文档作为中间文件(不带id)
		public static void newfileNoID(String xmlfileName,String typeelement,String writeelement,String allfileNoID) throws  Exception{
			ArrayList<String> typetags=XmlFile.typeByElement(xmlfileName, typeelement);
			ArrayList<String> documenttags=IO_xml.getElementValue(xmlfileName, typeelement);	
			ArrayList<String> idlists=IO_xml.getElementID(xmlfileName, typeelement);
			for(int i=0;i<documenttags.size();i++){
				String documenttag=documenttags.get(i).toString();
				//System.out.println(idlists.get(i)+";"+documenttag);
				for(int j=0;j<typetags.size();j++){
					String type=typetags.get(j).toString();
					if(documenttag.equals(type)){
						//System.out.println(IO_xml.getElementValueByID(xmlfileName, idlists.get(i), writeelement));
						IO.newFileDir(allfileNoID);
						IO.writeFile(allfileNoID+"//"+type+".txt", IO_xml.getElementValueByID(xmlfileName, idlists.get(i), writeelement));
					}
				}
			}
		}
	
	public static void main(String[] args) throws Exception {
		/*
		ArrayList<String>  termlists=XmlFile.typeByElement("3", "classfication");
		for(String a:termlists){
			System.out.println(a);
		}*/
		
		XmlFile.newfileNoID("3", "classfication", "Segment","test//");
	}
	
}
