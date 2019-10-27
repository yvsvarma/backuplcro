package com.oracle.hpcm.tests;

import java.util.ArrayList;

public class Reporter {
	private static ArrayList<Report> reports = new ArrayList<Report>();
	public static void put(Report report){
		reports.add(report);
	}
	public static ArrayList<Report> get(){
		return reports;
	}
}
