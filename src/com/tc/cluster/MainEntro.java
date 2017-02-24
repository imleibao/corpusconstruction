package com.tc.cluster;

public class MainEntro {
	public static void main(String[] args) throws Exception { 
		for(int i=3;i<=15;i++){
			Entropy_Classifiers.entropy_classifiers_method("E:\\PYH\\feature\\train_tfidf_fs_removespeechstopword_1800.arff",i,"E:\\PYH\\feature\\Entropy_Classifiers_result.txt");
			
		}
	}
}
