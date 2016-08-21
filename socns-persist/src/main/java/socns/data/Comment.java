/**
 * 
 */
package socns.data;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author langhsu
 * 
 */
public class Comment {
	private long id;
	private long toId;
	private long toPid;
	private String content;
	private Date created;
	private User author;
	private int status;

	// params
	private long authorId;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getToId() {
		return toId;
	}

	public void setToId(long toId) {
		this.toId = toId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(long authorId) {
		this.authorId = authorId;
	}

	public long getToPid() {
		return toPid;
	}

	public void setToPid(long toPid) {
		this.toPid = toPid;
	}

}
