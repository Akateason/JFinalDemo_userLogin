package cn.myapp.module.titleScore;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.Gson;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import cn.myapp.model.ResultObj;
import cn.myapp.module.titleScore.model.Title;

public class TitleHandler {

	public ResultObj makeTitleWithTitleStr(String titleStr) {
		
		if (titleStr == null) {
			ResultObj resultObj = new ResultObj("0", "标题不能为空", null) ;			
			return resultObj ;
		}
		
		Title title = new Title(titleStr) ;		
		// IF exist in DB .
		Title titleInDb = Title.selectTitleByContent(titleStr) ;		
		if (titleInDb != null) {
			ResultObj resultObj = new ResultObj("1001", "该标题已存在", dataOfTitleID(titleInDb)) ;			
			return resultObj ;
		}		
		// DB insert 
		title.daoInsert("title", "titleID") ;
		titleInDb = Title.selectTitleByContent(titleStr) ; // get obj with new ID . //		
		ResultObj resultObj = new ResultObj(dataOfTitleID(titleInDb)) ;
		return resultObj ;
	}
	
	public ResultObj allTitles() {		
		Gson gson = new Gson() ;
		java.util.List<Record> List = Db.find("SELECT * FROM nutzbook.title;") ;
		ArrayList<String> titleList = new ArrayList<String>() ;
		for (Record record : List) {
			if (record != null) {
				Title title = new Title() ;
				title.fetchFromRecord(record) ;				
				String jsonString = gson.toJson(title) ;		 
				//System.out.println(jsonString) ;
				titleList.add(jsonString) ;
			}
		}	
		
		String jsonStr = gson.toJson(titleList) ;
		ResultObj resultObj = new ResultObj(jsonStr) ;
		return resultObj ;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	//	private
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * returndata Of TitleID
	 * @param title
	 * @return {titleID : xxx}
	 */
	private HashMap<String, Object> dataOfTitleID(Title title) {
		long titleID = title.getTitleID() ;
		HashMap<String, Object> map = new HashMap<String, Object>() ;
		map.put("titleID", titleID) ;
		return map ;
	}
	
	
}
