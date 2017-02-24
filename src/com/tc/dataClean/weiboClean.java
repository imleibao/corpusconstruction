package com.tc.dataClean;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;




import com.tc.util.IO;
import com.tc.corpus.DBConnector;


public class weiboClean {
	
	 

    
	
	public static void cleanMethod(String tableName,String wordsfileName) throws ClassNotFoundException{
	         // TODO Auto-generated method stub  
	        //数据库的连接对象  
	        Connection conn = null;  
	        //PreparedStatement 预处理  
	        PreparedStatement pstmt = null;  
	        //ResultSet 结果集  
	        ResultSet result = null;  
	          
	        //sql语句  选择employees表
	        String sql = "select * from "+tableName; 
	        
	          
	        try {  
	        	 conn = DBConnector.createConnection();
		            //实例化PreparedStatement  
		            pstmt = conn.prepareStatement(sql);  
		            //返回结果集  
		            result = pstmt.executeQuery();  
		            //输出结果集  
	            while (result.next()) {  
	                String text = result.getString("content");
	                System.out.println(text);            
	                //3-开始循环，从数据库中每次获取一个text，赋值给str
	    	    	 String str = regexpClean(text);
					 System.out.println(str);
					 str= selectSuitComment(str,wordsfileName);
					 System.out.println(str);
					 String enoughStr=selectEnoughWordComment(str);
					 System.out.println(enoughStr);
	                //4-将解析结果存储到数据库中
	                String id=result.getString("id");
	                updatCleanData(tableName, "cleanContent",str, id); 
	                updatCleanData(tableName, "enoughContent",enoughStr, id);
	                
	            } 
	              
	        }catch (SQLException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	}
	
	public static String regexpClean(String text) {   
		
		String str = text;   
		str=str.replaceAll("@.* ","");   
		//清洗包含主体的转发微博内容
		str=str.replaceAll("\\@头条新闻.*","");
		//去除网址
		str=str.replaceAll("http.*","");
		//清洗“回复@***：”结构
		str=str.replaceAll("回复.*:","");
		//清除转发中人名结构
		str=str.replaceAll("@.*:","");
		//清除带有评论内容 @他人的字段(最有一个@没清掉)
		str=str.replaceAll("@.* ","");
		//清除纯@他人的数据
		str=str.replaceAll("@.*","");
		//去除多余字段“转发微博”
		str=str.replaceAll("转发微博","");
		//清洗“[微笑]”表情信息
		str=str.replaceAll("\\[.*\\]","");
		//去除转发符号
		str=str.replaceAll("\\/\\/","");
		//清除垃圾字段
		str=str.replaceAll("回复|分享图片","");  
		str=str.replaceAll("查看原文：","");  
		str=str.replaceAll("(分享自.*)","");  
		str=str.replaceAll("分享文章《.*》","");  
		str=str.replaceAll("分享.*:","");  
		str=str.replaceAll("看评论","");  
		str=str.replaceAll("（想看更多？下载知乎 App：","");  
		str=str.replaceAll("- 回答作者：.*","");  
		str=str.replaceAll("- .*个回答, .*人关注","");  		
		//清除话题
		str=str.replaceAll("#.*#","");
		//清除【】话题
		str=str.replaceAll("【.*】","");
		//清除|***:
		str=str.replaceAll("\\|.*","");
		return str;
	}   
	

	public  static String selectSuitComment(String str,String wordsfileName){
		//特别注意：选择用的词表第一行应该空出来
		ArrayList<String> wordsList=IO.getfileLineToList(wordsfileName);
		int mark=0;
		for(int i=0;i<wordsList.size();i++){
			if(str.contains(wordsList.get(i))){
				mark=1;
			}
		}	
		if(mark==0){
			str="";
		}
		return str;
	}
	
	public static String selectEnoughWordComment(String str){
		if(str.length()<10){
			str="";
		}
		return str;
	}
	
	public static String updatCleanData(String tableName,String colmnName,String cleanData,String id) {
		String updateSql="update "+tableName+" set "+colmnName+"='"+cleanData+"' WHERE id="+id+";";
		String info = "";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBConnector.createConnection();
			int result = 0;
			if (conn == null) {
				info = "数据库连接失败";
				return info;
			}
			ps = conn.prepareStatement(updateSql);
			ps.executeUpdate();
			info = "更新成功";
			System.out.println("更新成功");
		} catch (Exception e) {
			info = "更新失败";
			return info;
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return info;

	}
	
	public static void main(String[] args) throws ClassNotFoundException { 
		weiboClean.cleanMethod("themecorpuse10", "F://ertaisuitword.txt");
	}
	
	
	
}