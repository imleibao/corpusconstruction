package com.tc.DB;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.tc.util.IO;

public class RemoveRepetition {

	public static void removeRepeMethod(String fileName,String saveFileName){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
			BufferedReader nextbr = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
			String line = br.readLine();
			String nextline = nextbr.readLine();
			nextline=nextbr.readLine();
			while (nextline != null) {
				//count++;
				//System.out.println(line);
				//System.out.println(nextline);
				String eachfile=line.split(",")[4];
				String nexteachfile=nextline.split(",")[4];
				if(!eachfile.equals(nexteachfile)){
					//System.out.println(line);
					IO.writeFile(saveFileName, line);
				}
				line = br.readLine();	
				nextline=nextbr.readLine();
			}
			br.close();		
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception{
		RemoveRepetition.removeRepeMethod("F://ertaiCom.txt","F://ertaiComRe.txt");
	}
}
