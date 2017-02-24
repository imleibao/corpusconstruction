package com.tc.DB;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.tc.util.IO;

public class SelectSuitContent {
	public static void suitContentMethod(String keepWordFileName,String removeWordFileName,String fileName,String saveFileName){
		ArrayList<String> keepwords=IO.getfileLineToList(keepWordFileName);
		ArrayList<String> removewords=IO.getfileLineToList(removeWordFileName);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
			String line = br.readLine();
			while (line != null) {
				//count++;
				//System.out.println(line);
				//System.out.println(nextline);
				String eachfile=line.split(",")[4];
				int count=0;
				//System.out.println(eachfile);
				for(int j=0;j<keepwords.size();j++){
					if(eachfile.contains(keepwords.get(j))){
						//System.out.println(keepwords.get(j));
						//System.out.println(line);
						count=0;
						break;
					}
						//System.out.println("保留词:"+eachfile);
					else{
						count=1;
					}		
				}	
				for(int i=0;i<removewords.size();i++){
					if(eachfile.contains(removewords.get(i))){
						count=1;
						//System.out.println("去掉词:"+eachfile);
					}
				}

				if(count==0){
					//System.out.println(line);
					IO.writeFile(saveFileName, line);
				}
				line = br.readLine();	
			}
			br.close();		
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception{
		SelectSuitContent.suitContentMethod("F://ertaisuitword.txt", "F://ertairemoveword.txt", "F://ertaiComRe.txt", "F://ertaiComReSuit.txt");
	}
}
