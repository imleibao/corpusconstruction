package com.tc.cluster;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.AddCluster;
import weka.filters.unsupervised.attribute.Remove;

import com.tc.util.IO;

public class WekaCluster2 {
	 public static void cluster(String arffFileName,String clusterResultFileName,String saveFileName,int clusterNum,int clusterSeed) {
		    long begintime = System.currentTimeMillis();  
		    Instances ins = null;  
	        Instances tempIns = null;  
	        Instances clusteredInstances = null;  
	        int maxIteration=0;
	       
	        
	        try {  
	            String resultSaveFileName=clusterResultFileName;
	        	// 1.读入样本  
	            // File file= new  
	            // File("E://application//Weka-3-7//data//contact-lenses.arff");  
	            File file = new File(arffFileName);  
	            ArffLoader loader = new ArffLoader();  
	            loader.setFile(file);  
	            ins = loader.getDataSet();  
	            //System.out.println(ins.toSummaryString());
	            System.out.println("数据导入成功！");
	            
	            //2、去除class列
	            Remove removeClass=new Remove();
	            String[] options=new String[2];
	            options[0] = "-R"; 
	            options[1] = "1"; 
	            removeClass.setOptions(options);
	            removeClass.setInputFormat(ins); 
	            ins = Filter.useFilter(ins, removeClass); 
	            //System.out.println(ins.toSummaryString()); 
	            System.out.println("去除class列！");
	           	            	            
	            //将所有的文档转化为三维数值供后续使用
	            Map<Integer, double[][]> idIndexValue = new HashMap();
	            for(int i=0;i<ins.numInstances();i++){
	            	//System.out.println(ins.instance(i));
	            	//System.out.println(ins.instance(i).toString());
	            	double[][] atestp=SilhouetteCoefficient.indexValue(ins.instance(i).toString());
	            	idIndexValue.put(i, atestp);	                       
	            }
	            System.out.println("数据全部转成double三维数组!");
	            
	            //3。求解轮廓系数
	            double Silhouette=SilhouetteCoefficient.SilhouetteCoefficientCluster(ins, clusterNum, clusterSeed,idIndexValue);	           
	            System.out.println("轮廓系数："+Silhouette+";类别数："+clusterNum+"随机种子数："+clusterSeed); 
	            IO.writeFile(resultSaveFileName, arffFileName+" 轮廓系数："+Silhouette+";类别数："+clusterNum+"随机种子数："+clusterSeed);
	            
	           // 4.聚类结果  
	            SimpleKMeans KM = new SimpleKMeans();  
	            KM.setNumClusters(clusterNum);// 设置类别数量  
	            KM.setSeed(clusterSeed);// 设置种子数目
	            KM.setMaxIterations(500);//设置最大迭代次数,默认500 
	            KM.buildClusterer(ins);   
	              
	            // 5.打印聚类结果   
	            tempIns = KM.getClusterCentroids();    
	            System.out.println(KM.getClass()+" 聚类类数："+KM.getNumClusters()+" seed值："+KM.getSeed()+" Attributes:"+tempIns.numAttributes());  
	            //System.out.println(KM.toString());      
	            IO.writeFile(resultSaveFileName, KM.getClass()+" 聚类类数："+KM.getNumClusters()+" seed值："+KM.getSeed()+" Attributes:"+tempIns.numAttributes() );
	            IO.writeFile(resultSaveFileName, KM.toString());
	           
	            int[] percentresuilt=KM.getClusterSizes();
	            double instanceNum=0.0; 
	            for(int i=0;i<percentresuilt.length;i++){
	            	instanceNum=instanceNum+percentresuilt[i];
	            }
	            System.out.println("Clustered Instances");
	            IO.writeFile(resultSaveFileName, "Clustered Instances");
	            for(int i=0;i<percentresuilt.length;i++){
	            	System.out.println(i+" "+percentresuilt[i]+"("+percentresuilt[i]/instanceNum*100+"%)");
	            	IO.writeFile(resultSaveFileName, i+" "+percentresuilt[i]+"("+percentresuilt[i]/instanceNum*100+"%)");
	            }
	       
	            
	            //6.将聚类结果加入arff文件，保存新的ARFF
	            AddCluster clusterClass=new  AddCluster();
	            String[] addoptions=new String[2];
	            addoptions[0] = "-W"; 
	            addoptions[1] = "weka.clusterers.SimpleKMeans -N "+clusterNum+" -A \"weka.core.EuclideanDistance -R first-last\" -I 500 -S "+clusterSeed; 
	            clusterClass.setOptions(addoptions);
	            clusterClass.setInputFormat(ins); 
	            ins = Filter.useFilter(ins, clusterClass);                      
	            IO.writeFile(saveFileName, ins.toString());
	            IO.writeFile(saveFileName, "");
	            IO.writeFile(saveFileName, "");
	            IO.writeFile(saveFileName, "");
	            
	            long endtime = System.currentTimeMillis();
	    		long costTime = (endtime - begintime) / 1000;
	    		System.out.println("耗时：" + costTime + "秒！");
	            
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	   
	 }
}
