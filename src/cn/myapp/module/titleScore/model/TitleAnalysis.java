package cn.myapp.module.titleScore.model;

public class TitleAnalysis {
	
	private Integer 	analysisID ;				// PK	id
	private Integer 	titleID ;					// FK  	所属标题id
	private String 		classify ;					// 新闻分类
	private String		mainKeyword ;				// 主关键词
	private String		keywordList ;				// 关键词权重数组
	private double		emotionPositive ;			// 积极情绪
	private double		emotionNegative ;			// 消极情绪
	private String		mainKeywordSuggestList ;	// 主关键字联想数组
		
	
}
