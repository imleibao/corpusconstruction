package com.tc.cluster;

import java.io.File;


import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class testCluster {
	 public static void main(String[] args) {
		  
		    Instances ins = null;  
	        Instances tempIns = null;  
	        Instances clusteredInstances = null;  
	        
	        try {  
	            
	        	// 1.读入样本  
	            // File file= new  
	            // File("E://application//Weka-3-7//data//contact-lenses.arff");  
	            File file = new File("文件路径");  
	            ArffLoader loader = new ArffLoader();  
	            loader.setFile(file);  
	            ins = loader.getDataSet();  
	            //System.out.println(ins.toSummaryString());
	            // 4.聚类结果  
	            SimpleKMeans KM = new SimpleKMeans();  
	            KM.setNumClusters(2);// 设置类别数量  
	            KM.setSeed(10);// 设置种子数目
	            KM.setMaxIterations(500);//设置最大迭代次数,默认500 
	            KM.buildClusterer(ins);   
	            // 5.打印聚类结果   
	            tempIns = KM.getClusterCentroids();    
	            System.out.println(KM.getClass()+" 聚类类数："+KM.getNumClusters()+" seed值："+KM.getSeed()+" Attributes:"+tempIns.numAttributes());  
	            //System.out.println(KM.toString());      
	        } 
	        catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	  }

}
