package cn.myapp.module.titleScore.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import cn.myapp.model.DaoObject;

public class TitleAnalyze extends DaoObject {
	
	private static final long serialVersionUID = 1L;
		
	private long 		analyzeID ;					// PK	id
	private long		titleID ;					// FK  	所属标题id
	private long 		classify ;					// 新闻分类 * 0体育* 1教育*  2财经*  3社会*  4娱乐*  5军事*  6国内 * 7科技* 8互联网* 9房产* 10国际* 11女人* 12汽车* 13游戏	
	private String		mainKeyword ;				// 主关键词
	private String		keywordList ;				// 关键词权重数组
	private double		emotionPositive ;			// 积极情绪
	private double		emotionNegative ;			// 消极情绪
	private String		mainKeywordSuggestList ;	// 主关键字联想数组
	
	public long getAnalyzeID() {
		return analyzeID;
	}
	public void setAnalyzeID(long analyzeID) {
		this.analyzeID = analyzeID;
	}
	public long getTitleID() {
		return titleID;
	}
	public void setTitleID(long titleID) {
		this.titleID = titleID;
	}
	public long getClassify() {
		return classify;
	}
	public void setClassify(long classify) {
		this.classify = classify;
	}
	public String getMainKeyword() {
		return mainKeyword;
	}
	public void setMainKeyword(String mainKeyword) {
		this.mainKeyword = mainKeyword;
	}
	public String getKeywordList() {
		return keywordList;
	}
	public void setKeywordList(String keywordList) {
		this.keywordList = keywordList;
	}
	public double getEmotionPositive() {
		return emotionPositive;
	}
	public void setEmotionPositive(double emotionPositive) {
		this.emotionPositive = emotionPositive;
	}
	public double getEmotionNegative() {
		return emotionNegative;
	}
	public void setEmotionNegative(double emotionNegative) {
		this.emotionNegative = emotionNegative;
	}
	public String getMainKeywordSuggestList() {
		return mainKeywordSuggestList;
	}
	public void setMainKeywordSuggestList(String mainKeywordSuggestList) {
		this.mainKeywordSuggestList = mainKeywordSuggestList;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	/**	DAO SELECT
	 *	select analyze by titleID .
	 */
	public static TitleAnalyze selectAnalyzeByTitleID(long titleID) {
		Record record = Db.findById("analyze", "titleID", titleID) ;	
		if (record != null) {
			return (TitleAnalyze) (new TitleAnalyze().fetchFromRecord(record)) ;
		}
		return null ;
	}
	
}
