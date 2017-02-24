package com.tc.DB;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLKits {
	public static String SQLFilter(String str)
	{
		if(str != null )
		{
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < str.length(); i++)
			{
				int c = str.codePointAt(i);
				if(c < 0xffff && c != '\'' && c != '\n' && c != '\r')
				{
					sb.append((char)c);
				}
			}		
			return sb.toString();
		}
		else
		{
			return null;
		}
		
	}
	public static String toSQLString(String str)
	{
		str = SQLFilter(str);
		String SQLStr = null;
		if(str == null)
		{
			SQLStr = "null";
		}
		else
		{
			SQLStr = SQLFilter(str);
			SQLStr = SQLStr;
		}
		return SQLStr;
	}
	public static String toSQLString(boolean value)
	{
		if(value)
		{
			return "true";
		}
		else
		{
			return "false";
		}
	}
	public static String toSQLTimeStr(Date time)
	{
		String timeStr = "";
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(time != null)
		{
			timeStr = sf.format(time);
		}
		else
		{
			timeStr = "null";
		}
		return timeStr;
	}
	public static Date fromSQLTimeStrToDate(String timeStr)
	{
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			java.sql.Date date1 = null;
			date = sdf.parse(timeStr);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return date;
	}
	public static Timestamp fromDateToSqlDate(Date time)
	{
		
		java.sql.Timestamp st = new java.sql.Timestamp(time.getTime());
//		java.sql.Date date = null;
//		date = new java.sql.Date(time.getTime());
		return st;
	}
	public static String toTimeStr(Date time)
	{
		String timeStr = "";
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(time != null)
		{
			timeStr = sf.format(time);
		}
		else
		{
			timeStr = "";
		}
		return timeStr;
	}
	
	public static Date fromTimeStr(String timeStr)
	{
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(timeStr);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return date;
	}
	public static Date fromSQLTimeStrWithException(String timeStr) throws ParseException
	{
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		date = sdf.parse(timeStr);
	
		return date;
	}
	
	public static Date fromStringToDate(String dateStr, String dateFormat){
		DateFormat format = new SimpleDateFormat(dateFormat); 
		    Date date=null;
			try {
				date = format.parse(dateStr);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			} 
			return date;
	}
	public static String fromDateToString(Date date, String dateFormat){
		DateFormat format = new SimpleDateFormat(dateFormat); 
		    String dateStr="";
			try {
				dateStr = format.format(date);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return dateStr;
	}
	
	public static void main(String[] args) {
		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		String dateStr="2015-11-01 00:14:00";
		System.out.println(SQLKits.fromStringToDate(dateStr, dateFormat).toString());
	}
}
