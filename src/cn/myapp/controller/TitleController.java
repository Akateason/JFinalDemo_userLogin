package cn.myapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import cn.myapp.model.ResultObj;
import cn.myapp.module.titleScore.TitleHandler;
import cn.myapp.module.titleScore.model.Title;


public class TitleController extends Controller {
	
	/**	 
	 * dao insert title with content .
	 * insert if not exist . 
	 * if exist return obj in DB .
	 * @see 	/title/insert 
	 * @param 	title
	 * @return 	{titleID : xxx}
	 */
	public void insert() {
		String titleStr = getPara("title") ;	
		TitleHandler handler = new TitleHandler() ;
		ResultObj resultObj = handler.makeTitleWithTitleStr(titleStr) ;
		renderJson(resultObj) ;
	}
	
	/**	 
	 * dao select all title 
	 * @see 	/title/all 
	 * @return 	[title, ...]
	 */
	public void all() {
		TitleHandler handler = new TitleHandler() ;
		ResultObj resultObj = handler.allTitles() ;
		renderJson(resultObj) ; 
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	

}
