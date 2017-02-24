package com.tc.util;

public class Parameter {
	private final static String all_FilePath="F:\\weibo\\xidada\\cluster_after_final";	
	private final static String test_FilePath=all_FilePath+"\\test";
	private final static String train_FilePath=all_FilePath+"\\train";
	private final static String all_segment_Path=all_FilePath+"\\allsegment";
	private final static String train_segment_Path=all_segment_Path+"\\train";
	private final static String test_segment_Path=all_segment_Path+"\\test";
	private final static String featurePath=all_FilePath+"\\feature";
	private final static String stopWordPath=featurePath+"\\stopword";
	
	public static String getAllFilepath() {
		return all_FilePath;
	}
	public static String getTest_FilePath() {
		return test_FilePath;
	}
	public static String getTrain_FilePath() {
		return train_FilePath;
	}
	public static String getAllSegmentPath() {
		return all_segment_Path;
	}
	public static String getTrainSegmentPath() {
		return train_segment_Path;
	}
	public static String getTestSegmentPath() {
		return test_segment_Path;
	}
	public static String getFeaturepath() {
		return featurePath;
	}
	public static String getStopwordpath() {
		return stopWordPath;
	}
	private static final String[] ClassName={""};
	public String[] getClassName() {
		return ClassName;
	}	
}
