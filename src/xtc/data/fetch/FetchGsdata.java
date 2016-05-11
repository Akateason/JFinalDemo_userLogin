package xtc.data.fetch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import xtc.http.HttpRequest;
import xtc.json.JsonToMap;
import xtc.model.gsdata.Article;
import xtc.model.gsdata.Nickname;

public class FetchGsdata {

	private final static String kUrlGsdataApi	= "http://114.55.74.220:8080/gsdata/api" ; 
	private final static String kSName7Days	  	= "wx/opensearchapi/nickname_order_list" ;
	private final static String kParamJsonStr 	= "{\"wx_nickname\":\"日本流行每日速报\",\"num\":7,\"sort\":\"asc\"}" ;
	
	/**
	 * 前天 速报 详细
	 * fetchSubaoNickname
	 */
	public Nickname fetchSubaoNickname() {
		// REQUEST .
		String resultStr = HttpRequest.sendGet(kUrlGsdataApi, "spaceName=" + kSName7Days + "&jsonStr=" + kParamJsonStr) ; 
		// PARSE .
		JsonObject resultMap = JsonToMap.parseJson(resultStr) ;		
		JsonObject resultData = resultMap.get("returnData").getAsJsonObject() ;
		JsonArray itemsList = resultData.get("items").getAsJsonArray() ;
		JsonElement lastDayInfoElement = itemsList.get((itemsList.size() - 2)) ; // 前天的 .
		Gson gson = new Gson() ;
		// GET NICKNAME INFO .
		Nickname nickname = gson.fromJson(lastDayInfoElement, Nickname.class) ;
		return nickname ;
	}
	
	
	public String fetchDateString(Nickname nickname) throws ParseException {
		String dateOrigin = nickname.getResult_day() ;
		SimpleDateFormat oldFormat = new SimpleDateFormat( "yyyyMMdd" );
		SimpleDateFormat newFormat = new SimpleDateFormat( "yyyy-MM-dd" );
		Date date = oldFormat.parse(dateOrigin) ;
		return newFormat.format(date) ;
	}
	
	private final static String kResultDay = "wx/wxapi/result_day" ;
	/**
	 * 前日 排名
	 * fetch Sort From Two Days Ago
	 * @param String dateString . yyyy-MM-dd
	 */
	public String fetchSortFromTwoDaysAgo(String dateString) {
		
		String returnString = "【前日竞品排名】\n\n" ;	
		
		// SETUP .
		String spaceName = "spaceName=" + kResultDay ;
		String jsonStr   = "&jsonStr=" 
							+ "{\"order\":\"desc\",\"day\":\"" 
							+ dateString
							+ "\",\"groupid\":\"38504\",\"sort\":\"wci\"}" ;
		// REQUEST .
		String resultStr = HttpRequest.sendGet(kUrlGsdataApi, spaceName + jsonStr) ;
		// PARSE .
		JsonObject resultMap = JsonToMap.parseJson(resultStr) ;		
		JsonObject resultData = resultMap.get("returnData").getAsJsonObject() ;
		JsonArray rowsList = resultData.get("rows").getAsJsonArray() ;
		Gson gson = new Gson() ;
		ArrayList<Nickname> nicknames = new ArrayList<Nickname>() ; 
		for (JsonElement jsonElement : rowsList) {
			Nickname nick = gson.fromJson(jsonElement, Nickname.class) ;
			nicknames.add(nick) ;
		}
		
		// GET TOP3 .
		for (int i = 0; i < 3; i++) {
			Nickname nick = nicknames.get(i) ;
			String topStr = "TOP" + (i+1) + " : " + nick.getWx_nickname() + " , 最高阅读数 : " + nick.getReadnum_max() + "\n";  
			returnString += topStr ;
		}
		
		// SORT OF SUBAO
		String subaoSortInfo = "\n日本流行每日速报 : 排名" ;
		int sortNumber = 1 ;
		for (Nickname nickname : nicknames) {
			if (nickname.getWx_name().equals("zhepen") ) {
				subaoSortInfo += sortNumber + " 最高阅读数 : " + nickname.getReadnum_max() ;
				break ;
			}
			sortNumber ++ ;
		}
		
		returnString += subaoSortInfo + "\n\n" ;
		
		return returnString ;
	}
	
	/*
	 * 某天所有文章 .
	 * get articles In Date
	 * @param dayStr yyyy-MM-dd
	 */	
	private final static String kContentList = "wx/opensearchapi/content_list" ;
	
	public String articlesInDate(String dayStr) {
		
		String spaceName = "spaceName=" + kContentList ;
		String jsonStr   = "&jsonStr=" 
							+ "{\"wx_name\":\"zhepen\",\"postdate\":\""
							+ dayStr
							+ "\",\"sortname\":\"readnum\",\"sort\":\"desc\"}" ;
		// REQUEST .
		String response = HttpRequest.sendGet(kUrlGsdataApi, spaceName + jsonStr) ;
		// PARSE .
		JsonObject resultMap = JsonToMap.parseJson(response) ;		
		JsonObject resultData = resultMap.get("returnData").getAsJsonObject() ;
		JsonArray rowsList = resultData.get("items").getAsJsonArray() ;
		Gson gson = new Gson() ;
		ArrayList<Article> articles = new ArrayList<Article>() ; 
		for (JsonElement jsonElement : rowsList) {			
			Article article= gson.fromJson(jsonElement, Article.class) ;
			articles.add(article) ;
		}
		// RESULT STR .
		String resultStr = "【前日文章】\n" ;

		for (Article article : articles) 
		{
			resultStr += "[top" + article.getTop() + "]\t" + article.getTitle() 
						 + "\nPV:" + article.getReadnum() + "\t\t"
						 + "赞:" + article.getLikenum() + "\n" ;						 								
		}
		
		return resultStr + "\n" ;
	}
	
}
