package com.oracle.hpcm.utils;

import org.json.simple.JSONArray;

public class TestObject {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JSONArray jarr = new JSONArray();
		Object obj = jarr;
		System.out.println(obj instanceof JSONArray);
	}

}
