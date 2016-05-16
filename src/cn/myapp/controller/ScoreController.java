package cn.myapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.jfinal.core.Controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import cn.myapp.util.HttpRequest;


public class ScoreController extends Controller {
	
	private final static String kBosonAPI_token		 = "I5yHDctw.6474.tuiZDNyxwvqn" ;	
	private final static String kURL_EmotionAnalysis = "http://api.bosonnlp.com/sentiment/analysis" ;
	
	/**
	 * 情感分析
	 * @param 	String title
	 * @return 	JSON 格式的 [double, double] 类型组成的列表。
	 * 			每个元素分别表示请求的列表对应位置的文本的情感判断结果；
	 *		 	第一个值为非负面概率，第二个值为负面概率，两个值相加和为 1。
	 * @throws UnirestException
	 * @throws IOException 
	 */
	public void sentimentAnalysis() throws UnirestException, IOException {		
		HashMap<String, Object> mapHeader = new HashMap<String, Object>() ;
		mapHeader.put("Content-Type", "application/json") ;
		mapHeader.put("Accept", "application/json") ;
		mapHeader.put("X-Token", kBosonAPI_token) ;
		
		String title = getPara("title") ;
		
		ArrayList<String> list = new ArrayList<String>() ;
		list.add(title) ;		
		Gson gson = new Gson() ;
        String body = gson.toJson(list) ;        	
        
		String response = HttpRequest.doPost(kURL_EmotionAnalysis, mapHeader, null, body) ;
		System.out.println(response) ;
		renderJson(response) ;
	}
	
}
