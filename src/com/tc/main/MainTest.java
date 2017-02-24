package com.tc.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.tc.XML.IO_xml;
import com.tc.XML.XmlFile;
import com.tc.dataType.DataType;
import com.tc.feature.TFIDF_FS;
import com.tc.segment.Segment;
import com.tc.util.IO;
import com.tc.weight.TF_IDF;
import com.tc.wordCount.TermCalculate;

public class MainTest {
	public static void main(String[] args) throws Exception {
		
		//生成一个语料库xml文档
		/*
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("F:\\weibo\\xidada\\cluster_before\\corpus\\data.txt"), "utf-8"));
		String line = br.readLine();
		int id=1;
		while (line != null) {
			//System.out.println(line);
			IO_xml.addDocument("test1//3", String.valueOf(id), "", "彭燕虹", "20160123", "", "url", "中文", "微博", line, "");
			id++;
			line = br.readLine();
		}
		br.close();	
		*/
		
		for(int i=1;i<=6;i++){
			if(i<10){
			String fileName="F:\\data\\weibo.15120"+String.valueOf(i)+".all";
			MainTest.method(fileName);
			}	
			else{
				String fileName="F:\\data\\weibo.1512"+String.valueOf(i)+".all";
				MainTest.method(fileName);
			}
		}
		//切分词以及去停用词，基于语料库XML文档
		//Segment.fileSegment("test1//3","test1//userdic.txt","test1//stopworddic.txt");
		//聚类前自动生成语料库训练集，测试集（语料库训练集用于机器自动生成聚类结果，测试集用于后期人工分类）
		//(1)增加节点以供语料库类型记录,该节点赋值为训练集
		//IO_xml.addXMLElement("test1//3", "CorpusType", "train");
		//(2)系统随机选取其中百分之20的语料作为语料库测试集
		//DataType.testData("test1//3", "CorpusType", 0.2,"test");
		//进入聚类环节
		//将xml文档中待处理的训练集、测试集分开(提取的部分为Segment)，生成带有id标签的txt文档和不带id标签的txt文档
		//XmlFile.newfile("test1//3", "CorpusType", "Segment","test1//cluster//ID");
		//XmlFile.newfileNoID("test1//3", "CorpusType", "Segment","test1//cluster//noID");
		//删除noID里面的test文档
		//IO.deleteFile("test1//cluster//noID//test.txt");
		//基于noid的文档生成中间文件wordCount
		//TermCalculate.getTermCaculate("test1//cluster//noID", "wordCount.txt","test1//cluster//feature");
		//特征选择，采用tfidf方法
		//TFIDF_FS.TFIDF("test1//cluster//noID", "test1//cluster//feature//wordCount.txt", "test1//cluster//feature//tfidf_fs", 800);
		//文本表示
		//TF_IDF t = new TF_IDF();
		//t.process("test1\\cluster\\noID", "test1//cluster//feature//tfidf_fs_800.txt","test1//cluster//feature//train_tfidf_fs_800.arff");
		
	}
	
	public  static void method(String fileName) throws Exception{
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
			String line = br.readLine();
			int id=1;
			while (line != null) {
				//count++;
				//System.out.println(line);
				String eachfile=line.split("\t")[8];
				if(eachfile.contains("二胎")){
				System.out.println(eachfile);
				String datetime = line.split("\t")[0].split("\\s+")[0];
				String url=line.split("\t")[5];
				System.out.println(line.split("\t")[0].split("\\s+")[0]);
				IO_xml.addDocument("F:\\ertai\\201512", String.valueOf(id), "二胎", datetime,"", url, "新浪微博", eachfile, "");
				id++;
				}
				
				
				line = br.readLine();	
			}
			br.close();		
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
