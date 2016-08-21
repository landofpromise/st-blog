/**
 * 
 */
package socns.persist.service;

import java.util.List;

import mtons.modules.pojos.Paging;
import socns.data.Comment;

/**
 * @author langhsu
 *
 */
public interface CommentService {
	void paging(Paging paging, String key);
	
	/**
	 * 查询评论列表
	 * @param page
	 * @param toId
	 */
	void paging(Paging paging, long toId);
	
	/**
	 * 发表评论
	 * @param comment
	 * @return
	 */
	long post(Comment comment);
	
	void delete(List<Long> ids);
}
