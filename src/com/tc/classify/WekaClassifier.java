package com.tc.classify;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tc.util.Parameter;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.IBkSimilarity;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class WekaClassifier {
	public static void main(String[] args) throws Exception {
		WekaClassifier test=new WekaClassifier();
		test.Classify(0,"E://PYH//feature//xi.arff","E://PYH//feature//xi.arff");
	}

	public Map<String,String> Classify(int type,String arffTrainFileName,String arffTestFileName) throws Exception {
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

		inputFile = new File(arffTestFileName);// 测试语料文件
		atf.setFile(inputFile);
		Instances instancesTest = atf.getDataSet(); // 读入测试文件
		instancesTest.setClassIndex(instancesTest.numAttributes()-1); // 设置分类属性所在行号（第一行为0号），
		System.out.println(instancesTest.numAttributes());// 可以取得属性总数
		for(int i=0;i<instancesTest.numAttributes();i++){
			System.out.println(instancesTest.attribute(i));
		}
		//System.out.println(instancesTest.attribute(instancesTest.numAttributes()-1));

		double sum = instancesTest.numInstances(), // 测试语料实例数
		right = 0.0f;
		System.out.println("测试语料实例数:"+sum);
		instancesTrain.setClassIndex(instancesTest.numAttributes()-1);
		//instancesTrain.deleteAttributeAt(301);
		//instancesTest.deleteAttributeAt(301);
		m_classifier.buildClassifier(instancesTrain); // 训练
		Map<String,String> print=new HashMap<String,String>();
		String printout="";
		
		List<String> Results=new ArrayList<String>();
		for (int i = 0; i < sum; i++)// 测试分类结果
		{
			double result = m_classifier.classifyInstance(instancesTest.instance(i));
			double realclass = instancesTest.instance(i).classValue();
			printout+="分类结果：" + result + " ，实际类别： " + realclass+"\n";
			Results.add(realclass+"_"+result);
			System.out.println("分类结果：" + result + " ，实际类别： " + realclass);
			if (result == realclass)// 如果预测值和答案值相等（测试语料中的分类列提供的须为正确答案，结果才有意义）
			{
				right++;// 正确值加1
			}
		}

		System.out.println(" 分类准确率:" + (right / sum));
		print.put("Result", printout);
		double Acc=Double.parseDouble(new java.text.DecimalFormat(
				"#.0000").format((right / sum)*100));
		print.put("Acc",String.valueOf(Acc)+"%");
		print.put("macF", String.valueOf(macF(Results,9)));
		print.put("micF", String.valueOf(micF(Results,9)));
		
//		printout+="classification precision:" + (right / sum);
		System.out.println(" 宏平均:"  +String.valueOf(macF(Results,9)));
		System.out.println(" 微平均:" + String.valueOf(micF(Results,9)));
		return print;
	}
		
	private double  macF(List<String> list, int classNum){
		double[] n1=new double[classNum];
		double[] n2=new double[classNum];
		double[] n3=new double[classNum];
		double[] n4=new double[classNum];
		
		double[] P=new double[classNum];
		double[] R=new double[classNum];
		
		double[] F=new double[classNum];
		
		double macF=0.0;
		
		for(double i=0;i<classNum;i++){
			n1[(int)i]=0;
			n2[(int)i]=0;
			n3[(int)i]=0;
			n4[(int)i]=0;
			for(int j=0;j<list.size();j++){
				double real=Double.parseDouble(list.get(j).split("_")[0]);
				double result=Double.parseDouble(list.get(j).split("_")[1]);
				if(real==i && result==i){
					n1[(int)i]++;
				}else if(real!=i && result ==i){
					n2[(int)i]++;
				}else if(real==i && result!=i){
					n3[(int)i]++;
				}else if(real!=i && result!=i){
					n4[(int)i]++;
				}
			}					
		}
		
		for(int i=0;i<classNum;i++){
			P[i]=n1[i]/(n1[i]+n2[i]+0.0001);//防止除以0
			R[i]=n1[i]/(n1[i]+n3[i]+0.0001);//防止除以0
			F[i]=(2*P[i]*R[i])/(P[i]+R[i]+0.001);
			macF+=F[i];
		}
		macF/=classNum;
		return Double.parseDouble(new java.text.DecimalFormat(
				"#.0000").format(macF));
	}
	
	private double micF(List<String> list, int classNum){
		double[] n1=new double[classNum];
		double[] n2=new double[classNum];
		double[] n3=new double[classNum];
		double[] n4=new double[classNum];
		double P=0.0;
		double R=0.0;
		
		double micF=0.0;
		
		for(double i=0;i<classNum;i++){
			n1[(int)i]=0;
			n2[(int)i]=0;
			n3[(int)i]=0;
			n4[(int)i]=0;
			for(int j=0;j<list.size();j++){
				double real=Double.parseDouble(list.get(j).split("_")[0]);
				double result=Double.parseDouble(list.get(j).split("_")[1]);
				if(real==i && result==i){
					n1[(int)i]++;
				}else if(real!=i && result ==i){
					n2[(int)i]++;
				}else if(real==i && result!=i){
					n3[(int)i]++;
				}else if(real!=i && result!=i){
					n4[(int)i]++;
				}
			}					
		}
		
		double tmp1=0.0,tmp2=0.0,tmp3=0.0;
		for(int i=0;i<classNum;i++){
			tmp1+=n1[i];
			tmp2+=(n1[i]+n2[i]);
			tmp3+=(n1[i]+n3[i]);
		}
		P=tmp1/tmp2;
		R=tmp1/tmp3;
		
		micF=(2*P*R)/(P+R);
		return Double.parseDouble(new java.text.DecimalFormat(
				"#.0000").format(micF));
	}

}
