package com.tc.cluster;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.tc.util.IO;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class DB_index {
	
	public static void DB_index_method(String arffFileName,int numCluster,String resultsaveFileName) throws Exception{
	long begintime = System.currentTimeMillis();  
	Instances ins=null;
	Instances tempIns = null;  
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
	                             
	    //3、  kmeans聚类器
	    SimpleKMeans KM = new SimpleKMeans();          
        //使用聚类算法对样本进行聚类
        KM.buildClusterer(ins);
        KM.setNumClusters(numCluster);// 设置类别数量  
        KM.setSeed(10);// 设置种子数目
        KM.setMaxIterations(500);//设置最大迭代次数,默认500 
        //使用聚类算法对样本进行聚类  
        KM.buildClusterer(ins);  	
        
        
        //4、获取kmeans聚类中心
        tempIns=KM.getClusterCentroids();
        /*
        for(int i=0;i<tempIns.numInstances();i++){        	
           Instance clusterCentroid=tempIns.instance(i);
           System.out.println("cluster:"+i+";centroid:"+clusterCentroid);
          
        }
           
        
        //5、获取kmenas后每个文档的类别
        for(int i=0;i<ins.numInstances();i++){
        	int cluster=KM.clusterInstance(ins.instance(i));
        	System.out.println("id:"+i+";cluster:"+cluster);
        	System.out.println("id:"+i+";vector:"+ins.instance(i));
        }
        */
        //6、获取kmeans聚类后的每个类的文档数
        int[] clusterNums=KM.getClusterSizes();
        for(int i=0;i<clusterNums.length;i++){
        	System.out.println(i+" "+clusterNums[i]);
        }
        
        //7.将所有的文档转化为三维数值供后续使用
        Map<Integer, double[][]> idIndexValue = new HashMap();
        for(int i=0;i<ins.numInstances();i++){
        	//System.out.println(ins.instance(i));
        	//System.out.println(ins.instance(i).toString());
        	double[][] atestp=SilhouetteCoefficient.indexValue(ins.instance(i).toString());
        	idIndexValue.put(i, atestp);	                       
        }
        //System.out.println("数据全部转成double三维数组!");
        
        
        //获取si的簇内离散度，未求平均
        double[] clusterS=new double[tempIns.numInstances()];
        //计算同类中每个文档与聚类中心的距离
        double clusterdistance=0.0;
        for(int i=0;i<ins.numInstances();i++){
        	int idcluster=KM.clusterInstance(ins.instance(i));
        	for(int j=0;j<tempIns.numInstances();j++){       		
        		if(idcluster==j){      			
        			double[][] document=idIndexValue.get(i);
        			double[][] clusterCentroid=stringtodouble(tempIns.instance(j).toString());
        			double distance=distanceCalculation(document,clusterCentroid);
        			//System.out.println(i+" cluster:"+j+" "+distance);
        			clusterS[j]=clusterS[j]+distance;        			
        		}
        	}
        }
       
        
        //计算每个类的类内平均离散度
        for(int i=0;i<tempIns.numInstances();i++){
        	clusterS[i]=clusterS[i]/clusterNums[i];
        }
               
        //计算类间距离
        double[] R=new double[tempIns.numInstances()];
        for(int i=0;i<tempIns.numInstances();i++){    
        	double[][] firstclusterCentroid=stringtodouble(tempIns.instance(i).toString());
        	double[] priR=new double[tempIns.numInstances()];
        	for(int j=0;j<tempIns.numInstances();j++){  
        		if(i!=j){
        			double[][] secondclusterCentroid=stringtodouble(tempIns.instance(j).toString());
        			double outdistance=distanceCalculation(firstclusterCentroid,secondclusterCentroid);
        			//System.out.println("cluster"+i+";cluster"+(j)+"的类间距离:"+outdistance);
        			//System.out.println("cluster"+i+"的类内距离:"+clusterS[i]+";cluster"+j+"的类内距离:"+clusterS[j]);
        			//计算R
        			priR[j]=(clusterS[i]+clusterS[j])/outdistance;
        			//System.out.println(priR[j]);
        		}
        	}
        	R[i]=getMax(priR);
        	System.out.println(R[i]);
        }
        
        //计算DB_index
        double DB_index=0.0;
        for(int i=0;i<tempIns.numInstances();i++){
        	DB_index=DB_index+R[i];
        }
        DB_index=DB_index/(tempIns.numInstances());
        System.out.println(arffFileName+"；聚类方法："+KM.getClass()+";类数:"+numCluster+"DB_index:"+DB_index);
        IO.writeFile(resultsaveFileName, arffFileName+"；聚类方法："+KM.getClass()+";类数:"+numCluster+"DB_index:"+DB_index+";SquaredError:"+KM.getSquaredError());
        long endtime = System.currentTimeMillis();
		long costTime = (endtime - begintime) / 1000;
		System.out.println("耗时：" + costTime + "秒！");
        
	} 
	catch (IOException e) {
		e.printStackTrace();
	}
	}
	
	public static double[][] stringtodouble(String str){
		
		String[] eachstr=str.split(",");
		ArrayList eachValue=new ArrayList();
		ArrayList eachIndex=new ArrayList();
		for(int i=0;i<eachstr.length;i++){
			if(!eachstr[i].equals("0")){
				eachValue.add(eachstr[i]);
				eachIndex.add(i);
			}			
		}
		/*
		for(int i=0;i<eachValue.size();i++){
			System.out.print(eachIndex.get(i)+" "+eachValue.get(i)+",");
		}
		System.out.println("");
		*/
		double[][] indexValue = new double[eachValue.size()][2];
		for(int i=0;i<eachValue.size();i++){
			indexValue[i][0]=Double.valueOf(eachIndex.get(i).toString());
			indexValue[i][1]=Double.valueOf(eachValue.get(i).toString());
		}
		/*
		for(int i=0;i<eachValue.size();i++){
			System.out.print(indexValue[i][0]+" "+indexValue[i][1]+",");
		}
		System.out.println("");
		*/
		return indexValue;
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
	
	public static void main(String[] args) throws Exception {  
		//DB_index_method("E://PYH//feature//train_tfidf_fs_removespeechstopword_100.arff",3);
	}

}
