package com.tc.cluster;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class SilhouetteCoefficient {
	
	 public static double SilhouetteCoefficientCluster(Instances ins,int numCluster,int seed, Map<Integer, double[][]> idIndexValue) {
		   
	        //整体轮廓系数
            double Silhouette=0.0;
	        try {  
	            //1、聚类得到各个实例的类别
	            //初始化聚类器
	            SimpleKMeans KM = new SimpleKMeans();          
	            //使用聚类算法对样本进行聚类
	            KM.buildClusterer(ins);
	            KM.setNumClusters(numCluster);// 设置类别数量  
	            KM.setSeed(seed);// 设置种子数目
	            KM.setMaxIterations(50);//设置最大迭代次数,默认500 
	            //使用聚类算法对样本进行聚类  
	            KM.buildClusterer(ins);  	
	            // System.out.println(KM.getNumClusters());
	            //得到各个实例的类别
	            //instance的类别，map中左边为instance,右边为instance对应类别
	            Map<Integer, Integer> instanceClusterList = new HashMap();
	            for(int i=0;i<ins.numInstances();i++){
	            	//System.out.println(i+" "+KM.clusterInstance(ins.instance(i)));	
	            	instanceClusterList.put(i, KM.clusterInstance(ins.instance(i)));
	            } 
	            
	            /*
	            //将所有的文档转化为三维数值供后续使用
	            Map<Integer, double[][]> idIndexValue = new HashMap();
	            for(int i=0;i<ins.numInstances();i++){
	            	//System.out.println(ins.instance(i));
	            	double[][] atestp=indexValue(ins.instance(i).toString());
	            	idIndexValue.put(i, atestp);	                       
	            }
	            */
	            //输出idIndexValue
	            /*
	            for(int i=0;i<ins.numInstances();i++){
	            	double[][] atest=idIndexValue.get(i);
	            	System.out.println("id="+i+" ");
	            	for(int j=0;j<atest.length;j++){  	                   
	                     for(int m=0;m<atest[j].length;m++){            
	                         System.out.print(atest[j][m]+" ");  
	                     }  
	                     //输出一列后就回车空格  
	                     System.out.println();  
	                 }
	            }
	           */
	            
	            //2、计算轮廓系数
	            //得到所有的si
	            double[] s=new double[ins.numInstances()] ;
	            for(int k=0;k<ins.numInstances();k++){
	            	int i=k;
	            	 //计算每一个元素的s
	           	 	/*
	           	 	 * 对于第i个元素x_i，计算x_i与其同一个簇内的所有其他元素距离的平均值，记作a_i，用于量化簇内的凝聚度。
	           		选取x_i外的一个簇b，计算x_i与b中所有点的平均距离，遍历所有其他簇，找到最近的这个平均距离,记作b_i，用于量化簇之间分离度。
	           		对于元素x_i，轮廓系数s_i = (b_i – a_i)/max(a_i,b_i) 
	           	 	 */
	            
	            	int needinstanceCluster=KM.clusterInstance(ins.instance(i));
	            	
	                //得到计算x_i与簇b中所有点的距离之和(区分同簇(去除自己)、不同簇(所有))
	                double[] clusterDisList = new double[KM.getNumClusters()];
	                //得到x_i与不同簇b中所有点的个数
	                int[] clusterNumList=new int[KM.getNumClusters()];
	                
	            	for(int j=0;j<ins.numInstances();j++){
	            		//找到同类的instance
	            		if(instanceClusterList.get(j).equals(needinstanceCluster)){
	            			if(j!=i){
	            				//samedis用于计算x_i与其同一个簇内第j个元素距离
	            				double samedis=distanceCalculation(idIndexValue.get(i),idIndexValue.get(j));
	            				System.out.println(samedis);
	            				clusterDisList[needinstanceCluster]=clusterDisList[needinstanceCluster]+samedis;  
	            				clusterNumList[needinstanceCluster]++;
	            			}
	            		}
	            		//找到不同类的instance
	            		else{
	            			for(int m=0;m<KM.getNumClusters();m++){	
	            				if(instanceClusterList.get(j).equals(m)){
	            					double differentdis=distanceCalculation(idIndexValue.get(i),idIndexValue.get(j));   
	            					clusterDisList[m]=clusterDisList[m]+differentdis;
	            					clusterNumList[m]++;
	            					System.out.println(differentdis);
	            				}
	            			}
	            		}	
	            	}
	            	
	            	//System.out.println("第"+i+"个文档");
	            	//对于第i个元素x_i，计算x_i与簇内的所有其他元素距离的平均值
	            	double[] averageClusterDisList=new double[KM.getNumClusters()];
	            	for(int m=0;m<KM.getNumClusters();m++){
	            			//System.out.println(clusterDisList[m]);
	            			//System.out.println(clusterNumList[m]);
	            		    if(clusterNumList[m]!=0){
	            		    	averageClusterDisList[m]=clusterDisList[m]/clusterNumList[m];
	            		    	//System.out.println(averageClusterDisList[m]);
	            		    }
	            		    else{
	            		    	averageClusterDisList[m]=0.0;
	            		    	//System.out.println(averageClusterDisList[m]);
	            		    }
	            			//System.out.println(averageClusterDisList[m]);
	            	}
	            	
	            	//得到a
	            	double a=averageClusterDisList[needinstanceCluster];
	            	System.out.println("同簇："+a);
	            	//得到b
	                double b=getMin(delete(a,averageClusterDisList));
	                System.out.println("不同簇："+b);
	            	//计算s
	                s[i]=(b-a)/getMax(a,b); 
	                System.out.println(i+" "+"同簇："+a+" 不同簇："+b+" "+s[i]);
	            }
	            
	            
	            for(int i=0;i<s.length;i++){
	            	Silhouette=Silhouette+s[i];
	            }
	            Silhouette=Silhouette/s.length;
	            //System.out.println(Silhouette);
	           
	                      
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }
	        //System.out.println(Silhouette);
			return Silhouette;  
	   
	 }
	 
	 public static double distanceCalculation(double[][] firstpoint,double[][] secondpoint){
		 
		 //欧式距离
		 double distance=0.0;
		
		 int f=firstpoint.length;
		 int s=secondpoint.length;
		 //System.out.println(f+" "+s);
		 int num=0;
		 if(firstpoint[firstpoint.length-1][0]>=secondpoint[secondpoint.length-1][0]){
			 //System.out.println(firstpoint[f-1][0]);
			 num=(int) firstpoint[f-1][0]+1;
		 }
		 else{
			 //System.out.println(secondpoint[s-1][0]);
			 num=(int) secondpoint[s-1][0]+1;
		 }
		 //System.out.println(num);
		 
		 int fIndex=0;
		 int sIndex=0;
		 double fValue=0.0;
		 double sValue=0.0;
		 double[] X=new double[num];
		 for(int i=0;i<num;i++){			
			 for(int j=0;j<firstpoint.length;j++){
				 fIndex=(int) firstpoint[j][0];
				 fValue=firstpoint[j][1]; 
				 for(int m=0;m<secondpoint.length;m++){
					 sIndex=(int) secondpoint[m][0];
					 sValue=secondpoint[m][1];
					 if(fIndex==i&&sIndex==i){
						 X[i]=fValue-sValue;	
					 }
				 }
			 }
			 for(int j=0;j<firstpoint.length;j++){
				 fIndex=(int) firstpoint[j][0];
				 fValue=firstpoint[j][1]; 
				 if(X[i]==0.0){
					 if(fIndex==i){
						 X[i]=fValue;	
					 } 
				 }
			 }
			 for(int m=0;m<secondpoint.length;m++){
				 sIndex=(int) secondpoint[m][0];
				 sValue=secondpoint[m][1];
				 if(X[i]==0.0){
					 if(sIndex==i){
						 X[i]=sValue;	
					 } 
				 }
			 }	
		 }
		 for(int i=0;i<num;i++){		
			 if(X[i]!=0.0){
				 distance=distance+Math.pow(X[i],2);
			 }
		 }
		 distance=Math.sqrt(distance);
		 return distance;
		 /*
		 System.out.println("firstpoint");
		 //得到firstpoint各个点
		 for(int j=0;j<firstpoint.length;j++){  	                   
             for(int m=0;m<firstpoint[j].length;m++){            
                 System.out.print(firstpoint[j][m]+" ");  
             }  
             //输出一列后就回车空格  
             System.out.println();  
         }
		 
		 System.out.println("secondpoint");
		 //得到secondpoint各个点
		 for(int j=0;j<secondpoint.length;j++){  	                   
             for(int m=0;m<secondpoint[j].length;m++){            
                 System.out.print(secondpoint[j][m]+" ");  
             }  
             //输出一列后就回车空格  
             System.out.println();  
         }
         */
		
	 }
	 
	 
	 public static double[][] indexValue(String line){
		 String s=line.substring(1, line.toString().length()-1);
		 String[] m=s.split(",");
		 double[][] a=new double[m.length][2];
		 for(int j=0;j<m.length;j++){
			int index=Integer.parseInt(m[j].split("\\s+")[0]);
			a[j][0]=index;
			a[j][1]=Double.parseDouble(m[j].split("\\s+")[1]);
		}
		 return a;
	 }
	 
	
	 //得到数组最小值
	 public static double getMin(double[] n) {
		    double minN = 0.0;
		    if(n.length==1){
		    	minN=n[0];
		    }
		    else{
		    	for(int i=0;i<(n.length-1);i++){
		    		if(n[i]<n[i+1]){
		    			minN=n[i];
		    		}
		    		else
		    		{
		    			minN=n[i+1];
		    		}
		    	}
		    }
			return minN;
	}
	//得到两个数中最大值
	 public static double getMax(double a,double b) {
		    double maxN=0.0;
		 	if(a>b){
		    	maxN=a;
		    }
		 	else{
		 		maxN=b;
		 	}
			return maxN;		
	 }
	 
	 //删除数组中n的值
	 public static double[] delete(double n,double[] a)
	 {
		 List<Double> tempList = new ArrayList();
		 for(double i:a){
		     if(i!=n){
		         tempList.add(i);
		         //System.out.println(i);
		     }
		 }
		 //System.out.println(tempList.size());
		 double[] newArr=new double[tempList.size()];
		 for(int i=0;i<newArr.length;i++){
			 newArr[i]=tempList.get(i);
			 //System.out.println(newArr[i]);
		 }
		 return newArr;
	 }
}
