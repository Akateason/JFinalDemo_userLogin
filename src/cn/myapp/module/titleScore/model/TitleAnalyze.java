package cn.myapp.module.titleScore.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import cn.myapp.model.DaoObject;

public class TitleAnalyze extends DaoObject{
	
	private static final long serialVersionUID = 1L;
		
	private int 		analyzeID ;					// PK	id
	private int			titleID ;					// FK  	所属标题id
	private int 		classify ;					// 新闻分类 * 0体育* 1教育*  2财经*  3社会*  4娱乐*  5军事*  6国内 * 7科技* 8互联网* 9房产* 10国际* 11女人* 12汽车* 13游戏	
	private String		mainKeyword ;				// 主关键词
	private String		keywordList ;				// 关键词权重数组
	private double		emotionPositive ;			// 积极情绪
	private double		emotionNegative ;			// 消极情绪
	private String		mainKeywordSuggestList ;	// 主关键字联想数组
	
	public int getAnalyzeID() {
		return analyzeID;
	}

	public void setAnalyzeID(int analyzeID) {
		this.analyzeID = analyzeID;
	}

	public int getTitleID() {
		return titleID;
	}

	public void setTitleID(int titleID) {
		this.titleID = titleID;
	}
	
	public int getClassify() {
		return classify;
	}

	public void setClassify(int classify) {
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

	///////////////////////////////////////////////////////////////////////////////////////


	/**	DAO SELECT
	 *	select analyze by titleID .
	 */
	public TitleAnalyze selectAnalyzeByTitleID(int titleID) {
		Record record = Db.findById("analyze", "titleID", titleID) ;	
		if (record != null) {
			return (TitleAnalyze) fetchFromRecord(record) ;
		}
		return null ;
	}
}
