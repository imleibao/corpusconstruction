package com.tc.segment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.tc.corpus.DBConnector;

public class SegmentDB {
	
    
	
	public static void ICTCLASSegmentMethod(String tableName,String userdicfile,String stopwordfile,int sqlid) throws Exception{
	        // TODO Auto-generated method stub  
	        //数据库的连接对象  
	        Connection conn = null;  
	        //PreparedStatement 预处理  
	        PreparedStatement pstmt = null;  
	        //ResultSet 结果集  
	        ResultSet result = null;  
	          
	        //sql语句  选择employees表
	        String sql = "select * from "+tableName+" WHERE id="+sqlid+";"; 
	        
	          
	        try {  
	            conn = DBConnector.createConnection();
	            //实例化PreparedStatement  
	            pstmt = conn.prepareStatement(sql);  
	            //返回结果集  
	            result = pstmt.executeQuery();  
	            //输出结果集  
	            while (result.next()) {  
	            	//System.out.println(result.getString("content"));
	                String content = result.getString("content");
	    		    String segment=Segment.DBsegment(content, userdicfile, stopwordfile);
	    		   // System.out.println("分词结果"+segment);
	                //4-将解析结果存储到数据库中
	                String id=result.getString("id");
	                //System.out.println(result.getString("id"));
	                SegmentDB.updateCorpus(tableName, segment, id); 
	            } 
	              
	        }catch (ClassNotFoundException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } catch (SQLException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	}
	
	public static String updateCorpus(String tableName,String segment,String id) {
		String updateSql="update "+tableName+" set segment='"+segment+"' WHERE id="+id+";";
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
	
	public static void main(String[] args) throws Exception { 
		for(int i=193840;i<=253839;i++){
			System.out.println(i);
		    SegmentDB.ICTCLASSegmentMethod("themecorpus", "F:\\ertaicorpus\\userdic.txt", "F:\\ertaicorpus\\stopworddic.txt",i);
		}
	}

}
