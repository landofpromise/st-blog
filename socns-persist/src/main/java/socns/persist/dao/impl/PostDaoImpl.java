/**
 * 
 */
package socns.persist.dao.impl;

import java.util.Collection;
import java.util.List;

import mtons.modules.persist.impl.DaoImpl;
import mtons.modules.pojos.Paging;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import socns.persist.dao.PostDao;
import socns.persist.entity.PostPO;

/**
 * @author langhsu
 *
 */
public class PostDaoImpl extends DaoImpl<PostPO> implements PostDao {
	private static final long serialVersionUID = -8144066308316359853L;
	
	public PostDaoImpl() {
		super(PostPO.class);
	}
	
	@Override
	public Session getSession() {
		return super.session();
	}

	@Override
	public List<PostPO> paging(Paging paging, String ord) {
		PagingQuery<PostPO> q = pagingQuery(paging);
		
		if ("hottest".equals(ord)) {
			q.desc("views");
		}
		q.desc("created");
		return q.list();
	}

	@Override
	public List<PostPO> pagingByUserId(Paging paging, long userId) {
		PagingQuery<PostPO> q = pagingQuery(paging);
		if (userId > 0) {
			q.add(Restrictions.eq("author.id", userId));
		}
		q.desc("created");
		return q.list();
	}

	@Override
	public List<PostPO> findRecents(int maxResutls, long ignoreUserId) {
		TopQuery<PostPO> q = topQuery(maxResutls);
		q.add(Restrictions.neOrIsNotNull("title", ""));
		if (ignoreUserId > 0) {
			q.add(Restrictions.ne("author.id", ignoreUserId));
		}
		q.desc("created");
		return q.list();
	}
	
	@Override
	public List<PostPO> findHots(int maxResutls, long ignoreUserId) {
		TopQuery<PostPO> q = topQuery(maxResutls);
		q.add(Restrictions.neOrIsNotNull("title", ""));
//		if (ignoreUserId > 0) {
//			q.add(Restrictions.ne("author.id", ignoreUserId));
//		}
		q.desc("views");
		return q.list();
	}

	@Override
	public List<PostPO> findByIds(Collection<Long> ids) {
		return find(Restrictions.in("id", ids));
	}
}
