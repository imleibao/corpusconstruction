package com.tc.DB;

import java.util.Date;

public class KeywordWeibo {
	/**
	* keyWord;关键词
	* uId;用户id
	* uName;用户名
	* uUrl;用户url
	* content;微博内容
	* commentUrl;评论url
	* cncommentUrl;评论cnurl
	* time;创建时间
	* client;客户端信息
	* praiseNum;赞数目
	* commentNum;评论数目
	* forwardNum;转发数目
	
	 */
	private String keyWord;
	private String uid;
	private String uname;
	private String uUrl;
	private String content;
	private Date time;
	private String client;
	private int praiseNum;
	private int commentNum;
	private int forwardNum;
	private String commentUrl;
	private String cncommentUrl;
	
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public void setuUrl(String uUrl) {
		this.uUrl = uUrl;
	}
	public String getuUrl() {
		return uUrl;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getCncommentUrl() {
		return cncommentUrl;
	}
	public void setCncommentUrl(String cncommentUrl) {
		this.cncommentUrl = cncommentUrl;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getuId() {
		return uid;
	}
	public void setuId(String uid) {
		this.uid = uid;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date date) {
		this.time = date;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	
	public int getPraiseNum() {
		return praiseNum;
	}
	public void setPraiseNum(int praiseNum) {
		this.praiseNum = praiseNum;
	}
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public int getForwardNum() {
		return forwardNum;
	}
	public void setForwardNum(int forwardNum) {
		this.forwardNum = forwardNum;
	}
	
	public String getCommentUrl() {
		return commentUrl;
	}
	public void setCommentUrl(String commentUrl) {
		this.commentUrl = commentUrl;
	}
	
	
	
}
