package cn.myapp.controller;

import java.text.ParseException;
import java.util.HashMap;

import com.jfinal.core.Controller;

import cn.myapp.model.ResultObj;
import cn.myapp.module.gsdata.mailInfo.EmailContentDisplay;
import cn.myapp.module.gsdata.mailInfo.FetchGsdata;
import cn.myapp.module.gsdata.model.Nickname;
import cn.myapp.util.HttpRequest;

public class DataNoteController extends Controller {
	
	/**
	 * [获取群发信息] 标题,内容
	 * @return returnData : {title,detail}
	 * @throws ParseException 
	 */
	public void fetchGroupSendInfo() throws ParseException {
		
		FetchGsdata fetcher = new FetchGsdata() ;	
		// GET SUBAO DETAIL NICKNAME INFO .
		Nickname nickname = fetcher.fetchSubaoNickname() ; 
		// EMAIL CONTENT .
		EmailContentDisplay display = new EmailContentDisplay() ;
		String subaoDetailInfo = display.getEmailContentWillDisplay(nickname) ;
		
		// GET SORT INFO
		String dayString = fetcher.fetchDateString(nickname) ;
		String articlesInday = fetcher.articlesInDate(dayString) ;
		String sortInfo = fetcher.fetchSortFromTwoDaysAgo(dayString) ;
		String emailContentStr = articlesInday + sortInfo + subaoDetailInfo ; // fetch detail .
		
		if (emailContentStr != null) {					
			// EMIAL TITLE .
			String sEmailTitle = "【通知】" + nickname.getResult_day() + "数据分析" ;					
			
			HashMap<String, Object> map = new HashMap<String, Object>() ;
			map.put("title", sEmailTitle) ;
			map.put("detail", emailContentStr) ;
			
			ResultObj resultObj = new ResultObj(map) ;
			renderJson(resultObj) ;				
		}					
	}
	
	/**
	 * 钉钉 链接测试 .
	 */
	private final static String kCorpID		 = "dingef5b18dc7fedfada" ;
	private final static String kCorpSecret	 = "0hUGyiRHz8WHTpsPG72jV6MM26rQzGVAi31E4IUcEIi6NRXBdh52ZZ_i0wqeHcSU" ;
	
	public void test() {
		String response = HttpRequest.sendGet("https://oapi.dingtalk.com/gettoken", 
				"corpid="+kCorpID+"&corpsecret="+kCorpSecret) ;
		renderJson(response) ;
	}
	
}
