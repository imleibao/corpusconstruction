package com.tc.DB;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class KeywordCommentWeiboDao {
	protected static final Log logger = LogFactory.getLog(KeywordWeiboDao.class);
	/**
	* keyWord;关键词
	* cncommentUrl;评论cnurl
	* time;创建时间
	* client;客户端信息
	* praiseNum;赞数目
	* content;微博内容
	* uName;用户名
	 */
	private static String add_keywordWeibo_str = "INSERT INTO "
			+" keywordweibocomment( "
			+ "cncommentUrl,time," 
			+ "client,praiseNum,content,uName,keyWord)"
		    + " VALUES "
			+ "(?, ?, ?, ?, ?, ?, ?)";
	
	private static String update_keywordWeibo_str = "UPDATE keywordweibocomment SET "
			+ "cncommentUrl=?,time=?,client=?,"
			+ "praiseNum=?,"
			+ "keyWord=? " + " WHERE " + "uName =? and content=?";
	
	private static String delete_keywordWeibo_str = "DELETE FROM keywordweibocomment WHERE cncommentUrl like ?";
	
	
	public static String addKeywordCommentWeibos(KeywordCommentWeibo keywordcommentweibo) {
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
				logger.error("find error:addKeywordWeibos 数据库连接失败");
				info = "数据库连接失败";
				return info;
			}
			stmt = conn.createStatement();
			String sql = "select * FROM keywordweibocomment WHERE uName= ? "
					+ " AND "+ "content = ?";
			
//			stmt.executeQuery(sql);
//			System.out.println(weibo.getuId()+"****"+SQLKits.fromDateToSqlDate(weibo.getTime()));
			ps = conn.prepareStatement(sql);
			ps.setString(1, keywordcommentweibo.getUname());
			ps.setString(2, keywordcommentweibo.getContent());
			rs = ps.executeQuery();
			if (rs.first() != false){
				//updateKeywordWeibo(keywordcommentweibo);
				//System.out.println("已有该内容！");
			}
			else{
				//System.out.println("add add ----");
				ps = conn.prepareStatement(add_keywordWeibo_str);
				// "uid,uName,content,time," +
				// "client,beOriginal,praiseNum," +
				// "commentNum,forwardNum,praiseUrl," +
				// "commentUrl,forwardUrl"
				ps.setString(1, keywordcommentweibo.getCncommentUrl());
				ps.setTimestamp(2, SQLKits.fromDateToSqlDate(keywordcommentweibo.getTime()));
				ps.setString(3, keywordcommentweibo.getClient());
				ps.setInt(4, keywordcommentweibo.getPraiseNum());
				ps.setString(5, keywordcommentweibo.getContent());
				ps.setString(6, keywordcommentweibo.getUname());
				ps.setString(7, keywordcommentweibo.getKeyWord());
				ps.executeUpdate();
			}
			info = "新建微博成功";
		} catch (Exception e) {
			logger.error("findError:" + add_keywordWeibo_str + " 执行出错", e);
			info = "新建微博失败";
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
	
	public static String updateKeywordWeibo(KeywordCommentWeibo weibo) {
		String info = "";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBConnector.createConnection();
			int result = 0;
			if (conn == null) {
				logger.error("find error:updateAllKeywordWeibo 数据库连接失败");
				info = "数据库连接失败";
				return info;
			}
			ps = conn.prepareStatement(update_keywordWeibo_str);
			ps.setString(1, weibo.getCncommentUrl());
			ps.setTimestamp(2, SQLKits.fromDateToSqlDate(weibo.getTime()));
			ps.setString(3,weibo.getClient());
			ps.setInt(4, weibo.getPraiseNum());
			ps.setString(5, weibo.getContent());
			ps.setString(6, weibo.getUname());
			ps.setString(7, weibo.getKeyWord());
			ps.executeUpdate();
			info = "更新微博成功";
		} catch (Exception e) {
			logger.error("findError:" + update_keywordWeibo_str + " 执行出错", e);
			info = "更新微博失败";
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
	
	public static String deleteKeywordCommentWeibo(String deteleStr) {
		String keyWord=deteleStr+"%";
		String info = "";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBConnector.createConnection();
			int result = 0;
			if (conn == null) {
				logger.error("find error:deleteKeywordWeibo 数据库连接失败");
				info = "数据库连接失败";
				return info;
			}
			ps = conn.prepareStatement(delete_keywordWeibo_str);
			ps.setString(1, keyWord);
			ps.executeUpdate();
			info = "删除微博成功";
			System.out.println(info);
		} catch (Exception e) {
			logger.error("findError:" + delete_keywordWeibo_str + " 执行出错", e);
			info = "删除微博失败";
			System.out.println(info);
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
	
	public static void fileAddComment(String fileName,String keyword){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
			String line = br.readLine();
			line = br.readLine();	
			//int count=0;
			while (line != null) {
				//count++;
				System.out.println(line);
				String cncommentUrl=line.split("\t")[0];
				String timeClientStr=line.split("\t")[1];
				String dateStr=timeClientStr.split("\\s+")[0];
				String detailTimeclientStr=timeClientStr.split("\\s+")[1];
				String detailTimeStr=detailTimeclientStr.split("来")[0];
				String timeStr=dateStr+" "+detailTimeStr.substring(0,detailTimeStr.length()-1);
				Date time = KeywordCommentWeiboDao.fromWeiboTimeStrToDate(timeStr);
				String clientStr=detailTimeclientStr.split("来")[1];
				String clientBegin="自";
				String client=clientStr.substring(clientBegin.length());
				String praiseStr=line.split("\t")[2];
				String praiseBegin="赞[";
				String praiseEnd="]";
				String praiseNumStr=praiseStr.substring(praiseBegin.length(),praiseStr.length()-praiseEnd.length());
				int praiseNum= Integer.parseInt(praiseNumStr);
				String content=line.split("\t")[3];
				String uname=line.split("\t")[4];
				KeywordCommentWeibo weibo=new KeywordCommentWeibo();
				weibo.setCncommentUrl(cncommentUrl);
				weibo.setTime(time);
				weibo.setClient(client);
				weibo.setPraiseNum(praiseNum);
				weibo.setContent(content);
				weibo.setUname(uname);
				weibo.setKeyWord(keyword);
				KeywordCommentWeiboDao.addKeywordCommentWeibos(weibo);
				line = br.readLine();	
			}
			br.close();		
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static  void fileDeteleComment(String fileName){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
			String line = br.readLine();
			while (line != null) {
				KeywordCommentWeiboDao.deleteKeywordCommentWeibo(line);
				line = br.readLine();	
			}
			br.close();	
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Date fromWeiboTimeStrToDate(String timeStr) {
		Date date = null;
		if (timeStr.contains("月")) {
			Date now = new Date();
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			timeStr = year + "年" + timeStr+":00";
			//System.out.println(timeStr);
			String dateFormat = "yyyy年MM月dd日 HH:mm:ss";
			date = SQLKits.fromStringToDate(timeStr, dateFormat);
		} else if (timeStr.contains("分钟前")) {
			int minusMinutes = Integer.parseInt(timeStr.replace("分钟前", ""));
			minusMinutes = 0-minusMinutes;
			Date now = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);
			cal.add(Calendar.MINUTE, minusMinutes);
			date = cal.getTime();
		} else if (timeStr.contains("秒前")) {
			int minusSeconds = Integer
					.parseInt(timeStr.replace("秒前", ""));
			minusSeconds = 0- minusSeconds;
			Date now = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);
			cal.add(Calendar.SECOND, minusSeconds);
			date = cal.getTime();
		} else if (timeStr.contains("今天")){
			String time = timeStr.replace("今天 ", "");
			Date now = new Date();
			//yyyy-MM-dd HH:mm:ss
			String nowTime = SQLKits.toSQLTimeStr(now);
			String weiboTime = nowTime.substring(0,nowTime.indexOf(" "))+" "+time+":00";
			date = SQLKits.fromSQLTimeStrToDate(weiboTime);
			
		}else {
			String dateFormat = "yyyy-MM-dd HH:mm:ss";
			date = SQLKits.fromStringToDate(timeStr, dateFormat);
		}
		return date;
	}
	
	public static void main(String[] args) {
		//for(int i=38;i<=38;i++){
			//System.out.println("F:\\ertaicomment\\"+String.valueOf(i)+".txt");
			//KeywordCommentWeiboDao.fileAddComment("F:\\ertaicomment\\"+String.valueOf(37)+".txt", "二孩政策");
		//}
		KeywordCommentWeiboDao.fileDeteleComment("F:\\deleteCommentUrl.txt");
	}
	
}
