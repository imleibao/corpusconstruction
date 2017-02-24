package com.tc.weight;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tc.util.IO;
import com.tc.util.Parameter;

public class TF_IDF extends TermWeight_Caculation {

	public static void main(String[] args) {
		
		TF_IDF t = new TF_IDF();
		try {
			//t.process("F://ertaicorpus//c", "F://ertaicorpus//feature//tfidf_fs_500.txt","F://ertaicorpus//feature//train_tfidf_fs_500.arff");
			t.process("E://PYH//Corpus", "E://PYH//feature//tfidf_fs_removespeechstopword_100.txt","E://PYH//feature//train_tfidf_fs_removespeechstopword_100.arff");
			t.process("E://PYH//Corpus", "E://PYH//feature//tfidf_fs_removespeechstopword_200.txt","E://PYH//feature//train_tfidf_fs_removespeechstopword_200.arff");
			t.process("E://PYH//Corpus", "E://PYH//feature//tfidf_fs_removespeechstopword_300.txt","E://PYH//feature//train_tfidf_fs_removespeechstopword_300.arff");
			t.process("E://PYH//Corpus", "E://PYH//feature//tfidf_fs_removespeechstopword_500.txt","E://PYH//feature//train_tfidf_fs_removespeechstopword_500.arff");
			t.process("E://PYH//Corpus", "E://PYH//feature//tfidf_fs_removespeechstopword_600.txt","E://PYH//feature//train_tfidf_fs_removespeechstopword_600.arff");
			t.process("E://PYH//Corpus", "E://PYH//feature//tfidf_fs_removespeechstopword_700.txt","E://PYH//feature//train_tfidf_fs_removespeechstopword_700.arff");
			t.process("E://PYH//Corpus", "E://PYH//feature//tfidf_fs_removespeechstopword_800.txt","E://PYH//feature//train_tfidf_fs_removespeechstopword_800.arff");
			//t.process("E://PYH//test", "E://PYH//feature//tfidf_fs_removespeechstopword_400.txt","E://PYH//feature//test_400_2.arff");
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}

	public String process(String datafilePath, String featureFile,
			String targetFile) throws IOException {
		long begintime = System.currentTimeMillis();
		
		String print = "";
		String filePath = targetFile;
		File file = new File(filePath);
		if (file.isFile() && file.exists())
			file.delete();
		
		TermWeight_Caculation.writeAttribute(datafilePath, featureFile, targetFile);
		
		//特征词信息
		List<String> lines = IO.getfileLineToList(featureFile);
		int FeatureNum = lines.size();
		
		//文档列表
		File f = new File(datafilePath);
		File[] fileSetlist = f.listFiles();
		
		//String tmpfeature="";
		//for(int i=0;i<lines.size();i++){
		//	tmpfeature= tmpfeature+" "+lines.get(i).split("/")[0];
		//}
		
		//System.out.println(tmpfeature+" "+"type");
		//IO.writeFile(filePath, tmpfeature+" "+"type");
		
		double N=0.0;
		int classNum=0;
		
		
		//区分聚类和分类
		if((datafilePath.equals("train"))||(datafilePath.equals("test"))){
			System.out.println("分类环节！");
			N=TF_IDF.documentN("train");
			classNum=TF_IDF.classN("train");
		}
		else{
			System.out.println("聚类环节！");
			N=TF_IDF.documentN(datafilePath);
			classNum=TF_IDF.classN(datafilePath);
		}
		
		
		
		System.out.println("N:" + N);
		System.out.println("ClassNum:" + classNum);		
		System.out.println("正在计算tfidf：......");
		
		//计算IDF
		double idf[] = new double[FeatureNum];
		String term[] = new String[FeatureNum];
		for (int j = 0; j < lines.size(); j++) {
			String line = lines.get(j);
			String[] tmp = line.split(" ");
			term[j] = tmp[0];
			//df其实就是文档频数，但是作为中间变量，所以没有用数组存储
			double df=0.0;
			for(int i = 0; i < classNum; i++){
				df=df+Double.parseDouble(tmp[i+1].split("\\|")[1]);
			}
			idf[j] = Math.log(N/df+0.01);
		}
		
		
		//计算TF和TF-IDF
		for(int  l=0;l<fileSetlist.length;l++){
			System.out.println(datafilePath+"\\"+fileSetlist[l].getName());
			List<String> Doclist = IO.getfileLineToList(datafilePath+"\\"+fileSetlist[l].getName());
			
			for(int p=0;p<Doclist.size();p++){
				if(!Doclist.get(p).equals("")){
				String a[] = Doclist.get(p).split(" ");			
				double tf[] = new double[FeatureNum];
				//计算每个tiidf相乘的值
				double[] pretfidf = new double[FeatureNum];
				double normalization=0.0;
				double[] tmptfidf = new double[FeatureNum];
				for(int j = 0;j<tf.length;j++){
						tf[j] = 0.0;
				}
				for (int j = 0; j < lines.size(); j++) {
					for(String str:a){
						if(!(str.equals("/"))){
							if(lines.get(j).split("/")[0].equals(str.split("/")[0]))
							{
								tf[j] = tf[j]+1.0;
							}
						}
					}
					pretfidf[j]= tf[j]*idf[j];
				}	
				//归一化
				for(int j=0;j<pretfidf.length;j++){
					normalization=normalization+Math.pow(pretfidf[j], 2);
				}
				//计算tf-idf
				for(int j=0;j<pretfidf.length;j++){
					if(pretfidf[j]!=0.0){
						double tmpDouble = pretfidf[j]/Math.sqrt(normalization);
						tmptfidf[j] = tmpDouble;
					}
				}
				
				//用来筛掉没有任何内容的行，该行输出后将为空
				int make=0;
				for(int j=0;j<FeatureNum; j++){
					if(tmptfidf[j]==0){
						make++;
					}
				}
				//System.out.println(make);
				
				//输出矩阵
				String tmp ="";
				if(make!=FeatureNum){
					tmp="{0 "+fileSetlist[l].getName().split("\\.")[0];
					for (int j = 0; j < FeatureNum; j++) {
						double tmp1 = tmptfidf[j];
						double tmp2 = Double.parseDouble(new java.text.DecimalFormat( "#.00000000000000000").format(tmp1));
						tmp += "," + (j+1) + " " + (tmp2);
					}
					tmp = tmp + "}";	
				}
					
				//System.out.println(tmp);
				IO.writeFile(filePath, tmp);
				print += "OK";	
				}	
			}			
		}			
		System.out.println("OK");
		
		long endtime = System.currentTimeMillis();
		long costTime = (endtime - begintime) / 1000;
		System.out.println("耗时：" + costTime + "秒！");
		
		return print; 		
		
		
	}	
	
	//文档数
	public static double documentN(String datafilePath){
		//文档列表
		File f = new File(datafilePath);
		File[] fileSetlist = f.listFiles();
		double N =0.0;
		for(int l=0;l<fileSetlist.length;l++){
			List<String> eachClassDoc = IO.getfileLineToList(datafilePath+"\\"+fileSetlist[l].getName());
			N=N+eachClassDoc.size();
		}
		return N;
	}
	
	//类别数
	public static int classN(String datafilePath){
		//文档列表
		File f = new File(datafilePath);
		File[] fileSetlist = f.listFiles();
		int classNum= fileSetlist.length;
		return classNum;
	}
	
	//生成可用数据文档和不可用数据文档
	public static void replaceDocument(String filePath, String featureFile,String savefilePath){
		long begintime = System.currentTimeMillis();
		String path = savefilePath;
		File ff = new File(path);
		if (ff.isFile() && ff.exists())
			ff.delete();
		
		
		//特征词信息
		List<String> featurelines = IO.getfileLineToList(featureFile);
		int FeatureNum = featurelines.size();
		 
		List<String> Doclist = IO.getfileLineToList(filePath);
		for(int p=0;p<Doclist.size();p++){
			String document=Doclist.get(p);
				if(!document.equals("")){
				String[] word = document.split(" ");	
	             //用来记录文档中多少个词是未出现在全部特征词中的
				int count=0;
				//用于将每一篇文档的词都记录出来
				ArrayList<String> term=new ArrayList<String>();
				for(int i=0;i<word.length;i++){
					if(!word[i].equals("/")){
						if(!term.contains(word[i].split("/")[0])){
							term.add(word[i].split("/")[0]);
						}
					}
				}
				for(int i=0;i<featurelines.size();i++){
					for(String str:term){
						if(featurelines.get(i).split("/")[0].equals(str)){
							count++;
						}
					}		
				}
				if(count==0){
						document="%";
				}
			}
			IO.writeFile(path, document);
		}
	}	
}