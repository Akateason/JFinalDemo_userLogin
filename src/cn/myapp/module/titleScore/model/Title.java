package cn.myapp.module.titleScore.model;

import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import cn.myapp.model.DaoObject;

/**
 * 标题信息类 
 * @author teason .
 */
public class Title extends DaoObject {
	
	private static final long serialVersionUID = 1L;
	
	private int		 	titleID ;					// id
	private String		content ;					// 标题
	private String		from ;						// 出处
	private String		createTime ;				// 创建时间
	private String		updateTime ;				// 更新时间
	private String		author ;					// 作者
	private int			selectedNumber ;			// 被选中次数
	private int			readNum ;					// 阅读
	private int			likeNum ; 					// 点赞
	private int 		score ;						// 评分
	
	public int getTitleID() {
		return titleID;
	}
	public void setTitleID(int titleID) {
		this.titleID = titleID;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getSelectedNumber() {
		return selectedNumber;
	}
	public void setSelectedNumber(int selectedNumber) {
		this.selectedNumber = selectedNumber;
	}
	public int getReadNum() {
		return readNum;
	}
	public void setReadNum(int readNum) {
		this.readNum = readNum;
	}
	public int getLikeNum() {
		return likeNum;
	}
	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	/**
	 * CONSTRUCTOR .
	 * @param contentStr
	 */
	public Title(String contentStr) {
		// TODO Auto-generated constructor stub
		this.content = contentStr ;
	}
	
	public Title() {
		// TODO Auto-generated constructor stub
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	/**	DAO SELECT
	 *	select title by content .
	 */
	public Title selectTitleByContent(String content) {
		Record record = Db.findById("title", "content", content) ;	
		if (record != null) {
			return (Title)fetchFromRecord(record) ;
		}
		return null ;
	}
	
}
