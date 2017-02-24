package com.tc.feature;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tc.util.IO;
import com.tc.util.Parameter;

public class TFIDF_FS {
	public static void main(String[] args) throws IOException {	
		//词-词频-文档数
		String wordCountPath="F://ertaicorpus//feature//wordCount.txt";
		//存储文件
		String savePath="F://ertaicorpus//feature//tfidf_fs";
		//选择排序前num个词
		TFIDF_FS.TFIDF("F://ertaicorpus//Corpus",wordCountPath, savePath,300);
	}
	
	public static void TFIDF(String trainPath,String wordCountPath,String savePathDic,int num){
		
		long begintime = System.currentTimeMillis();
		
		String savePath=savePathDic+"_"+num+".txt";
		
		File ff = new File(savePath);
		if (ff.isFile() && ff.exists())
			ff.delete();
		
		//训练集文档列表	
		File trainf = new File(trainPath);
		File[] trainSetlist = trainf.listFiles();
 
		int classNum=trainSetlist.length;
		
		//各个类的文档数
		double[] classN=new double[trainSetlist.length];
		//总文档数
		double N =0.0;
		for(int l=0;l<trainSetlist.length;l++){
			List<String> eachClassDoc = IO.getfileLineToList(trainPath+"//"+trainSetlist[l].getName());
			classN[l]=eachClassDoc.size();
			System.out.println(trainSetlist[l].getName().split("\\.")[0]+"文档数:"+classN[l]);
			N=N+eachClassDoc.size();
		}
		

		System.out.println("总文档数:"+N);
		
		//词-词频-文档数
		List<String> wordlines = IO.getfileLineToList(wordCountPath);		
		
		//计算TF
		double tf[] = new double[wordlines.size()];
		String term[] = new String[wordlines.size()];
		for (int j = 0; j < wordlines.size(); j++) {
			String line = wordlines.get(j);
			String[] tmp = line.split(" ");
			term[j] = tmp[0];
			double totallTf=0.0;
			for(int i = 0; i < classNum; i++){
				totallTf=totallTf+Double.parseDouble(tmp[i+1].split("\\|")[0]);
			}
			tf[j]=totallTf;
		}
		
		//计算IDF
		double idf[] = new double[wordlines.size()];
		for (int j = 0; j < wordlines.size(); j++) {
			String line = wordlines.get(j);
			String[] tmp = line.split(" ");
			term[j] = tmp[0];
			//df其实就是文档频数，但是作为中间变量，所以没有用数组存储
			double df=0.0;
			for(int i = 0; i < classNum; i++){
				df=df+Double.parseDouble(tmp[i+1].split("\\|")[1]);
			}
			idf[j] = Math.log(N/df+0.01);
		}
			
		//计算TF-IDF
		double tfidf[] = new double[wordlines.size()];
		for (int j = 0; j < wordlines.size(); j++) {
			tfidf[j]=tf[j]*idf[j];
		}
		
		//按顺序排列
		double[] a = new double[wordlines.size()];
		double b;
		String[] aword = new String[wordlines.size()];
		String bword;
		
		for(int j = 0; j < wordlines.size(); j++){
			String line = wordlines.get(j);
			String[] tmp = line.split(" ");
			if(!tmp[0].equals("/")){
				a[j]=tfidf[j];
				aword[j]=line;
			}
		}
		//外面的for循环控制趟数
		for(int i=0;i<a.length;i++)
		{
			//里面的for循环控制比较次数
		    for(int j=0;j<a.length-1-i;j++)
		    {
		       //满足条件则交换数据
		        if(a[j]<a[j+1])
		        {
		          b=a[j];
		          bword=aword[j];
		          a[j]=a[j+1];
		          aword[j]=aword[j+1];
		          a[j+1]=b;
		          aword[j+1]=bword;
		        }
		     }
		 }
		
		ArrayList<String> termlists=new ArrayList<String>();
		//从高到低词频的顺序
	    for(int i=0;i<a.length;i++)
	    {
	    	if(a[i]>=5){
	    		//if(!(aword[i].split(" ")[0].split("/")[0].length()==1)){
	    		//System.out.println(aword[i]+a[i]);
	    		termlists.add(aword[i]+a[i]);
	    		//IO.writeFile(savePath,aword[i]+a[i]);
	    	}
	    }
	    	
	    for(int i=0;i<num;i++){
	    	IO.writeFile(savePath,termlists.get(i));
	    }
	    
	    long endtime = System.currentTimeMillis();
		long costTime = (endtime - begintime) / 1000;
		System.out.println("耗时：" + costTime + "秒！");
	}
}
