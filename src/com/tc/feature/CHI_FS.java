package com.tc.feature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tc.util.IO;
import com.tc.util.Parameter;

/*
 * x^2(t,c)=N*(AD-BC)^2/[(A+C)(B+D)(A+B)(C+D)]
 * 特注，N为常数，可以忽略，不过后续算法设计还是考虑了
 * 其中， A为属于类别c并且包含词t的文档的数量，
 * B为不属于类别c并且包含词t的文档的数量，
 * C为属于类别c并且不包含词t的文档的数量，可以用类别C的总文档数减去A
 * D为既不属于类别c也不包含词t的文档的数量，
 * N为文档系统的总文档数量。
 */

public class CHI_FS {
	public static void main(String[] args) throws IOException {	
		//训练集
		String trainPath="Corpus";
		//词-词频-文档数
		String wordCountPath="wordCount.txt";
		//存储文件
		String savePath="CHI_fs.txt";
		//选择排序前num个词
		int num=1000;
		
		CHI_FS.CHI(trainPath,wordCountPath, savePath,num);
	}
	public static void CHI(String trainPath,String wordCountPath,String savePath,int n){
		
		File ff = new File(savePath);
		if (ff.isFile() && ff.exists())
			ff.delete();
		
		//训练集文档列表	
		File trainf = new File(trainPath);
		File[] trainSetlist = trainf.listFiles();

		
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
		
		//各个类中各个词的文档数,即求解A
		List<Map<Integer,Double >> mapTermClassDF = new ArrayList();
				
		for (int j = 0; j < wordlines.size(); j++) {	
			String line = wordlines.get(j);
			String[] tmp = line.split(" ");
			Map<Integer,Double> mapClassDF = new HashMap();
			for(int i=0;i<trainSetlist.length;i++){
				//类c下包含单词tk的文件数,即A
				double df= Double.parseDouble(tmp[i+1].split("\\|")[1]);
				//System.out.println(tmp[0]+" "+df);
				mapClassDF.put(i, df);
			}
			mapTermClassDF.add(mapClassDF);	
		}
		
		//每个词的文档总数
		double[] termNum=new double[wordlines.size()];
		for(int j=0;j < termNum.length; j++){
			termNum[j]=0.0;
		}
		for (int j = 0; j < wordlines.size(); j++) {
			String line = wordlines.get(j);
			String[] tmp = line.split(" ");
			if(!tmp[0].equals("/")){
				for(int i=0;i<trainSetlist.length;i++){
					//System.out.println(mapClassTermDF.get(i).get(tmp[j]));
					termNum[j] = termNum[j] + mapTermClassDF.get(j).get(i);
				}
			}
		}
		
		
		//CHI
		Map<String, Double> mapCHI = new HashMap();
		for (int j = 0; j < wordlines.size(); j++) {
			String line = wordlines.get(j);
			String[] tmp = line.split(" ");	
			double[] chi=new double[trainSetlist.length];
			Map<Integer, Double> mapClassCHI = new HashMap();
			for(int i=0;i<trainSetlist.length;i++){
				double A=mapTermClassDF.get(j).get(i);
				double B=termNum[j]-A;
				double C=classN[i]-A;
				double D=N-A-B-C;
				chi[i]=N*(A*D-B*C)*(A*D-B*C)/(A+C)/(B+D)/(A+B)/(C+D);
				//System.out.println(tmp[0]+" "+chi[i]);
				mapClassCHI .put(i,chi[i]);
			}
			mapCHI.put(tmp[0],CHI_FS.getMax(chi));
			
			String allChi=" ";
			for(int i=0;i<trainSetlist.length;i++){
				allChi=allChi+String.valueOf(mapClassCHI.get(i))+" ";
			}
			
			IO.writeFile(Parameter.getFeaturepath()+"//chi_all.txt",tmp[0]+allChi);
			
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
				a[j]=mapCHI.get(tmp[0]);
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
		
		
		//循环打印出从高到低词频的顺序
	    for(int i=0;i<a.length;i++)
	    {
	    	if(a[i]>=0){
	    		//if(!(aword[i].split(" ")[0].split("/")[0].length()==1)){
	    	//System.out.println(aword[i]+a[i]);
	    	IO.writeFile(savePath,aword[i]+a[i]);}//}
	    }
	}
	public static double getMax(double[] n) {
		// TODO Auto-generated method stub
	    double maxN = 0.0;
		for(int i=0;i<(n.length-1);i++){
	    	if(n[i]>n[i+1]){
	    		maxN=n[i];
	    	}
	    	else
	    	{
	    		maxN=n[i+1];
	    	}
	    }
		//System.out.println(maxN);
		return maxN;
		
	}
}
