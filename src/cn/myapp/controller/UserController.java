package cn.myapp.controller ;

import java.util.HashMap;

import com.google.gson.Gson;
import com.jfinal.core.Controller;
import cn.myapp.model.ResultObj;
import cn.myapp.model.User;

public class UserController extends Controller {

	/**
	 * register user with name ,password ,kind .
	 */
	public void register() {		
		// get Param from url .
		String name = getPara("name") ;
		String pass = getPara("password") ;
		int kind = getParaToInt("kind") ;
		
		// set bean .
		User user = new User(name,pass,kind) ;
		
		// db . if exist in db .
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
	    
		Gson gson = new Gson() ;
		String jsonString = gson.toJson(user) ;		 
		ResultObj resultObj = new ResultObj(jsonString) ;
		renderJson(resultObj);		
	}
	
	/**
	 *	login .with username .password .
	 * 	@return . username .password . kind . userID
	 */
	public void login() {
		// get Param from url .
		String name = getPara("name") ;
		String pass = getPara("password") ;
		User userSended = new User(name, pass, null) ;
		User userFetch = userSended.selectUserByName(name) ;
		if (userFetch == null) {
			// 用户名不存在 .
			ResultObj resultObj = new ResultObj("0", "该账号不存在", null) ;
			renderJson(resultObj);
			return ;
		}
		else {
			// 用户存在
			// 1. 密码错误
			String passFetch = userFetch.getPassword() ;
			if (!pass.equals(passFetch)) {
				ResultObj resultObj = new ResultObj("0", "密码错误", null) ;
				renderJson(resultObj) ;		
			}
			// 2. 登录成功 . 返回 user
			else {
				Gson gson = new Gson() ;
				String jsonString = gson.toJson(userFetch) ;
				ResultObj resultObj = new ResultObj(jsonString) ;
				renderJson(resultObj) ;
			}
		} 					
	}	
	
}