package com.tc.segment;

import java.util.ArrayList;

import com.tc.util.IO;
import com.tc.util.Parameter;

public class StopWordRemove {
	public static void main(String[] args) throws Exception {
		StopWordRemove.getStopWordRemove("stopworddic.txt", "比尔盖茨/nrf 的/ude1 豪宅/n ，/wd 22万/m 参观/v 一/m 次/qv 。/wj “/wyz 习大大/nr 访美/v ”/wyy ，/wd 比尔/nrf 设/v 家宴/n 招待/vn 。/wj urlalter/n");
	}
	
	//去停用词
	public static String getStopWordRemove(String stopWordTable,String nativeBytes)
	{	
		//System.out.println(nativeBytes);
		String[] wordlists = nativeBytes.split("\\s+");
		ArrayList<String> stopwordlists=IO.getfileLineToList(stopWordTable);
		ArrayList<String> truewordlists=new ArrayList<String>();
		int[] tagsList=new int[wordlists.length];
		for(int i=0;i<wordlists.length;i++){
			tagsList[i]=0;
		}
		for(int i=0;i<wordlists.length;i++){
			String word=wordlists[i].split("/")[0];
			for(int j=0;j<stopwordlists.size();j++){
				
				 String stopword=stopwordlists.get(j).trim();
				 if(word.equals(stopword)){	 
					// System.out.println(wordlists[i]);		
					// System.out.println(word+stopword);		 
					 tagsList[i]=1;
					 break;
				 }	
				
			}
		}
		for(int i=0;i<wordlists.length;i++){
			if(tagsList[i]==0){
				truewordlists.add(wordlists[i]);
			}
		}
		//System.out.println(wordlists.length);
		//System.out.println(truewordlists.size());
		
		String str="";
		for(int i=0;i<truewordlists.size();i++){
			str=str+truewordlists.get(i)+" ";
		}
		System.out.println(str);
		return str;
	}
	
	//去停用词方法2-适用于无词性情况
	public static String getStopWordRemove2(String stopWordTable,String nativeBytes)
	{
		String str=nativeBytes;
		ArrayList<String> stopwordlists=IO.getfileLineToList(stopWordTable);
		for(int i=0;i<stopwordlists.size();i++){
			str=str.replace(stopwordlists.get(i).trim(),"");
		}
		System.out.println(str); 
		return str;
	}
}


