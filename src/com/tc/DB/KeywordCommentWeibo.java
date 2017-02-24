package com.tc.DB;

import java.util.Date;

public class KeywordCommentWeibo {
	/**
	* keyWord;关键词
	* cncommentUrl;评论cnurl
	* time;创建时间
	* client;客户端信息
	* praiseNum;赞数目
	* content;微博内容
	* uName;用户名
	 */
	private String keyWord;
	private String cncommentUrl;
	private Date time;
	private String client;
	private int praiseNum;
	private String content;
	private String uname;
	
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getCncommentUrl() {
		return cncommentUrl;
	}
	public void setCncommentUrl(String cncommentUrl) {
		this.cncommentUrl = cncommentUrl;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}

	
	
}
