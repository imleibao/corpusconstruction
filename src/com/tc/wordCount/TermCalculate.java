package com.tc.wordCount;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tc.util.IO;
import com.tc.util.Parameter;

public class TermCalculate {
	public static void main(String[] args) throws IOException {		
		//正常权值计算
		TermCalculate train = new TermCalculate();
		//正常权值计算所得结果存储文件
		//String wordCountPath=Parameter.getFeaturepath()+"//" + "wordCount_self.txt"; 
		String wordCountPath="wordCount.txt"; 
		train.getTermCaculate("F://ertaicorpus//Corpus",wordCountPath,"F://ertaicorpus//feature");
		// test.getWordInTest(Method.getFeature());								
	}

	public static void getTermCaculate(String filePath,String wordCountpath,String featureDic) throws IOException {
		long begintime = System.currentTimeMillis();
		IO.newFileDir(featureDic);
		String path = featureDic+"//"+wordCountpath;
		File ff = new File(path);
		if (ff.isFile() && ff.exists())
			ff.delete();
		// Parameter pm = new Parameter();
		// IO io = new IO();
		// 保存索引文件的地方
		
		long classNum = 0;
		File f = new File(filePath);
		//file：文件夹里面的文件的集合，即每个类的内容存储的文件的集合
		File[] file = f.listFiles();		
		//for(int i=0;i<file.length;i++){System.out.println(file[i]);}
		classNum = file.length;
		System.out.println("分类数目： " + classNum);
		// 各个类的存放路径
		String className[] = new String[(int) classNum];
		// 各个词在类中的个数
		Integer[] termCount = new Integer[(int) classNum];
		Integer[] termCountSum = new Integer[(int) classNum];
		// 各个词出现的文档数
		Integer[] docFreq = new Integer[(int) classNum];
		Integer[] docCountSum = new Integer[(int) classNum];
		// Integer[] docFreqSum = new Integer[(int) classNum];
		System.out.println("正在计算……");
		for (int i = 0; i < classNum; i++) {
			className[i] = file[i].getName().split("\\.")[0];
			System.out.println("@termcaculate.java classname: " + className[i]);
			termCount[i] = 0;
			termCountSum[i] = 0;
			docFreq[i] = 0;
			docCountSum[i] = 0;
			// System.out.println(className[i]);
		}
		
		//得到所有的词
		List<String> termLists = IO.getAllTerms(filePath);
		List<String> termList=new ArrayList();
		for(int i=0;i<termLists.size();i++){
			if(!(termLists.get(i).equals(""))){
				termList.add(termLists.get(i));
			}
		}
		/*
		for(int i=0;i<termList.size();i++){
			//System.out.println(termList.get(i));
			IO.writeFile(wordCountpath, termList.get(i));
		}
		*/
		//基于类别各个词的文档数计算
		List<Map<String, Integer>> classDocList = new ArrayList();
		//基于类别各个词的词频计算
		List<Map<String, Integer>> classTermList = new ArrayList();
		
		for (int i = 0; i < file.length; i++) {
			Map<String, Integer> mapDoc = new HashMap();
			Map<String, Integer> mapTerm = new HashMap();
			for (String term : termList) {
				mapDoc.put(term, 0);
				mapTerm.put(term, 0);
			}
			System.out.println(filePath+"//"+file[i].getName());
			ArrayList<String> fileArray=IO.getfileLineToList(filePath+"//"+file[i].getName());
			for(int p=0;p<fileArray.size();p++){
				String a[] = fileArray.get(p).split(" ");
				List<String> docList = new ArrayList();
				List<String> tmpList = new ArrayList();
				for (int j = 0; j < a.length; j++){
					tmpList.add(a[j]);
					if(!docList.contains(a[j])){
						docList.add(a[j]);
					}
				}
				for (String docStr:docList) {
					if (mapDoc.containsKey(docStr)) {
						int docCnt = mapDoc.get(docStr) + 1;						
						docCountSum[i] += docCnt;
						mapDoc.put(docStr, docCnt);
					}
				}	
				for (String tmpStr:tmpList) {
					if (mapTerm.containsKey(tmpStr)) {
						int tmpCnt = mapTerm.get(tmpStr) + 1;						
						termCountSum[i] += tmpCnt;
						mapTerm.put(tmpStr, tmpCnt);
					}
				}	
			}
			classDocList.add(mapDoc);
			classTermList.add(mapTerm);
		}		
		
		//输出结果
		for (String term : termList) {
			String termdocStr="";
			for (int j = 0; j < classDocList.size(); j++) {
				termdocStr +=classTermList.get(j).get(term)+"|"+classDocList.get(j).get(term)+" ";
			}
			String str = term + " " + termdocStr;
			//System.out.println(str);
			IO.writeFile(path, str);
		}
	
		
		long endtime = System.currentTimeMillis();
		long costTime = (endtime - begintime) / 1000;
		System.out.println("耗时：" + costTime + "秒！");
	}
}
