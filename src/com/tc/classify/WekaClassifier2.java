package com.tc.classify;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.tc.util.IO;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.IBkSimilarity;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class WekaClassifier2 {
	//使用测试集验证
	public static void Classify(int type,String arffTrainFileName,String arffTestFileName,String saveFileName) throws Exception {				
		Classifier m_classifier = null;
		switch (type) {
		case 0:
			m_classifier = new IBkSimilarity(100);
			break;
		case 1:
			//设置括号内数值以设置knn的K值
			m_classifier = new IBk(10);//knn
			break;
		case 2:
			m_classifier = new LibSVM();
			/*
			 * -K <int>
  			Set type of kernel function (default: 2)
    		0 = linear: u'*v
    		1 = polynomial: (gamma*u'*v + coef0)^degree
    		2 = radial basis function: exp(-gamma*|u-v|^2)
    		3 = sigmoid: tanh(gamma*u'*v + coef0)
			 */
			String[] options=new String[2];
	        options[0] = "-K"; 
	        //设置options[1]的值用于选择哪个svm函数
	        options[1] = "0"; 
	        m_classifier.setOptions(options);
			break;
		case 3:
			m_classifier = new J48();//决策树
			break;
		case 4:
			m_classifier = new NaiveBayesMultinomial();
			break;
		default:
			m_classifier = new LibSVM();
			break;
		}

		File inputFile = new File(arffTrainFileName);// 训练语料文件

		ArffLoader atf = new ArffLoader();
		atf.setFile(inputFile);
		Instances instancesTrain = atf.getDataSet(); // 读入训练文件
		instancesTrain.setClassIndex(instancesTrain.numAttributes()-1);// 设置分类属性所在行号

		inputFile = new File(arffTestFileName);// 测试语料文件
		atf.setFile(inputFile);
		Instances instancesTest = atf.getDataSet(); // 读入测试文件
		instancesTest.setClassIndex(instancesTest.numAttributes()-1); // 设置分类属性所在行号，
		
		m_classifier.buildClassifier(instancesTrain); // 训练
		
		Evaluation eval = new Evaluation(instancesTrain);
		eval.evaluateModel(m_classifier, instancesTest); //测试	
		
		
		
        IO.writeFile(saveFileName, "");
        IO.writeFile(saveFileName, "");
        IO.writeFile(saveFileName, "");
        //获取当前时间
      	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
      	String time=df.format(System.currentTimeMillis());
		//System.out.println("训练集："+arffTrainFileName+";   测试集："+arffTestFileName+";   分类器："+m_classifier.getClass());
		IO.writeFile(saveFileName,time+"    训练集："+arffTrainFileName+";   测试集："+arffTestFileName+";   分类器："+m_classifier.getClass());
        System.out.println("=== 各个类别具体分类情况： ===");
        IO.writeFile(saveFileName, "=== 各个类别具体分类情况： ===");
        System.out.println("                TP Rate   FN Rate   FP Rate   TN Rate   Precision   Recall  F-Measure   ROC Area");
        IO.writeFile(saveFileName, "                TP Rate   FN Rate   FP Rate   TN Rate   Precision   Recall  F-Measure   ROC Area");
        for(int i=0;i<instancesTest.numClasses();i++){
        	System.out.println("类别:"+i+"           "+eval.truePositiveRate(i)+"   "+eval.falseNegativeRate(i)+"   "+
        			eval.falsePositiveRate(i)+"   "+eval.trueNegativeRate(i)+"   "+eval.precision(i)+"   "+
        			eval.recall(i)+"   "+eval.fMeasure(i)+"   "+eval.areaUnderROC(i));
        	IO.writeFile(saveFileName, "类别:"+i+"           "+eval.truePositiveRate(i)+"   "+eval.falseNegativeRate(i)+"   "+
        			eval.falsePositiveRate(i)+"   "+eval.trueNegativeRate(i)+"   "+eval.precision(i)+"   "+
        			eval.recall(i)+"   "+eval.fMeasure(i)+"   "+eval.areaUnderROC(i));
        }
        
        System.out.println("Weighted Avg.   "+eval.weightedTruePositiveRate()+"   "+eval.weightedFalseNegativeRate()+"   "+
        		eval.weightedFalsePositiveRate()+"   "+eval.weightedTrueNegativeRate()+"   "+eval.weightedPrecision()+"   "+
        		eval.weightedRecall()+"   "+eval.weightedFMeasure()+"   "+eval.weightedAreaUnderROC());
        IO.writeFile(saveFileName, "Weighted Avg.   "+eval.weightedTruePositiveRate()+"   "+eval.weightedFalseNegativeRate()+"   "+
        		eval.weightedFalsePositiveRate()+"   "+eval.weightedTrueNegativeRate()+"   "+eval.weightedPrecision()+"   "+
        		eval.weightedRecall()+"   "+eval.weightedFMeasure()+"   "+eval.weightedAreaUnderROC());
        System.out.println("=== 总体数据集分类情况：===");
        IO.writeFile(saveFileName,"=== 总体数据集分类情况：===");
		System.out.println("Correctly Classified Instances  "+eval.correct()+"  Accuracy:"+(1-eval.errorRate())*100+"%");
		IO.writeFile(saveFileName,"Correctly Classified Instances  "+eval.correct()+"  Accuracy:"+(1-eval.errorRate())*100+"%");
		System.out.println("Incorrectly Classified Instances  "+eval.incorrect()+"  error:"+eval.errorRate()*100+"%");
        IO.writeFile(saveFileName, "Incorrectly Classified Instances  "+eval.incorrect()+"  error:"+eval.errorRate()*100+"%");
		double macPrecision=0.0;
        double macRecall=0.0;
        double macF1=0.0;
        for(int i=0;i<instancesTest.numClasses();i++){
        	macPrecision=macPrecision+eval.precision(i);
        	macRecall=macRecall+eval.recall(i);
        	macF1=macF1+eval.fMeasure(i);
        }
        macPrecision=macPrecision/instancesTest.numClasses();
        macRecall=macRecall/instancesTest.numClasses();
        macF1=macF1/instancesTest.numClasses();
        System.out.println("宏平均:    "+"macPrecision:"+macPrecision+"   macRecall:"+macRecall+"   macF1:"+ macF1);
        //System.out.println("每个Instance的分类情况:");
        //System.out.println("inst#,    actual, predicted, error");
        IO.writeFile(saveFileName, "宏平均:    "+"macPrecision:"+macPrecision+"   macRecall:"+macRecall+"   macF1:"+ macF1);
        IO.writeFile(saveFileName, "=== 每个文档的分类情况: ===");
        IO.writeFile(saveFileName, "inst#,    actual, predicted, error");
        for(int i=0;i<instancesTest.numInstances();i++){
        	if(instancesTest.instance(i).classValue()==eval.evaluateModelOnce(m_classifier, instancesTest.instance(i))){
        		//System.out.println(i+"    "+instancesTest.instance(i).classValue()+", "+eval.evaluateModelOnce(m_classifier, instancesTest.instance(i)));
        		IO.writeFile(saveFileName, i+"    "+instancesTest.instance(i).classValue()+", "+eval.evaluateModelOnce(m_classifier, instancesTest.instance(i)));
        	}
        	else{
        		//System.out.println(i+"    "+instancesTest.instance(i).classValue()+", "+eval.evaluateModelOnce(m_classifier, instancesTest.instance(i))+",   +");
        		IO.writeFile(saveFileName, i+"    "+instancesTest.instance(i).classValue()+", "+eval.evaluateModelOnce(m_classifier, instancesTest.instance(i))+",   +");
        	}
        }      
        
	}
	
	//交叉十次验证
	public static void ClassifyCrossValidation(int type,String arffFileName,String saveFileName) throws Exception {				
		Classifier m_classifier = null;
		switch (type) {
		case 0:
			m_classifier = new IBkSimilarity(100);
			break;
		case 1:
			//设置括号内数值以设置knn的K值
			m_classifier = new IBk(10);//knn
			break;
		case 2:
			m_classifier = new LibSVM();
			/*
			 * -K <int>
  			Set type of kernel function (default: 2)
    		0 = linear: u'*v
    		1 = polynomial: (gamma*u'*v + coef0)^degree
    		2 = radial basis function: exp(-gamma*|u-v|^2)
    		3 = sigmoid: tanh(gamma*u'*v + coef0)
			 */
			String[] options=new String[2];
	        options[0] = "-K"; 
	        //设置options[1]的值用于选择哪个svm函数
	        options[1] = "0"; 
	        m_classifier.setOptions(options);
			break;
		case 3:
			m_classifier = new J48();//决策树
			break;
		case 4:
			m_classifier = new NaiveBayesMultinomial();
			break;
		default:
			m_classifier = new LibSVM();
			break;
		}

		File inputFile = new File(arffFileName);// 训练语料文件

		ArffLoader atf = new ArffLoader();
		atf.setFile(inputFile);
		Instances instancesTrain = atf.getDataSet(); // 读入训练文件
		instancesTrain.setClassIndex(instancesTrain.numAttributes()-1);// 设置分类属性所在行号

			
		 /*
         * 分类器不应该在作为crossValidateModel参数之前训练，为什么？
         * 因为每当buildClassifier方法被调用时，一个分类器必需被重新初始化
         * （换句话说：接下来调用buildClassifier 方法总是返回相同的结果），你将得到不一致，没有任何意义的结果。
         * crossValidateModel方法处理分类器的training和evaluation
         * （每一次cross-validation，它产生一个你作为参数的原分类器的复本(copy)）
         */
		Evaluation eval = new Evaluation(instancesTrain);
		eval.crossValidateModel(m_classifier, instancesTrain, 10, new Random(1));//交叉交叉10次验证
		
		 IO.writeFile(saveFileName, "");
		 IO.writeFile(saveFileName, "");
		 IO.writeFile(saveFileName, "");
		
        //获取当前时间
      	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
      	String time=df.format(System.currentTimeMillis());
		System.out.println(time+"    数据集："+arffFileName+"交叉10次验证;   分类器："+m_classifier.getClass());
        IO.writeFile(saveFileName, time+"    数据集："+arffFileName+"交叉10次验证;   分类器："+m_classifier.getClass());
        System.out.println(eval.toSummaryString());
        IO.writeFile(saveFileName, eval.toSummaryString());       
        
	}
	
	public static void main(String[] args) throws Exception {
		WekaClassifier2 test=new WekaClassifier2();
		test.ClassifyCrossValidation(1,"E://PYH//feature//train_tfidf_fs_removespeech_xmeans.arff","E://PYH//feature//classifierResult_xi.txt");
	}
	
}
