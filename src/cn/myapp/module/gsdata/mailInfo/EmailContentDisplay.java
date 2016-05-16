package cn.myapp.module.gsdata.mailInfo;

import cn.myapp.module.gsdata.mailInfo.model.Nickname;
import cn.myapp.module.gsdata.mailInfo.model.NicknameTranslate;
import cn.myapp.util.UtilSplit;


public class EmailContentDisplay 
{
	/**
	 * getEmailContentWillDisplay
	 * @param nickname
	 * @return string .
	 */
	public String getEmailContentWillDisplay(Nickname nickname) 
	{
		String resultString = "【前日日本流行每日速报详细数据】\n\n" 
					+ "最高阅读数 : " + nickname.getReadnum_max() + "\n" 
					+ "总阅读相对上次的增量(负数即为下降) : " + nickname.getReadnum_all_up() + "\n\n";
		
		UtilSplit utilSplit = new UtilSplit() ;		
		String[] namelist = utilSplit.getFiledName(nickname) ;
		
		for (String name : namelist) 
		{
			String val = "" ;
			if (utilSplit.getFieldValueByName(name,nickname) != null) {
				val = utilSplit.getFieldValueByName(name,nickname).toString() ;
			}
			String zhongwen = NicknameTranslate.getZhongwen(name) ;
			if (zhongwen == null) {
				continue ;
			}
			resultString += zhongwen + " : " + val + "\n" ;
		}
		
		return resultString ;
	}
	
}
