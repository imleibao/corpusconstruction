/**
 * 
 */
package com.tc.cluster;
/** 
 * @author 作者 E-mail: 
 * @version 创建时间：2014-3-3 下午10:14:07 
 * 类说明 
 */
/**
 * @author chen
 *
 */
import java.io.File;  

import com.tc.classify.WekaClassifier;
import com.tc.util.IO;
import com.tc.util.Parameter;

import weka.attributeSelection.AttributeSelection;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.trees.J48;
import weka.clusterers.SimpleKMeans;  
import weka.core.DistanceFunction;  
import weka.core.Instance;  
import weka.core.Instances;  
import weka.core.converters.ArffLoader;  
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.AddCluster;
import weka.filters.unsupervised.attribute.Remove;
  
public class Test {  
    public static void main(String[] args) {  
        Instances ins = null;  
        Instances tempIns = null;  
        Instances clusteredInstances = null;  
        int maxIteration=0;
        double squaredError=0.0;
        try {  
            String resultSaveFileName="E://PYH//feature//kMeansResult_test.txt";
            String saveFileName="E://PYH//feature//test.arff";
        	// 1.读入样本  
            // File file= new  
            // File("E://application//Weka-3-7//data//contact-lenses.arff");  
            File file = new File("E://PYH//feature//train_tfidf_fs_test.arff");  
            ArffLoader loader = new ArffLoader();  
            loader.setFile(file);  
            ins = loader.getDataSet();  
            //System.out.println(ins.toSummaryString());
            
            //去除class列
            Remove removeClass=new Remove();
            String[] options=new String[2];
            options[0] = "-R"; 
            options[1] = "1"; 
            removeClass.setOptions(options);
            removeClass.setInputFormat(ins); 
            ins = Filter.useFilter(ins, removeClass); 
            //System.out.println(ins.toSummaryString()); 
            
            
           // 2.初始化聚类器  
            SimpleKMeans KM = new SimpleKMeans();  
           
            //循环Kmeans找到最小squaredError 
            int maxNumClusters= 10;
            int maxSeed= 100;
            //先找到最佳的N
            //初始化squared errors	
            KM.setNumClusters(2);// 设置类别数量 
            KM.setSeed(10);// 设置种子数目
            //使用聚类算法对样本进行聚类
            KM.buildClusterer(ins);
            //得到初始化squared errors	
            squaredError=KM.getSquaredError();
            double MinSquaredError=squaredError;
            int bestNumClusters=2;
            int bestSeed=10;
            for(int i=bestNumClusters+1;i<=maxNumClusters;i++){
            	 KM.setNumClusters(i);// 设置类别数量  
            	 KM.setSeed(bestSeed);// 设置种子数目
            	 // 3.使用聚类算法对样本进行聚类  
                 KM.buildClusterer(ins);
                 squaredError=KM.getSquaredError();//Within cluster sum of squared errors	
                 if(MinSquaredError>squaredError){
                	 MinSquaredError= squaredError;
                	 bestNumClusters=i;
                	 System.out.println("NumClusters:"+bestNumClusters+";seed="+bestSeed+";squaredError:"+MinSquaredError);
                 }
            }
            
            //再找到最佳的seed
            for(int i=bestSeed+10;i<=maxSeed;i=i+10){
            	 KM.setNumClusters(bestNumClusters);// 设置类别数量  
            	 KM.setSeed(i);// 设置种子数目
            	// 3.使用聚类算法对样本进行聚类  
                 KM.buildClusterer(ins);
                 squaredError=KM.getSquaredError();//Within cluster sum of squared errors	
                 if(MinSquaredError>squaredError){
                	 MinSquaredError= squaredError;
                	 bestSeed=i;
                	 System.out.println("NumClusters:"+bestNumClusters+";seed="+bestSeed+";squaredError:"+MinSquaredError);
                 }
            }
            
            KM.setNumClusters(bestNumClusters);// 设置类别数量  
            KM.setSeed(bestSeed);// 设置种子数目
            KM.setMaxIterations(500);//设置最大迭代次数,默认500 
            // 3.使用聚类算法对样本进行聚类  
            KM.buildClusterer(ins);  
              
            // 4.打印聚类结果   
            tempIns = KM.getClusterCentroids();  
            
            System.out.println(KM.getClass()+" 聚类类数："+KM.getNumClusters()+" seed值："+KM.getSeed()+" Attributes:"+tempIns.numAttributes());  
            System.out.println(KM.toString()); 
            IO.writeFile(resultSaveFileName, KM.getClass()+" 聚类类数："+KM.getNumClusters()+" seed值："+KM.getSeed()+" Attributes:"+tempIns.numAttributes() );
            IO.writeFile(resultSaveFileName, KM.toString());
            /*
            //最大迭代次数
            maxIteration=KM.getMaxIterations();
            System.out.println("Number of iterations:" + maxIteration);  
            IO.writeFile(saveFileName, "Number of iterations:" + maxIteration);
            //Within cluster sum of squared errors
            squaredError=KM.getSquaredError();
            System.out.println("Within cluster sum of squared errors: " + squaredError);  
            IO.writeFile(saveFileName, "Within cluster sum of squared errors: " + squaredError);
            //聚类中心
            System.out.println("-------------------/n");  
            IO.writeFile(saveFileName, "-------------------/n");
            tempIns = KM.getClusterCentroids();  
            System.out.println("Cluster centroids: " ); 
            IO.writeFile(saveFileName, "Cluster centroids: " );
 
            for (int i = 0; i < tempIns.numInstances(); i++) {  
                Instance temp = tempIns.instance(i);  
                //IO.writeFile(saveFileName, String.valueOf(temp.numAttributes()));
                for (int j = 0; j < temp.numAttributes(); j++) {  
                    System.out.print(temp.value(j) + ",");  
                    IO.writeFile2(saveFileName, temp.value(j) + ",");
                }  
                System.out.println(""); 
                IO.writeFile(saveFileName, "");
            }  
            */

            //5.记录聚类结果
            AddCluster clusterClass=new  AddCluster();
            String[] addoptions=new String[2];
            addoptions[0] = "-W"; 
            addoptions[1] = "weka.clusterers.SimpleKMeans -N 10 -A \"weka.core.EuclideanDistance -R first-last\" -I 500 -S 10"; 
            clusterClass.setOptions(addoptions);
            clusterClass.setInputFormat(ins); 
            ins = Filter.useFilter(ins, clusterClass); 
            System.out.println(ins.toString()); 
            IO.writeFile(saveFileName, ins.toString());
            //System.out.println(ins.attribute("cluster"));
           
          
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
   
}  
