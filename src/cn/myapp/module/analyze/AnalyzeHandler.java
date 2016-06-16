package cn.myapp.module.analyze;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import cn.myapp.model.ResultObj;
import cn.myapp.module.titleScore.model.Title;
import cn.myapp.module.titleScore.model.TitleAnalyze;
import cn.myapp.util.HttpRequest;
import cn.myapp.util.HttpRequest.TypeOfRequest;

public class AnalyzeHandler {

	private final static String kURL_titleInsert = "http://localhost/GsdataApp/title/insert" ;
	
	/**
	 * request to titleCtrller/title/insert
	 * select title from db, if not exist , insert one .
	 * @param titleStr
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public Title titleFromDB(String titleStr) throws JsonParseException, JsonMappingException, IOException {
		HashMap<String, Object> mapReq = new HashMap<String, Object>() ;
		mapReq.put("title", titleStr) ;	
		String response = HttpRequest.sendRequest(TypeOfRequest.GetType, kURL_titleInsert, mapReq) ; // fetch title ,if not exist insert one . 		
		ObjectMapper mapper = new ObjectMapper() ;
		ResultObj resultObj = mapper.readValue(response, ResultObj.class) ;
		
		//GET titleID
		Map<String, Object> data = getReturnDataIntoMap(resultObj) ;		
		int idTitle = ((Integer) data.get("titleID")).intValue() ;
		Title title = Title.selectTitleByTitleID(idTitle) ;		

		return title ;
	}
	
	
	private final static String kURL_EmotionAnalysis_wei 	= "http://localhost/GsdataApp/language/sentiment" ;	
	private final static String kURL_KeywordsAnalysis_wei 	= "http://localhost/GsdataApp/language/keywords" ;
	private final static String kURL_ClassifyAnalysis_wei 	= "http://localhost/GsdataApp/language/classify" ;
	private final static String kURL_SuggestAnalysis_wei 	= "http://localhost/GsdataApp/language/suggest" ;
	
	/**
	 * get complete Analyze From Request
	 * @param titleStr
	 * @param titleID
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */	
	public TitleAnalyze getCompleteAnalyzeFromRequest(String titleStr, long titleID) throws JsonParseException, JsonMappingException, IOException {
		
		titleStr = "\"" + titleStr + "\"" ;				
		
		TitleAnalyze analyze = new TitleAnalyze() ;		
		
		analyze.setTitleID(titleID) ; 
		
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
		HashMap<String, Object> mapSuggest = new HashMap<String, Object>() ;
		mapSuggest.put("keyword", titleStr) ;	
		String response_suggest = HttpRequest.sendRequest(TypeOfRequest.GetType, kURL_SuggestAnalysis_wei, mapSuggest) ;
		List<Object> listSuggest = getReturnDataListWithStr(response_suggest) ;
		if (listSuggest == null) return null ; 
		if (listSuggest.size() > 0) analyze.setMainKeywordSuggestList(response_suggest) ;		
		
		return analyze ;
	}
	
	/**
	 * dao insert . 
	 * @param analyze
	 */
	public TitleAnalyze insertAnalyze(TitleAnalyze analyze) {
		analyze.daoInsert("analyze", "analyzeID") ;
		analyze = TitleAnalyze.selectAnalyzeByTitleID(analyze.getTitleID()) ;				
		return analyze ;
	}
	
	/**
	 * complete analyze
	 * @param titleStr
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public ResultObj analyzeResult(String titleStr) throws JsonParseException, JsonMappingException, IOException {
		
		//1. request to titleCtrller/title/insert 		 
		Title title = titleFromDB(titleStr) ;
		long titleID = title.getTitleID() ; 		
		//2. SELECT titleID from analyzeTB .
		// the analyze obj exists in DB or not .
		TitleAnalyze analyze = new TitleAnalyze() ;		
		analyze = TitleAnalyze.selectAnalyzeByTitleID(titleID) ;
		
		// if it not exist . go request for analyze and insert analyzeTB .
		if (analyze == null) {
			analyze = getCompleteAnalyzeFromRequest(titleStr, titleID) ;
			if (analyze != null) { 
				//1 let complete analyze insert . DB
				analyze = insertAnalyze(analyze) ;
				
				//2 return Analyze
				ResultObj resultObj = mixTitleAndAnalyze(title, analyze) ;
				return resultObj ;
			}
		}
		// if exist . return analyze .
		else {
			ResultObj resultObj = mixTitleAndAnalyze(title, analyze) ;			
			return resultObj ;
		}
		
		return null ;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	//	private .
	//////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	private Map<String, Object> getReturnDataIntoMap(ResultObj resultObj) {
		return ((Map<String, Object>)(resultObj.getReturnData())) ;
	}
		
	private List<Object> getReturnDataListWithStr(String dataStr) throws JsonParseException, JsonMappingException, IOException {
	    ObjectMapper mapper = new ObjectMapper() ;
	    JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, Object.class) ;	   
	    return mapper.readValue(dataStr, javaType) ;
	}
	
	private ResultObj mixTitleAndAnalyze(Title title , TitleAnalyze analyze) {
		HashMap<String, Object> map = new HashMap<String, Object>() ;
		Gson gson = new Gson() ;
		String jsonTitle =  gson.toJson(title) ;
		String jsonAnalyze = gson.toJson(analyze) ;		
		map.put("title", jsonTitle) ;
		map.put("analyze", jsonAnalyze) ;
		return new ResultObj(map) ;
	}
	
}
