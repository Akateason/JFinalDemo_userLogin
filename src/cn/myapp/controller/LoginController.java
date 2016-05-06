package cn.myapp.controller ;

import java.util.HashMap;

import com.jfinal.core.Controller;


import cn.myapp.model.ResultObj;
import cn.myapp.model.User;

public class LoginController extends Controller {

	public void index() {
		
		// get Param from url .
		String name = getPara("name") ;
		String pass = getPara("password") ;
		int kind = getParaToInt("kind") ;
		
		// set bean .
		User user = new User(name,pass,kind) ;
		
		// db .
		User userInDb = user.selectUserByName(name) ;		
		if (userInDb != null) {
			ResultObj resultObj = new ResultObj("0", "该账号已注册", null) ;
			renderJson(resultObj);
			return ;
		}
		
		// db insert 
		user.addin();
		
		// set resultData .
    	HashMap<String, Object> resMap = null ; 
    	if (name != null && pass != null && kind != 0) {
    		resMap = new HashMap<String,Object>() ;
        	resMap.put("name", name) ;
        	resMap.put("password", pass) ;
        	resMap.put("kind",kind) ;
		}
		
		ResultObj resultObj = new ResultObj(resMap) ; 
		renderJson(resultObj);		
	}
	
	
}