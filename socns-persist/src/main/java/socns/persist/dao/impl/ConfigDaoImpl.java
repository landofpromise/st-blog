/**
 * 
 */
package socns.persist.dao.impl;

import mtons.modules.persist.impl.DaoImpl;
import socns.persist.dao.ConfigDao;
import socns.persist.entity.ConfigPO;

/**
 * @author langhsu
 *
 */
public class ConfigDaoImpl extends DaoImpl<ConfigPO> implements ConfigDao {
	private static final long serialVersionUID = 1661965983527190778L;

	public ConfigDaoImpl() {
		super(ConfigPO.class);
	}

	@Override
	public ConfigPO findByName(String key) {
		return findUniqueBy("key", key);
	}
	
}
