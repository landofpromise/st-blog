package socns.persist.dao.impl;

import mtons.modules.persist.impl.DaoImpl;
import mtons.modules.pojos.Paging;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import socns.persist.dao.STUserDao;
import socns.persist.entity.STUserPO;
import socns.persist.entity.UserPO;

import java.util.List;

/**
 * Created by shaohua.hsh on 2015-08-10.
 */
public class STUserDaoImpl extends DaoImpl<STUserPO> implements STUserDao {

    public STUserDaoImpl() {
        super(STUserPO.class);
    }


    @Override
    public STUserPO get(String mobile) {
        return findUniqueBy("phone", mobile);
    }

    @Override
    public List<STUserPO> paging(Paging paging, String key) {
        PagingQuery<STUserPO> q = pagingQuery(paging);
        if (StringUtils.isNotBlank(key)) {
            q.add(Restrictions.or(
                    Restrictions.like("phone", key, MatchMode.ANYWHERE),
                    Restrictions.like("name", key, MatchMode.ANYWHERE)
            ));
        }
        q.desc("id");
        return q.list();
    }
}
