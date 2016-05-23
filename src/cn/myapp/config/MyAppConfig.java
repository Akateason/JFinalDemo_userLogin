/**
 * 
 */
package cn.myapp.config;

import cn.myapp.controller.DataNoteController;
import cn.myapp.controller.ScoreController;
import cn.myapp.controller.UserController;
import cn.myapp.controller.DingController;
import cn.myapp.model.User;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

/**
 * @author teason
 *
 */
public class MyAppConfig extends JFinalConfig {

	/* (non-Javadoc)
	 * @see com.jfinal.config.JFinalConfig#configConstant(com.jfinal.config.Constants)
	 */
	@Override
	public void configConstant(Constants me) {
		me.setDevMode(true);
		me.setEncoding("utf-8");		
	}

	/* (non-Javadoc)
	 * @see com.jfinal.config.JFinalConfig#configRoute(com.jfinal.config.Routes)
	 */
	@Override
	public void configRoute(Routes me) {		
		me.add("/user", UserController.class) ;
		me.add("/note", DataNoteController.class) ;
		me.add("/score",ScoreController.class) ;
		me.add("/ding",DingController.class) ;
	}
	
	/* (non-Javadoc)
	 * @see com.jfinal.config.JFinalConfig#configPlugin(com.jfinal.config.Plugins)
	 */
	@Override
	public void configPlugin(Plugins me) {
		C3p0Plugin cp = new C3p0Plugin("jdbc:mysql://127.0.0.1:3306/nutzbook?useUnicode=true&characterEncoding=utf8",
				"root", "") ;
		
		me.add(cp) ;
		ActiveRecordPlugin arp = new ActiveRecordPlugin(cp) ;
		me.add(arp) ;
		arp.addMapping("user", User.class) ;
	}
	
	/* (non-Javadoc)
	 * @see com.jfinal.config.JFinalConfig#configInterceptor(com.jfinal.config.Interceptors)
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		
	}
	
	/* (non-Javadoc)
	 * @see com.jfinal.config.JFinalConfig#configHandler(com.jfinal.config.Handlers)
	 */
	@Override
	public void configHandler(Handlers me) {

	}
	
}
