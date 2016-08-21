package socns.persist.dao;

import mtons.modules.persist.Dao;
import mtons.modules.pojos.Paging;
import socns.persist.entity.STUserPO;
import socns.persist.entity.UserPO;

import java.util.List;

/**
 * Created by shaohua.hsh on 2015-08-10.
 */
public interface STUserDao extends Dao<STUserPO> {
    STUserPO get(String mobile);
    List<STUserPO> paging(Paging paging, String key);
}
