package cn.myapp.model;

import cn.myapp.util.HttpRequest;
import cn.myapp.util.JsonToMap;
import com.google.gson.JsonObject;
import com.jfinal.json.Json;
import com.jfinal.kit.PropKit;

/**
 * Created by apple on 16/5/13.
 */
public class DingDing {
    private String CorpID;
    private String CorpSecret;
    private String access_token;
    private String agetid;
    private String touser;

    public DingDing(){
        CorpID=PropKit.use("ding_config.txt").get("CorpID");
        CorpSecret=PropKit.use("ding_config.txt").get("CorpSecret");
        agetid=PropKit.use("ding_config.txt").get("agentid");
        touser=PropKit.use("ding_config.txt").get("touser");
        getAccessToken();
    }

    public void getAccessToken() {
        String param="corpid="+CorpID+"&corpsecret="+CorpSecret;
        System.out.println(param);
        String result=HttpRequest.sendGet("https://oapi.dingtalk.com/gettoken",param);
        JsonObject result_json= JsonToMap.parseJson(result);
        if(result_json.get("errcode").getAsInt()==0){
            access_token=result_json.get("access_token").getAsString();
        }
    }

    public String sendToUser(){
        String url="https://oapi.dingtalk.com/message/send?access_token="+access_token;
        String params="{\"touser\":\""+touser+"\",\"agentid\":\""+agetid+"\",\"msgtype\":\"text\",\"text\":{\"content\":\""+getData()+"\"}}";
        String result=HttpRequest.sendPost(url,params);
        JsonObject result_json=JsonToMap.parseJson(result);
        if(result_json.get("errcode").getAsInt()==0){
            return "success";
        }else{
            return "fail";
        }
    }

    private String getData(){
        String result=HttpRequest.sendGet("http://localhost:8080/note/fetchGroupSendInfo","");
        JsonObject result_json= JsonToMap.parseJson(result);
        if(result_json.get("returnCode").getAsString().equals("1001")){
            JsonObject detail=result_json.get("returnData").getAsJsonObject();
            result=detail.get("detail").getAsString();
        }else{
            result="失败";
        }
        return result;
    }
}
