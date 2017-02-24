package com.tc.corpus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.tc.corpus.DBConnector;
import com.tc.DB.SQLKits;

public class CorpusDB {
	
	private static String addstr = "INSERT INTO "
			+" themecorpus( "
			+ "title,creator,subject," 
			+ "description,contributor,date,type,"
			+ "format,identifier,language,"
			+ "source,content,segment)" + " VALUES "
			+ "( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static String selectstr = "SELECT "
			+ " *"
			+ " FROM themecorpus"
			+ " WHERE " + " subject = ?";
	private static String deletestr = "DELETE FROM themecorpus WHERE subject=?";

	
	public static String addCorpus(Corpus corpus) {
		String info = "";
		Connection conn = null;
		PreparedStatement ps = null;
		Statement stmt = null;
		ResultSet rs = null;
		//System.out.println("addKeywordWeibos ----");
		try {
			conn = DBConnector.createConnection();
			int result = 0;
			if (conn == null) {
				info = "数据库连接失败";
				return info;
			}
			stmt = conn.createStatement();
			String sql = "select * themecorpus WHERE time= ? and content= ? ";	
//			stmt.executeQuery(sql);
//			System.out.println(weibo.getuId()+"****"+SQLKits.fromDateToSqlDate(weibo.getTime()));
			ps = conn.prepareStatement(sql);
			ps.setString(7, corpus.getDate());
			ps.setString(13, corpus.getContent());
			rs = ps.executeQuery();
			if (rs.first() != false){
				System.out.println("已有该内容！");
			}
			else{
				System.out.println("add add ----");
				ps = conn.prepareStatement(addstr);
				// "uid,uName,content,time," +
				// "client,beOriginal,praiseNum," +
				// "commentNum,forwardNum,praiseUrl," +
				// "commentUrl,forwardUrl"
				ps.setString(1, corpus.getId());
				ps.setString(2, corpus.getTitle());
				ps.setString(3, corpus.getCreator());
				ps.setString(4, corpus.getSubject());
				ps.setString(5, corpus.getDescription());
				ps.setString(6, corpus.getContributor());
				ps.setString(7, corpus.getDate());
				ps.setString(8, corpus.getType());
				ps.setString(9, corpus.getFormat());
				ps.setString(10, corpus.getIdentifier());
				ps.setString(11, corpus.getLanguage());
				ps.setString(12, corpus.getSource());
				ps.setString(13, corpus.getContent());
				ps.setString(14, corpus.getSegment());
				ps.executeUpdate();
			}
			info = "新建成功";
		} catch (Exception e) {
			info = "新建失败";
			return info;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (stmt != null) {
					stmt.close();
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
	
	public static String selectCorpus(String subject,String needItem){
		String selectStr="SELECT "+needItem+" FROM themecorpus"+ " WHERE " + " subject = "+subject+";";
		String info = "";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBConnector.createConnection();
			int result = 0;
			if (conn == null) {
				info = "数据库连接失败";
				return info;
			}
			ps = conn.prepareStatement(selectStr);
			rs = ps.executeQuery();
			while (rs.next()) {
				String text=rs.getString(needItem);
				System.out.println(text);
			}
			info = "查找微博成功";
			
		} catch (Exception e) {
			info = "查询失败";
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

	public static String deleteCorpus(String subject) {
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
			ps = conn.prepareStatement(deletestr);
			ps.setString(4, subject);
			ps.executeUpdate();
			info = "删除成功";
		} catch (Exception e) {
			info = "删除失败";
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
	
}
