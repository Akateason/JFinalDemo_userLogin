package cn.myapp.controller;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import cn.myapp.model.ResultObj;
import cn.myapp.module.titleScore.model.Title;

public class TitleController extends Controller {
	
	/**	 
	 * dao insert title with content .
	 * insert if not exist .
	 * @see 	/title/insert 
	 * @param 	title
	 * @return 	{titleID : xxx}
	 */
	public void insert() {
		String titleStr = getPara("title") ;	
		Title title = new Title(titleStr) ;

		// if exist in DB .
		Title titleInDb = title.selectTitleByContent(titleStr) ;		
		if (titleInDb != null) {
			ResultObj resultObj = new ResultObj("1001", "该标题已存在", dataOfTitleID(titleInDb)) ;
			renderJson(resultObj) ;
			return ;
		}
		
		// DB insert 
		title.daoInsert("title", "titleID");		
		titleInDb = title.selectTitleByContent(titleStr) ; // get obj with new ID .		
		ResultObj resultObj = new ResultObj(dataOfTitleID(titleInDb)) ;
		renderJson(resultObj) ;
	}
	
	/**	 
	 * dao select all title 
	 * @see 	/title/all 
	 * @return 	[title, ...]
	 */
	public void all() {
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
	
	////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Private
	 * returndata Of TitleID
	 * @param title
	 * @return {titleID : xxx}
	 */
	private HashMap<String, Object> dataOfTitleID(Title title) {
		int titleID = title.getTitleID() ;
		HashMap<String, Object> map = new HashMap<String, Object>() ;
		map.put("titleID", titleID) ;
		return map ;
	}
	
	
}
