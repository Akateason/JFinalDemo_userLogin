package cn.myapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import cn.myapp.model.ResultObj;
import cn.myapp.module.titleScore.model.Title;
import cn.myapp.util.HttpRequest;

/**************************************
 * [语义分析] BosonNLP HTTP API
 * http://bosonnlp.com
 * akateason@hotmail.com
 * x12345
 * @author teason
***************************************/

public class LanguageAnalysisCtrller extends Controller {
	private final static String kBosonAPI_token		 	= "I5yHDctw.6474.tuiZDNyxwvqn" ;	
	private final static String kURL_EmotionAnalysis 	= "http://api.bosonnlp.com/sentiment/analysis" ;	
	private final static String kURL_KeywordsAnalysis 	= "http://api.bosonnlp.com/keywords/analysis" ;
	private final static String kURL_ClassifyAnalysis 	= "http://api.bosonnlp.com/classify/analysis" ;
	private final static String kURL_SuggestAnalysis 	= "http://api.bosonnlp.com/suggest/analysis" ;
	
	// Header in BosonNLP HTTP API
	private static HashMap<String, Object> getMyHeader() {
		HashMap<String, Object> myHeader = new HashMap<String, Object>() ;
		myHeader.put("Content-Type", "application/json") ;
		myHeader.put("Accept", "application/json") ;
		myHeader.put("X-Token", kBosonAPI_token) ;		
		return myHeader;
	}
	
	/**
	 * 情感分析
	 * @param 	String 	title 单个字符串,或数组的jsonSTRING格式.
	 * @return 	JSON 格式的 [double, double] 类型组成的列表。
	 * 			每个元素分别表示请求的列表对应位置的文本的情感判断结果；
	 *		 	第一个值为[非负面]概率，第二个值为[负面]概率，两个值相加和为 1。
	 * @throws UnirestException
	 * @throws IOException 
	 */
	public void sentiment() throws UnirestException, IOException {				
		String title = getPara("title") ;		
		String response = HttpRequest.doPost(kURL_EmotionAnalysis, getMyHeader(), null, title) ;
		System.out.println(response) ;
		renderJson(response) ;
	}
	
	/**
	 * 关键词
	 * @param 	String 	title 单个字符串,或数组的jsonSTRING格式.
	 * @return [[权重,关键词],,,] 	JSON 格式的引擎提取出的关键词相应的权重和关键词组成的列表。
	 * @throws UnirestException
	 * @throws IOException
	 */
	public void keywords() throws UnirestException, IOException {
		String title = getPara("title") ;
		String response = HttpRequest.doPost(kURL_KeywordsAnalysis, getMyHeader(), null, title) ;
		System.out.println(response) ;
		renderJson(response) ;
	}
	
	/**
	 * 新闻分类
	 * @param 	String 	title 单个字符串,或数组的jsonSTRING格式.
	 * @return	JSON 格式的分类编号（ 0 到 13 之间的数字）组成的列表。比如： [5, 4, 8]
	 * 0体育* 1教育*  2财经*  3社会*  4娱乐*  5军事*  6国内 
	 * 7科技* 8互联网* 9房产* 10国际* 11女人* 12汽车* 13游戏
	 * @throws UnirestException
	 * @throws IOException
	 */
	public void classify() throws UnirestException, IOException {
		String title = getPara("title") ;
		String response = HttpRequest.doPost(kURL_ClassifyAnalysis, getMyHeader(), null, title) ;
		System.out.println(response) ;
		renderJson(response) ;		
	}
	
	/**
	 * 语义联想
	 * @param	String keyword
	 * @return	JSON 格式的列表。
	 * @throws UnirestException
	 * @throws IOException
	 */
	public void suggest() throws UnirestException, IOException {
		String keyword = getPara("keyword") ;
		String response = HttpRequest.doPost(kURL_SuggestAnalysis, getMyHeader(), null, keyword) ;
		System.out.println(response) ;
		renderJson(response);
	}
	
	
	public void testInsert() {
		Title title = new Title() ;
		
		title.setContent("newnewnew" + (int)(1 + Math.random() * (1000 - 1 + 1)) ) ;
		title.daoInsert("title", "titleID") ;
		renderJson("success") ;
	}
	
	public void testSelect() {
		java.util.List<Record> List = Db.find("SELECT * FROM nutzbook.title;") ;
		ArrayList<String> titleList = new ArrayList<String>() ;
		for (Record record : List) {
			if (record != null) {
				Title title = new Title() ;
				title.fetchFromRecord(record) ;
				
				Gson gson = new Gson() ;
				String jsonString = gson.toJson(title) ;		 
				System.out.println(jsonString) ;
				titleList.add(jsonString) ;
			}
		}	
		renderJson(titleList) ;
	}
	
	
}
