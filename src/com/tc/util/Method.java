package com.tc.util;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import com.tc.XML.IO_xml;



public class Method {
	
	public static ArrayList<String> getRandomNumbers(double percent,int range){
		int n=(int) (percent*range);
		System.out.println("总数"+range+"的"+percent+"为"+n+"个!");
		ArrayList<String> set=new ArrayList<String>();
		while(set.size()<n){
			int number=(int)(Math.random()*range);
			String randomnumber=String.valueOf(number);
			if(!set.contains(randomnumber)){
				set.add(randomnumber);
			}
		}
		return set;
	}
	
	
	
	public static void main(String[] args) throws Exception{
		ArrayList<String> set = Method.getRandomNumbers(0.1, 101);
		for(int i=0;i<set.size();i++){
			System.out.println(set.get(i));
		}		
	}

}
