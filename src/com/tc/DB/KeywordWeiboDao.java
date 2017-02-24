
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tc.DB.DBConnector;
import com.tc.DB.KeywordWeibo;
import com.tc.DB.SQLKits;


public class KeywordWeiboDao {
	protected static final Log logger = LogFactory.getLog(KeywordWeiboDao.class);
	/**	
	* uId;用户id
	* uName;用户名
	* uUrl;用户url
	* content;微博内容
	* commentUrl;评论url
	* cncommentUrl;评论cnurl
	* time;创建时间
	* client;客户端信息
	* forwardNum;转发数目
	* commentNum;评论数目
	* praiseNum;赞数目
	* keyWord;关键词
	 */
	private static String add_keywordWeibo_str = "INSERT INTO "
			+" keywordWeibo( "
			+ "uid,uName,uUrl,content,commentUrl,cncommentUrl,time," 
			+ "client,forwardNum,commentNum,praiseNum,keyWord)"
		    + " VALUES "
			+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static String update_keywordWeibo_str = "UPDATE keywordWeibo SET "
			+ "uName=?,uUrl=?,content=?,commentUrl=?,cncommentUrl=?,"
			+ "client=?,"
			+ "forwardNum=?,commentNum=?,praiseNum=?,"
			+ "keyWord=? " + " WHERE " + "uid =? and time=?";
	
	private static String delete_keywordWeibo_str = "DELETE FROM keywordWeibo WHERE cncommentUrl=?";

	public static String addKeywordWeibos(KeywordWeibo keywordweibo) {
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
			String sql = "select * FROM keywordweibo WHERE uid= ? "
					+ " AND "+ "time = ?";
			
//			stmt.executeQuery(sql);
//			System.out.println(weibo.getuId()+"****"+SQLKits.fromDateToSqlDate(weibo.getTime()));
			ps = conn.prepareStatement(sql);
			ps.setString(1, keywordweibo.getuId());
			ps.setTimestamp(2, SQLKits.fromDateToSqlDate(keywordweibo.getTime()));
			rs = ps.executeQuery();
			if (rs.first() != false){
				//updateKeywordWeibo(keywordweibo);
				System.out.println("已有该内容！");
			}
			else{
				//System.out.println("add add ----");
				ps = conn.prepareStatement(add_keywordWeibo_str);
				// "uid,uName,content,time," +
				// "client,beOriginal,praiseNum," +
				// "commentNum,forwardNum,praiseUrl," +
				// "commentUrl,forwardUrl"
				ps.setString(1, keywordweibo.getuId());
				ps.setString(2, keywordweibo.getUname());
				ps.setString(3, keywordweibo.getuUrl());
				ps.setString(4, keywordweibo.getContent());
				ps.setString(5, keywordweibo.getCommentUrl());
				ps.setString(6, keywordweibo.getCncommentUrl());
				ps.setTimestamp(7, SQLKits.fromDateToSqlDate(keywordweibo.getTime()));
				ps.setString(8, keywordweibo.getClient());
				ps.setInt(9, keywordweibo.getForwardNum());
				ps.setInt(10, keywordweibo.getCommentNum());
				ps.setInt(11, keywordweibo.getPraiseNum());
				ps.setString(12, keywordweibo.getKeyWord());
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

	public static String updateKeywordWeibo(KeywordWeibo weibo) {
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
			ps.setString(1, weibo.getuId());
			ps.setString(2, weibo.getUname());
			ps.setString(3, weibo.getuUrl());
			ps.setString(4, weibo.getContent());
			ps.setString(5, weibo.getCommentUrl());
			ps.setString(6, weibo.getCncommentUrl());
			ps.setTimestamp(7, SQLKits.fromDateToSqlDate(weibo.getTime()));
			ps.setString(8, weibo.getClient());
			ps.setInt(9, weibo.getForwardNum());
			ps.setInt(10, weibo.getCommentNum());
			ps.setInt(11, weibo.getPraiseNum());
			ps.setString(12, weibo.getKeyWord());
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
	
	public static String deleteKeywordWeibo(String keyWord) {
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
		} catch (Exception e) {
			logger.error("findError:" + delete_keywordWeibo_str + " 执行出错", e);
			info = "删除微博失败";
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
	
	
	
	public static void fileAdd(String fileName,String keyword){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
			String line = br.readLine();
			//int count=0;
			while (line != null) {
				//count++;
				System.out.println(line);
				String uid=line.split("\t")[0];
				String uname=line.split("\t")[1];
				String uUrl=line.split("\t")[2];
				String content=line.split("\t")[3];
				String commentUrl=line.split("\t")[4];
				String cncommentUrl=line.split("\t")[5];
				String timeStr=line.split("\t")[6];
				Date time = KeywordWeiboDao.fromWeiboTimeStrToDate(timeStr);
				String client=line.split("\t")[7];
				int forwardNum= Integer.parseInt(line.split("\t")[8]);
				int commentNum= Integer.parseInt(line.split("\t")[9]);
				int praiseNum= Integer.parseInt(line.split("\t")[10]);
				KeywordWeibo weibo=new KeywordWeibo();
				weibo.setUid(uid);
				weibo.setUname(uname);
				weibo.setContent(content);
				weibo.setCommentUrl(commentUrl);
				weibo.setCncommentUrl(cncommentUrl);
				weibo.setTime(time);
				weibo.setClient(client);
				weibo.setForwardNum(forwardNum);
				weibo.setCommentNum(commentNum);
				weibo.setPraiseNum(praiseNum);
				weibo.setKeyWord(keyword);
				KeywordWeiboDao.addKeywordWeibos(weibo);
				line = br.readLine();	
			}
			br.close();		
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static  void fileDetele(String fileName){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
			String line = br.readLine();
			while (line != null) {
				KeywordWeiboDao.deleteKeywordWeibo(line);
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
			System.out.println(timeStr);
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
			date = SQLKits.fromStringToDate(timeStr+":00", dateFormat);
		}
		return date;
	}
	

	public static void main(String[] args) {
		/*
		KeywordWeiboDao weiboDao = new KeywordWeiboDao();
		KeywordWeibo weibo = new KeywordWeibo();
		Date date = SQLKits.fromStringToDate("2013-11-5 00:4:56",
				"yyyy-MM-dd HH:mm:ss");
		weibo.setClient("iphone");
		weibo.setCommentUrl("http://12344");
		weibo.setContent("like u");
		weibo.setCommentNum(50);
		weibo.setForwardNum(10);
		weibo.setTime(date);
		weibo.setuId("001");
		weibo.setKeyWord("测试");
		weiboDao.addKeywordWeibos(weibo);
		*/
		/*
		for(int i=9;i<=10;i++){
			System.out.println("F:\\ertai\\"+String.valueOf(i)+".txt");
		KeywordWeiboDao.fileAdd("F:\\ertai\\"+String.valueOf(i)+".txt", "二孩政策");}
	    */
		KeywordWeiboDao.fileDetele("F:\\deleteSearchUrl.txt");
	}

}
