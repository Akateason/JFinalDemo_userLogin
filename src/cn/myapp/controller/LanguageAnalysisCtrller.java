package cn.myapp.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.protocol.HTTP;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.mashape.unirest.http.exceptions.UnirestException;

import cn.myapp.model.ResultObj;
import cn.myapp.module.titleScore.model.Title;
import cn.myapp.module.titleScore.model.TitleAnalyze;
import cn.myapp.util.HttpRequest;
import cn.myapp.util.HttpRequest.TypeOfRequest;
import cn.myapp.util.json.GsonUtils;
import cn.myapp.util.json.JsonToMap;

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
	
	//////////////////////////////////////////////////////////////////////////////////////////
	//	BosonNLP HTTP API
	//////////////////////////////////////////////////////////////////////////////////////////
	
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
//		ResultObj resultObj = new ResultObj(response) ;
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
//		ResultObj resultObj = new ResultObj(response) ;
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
//		ResultObj resultObj = new ResultObj(response) ;
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
//		ResultObj resultObj = new ResultObj(response) ;
		renderJson(response);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
	//	sbj api . make complete analyze for title .
	//////////////////////////////////////////////////////////////////////////////////////////	
	
	private final static String kURL_titleInsert = "http://localhost:8080/GsdataApp/title/insert" ;
	
	public void analyze() throws JsonParseException, JsonMappingException, IOException {
		
		String title = getPara("title") ;		
		//1. request to titleCtrller/title/insert 		 
		int titleID = titleIDFromDB(title) ;
		
		//2. SELECT titleID from analyzeTB .
		// analyze obj exist or not .
		TitleAnalyze analyze = new TitleAnalyze() ;
		analyze = analyze.selectAnalyzeByTitleID(titleID) ;
		// if not exist . go request for analyze and insert analyzeTB .
		if (analyze == null) {
			analyze = getAnalyzeWithRequest(title) ;
		}
		// if exist . return analyze .
		else {
			
		}
		
	}

	//////////////////////////////////////////////////////////////////////////////////////////
	//	private .
	//////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * request to titleCtrller/title/insert
	 * select title from db, if not exist , insert one .
	 * @param title
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private int titleIDFromDB(String title) throws JsonParseException, JsonMappingException, IOException {
		HashMap<String, Object> mapReq = new HashMap<String, Object>() ;
		mapReq.put("title", title) ;	
		String response = HttpRequest.sendRequest(TypeOfRequest.GetType, kURL_titleInsert, mapReq) ; 		
		ObjectMapper mapper = new ObjectMapper() ;
		ResultObj resultObj = mapper.readValue(response, ResultObj.class) ;
		
		//GET titleID
		Map<String, Object> data = getReturnDataIntoMap(resultObj);					
		int titleID = ((Integer) data.get("titleID")).intValue() ;
		System.out.println("the titleid is : " + titleID) ;
		return titleID ;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getReturnDataIntoMap(ResultObj resultObj) {
		return ((Map<String, Object>)(resultObj.getReturnData()));
	}
		
	private List<Object> getReturnDataListWithStr(String dataStr) throws JsonParseException, JsonMappingException, IOException {
	    ObjectMapper mapper = new ObjectMapper();
	    JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, Object.class);	   
	    return mapper.readValue(dataStr, javaType);
	}

	
	private final static String kURL_EmotionAnalysis_wei 	= "http://localhost:8080/GsdataApp/language/sentiment" ;	
	private final static String kURL_KeywordsAnalysis_wei 	= "http://localhost:8080/GsdataApp/language/keywords" ;
	private final static String kURL_ClassifyAnalysis_wei 	= "http://localhost:8080/GsdataApp/language/classify" ;
	private final static String kURL_SuggestAnalysis_wei 	= "http://localhost:8080/GsdataApp/language/suggest" ;
	
	private TitleAnalyze getAnalyzeWithRequest(String titleStr) throws JsonParseException, JsonMappingException, IOException {
		
		titleStr = "\"" + titleStr + "\"" ;				
		
		TitleAnalyze analyze = new TitleAnalyze() ;		
		//1. sentiment 
		HashMap<String, Object> mapReq = new HashMap<String, Object>() ;
		mapReq.put("title", titleStr) ;	
		String response_sentiment = HttpRequest.sendRequest(TypeOfRequest.GetType, kURL_EmotionAnalysis_wei, mapReq) ;
		System.out.println(response_sentiment) ;
		List<Object> listEmotion = getReturnDataListWithStr(response_sentiment) ;
		@SuppressWarnings("unchecked")
		List<Double> emotionListDouble = (List<Double>) listEmotion.get(0) ;
		if (emotionListDouble == null) return null ;
		analyze.setEmotionPositive(emotionListDouble.get(0));
		analyze.setEmotionNegative(emotionListDouble.get(1));
		
		//2. keywords
		String response_keywords = HttpRequest.sendRequest(TypeOfRequest.GetType, kURL_KeywordsAnalysis_wei, mapReq) ;
		List<Object> listKeywords = getReturnDataListWithStr(response_keywords) ;
		if (listKeywords == null) return null ;
		@SuppressWarnings("unchecked")
		List<Object> keywordList = (List<Object>) listKeywords.get(0) ;
		String mainKeyword = (String) keywordList.get(1) ;
		analyze.setMainKeyword(mainKeyword);
		analyze.setKeywordList(response_keywords);
		
		//3. classify
		String response_classify = HttpRequest.sendRequest(TypeOfRequest.GetType, kURL_ClassifyAnalysis_wei, mapReq) ;
		List<Object> listClassify = getReturnDataListWithStr(response_classify) ;
		if (listClassify == null) return null ;
		int classify = ((Integer) listClassify.get(0)).intValue() ;
		analyze.setClassify(classify) ; 
		
		//4. suggest
		
		
		return analyze ;
	}
	
	
}









