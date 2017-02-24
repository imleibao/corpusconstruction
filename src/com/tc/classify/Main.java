package com.tc.classify;

public class Main {
	public static void main(String[] args) throws Exception {
		WekaClassifier2 test=new WekaClassifier2();
		test.ClassifyCrossValidation(2,"E://PYH//feature//train_tfidf_fs_removespeech_kmeans_8.arff","E://PYH//feature//classifierResult_removespeech_kmeans.txt");
		test.ClassifyCrossValidation(2,"E://PYH//feature//train_tfidf_fs_removespeechstopword_kmeans_8.arff","E://PYH//feature//classifierResult_removespeechstopword_kmeans.txt");
		test.ClassifyCrossValidation(2,"E://PYH//feature//train_tfidf_fs_removestopword_kmeans_8.arff","E://PYH//feature//classifierResult_removestopword_kmeans.txt");
		test.ClassifyCrossValidation(2,"E://PYH//feature//train_tfidf_fs_removespeech_kmeans_9.arff","E://PYH//feature//classifierResult_removespeech_kmeans.txt");
		test.ClassifyCrossValidation(2,"E://PYH//feature//train_tfidf_fs_removespeechstopword_kmeans_9.arff","E://PYH//feature//classifierResult_removespeechstopword_kmeans.txt");
		test.ClassifyCrossValidation(2,"E://PYH//feature//train_tfidf_fs_removestopword_kmeans_9.arff","E://PYH//feature//classifierResult_removestopword_kmeans.txt");
		test.ClassifyCrossValidation(2,"E://PYH//feature//train_tfidf_fs_removespeech_kmeans_10.arff","E://PYH//feature//classifierResult_removespeech_kmeans.txt");
		test.ClassifyCrossValidation(2,"E://PYH//feature//train_tfidf_fs_removespeechstopword_kmeans_10.arff","E://PYH//feature//classifierResult_removespeechstopword_kmeans.txt");
		test.ClassifyCrossValidation(2,"E://PYH//feature//train_tfidf_fs_removestopword_kmeans_10.arff","E://PYH//feature//classifierResult_removestopword_kmeans.txt");
		
	}
}
