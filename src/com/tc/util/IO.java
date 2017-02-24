package com.tc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

public class IO {
	static String orginalEncoding;
	static String filePath;
	static ArrayList fileList = new ArrayList<String>();

	public IO() {
		super();
		this.filePath = filePath;
	}

	public void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					this.deleteFile(files[i]);
				}
			}
			file.delete();
		} else {
			System.out.println("所删除的文件不存在！" + '\n');
		}
	}

	public static String judgeFileCode(String filePath) {
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(new ParsingDetector(false));
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());

		Charset charset = null;
		File f = new File(filePath);
		try {
			charset = detector.detectCodepage(f.toURL());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (charset != null) {
			orginalEncoding = charset.name();
		} else {
			orginalEncoding = "未知";
		}
		// System.out.println(orginalEncoding);
		return orginalEncoding;

	}

	// 读取文件
	public static String loadFileToString(String filePath) {
		File f = new File(filePath);
		String encoding = judgeFileCode(filePath);
		if (encoding == "未知") {
			System.out.println("文件: " + f.getName() + "为空！");
			encoding = "GB2312";
			//return null;
		}
		if (encoding != "GB2312" && encoding != "UTF-8") {
			System.out.println("文件: " + f.getPath() + " 是其他编码");
		}
		// System.out.println("encoding:"+encoding);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath), encoding));
			StringBuffer sb = new StringBuffer();
			String line = br.readLine();
			while (line != null) {
				sb.append(" " + line);
				line = br.readLine();
				// System.out.println(line);
			}
			br.close();
			// String tem_str = new
			// String(sb.toString().getBytes(encoding),"utf-8");
			// String temp = new String(sb.toString().getBytes(),"utf-8");
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void writeFile(String path, String text) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(path, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(text);
			bw.newLine();
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 不换行
	public static void writeFile2(String path, String text) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(path, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(text);
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<String> getfileLineToList(String filePath) {
		ArrayList<String> alist = new ArrayList<String>();
		// String filePath = Parameter.getFeaturePath() + "\\"+txtFeature;
		File f = new File(filePath);
		String encoding = IO.judgeFileCode(filePath);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath), encoding));
			String line = br.readLine();
			while (line != null) {
				// line = new String(line.getBytes(),"utf-8");
				alist.add(line);
				line = br.readLine();
				// System.out.println(line);
			}

			br.close();
			return alist;
			// String tem_str = new
			// String(sb.toString().getBytes(encoding),"utf-8");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 遍历文件
	public static ArrayList<String> traverseFiles(String filePath) {
		File root = new File(filePath);
		if (!root.isDirectory()) {
			fileList.add(root);
			return fileList;
		}
		File[] filesOrDirs = root.listFiles();
		for (int i = 0; i < filesOrDirs.length; i++) {
			if (filesOrDirs[i].isDirectory()) {
				traverseFiles(filesOrDirs[i].getAbsolutePath());
			} else {
				// System.out.println(filesOrDirs[i].getPath());
				fileList.add(filesOrDirs[i].getPath());
			}
		}
		return fileList;
	}

	public static List<String> getDocPath(String train_FilePath) {
		File f = new File(train_FilePath);
		File[] files = f.listFiles();
		List<String> Doclist = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {
			String classname = files[i].getName();
			File subf = new File(train_FilePath + "\\" + files[i].getName());
			File[] subfile = subf.listFiles();
			for (int j = 0; j < subfile.length; j++) {
				String docPath = classname + "_" + subfile[j].getName();
				// System.out.println("@IO.java: doclist: "+docPath);
				Doclist.add(docPath);
			}
		}
		return Doclist;
	}

	public static List<String> getTestDocPath(String test_FilePath) {
		File f = new File(test_FilePath);
		File[] files = f.listFiles();
		List<String> Doclist = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {
			String classname = files[i].getName();
			File subf = new File(test_FilePath + "\\" + files[i].getName());
			File[] subfile = subf.listFiles();
			for (int j = 0; j < subfile.length; j++) {
				String docPath = classname + "_" + subfile[j].getName();
				Doclist.add(docPath);
			}
		}
		return Doclist;
	}

	public static List<String> getClassList(String trainFile_Path) {
		File f = new File(trainFile_Path);
		File[] files = f.listFiles();
		List<String> Doclist = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {
			String classname = files[i].getName();
			String docPath = classname;
			Doclist.add(docPath);
		}
		return Doclist;
	}

	public static List<String> turnDocToTermList(String filePath) {
		List<String> list = new ArrayList();
		String docStr = IO.loadFileToString(filePath);
		String a[] = docStr.split(" ");
		for (String tmp : a) {
			if (!list.contains(tmp))
				list.add(tmp);
		}
		return list;
	}

	public static List<String> getAllTerms(String filePath) {
		List<String> fileList = traverseFiles(filePath);
		List<String> termList = new ArrayList();
		for (String path : fileList) {
			for (String term : turnDocToTermList(path)) {
				if (!termList.contains(term)) {
					termList.add(term);
				}
			}
		}
		return termList;
	}
	//重命名文件
	public static void reNameFile(String filePath, String outPath) {
		File file = new File(filePath);
		//System.out.println(file.getParent());
		if (file.exists()) {
			File outFile = new File(outPath);
			if (outFile.exists()) {
				outPath = outFile.getParent()+"\\"+"rename"+outFile.getName();
			}
			String str = IO.loadFileToString(filePath);
			IO.writeFile(outPath, str);
			file.delete();
		}else{
			System.out.println("文件不存在："+filePath);
		}

	}
	//把文件夹下的文件名改成1 2 3 
	public static void filterAllFileTitle(String dirPath){
		ArrayList<String> filePathList = traverseFiles(dirPath);
		int i = 1;
		for(String path:filePathList){
			File file = new File(path);
			String name = file.getName();
			String []array = name.split("\\.");
			name = i+"."+array[array.length-1];
			String outPath = file.getParent()+"\\"+name;
			System.out.println(outPath);
			reNameFile(path,outPath);
			i++;
		}
	}
	
	public static void newFileDir(String str){
		File file =new File(str);    
		//如果文件夹不存在则创建    
		if  (!file .exists()  && !file .isDirectory())      
		{       
		    System.out.println("//目录不存在，创建目录");  
		    file .mkdir();    
		} else   
		{  
		    //System.out.println("//目录存在");  
		}  
	}
	
	//删除文件
	public static void deleteFile (String fileName) { 
		File file = new File (fileName); 
		if (file.isFile () && file.exists ()) 
		{ 
			file.delete (); 
			System.out.println (" delete a single file "+ fileName +" success! "); 
		} 
		else 
		{ 
			System.out.println (" delete a single file "+ fileName +" failed! "); 
		}
	}
	
	public static void main(String args[]) {
		filterAllFileTitle("F:\\2\\education\\abroad");
		// IO io = new IO();
		// String str = io.loadFileToString("E:\\corpus\\sogou\\test\\10.txt");
		// io.writeFile("d:\\a.txt", str);

		// List list = new ArrayList<String>();
		// IO io = new IO();
		// list = io.traverseFiles("E:\\corpus\\sogou\\Reduced");
		// for (Iterator iter = list.iterator(); iter.hasNext();) {
		// String element = (String) iter.next();
		// System.out.println(element);
		// }
	}

}
