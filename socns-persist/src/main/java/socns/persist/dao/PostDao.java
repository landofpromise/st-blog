/**
 * 
 */
package socns.persist.dao;

import java.util.Collection;
import java.util.List;

import mtons.modules.persist.Dao;
import mtons.modules.pojos.Paging;

import org.hibernate.Session;

import socns.persist.entity.PostPO;

/**
 * @author langhsu
 *
 */
public interface PostDao extends Dao<PostPO> {
	Session getSession();
	List<PostPO> paging(Paging paging, String ord);
	List<PostPO> pagingByUserId(Paging paging, long userId);
	List<PostPO> findRecents(int maxResutls, long ignoreUserId);
	List<PostPO> findHots(int maxResutls, long ignoreUserId);
	List<PostPO> findByIds(Collection<Long> ids);
}
