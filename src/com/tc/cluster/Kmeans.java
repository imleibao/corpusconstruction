package com.tc.cluster;

import java.io.File;
import java.io.IOException;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.AddCluster;
import weka.filters.unsupervised.attribute.Remove;

import com.tc.util.IO;

public class Kmeans {
	public static void kmeanscluster(String arffFileName,String resultFileName,int maxnumCluster,int maxseed) throws Exception{
		Instances ins=null;
		try {
			// 1.读入样本  
			File file = new File(arffFileName);  
			ArffLoader loader = new ArffLoader();  
			loader.setFile(file);  
			ins = loader.getDataSet();  
		            		            
			//2、去除class列
		    Remove removeClass=new Remove();
		    String[] options=new String[2];
		    options[0] = "-R"; 
		    options[1] = "1"; 
		    removeClass.setOptions(options);
		    removeClass.setInputFormat(ins); 
		    ins = Filter.useFilter(ins, removeClass); 
		                             
		    //3、  kmeans -xunhuan
		    for(int i=2;i<=maxnumCluster;i++){
		    		Kmeans(ins,i,arffFileName,resultFileName);
		    }
		    
		    
		    /*
		    //6.将聚类结果加入arff文件，保存新的ARFF

            AddCluster clusterClass=new  AddCluster();
            String[] addoptions=new String[2];
            addoptions[0] = "-W"; 
            addoptions[1] = "weka.clusterers.SimpleKMeans -N "+numCluster+" -A \"weka.core.EuclideanDistance -R first-last\" -I 500 -S "+seed; 
            clusterClass.setOptions(addoptions);
            clusterClass.setInputFormat(ins); 
            ins = Filter.useFilter(ins, clusterClass);                      
            IO.writeFile(saveFileName, ins.toString());
            */
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//生成kMeans聚类器
	public static void Kmeans(Instances ins,int numCluster,String arffFileName,String resultFileName) throws Exception{
		 SimpleKMeans KM = new SimpleKMeans();          
         //使用聚类算法对样本进行聚类
         KM.buildClusterer(ins);
         KM.setNumClusters(numCluster);// 设置类别数量  
         KM.setSeed(10);// 设置种子数目
         KM.setMaxIterations(500);//设置最大迭代次数,默认500 
         //使用聚类算法对样本进行聚类  
         KM.buildClusterer(ins);  	
		    
		    //System.out.println(KM.toString());
		    ClusterEvaluation eval = new ClusterEvaluation();
		    eval.setClusterer(KM);
		    eval.evaluateClusterer(ins);
		    //System.out.println(ins.toString());
		    double[] num = eval.getClusterAssignments();

		    /*
		    for (int i = 0; i < num.length; i++)
		    {
		    	System.out.println(String.valueOf(num[i]));
		    }
		    */
		    System.out.println("arff:"+arffFileName+";  clustermethod:"+KM.getClass()+"; clusternum:"+numCluster+"; seed:"+10);		    
		    System.out.println(eval.clusterResultsToString());
		    IO.writeFile(resultFileName, "arff:"+arffFileName+";  clustermethod:"+KM.getClass()+"; clusternum:"+numCluster+"; seed:"+10);
		    IO.writeFile(resultFileName, eval.clusterResultsToString());
		    //System.out.println(eval.getNumClusters());
	}
		
	
	 public static void main(String[] args) throws Exception {  
		 kmeanscluster("E://PYH//feature//train_tfidf_fs_removespeechstopword_100.arff","E://PYH//feature//kMeansResult.txt",20,100);
		 kmeanscluster("E://PYH//feature//train_tfidf_fs_removespeechstopword_200.arff","E://PYH//feature//kMeansResult.txt",20,100);
		 kmeanscluster("E://PYH//feature//train_tfidf_fs_removespeechstopword_300.arff","E://PYH//feature//kMeansResult.txt",20,100);
		 kmeanscluster("E://PYH//feature//train_tfidf_fs_removespeechstopword_400.arff","E://PYH//feature//kMeansResult.txt",20,100);
		 kmeanscluster("E://PYH//feature//train_tfidf_fs_removespeechstopword_500.arff","E://PYH//feature//kMeansResult.txt",20,100);
		 kmeanscluster("E://PYH//feature//train_tfidf_fs_removespeechstopword_600.arff","E://PYH//feature//kMeansResult.txt",20,100);
		 kmeanscluster("E://PYH//feature//train_tfidf_fs_removespeechstopword_700.arff","E://PYH//feature//kMeansResult.txt",20,100);
		 kmeanscluster("E://PYH//feature//train_tfidf_fs_removespeechstopword_800.arff","E://PYH//feature//kMeansResult.txt",20,100);
	 }
	
}
