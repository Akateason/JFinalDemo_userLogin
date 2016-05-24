package cn.myapp.module.titleScore.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import cn.myapp.model.DaoObject;

/**
 * 标题信息类 
 * @author teason .
 */
public class Title extends DaoObject {
	
	private static final long serialVersionUID = 1L;
	
	private long		titleID ;					// id
	private String		content ;					// 标题
	private String		from ;						// 出处
	private String		createTime ;				// 创建时间
	private String		updateTime ;				// 更新时间
	private String		author ;					// 作者
	private long		selectedNumber ;			// 被选中次数
	private long		readNum ;					// 阅读
	private long		likeNum ; 					// 点赞
	private long 		score ;						// 评分
	
	public long getTitleID() {
		return titleID;
	}

	public void setTitleID(long titleID) {
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

	public long getSelectedNumber() {
		return selectedNumber;
	}

	public void setSelectedNumber(long selectedNumber) {
		this.selectedNumber = selectedNumber;
	}

	public long getReadNum() {
		return readNum;
	}

	public void setReadNum(long readNum) {
		this.readNum = readNum;
	}

	public long getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(long likeNum) {
		this.likeNum = likeNum;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public static Title selectTitleByContent(String content) {
		Record record = Db.findById("title", "content", content) ;	
		if (record != null) {
			return (Title)new Title().fetchFromRecord(record) ;
		}
		return null ;
	}
	
	/**	DAO SELECT
	 *	select title by titleID .
	 */
	public static Title selectTitleByTitleID(int titleID) {
		Record record = Db.findById("title", "titleID", titleID) ;	
		if (record != null) {
			return (Title)new Title().fetchFromRecord(record) ;
		}
		return null ;
	}
	
}
