package com.tc.XML;

import java.io.BufferedReader; 
import java.io.File; 
import java.io.FileInputStream; 
import java.io.InputStreamReader; 
import java.util.List; 

import org.dom4j.Document; 
import org.dom4j.DocumentException; 
import org.dom4j.DocumentHelper; 
import org.dom4j.Element; 
import org.dom4j.Node; 

/** 
* @author  
* xml解析 
*/ 
public class XmlUtils { 

	//文档 
	private static Document doc = null; 
	//根节点 
	private static Element root = null; 

	public static void init(String xml){ 
		//SAXReader reader = new SAXReader(); 
		try { 
			doc = DocumentHelper.parseText(xml); 
			root = doc.getRootElement(); 
		} catch (DocumentException e) { 
			e.printStackTrace(); 
		} 
	} 

	/** 
	 * 获取名称为nodeName的文本 
	 * @param nodeName 
	 * @return 
	 */ 
	public static String getNodeText(String nodeName){ 
		Node n = root.selectSingleNode("//"+nodeName); 
		return n.getText(); 
	} 

	/** 
	 * 获取所有名称为nodeName的节点 
	 * @param nodeName 
	 * @return 
	 */ 
	@SuppressWarnings("unchecked") 
	public static List<Node> getNodeList(String nodeName){ 
		return root.selectNodes("//"+nodeName); 
	} 

	/** 
	 * 获取节点名称为nodeName的attrName属性 
	 * @param nodeName 
	 * @param attrName 
	 * @return 
	 */ 
	public static String getNodeAttribute(String nodeName,String attrName){ 
		Node node = root.selectSingleNode("//"+nodeName); 
		return node.valueOf("@"+attrName); 
	} 

	/** 
	 * 获取节点名称为nodeName的attrName属性 
	 * @param nodeName 
	 * @param attrName 
	 * @return 
	 */ 
	@SuppressWarnings("unchecked") 
	public static String getNodeAttribute(String nodeName,String attrName,int index){ 
		List<Node> nodes = root.selectNodes("//"+nodeName+"[@"+attrName+"]"); 
		return nodes.get(index).valueOf("@"+attrName); 
	} 

	/** 
	 * 获取节点名称为nodeName的attrName属性 条件：改节点里面有个属性为key值为value的属性 
	 @param nodeName 
	 * @param attrName 
	 * @return 
	 */ 
	@SuppressWarnings("unchecked") 
	public static String getNodeAttribute(String nodeName,String attrName,String key,String value){ 
		List<Node> nodes = root.selectNodes("//"+nodeName+"[@"+key+"='"+value+"']"); 
		return nodes.get(0).valueOf("@"+attrName); 
	} 
	/** 
	 * @param args 
	 */ 
	
	public static void main(String[] args) throws Exception{ 
		File file  = new File("1.xml"); 
		InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");   
		BufferedReader br = new BufferedReader(isr);   
		//BufferedReader br = new BufferedReader(new FileReader(file)); 
		String temp = null; 
		StringBuffer buffer = new StringBuffer(); 
		while((temp=br.readLine())!=null){ 
			buffer.append(temp).append("\n"); 
		} 
		//System.out.println(buffer); 
		XmlUtils.init(buffer.toString()); 
		String str =  XmlUtils.getNodeAttribute("Document","id","id","23");
		//String str =  XmlUtils.getNodeText("Contributor");
		//String str = XmlUtils.getNodeText("Document");//获取只有一个的节点的text 
		//String str = XmlUtils.getNodeAttribute("RESPONSE-INFO", "SERNUM");//获取只有一个节点的属性 
		//String str = XmlUtils.getNodeAttribute("CARDPRICES", "STOCKNUM",1);//获取多个节点的第二个属性 
		//String str = XmlUtils.getNodeAttribute("CARDPRICES", "STOCKNUM","CARDAMT","2990");//获取多个节点cardAmt=2990的stocknum的属性 
		System.out.println(str); 
	} 

}
