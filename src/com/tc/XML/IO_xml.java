package com.tc.XML;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;



public class IO_xml {
	
	//创建xml文件
	public static void writeXMLFile(String xmlfileName) throws Exception{	
		Document document = DocumentHelper.createDocument();
        Element root = DocumentHelper.createElement("Documents");
        document.setRootElement(root);     
        XMLWriter xmlWriter = new XMLWriter();
        xmlWriter.write(document);
        OutputFormat format = new OutputFormat("", true);// 设置缩进为4个空格，并且另起一行为true
        XMLWriter xmlWriter2 = new XMLWriter(new FileOutputStream(xmlfileName+".xml"), format);
        xmlWriter2.write(document);    
	}
	
	//查找文档对应子节点的赋值，以document的id作为key
	public static String getElementValueByID(String theme,String idValue,String element)throws Exception{
			
			//创建SAXReader对象  
			SAXReader reader = new SAXReader();  
	        //读取文件 转换成Document  
	        Document document = reader.read(theme+".xml");  
	        //获取根节点元素对象  
	        Element root = document.getRootElement();  
			
	        Node node = root.selectSingleNode("//Document[@id='"+idValue+"']//"+element); 
			String elementValue=node.getStringValue();
			return elementValue;	
	}
		
	//查找文档对应子节点所在的文档id，基于element的value
	public static String getElementValueByValue(String theme,String element,String elementValue)throws Exception{
			
		String id="";
		//创建SAXReader对象  
		SAXReader reader = new SAXReader();  
		//读取文件 转换成Document  
		Document document = reader.read(theme+".xml");  
		//获取根节点元素对象  
		Element root = document.getRootElement();  
				
		List<Node> nodes= root.selectNodes("//"+element);
		if(nodes.size()!=0){
			for(int i=0;i<nodes.size();i++){
				if(nodes.get(i).getStringValue().equals(elementValue)){
					//System.out.println(nodes.get(i).getStringValue());
					id = nodes.get(i).getParent().valueOf("@id");
					//System.out.println(id);
						
				}	
			}
		}
			
		return id;
		}
	
	//查找文档一系列子节点对应赋值，基于element,返回element的内容
	public static ArrayList<String> getElementValue(String theme,String element)throws Exception{
		ArrayList<String> contentlists = new ArrayList<String>();
		//创建SAXReader对象  
		SAXReader reader = new SAXReader();  
		//读取文件 转换成Document  
		Document document = reader.read(theme+".xml");  
		//获取根节点元素对象  
		Element root = document.getRootElement();  
				
		List<Node> nodes= root.selectNodes("//"+element);
		if(nodes.size()!=0){
			for(int i=0;i<nodes.size();i++){
				contentlists.add(nodes.get(i).getStringValue());
				//System.out.println(nodes.get(i).getStringValue());	
			}	
		}
		return contentlists;
	}
	
	//查找文档一系列子节点对应赋值，基于element,返回element所在的id
	public static ArrayList<String> getElementID(String theme,String element)throws Exception{		
		ArrayList<String> idlists = new ArrayList<String>();	
		//创建SAXReader对象  
		SAXReader reader = new SAXReader();  
		//读取文件 转换成Document  
		Document document = reader.read(theme+".xml");  
		//获取根节点元素对象  
		Element root = document.getRootElement();  
				
		List<Node> nodes= root.selectNodes("//"+element);
		if(nodes.size()!=0){
			for(int i=0;i<nodes.size();i++){
				idlists.add(nodes.get(i).getParent().valueOf("@id"));
				//System.out.println(nodes.get(i).getStringValue());	
			}	
		}
		return idlists;
	}

	//在XML中增加第二级节点，对应于一个文档
	public static void addDocument(String theme,String id,String subject,
			String date,String type,String identifier,String source,String content,String segment) throws Exception{
		
		File file = new File(theme+".xml");
		if (!file.exists()) {
			IO_xml.writeXMLFile(theme);
		}
		
		SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
              
        //获取根节点元素对象  
        Element root = document.getRootElement();   
        //新增子节点
        Element DocumentElement = root.addElement("Document");
        
        DocumentElement.addAttribute("id", id);
        //Element titleElement = DocumentElement.addElement("Title");//标题
        //Element creatorElement = DocumentElement.addElement("Creator");//创建者-
        Element subjectElement= DocumentElement.addElement("Subject");//主题-
        //Element descriptionElement= DocumentElement.addElement("Description");//描述
       // Element contributorElement= DocumentElement.addElement("Contributor");//贡献人-
        Element dateElement= DocumentElement.addElement("Date");//日期-
        Element typeElement= DocumentElement.addElement("Type");//类型-
        //Element formatElement= DocumentElement.addElement("Format");//格式
        Element identifierElement= DocumentElement.addElement("Identifier");//标识符-
       // Element languageElement= DocumentElement.addElement("Language");//语言-
        Element sourceElement= DocumentElement.addElement("Source");//来源信息-
        Element contentElement= DocumentElement.addElement("Content");//内容-
        Element segmentElement= DocumentElement.addElement("Segment");//分词内容-
        
        //titleElement.setText(Title);
        //creatorElement.setText(Creator);
        subjectElement.setText(subject);
        //descriptionElement.setText(Description);
       // contributorElement.setText(contributor);
        dateElement.setText(date);
        typeElement.setText(type);
        //formatElement.setText(Format);
        identifierElement.setText(identifier);
       // languageElement.setText(language);
        sourceElement.setText(source);
        contentElement.setText(content);
        segmentElement.setText(segment);
             
        //XMLWriter xmlWriter = new XMLWriter();
        //xmlWriter.write(document);
        
        OutputFormat format = new OutputFormat("", true);//另起一行为true
        XMLWriter xmlWriter2 = new XMLWriter(new FileOutputStream(theme+".xml"), format);
        xmlWriter2.write(document);             
	}
	
	//在XML中删除第二级节点，对应于一个文档，以document的id作为key
	public static void deleteDocument(String theme,String idValue) throws Exception{
		//创建SAXReader对象  
		SAXReader reader = new SAXReader();  
		//读取文件 转换成Document  
		Document document = reader.read(theme+".xml");  
		//获取根节点元素对象  
		Element root = document.getRootElement();  
		List<Node> nodes= root.selectNodes("//Document");
		if(nodes.size()!=0){
			//System.out.println(node.asXML());
			for(int i=0;i<nodes.size();i++){
				//System.out.println(nodes.get(i).valueOf("@id"));
				if(nodes.get(i).valueOf("@id").equals(idValue)){
					Node node = root.selectSingleNode("//Document[@id='"+idValue+"']"); 
					Element e=node.getParent();
					e.remove(node);
					
					//XMLWriter xmlWriter = new XMLWriter();
					//xmlWriter.write(document);
	        
					OutputFormat format = new OutputFormat("", true);//不缩进，另起一行为true
					XMLWriter xmlWriter2 = new XMLWriter(new FileOutputStream(theme+".xml"), format);
					xmlWriter2.write(document);   
					
					System.out.println("删除成功!");
				}
				else
				{
					System.out.println("无相应文档!");
				}
				
			}
			
		}		
	}
	
	//增加文档的子节点以及给子节点赋值
	public static void addXMLElement(String theme,String elementName,String elementValue) throws Exception{
			
	        
	        File file = new File(theme+".xml");
			if (!file.exists()) {
				IO_xml.writeXMLFile(theme);
			}
			
			//创建SAXReader对象  
			SAXReader reader = new SAXReader();  
	        //读取文件 转换成Document  
	        Document document = reader.read(file);  
	        //获取根节点元素对象  
	        Element root = document.getRootElement();  
	        
	        List<Element> DocumentElementList = root.elements("Document");
	       
	        for(int i=0;i<DocumentElementList.size();i++){
	        	Element DocumentElement=DocumentElementList.get(i);
	        	Element newElement = DocumentElement.addElement(elementName);  
	        	newElement.setText(elementValue); }
	        
	        XMLWriter xmlWriter = new XMLWriter();
	        xmlWriter.write(document);
	        
	        OutputFormat format = new OutputFormat("", true);//不缩进，另起一行为true
	        XMLWriter xmlWriter2 = new XMLWriter(new FileOutputStream(theme+".xml"), format);
	        xmlWriter2.write(document);     
		}
	
	//删除文档的子节点
	public static void deleteXMLElement(String theme,String element) throws Exception{
		//创建SAXReader对象  
		SAXReader reader = new SAXReader();  
		//读取文件 转换成Document  
		Document document = reader.read(theme+".xml");  
		//获取根节点元素对象  
		Element root = document.getRootElement();  
		List<Node> nodes= root.selectNodes("//"+element);
		for(int i=0;i<nodes.size();i++){
			Node node=nodes.get(i);	
	        Element e=node.getParent();
	        e.remove(node);
		   
		    OutputFormat format = new OutputFormat("", true);//不缩进，另起一行为true
			XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(theme+".xml"), format);
			xmlWriter.write(document);    
			
			System.out.println("删除成功!");
		}
	}
	
	//修改文档的对应子节点赋值，以document的id作为key
	public static void modifyElementValueByID(String theme,String idValue,String element,String modifyValue)throws Exception{
		
		//创建SAXReader对象  
		SAXReader reader = new SAXReader();  
        //读取文件 转换成Document  
        Document document = reader.read(theme+".xml");  
        //获取根节点元素对象  
        Element root = document.getRootElement();  
		
        Node node = root.selectSingleNode("//Document[@id='"+idValue+"']//"+element); 
		node.getParent().element(element).setText(modifyValue);
		
		OutputFormat format = new OutputFormat("", true);//不缩进，另起一行为true
		XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(theme+".xml"), format);
		xmlWriter.write(document);    
	}	
	
	//修改文档对应子节点赋值，基于element的value
	public static void modifyElementValueByValue(String theme,String element,String elementValue,String modifyValue)throws Exception{		
		//创建SAXReader对象  
		SAXReader reader = new SAXReader();  
		//读取文件 转换成Document  
		Document document = reader.read(theme+".xml");  
		//获取根节点元素对象  
		Element root = document.getRootElement();  	
		List<Node> nodes= root.selectNodes("//"+element);
		if(nodes.size()!=0){
			for(int i=0;i<nodes.size();i++){
				if(nodes.get(i).getStringValue().equals(elementValue)){
					Node node=nodes.get(i);
					node.getParent().element(element).setText(modifyValue);
					OutputFormat format = new OutputFormat("", true);//不缩进，另起一行为true
					XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(theme+".xml"), format);
					xmlWriter.write(document);    		
				}	
			}
		}		
	}
	
	public static void main(String[] args) throws Exception{
		//IO_xml.writeXMLFile("1");
		//for(int i=0;i<600;i++){
		//IO_xml.addDocument("3", String.valueOf(i+1), "", "彭燕虹", "20151003", "", "", "中文", "豆瓣电影", "比尔盖茨的豪宅，22万参观一次。“习大大访美”，比尔设家宴招待。http://t.cn/RyJgvWL.", "");
		//}
		//System.out.println("构建完成!");
		//IO_xml.findElementValue("1","23","Contributor");
		//IO_xml.deleteDocument("1", "21");
		//String id=IO_xml.findElementValueByValue("1", "Identifier", "45");
		//System.out.println(id);
		IO_xml.addXMLElement("3", "classfication", "1");
		//IO_xml.deleteXMLElement("1", "name");
		/*
		System.out.println(IO_xml.findElementValueByID("1", "21", "Source"));
		IO_xml.modifyElementValueByID("1", "21", "Source", "时光网");
		System.out.println(IO_xml.findElementValueByID("1", "21", "Source"));
		*/
		/*
		System.out.println(IO_xml.findElementValueByID("1", "21", "Source"));
		IO_xml.modifyElementValueByValue("1", "Source", "时光网","微信购票");
		System.out.println(IO_xml.findElementValueByID("1", "21", "Source"));
		*/
		/*
		List list = IO_xml.findElementValue("1", "Content");
		for(int i=0;i<list.size();i++){
			System.out.println(list.get(i));
		}*/
	}
}
