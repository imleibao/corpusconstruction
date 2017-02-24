package com.tc.cluster;

public class MainDB_index2 {
	public static void main(String[] args) throws Exception {  
		for(int i=3;i<=15;i++){
			DB_index.DB_index_method("E:\\PYH\\feature\\train_tfidf_word2vec_removespeechstopword_1600_50.arff", i, "E:\\PYH\\feature\\DB_index_result2.txt");
			//DB_index.DB_index_method("E:\\PYH\\feature\\train_tfidf_fs_removespeechstopword_500.arff", i, "E:\\PYH\\feature\\DB_index_result.txt");
			
		}
	}

}
