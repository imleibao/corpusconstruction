package com.tc.dataClean;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.tc.util.IO;

public class TxtWeiboClean {
	
	public static void txtweiboCleanMethod(String fileName,String savefileName,String wordsfileName){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
			String line = br.readLine();
			while (line != null){
				String content=line.split(";")[5];
				//System.out.println(content);
				content=content.replace("\"", "");
				//System.out.println(content);
				String str = weiboClean.regexpClean(content);
				//System.out.println(str);
				str= weiboClean.selectSuitComment(str,wordsfileName);
				//System.out.println(str);
				String enoughStr=weiboClean.selectEnoughWordComment(str);
				//System.out.println(enoughStr);
				line=line.split(";")[0]+";"+line.split(";")[1]+";"+line.split(";")[2]+";"+line.split(";")[3]+";"+line.split(";")[4]+";\""+content+"\";\""+enoughStr+"\"";
				IO.writeFile(savefileName, line);
				line = br.readLine();	
			}
		br.close();		
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void dictionaryContent(String fileName,String savefileName){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
			String line = br.readLine();
			while (line != null){
				String content=line.split(";")[6];
				if(!content.equals("\"\"")){
					//content=content.replace("\"", "");
					//System.out.println(content);
					IO.writeFile(savefileName, line.split(";")[0]+";"+line.split(";")[1]+";"+line.split(";")[2]+";"+line.split(";")[3]+";"+line.split(";")[4]+";"+line.split(";")[6]);
				}
				line = br.readLine();	
			}
			br.close();		
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {		
		//txtweiboCleanMethod("E:\\PYH\\data\\ertaicorpus.txt", "E:\\PYH\\data\\ertaicorpusclean.txt","E:\\PYH\\data\\ertaisuitword.txt");
		dictionaryContent("E:\\PYH\\data\\ertaicorpusclean.txt","E:\\PYH\\data\\ertaicorpuslast2.txt");
	}
}
