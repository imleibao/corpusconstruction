package com.tc.cluster;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import com.tc.util.IO;

import weka.associations.tertius.Predicate;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.EvaluationUtils;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.functions.LibSVM;
import weka.clusterers.SimpleKMeans;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.AddCluster;
import weka.filters.unsupervised.attribute.Remove;

public class Entropy_Classifiers {
	public static void main(String[] args) throws Exception {  
		entropy_classifiers_method("E:\\PYH\\feature\\train_tfidf_fs_removespeechstopword_100.arff",4,"E:\\PYH\\feature\\justtest2.txt");
	}
	
	
	public static void entropy_classifiers_method(String arffFileName,int numCluster,String resultsaveFileName) throws Exception{
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
	        
	        //4、获取kmeans聚类后的每个类的文档数
	        //聚类情况下：求每个类别的p(xi)
	        //System.out.println(ins.numInstances());
	        double[] X=new double[numCluster]; 
	        int[] clusterNums=KM.getClusterSizes();
	        for(int i=0;i<clusterNums.length;i++){
	        	//System.out.println(i+" "+clusterNums[i]);
	        	X[i]=(double) clusterNums[i]/ins.numInstances();
	        	System.out.println(X[i]);
	        }
	        
	        //求出聚类后的信息量
	        double AX=0.0;
	        for(int i=0;i<numCluster;i++){
	        	AX=AX+X[i]*Math.log(X[i]);
	        }	        
	        AX=-AX;
	        System.out.println(AX);
	        	        
	        //5.将Kmeans聚类结果增加入instances中
	        AddCluster clusterClass=new  AddCluster();
            String[] addoptions=new String[2];
            addoptions[0] = "-W"; 
            addoptions[1] = "weka.clusterers.SimpleKMeans -N "+numCluster+" -A \"weka.core.EuclideanDistance -R first-last\" -I 500 -S "+10; 
            clusterClass.setOptions(addoptions);
            clusterClass.setInputFormat(ins); 
            ins = Filter.useFilter(ins, clusterClass);  
            
            //7、利用SVM算法进行交叉十字验证分类
	        //初始化LIBSVM
	        Classifier m_classifier=new LibSVM();
	        String[] classifieroptions=new String[2];
	        classifieroptions[0] = "-K"; 
	        //设置options[1]的值用于选择哪个svm函数
	        classifieroptions[1] = "0"; 
	        m_classifier.setOptions(classifieroptions);
	        
	        ins.setClassIndex(ins.numAttributes() - 1);// 设置分类属性所在行号
	              	      
	        int seed = 1;          // the seed for randomizing the data
	        int folds = 10; 
	        Random rand = new Random(seed);   // create seeded number generator
	        Instances randData = new Instances(ins);   // create copy of original data
	       // randData.randomize(rand);         // randomize data with number generator
	       // randData.stratify(folds);
	        
	        int[] classCount=new int[numCluster];
	        
	        for (int n = 0; n < folds; n++) {
	        	   Instances train = randData.trainCV(folds, n);
	        	   Instances test = randData.testCV(folds, n);
	        	   m_classifier.buildClassifier(train); // 训练
	        	   Evaluation eval = new Evaluation(train);
	       		   eval.evaluateModel(m_classifier, test); //测试	
	       		   EvaluationUtils eu=new EvaluationUtils();
	       		   for(int i=0;i<test.numInstances();i++){
	       			   Prediction su=eu.getPrediction(m_classifier, test.instance(i));       			   
	       			   for(int k=0;k<test.numClasses();k++){
	       				   if(test.instance(i).classValue()==k){
	       					   if(test.instance(i).classValue()==su.predicted()){
	       						  // System.out.println(k+"个类别"+i+"真实类别："+test.instance(i).classValue()+";预测类别"+su.predicted());
	       						   classCount[k]++;
	       						   //System.out.println(classCount[k]);
	       					   }
	       				   }
	       			   }
	       		   } 		   
	        }
	        
	        //分类情况下：原属于xi划分的仍属于xi划分的概率
	        double[] P=new double[numCluster]; 
	        for(int i=0;i<numCluster;i++){
	        	System.out.println(classCount[i]);
	        	P[i]=(double) classCount[i]/ins.numInstances();
	        	System.out.println(P[i]);
	        }
	        
	        double BXP=0.0;
	        for(int i=0;i<numCluster;i++){
	        	BXP=BXP+X[i]*Math.log(P[i]);
	        	System.out.println(BXP);
	        }	        
	        BXP=-BXP;
	        System.out.println(BXP);
	        
	        //信息差异
	        double IDXP=Math.sqrt(Math.pow((BXP-AX),2));
	        System.out.println(IDXP);
	        
	        //分类准确率、错误率
	        Evaluation eval = new Evaluation(ins);
			eval.crossValidateModel(m_classifier, ins, 10, new Random(1));//交叉交叉10次验证
			System.out.println(eval.errorRate());
			
	        System.out.println(arffFileName+";聚类方法："+KM.getClass()+";类数:"+numCluster+";AX:"+AX+";BXP:"+BXP+";IDXP:"+IDXP+";准确率："+(1-eval.errorRate())+";错误率："+eval.errorRate());
	        IO.writeFile(resultsaveFileName, arffFileName+";聚类方法："+KM.getClass()+";类数:"+numCluster+";AX:"+AX+";BXP:"+BXP+";IDXP:"+IDXP+";准确率："+(1-eval.errorRate())+";错误率："+eval.errorRate());
	        
	        long endtime = System.currentTimeMillis();
			long costTime = (endtime - begintime) / 1000;
			System.out.println("耗时：" + costTime + "秒！");
	        
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
