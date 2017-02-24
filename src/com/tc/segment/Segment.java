package com.tc.segment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ICTCLAS2016.code.NlpirTest.CLibrary;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.tc.XML.IO_xml;
import com.tc.util.IO;

public class Segment {
	// 定义接口CLibrary，继承自com.sun.jna.Library
	public interface CLibrary extends Library {
		// 定义并初始化接口的静态变量
		CLibrary Instance = (CLibrary) Native.loadLibrary("C://Users//Administrator//workspace//CorpusConstruction//NLPIR"
				, CLibrary.class);

		// printf函数声明
		public int NLPIR_Init(byte[] sDataPath, int encoding,
				byte[] sLicenceCode);
		
		//添加用户词典声明
		public int NLPIR_AddUserWord(String sWord);
		
		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);
		
		public void NLPIR_Exit();
	}

	public static String transString(String aidString, String ori_encoding,
			String new_encoding) {
		try {
			return new String(aidString.getBytes(ori_encoding), new_encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String DBsegment(String content,String userdicfile,String stopwordfile) throws Exception{
		String argu = "";
		// String system_charset = "GBK";//GBK----0
		String system_charset = "UTF8";
		int charset_type = 1;
		// int charset_type = 0;
		// 调用printf打印信息
		
		
		int init_flag = CLibrary.Instance.NLPIR_Init(argu
				.getBytes(system_charset), charset_type, "0"
				.getBytes(system_charset));

		if (0 == init_flag) {
			System.err.println("初始化失败！");
			return "初始化失败！";
		}
		
		String sInput=content;
		String nativeBytes = null;
		String finalNativeBytes =null;
		try {
			//添加用户词典
			ArrayList<String> userwords=IO.getfileLineToList(userdicfile);
			for(int i=0;i<userwords.size();i++){
				//System.out.println(userwords.get(i));
				CLibrary.Instance.NLPIR_AddUserWord(userwords.get(i));
			}
			
			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 3);
			// String nativeStr = new String(nativeBytes, 0,
			// nativeBytes.length,"utf-8");
			//System.out.println(id+"分词结果为： " + nativeBytes);
			
			//去停用词
			finalNativeBytes=StopWordRemove.getStopWordRemove(stopwordfile, nativeBytes);
			// System.out.println("分词结果为： "
			// + transString(nativeBytes, system_charset, "UTF-8"));
			//
			// System.out.println("分词结果为： "
			// + transString(nativeBytes, "gb2312", "utf-8"));	
			
			/*
			int nCountKey = 0;
			
			String nativeByte = CLibrary.Instance.NLPIR_GetKeyWords(sInput, 10,false);

			System.out.println(id+"关键词提取结果是：" + nativeByte);
			*/
			CLibrary.Instance.NLPIR_Exit();

		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return finalNativeBytes;
	}

	public static void segment(String id,String content,String xmlfileName,String userdicfile,String stopwordfile) throws Exception{
		String argu = "";
		// String system_charset = "GBK";//GBK----0
		String system_charset = "UTF8";
		int charset_type = 1;
		// int charset_type = 0;
		// 调用printf打印信息
		
		
		int init_flag = CLibrary.Instance.NLPIR_Init(argu
				.getBytes(system_charset), charset_type, "0"
				.getBytes(system_charset));

		if (0 == init_flag) {
			System.err.println("初始化失败！");
			return;
		}
		
		String sInput=content;
		String nativeBytes = null;
		try {
			//添加用户词典
			ArrayList<String> userwords=IO.getfileLineToList(userdicfile);
			for(int i=0;i<userwords.size();i++){
				//System.out.println(userwords.get(i));
				CLibrary.Instance.NLPIR_AddUserWord(userwords.get(i));
			}
			
			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 3);
			// String nativeStr = new String(nativeBytes, 0,
			// nativeBytes.length,"utf-8");
			//System.out.println(id+"分词结果为： " + nativeBytes);
			
			//去停用词
			String finalNativeBytes=StopWordRemove.getStopWordRemove(stopwordfile, nativeBytes);
			
			IO_xml.modifyElementValueByID(xmlfileName, id, "Segment", finalNativeBytes);
			// System.out.println("分词结果为： "
			// + transString(nativeBytes, system_charset, "UTF-8"));
			//
			// System.out.println("分词结果为： "
			// + transString(nativeBytes, "gb2312", "utf-8"));	
			
			/*
			int nCountKey = 0;
			
			String nativeByte = CLibrary.Instance.NLPIR_GetKeyWords(sInput, 10,false);

			System.out.println(id+"关键词提取结果是：" + nativeByte);
			*/
			CLibrary.Instance.NLPIR_Exit();

		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}
	
	public static void txtsegment(String content,String savetxtfileName,String userdicfile,String stopwordfile) throws Exception{
		String argu = "";
		// String system_charset = "GBK";//GBK----0
		String system_charset = "UTF8";
		int charset_type = 1;
		// int charset_type = 0;
		// 调用printf打印信息
		
		
		int init_flag = CLibrary.Instance.NLPIR_Init(argu
				.getBytes(system_charset), charset_type, "0"
				.getBytes(system_charset));

		if (0 == init_flag) {
			System.err.println("初始化失败！");
			return;
		}
		
		String sInput=content;
		String nativeBytes = null;
		try {
			//添加用户词典
			ArrayList<String> userwords=IO.getfileLineToList(userdicfile);
			for(int i=0;i<userwords.size();i++){
				//System.out.println(userwords.get(i));
				CLibrary.Instance.NLPIR_AddUserWord(userwords.get(i));
			}
			
			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 3);
			// String nativeStr = new String(nativeBytes, 0,
			// nativeBytes.length,"utf-8");
			//System.out.println(id+"分词结果为： " + nativeBytes);
			
			//去停用词
			String finalNativeBytes=StopWordRemove.getStopWordRemove(stopwordfile, nativeBytes);
			IO.writeFile(savetxtfileName, finalNativeBytes);
			// System.out.println("分词结果为： "
			// + transString(nativeBytes, system_charset, "UTF-8"));
			//
			// System.out.println("分词结果为： "
			// + transString(nativeBytes, "gb2312", "utf-8"));	
			
			/*
			int nCountKey = 0;
			
			String nativeByte = CLibrary.Instance.NLPIR_GetKeyWords(sInput, 10,false);

			System.out.println(id+"关键词提取结果是：" + nativeByte);
			*/
			CLibrary.Instance.NLPIR_Exit();

		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}
	
	public static void fileSegment(String xmlfileName,String userdicfile,String stopwordfile) throws Exception{
		long begintime = System.currentTimeMillis();
		String element="Content";
		List contentlists = IO_xml.getElementValue(xmlfileName, element);
		List idlists = IO_xml.getElementID(xmlfileName, element);
		for(int i=0;i<contentlists.size();i++){
			String id=idlists.get(i).toString();
			String str=contentlists.get(i).toString();
			Segment.segment(id,str,xmlfileName,userdicfile,stopwordfile);
		}
		long endtime = System.currentTimeMillis();
		long costTime = (endtime - begintime) / 1000;
		System.out.println("耗时：" + costTime + "秒！");
	}
	
	public static void txtfileSegment(String txtfileName,String savetxtfileName,String userdicfile,String stopwordfile) throws Exception{
		long begintime = System.currentTimeMillis();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(txtfileName), "utf-8"));
		String line = br.readLine();
		//int count=0;
		while (line != null) {
			//count++;
			//System.out.println(line);
			Segment.txtsegment(line,savetxtfileName,userdicfile,stopwordfile);				
			line = br.readLine();	
		}
		br.close();		
		long endtime = System.currentTimeMillis();
		long costTime = (endtime - begintime) / 1000;
		System.out.println("耗时：" + costTime + "秒！");
	}
	
	public static void main(String[] args) throws Exception {
		//Segment.segment("比尔盖茨的豪宅，22万参观一次。“习大大访美”，比尔设家宴招待。http://t.cn/RyJgvWL.");
		//Segment.fileSegment("3","userdic.txt","stopworddic.txt");
		Segment.txtfileSegment("F://tianjiayu.txt", "F://tianjiayuSegment.txt", "userdic.txt","stopworddic.txt");
	}

}
