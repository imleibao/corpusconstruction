package com.tc.weight;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tc.util.IO;
import com.tc.util.Parameter;

public class TermWeight_Caculation {
	
	public static void writeAttribute(String trainFilePath,String featureFile,String targetFile) {
		ArrayList<String> termList = IO.getfileLineToList(featureFile);
		String targetPath = targetFile;
		List<String> classNameList = IO.getClassList(trainFilePath);
		IO.writeFile(targetPath, "@relation fileClassy");
		IO.writeFile2(targetPath, "@attribute class {");
		for (int i = 0; i < classNameList.size()-1; i++) {
			String className = classNameList.get(i).split("\\.")[0];
			IO.writeFile2(targetPath, className + ",");
		}
		System.out.println("类数@termweight_caculation.java"+classNameList.size());
		IO.writeFile(targetPath, classNameList.get(classNameList.size() - 1).split("\\.")[0]+"}");
		
		for(int i = 0; i < termList.size(); i++){
			//String term = termList.get(i).split(" ")[0];
			//System.out.println(term);
			IO.writeFile(targetPath, "@attribute "+(i+1) +" numeric");
		}
		IO.writeFile(targetPath, "@data");
	}
	public String process(String trainFilePath,String featureFile,String targetFile)throws IOException{
		return "";
	}
	public String process_Test(String testFilePath,String featureFile,String targetFile) throws IOException {
		return "";
	}
}
