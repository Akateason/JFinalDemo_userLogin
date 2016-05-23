package cn.myapp.controller;

import cn.myapp.model.DingDing;
import com.jfinal.core.Controller;

/**
 * Created by apple on 16/5/13.
 */
public class DingController extends Controller {
    public void index(){
        DingDing dd=new DingDing();
        String result=dd.sendToUser();
        renderText(result);
    }
}
