/**
 * 
 */
package socns.persist.service;

import java.util.List;

import socns.data.Attach;

/**
 * @author langhsu
 *
 */
public interface AttachService {
	/**
	 * 查询文章的附件列表
	 * @param toId
	 * @return
	 */
	List<Attach> list(long toId);
	
	/**
	 * 添加附件
	 * @param album
	 * @return
	 */
	long add(Attach album);
	
	/**
	 * 删除文章附件
	 * @param toId
	 */
	void deleteByToId(long toId);
}
