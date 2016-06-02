package cn.myapp.module.gsdata.mailInfo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cn.myapp.module.gsdata.mailInfo.model.Article;
import cn.myapp.module.gsdata.mailInfo.model.Nickname;
import cn.myapp.module.gsdata.mailInfo.model.NicknameTranslate;
import cn.myapp.util.HttpRequest;
import cn.myapp.util.XtDate;
import cn.myapp.util.json.JsonToMap;
import cn.myapp.util.reflection.UtilReflect;


public class EmailContentDisplay 
{
	/**
	 *  20160602 not used .
		【前日日本流行每日速报详细数据】
		
		最高阅读数 : 22858
		总阅读相对上次的增量(负数即为下降) : -482
		
		文章数 : 3
		文章达10w+的数量(负数为下降) : 0
		文章达10w+的数量的增量(负数为下降) : 0
		总阅读 : 29396
		总阅读的增量(+-) : -482
		平均阅读 : 9798.7
		平均阅读的增量(负数为下降) : -160.6
		总点赞 : 72
		总点赞的增量(负数为下降) : -26
		平均点赞 : 24
		平均点赞的增量 (负数为下降) : -8.7
		最大阅读数 : 22858
		最大点赞数 : 49
		阅读数与点赞数的比率 : 0.0024
		微信传播指数 : 812.0233948
		微信传播指数的增量 : -1.59186608
		排名 : 4558
		排名上升增量 : -96
		排名日期 : 20160531
		该公众号在上述日期中发文次数 : 1
		发文次数相对上次的增量（负数即为下降） : 0
		该公众号在上述日期中发文的总阅读数 : 22858
		发文的总阅读数相对上次的增量（负数即为下降） : 9238
		文章总数相对上次的增量（负数即为下降） : 0

	 * getEmailContentWillDisplay
	 * @param nickname
	 * @return string .
	 */
	public String getEmailContentWillDisplay(Nickname nickname) 
	{
		String resultString = "【前日日本流行每日速报详细数据】\n\n" 
					+ "最高阅读数 : " + nickname.getReadnum_max() + "\n" 
					+ "总阅读相对上次的增量(负数即为下降) : " + nickname.getReadnum_all_up() + "\n\n";
		
		UtilReflect utilSplit = new UtilReflect() ;
		String[] namelist = utilSplit.getFieldName(nickname) ;

		for (String name : namelist) 
		{
			String val = "" ;
			if (utilSplit.getFieldValueByName(name,nickname) != null) {
				val = utilSplit.getFieldValueByName(name,nickname).toString() ;
			}
			String zhongwen = NicknameTranslate.getZhongwen(name) ;
			if (zhongwen == null) {
				continue ;
			}
			resultString += zhongwen + " : " + val + "\n" ;
		}
		
		return resultString ;
	}
	
	/**
	 * 本月文章排名top10
	 * 
		jsonStr = "{\"dateend\":\"2016-05-31\",\"sort\":\"desc\",\"start\":0,
		\"datestart\":\"2016-05-01\",\"sortname\":\"readnum\",
		\"is_top\":true,\"wx_name\":\"zhepen\"}";
		spaceName = "wx/opensearchapi/content_list";		
	 * @return string
	 */
	private final static String kUrlGsdataApi           = "http://wei.subaojiang.com:8080/gsdata/api" ;
	private final static String kSpaceNameTop			= "wx/opensearchapi/content_list"; 
	
	public String getCurrentMonthArticleStatement() {
		
		// TEST		
		String dateStart 	= XtDate.getFirstDay() ; // "2016-05-01" ;
		String dateEnd 		= XtDate.getLastDay() ; // "2016-05-30" ;				
		
		System.out.println("date : " + dateStart + "\n" + dateEnd) ;
		
		String jsonString = "{\"dateend\":\""
				+ dateEnd
				+ "\",\"sort\":\"desc\",\"start\":0,\"datestart\":\""
				+ dateStart
				+ "\",\"sortname\":\"readnum\",\"is_top\":true,\"wx_name\":\"zhepen\"}" ;
		
		String response = HttpRequest.sendGet(kUrlGsdataApi, "spaceName=" + kSpaceNameTop + "&jsonStr=" + jsonString) ;
		
		// PARSE .
		Gson gson = new Gson() ;
		JsonObject resultMap = JsonToMap.parseJson(response) ;		
		JsonObject resultData = resultMap.get("returnData").getAsJsonObject() ;
		JsonArray itemsList = resultData.get("items").getAsJsonArray() ;		
		if (itemsList.size() == 0) return "" ;		
		
		// GET ARTICLES STR .
		String strArticleStatement = "【本月文章排行】\n\n" ;
		int index = 1 ;
		for (JsonElement jsonElement : itemsList) {
			Article article = gson.fromJson(jsonElement, Article.class) ;
			strArticleStatement += 	index + ") " 
									+ article.getTitle() + "\n"
									+ "PV:" + article.getReadnum() + "\t"
									+ "赞:" + article.getLikenum() 
									+ "\n" ;						
			if (index == 10) break ;			
			index ++ ;
		}
		
		return strArticleStatement ;
	}
}
