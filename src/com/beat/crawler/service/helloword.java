package com.beat.crawler.service;

import com.google.gson.Gson;

public class helloword {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int[] a = new int[100];
		
		for(int i = 0; i < 100; i++) {
			
			a[i] = (int)System.currentTimeMillis();
		}
		
		System.out.println(new Gson().toJson(a));
		
 	}

}
