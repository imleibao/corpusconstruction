package com.tc.cluster;

import java.io.File;
import java.io.IOException;

import com.tc.util.IO;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.XMeans;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.AddCluster;
import weka.filters.unsupervised.attribute.Remove;

public class Xmeans {
	public static void xmeanscluster(String arffFileName,String saveFileName,int minNumCluster,int maxNumCluster) throws Exception{
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
		    /*                             
		    //3、生成XMeans聚类器  
		    XMeans XM = new XMeans();  
		    XM.setMaxNumClusters(maxNumCluster);//最大聚类个数
		    XM.setMinNumClusters(minNumCluster);//最小聚类个数
		    XM.buildClusterer(ins);
		    
		    System.out.println(XM.toString());
		    ClusterEvaluation eval = new ClusterEvaluation();
		    eval.setClusterer(XM);
		    eval.evaluateClusterer(ins);
		    System.out.println(ins.toString());
		    double[] num = eval.getClusterAssignments();

		    for (int i = 0; i < num.length; i++)
		    {
		    	System.out.println(String.valueOf(num[i]));
		    }
		    System.out.println(eval.clusterResultsToString());
		    //System.out.println(eval.getNumClusters());
		    */
		    //6.将聚类结果加入arff文件，保存新的ARFF
            AddCluster clusterClass=new  AddCluster();
            String[] addoptions=new String[2];
            addoptions[0] = "-W"; 
            addoptions[1] = "weka.clusterers.XMeans -I 1 -M 1000 -J 1000 -L "+minNumCluster+" -H "+maxNumCluster+" -B 1.0 -C 0.5 -D \"weka.core.EuclideanDistance -R first-last\" -S 10"; 
            clusterClass.setOptions(addoptions);
            clusterClass.setInputFormat(ins); 
            ins = Filter.useFilter(ins, clusterClass);                      
            IO.writeFile(saveFileName, ins.toString());
		    
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	 public static void main(String[] args) throws Exception {  
		
		 xmeanscluster("E://PYH//feature//train_tfidf_fs_removespeechstopword_600.arff","E://PYH//feature//train_tfidf_fs_removespeechstopword_kmeans_600_8.arff",8,10);
		 
		 xmeanscluster("E://PYH//feature//train_tfidf_fs_removespeechstopword_600.arff","E://PYH//feature//train_tfidf_fs_removespeechstopword_kmeans_600_9.arff",9,10);
		
		 xmeanscluster("E://PYH//feature//train_tfidf_fs_removespeechstopword_600.arff","E://PYH//feature//train_tfidf_fs_removespeechstopword_kmeans_600_10.arff",10,10);
		
	 }
}
