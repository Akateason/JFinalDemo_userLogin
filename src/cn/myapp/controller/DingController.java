package cn.myapp.controller;

import com.jfinal.core.Controller;

import cn.myapp.module.ding.model.DingDing;


public class DingController extends Controller {

    public void index(){
        DingDing dd=new DingDing();
        String result=dd.sendToUser();
        renderText(result);
    }
    
}
