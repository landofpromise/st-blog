/**
 * 
 */
package socns.persist.service;

import java.util.List;
import java.util.Map;

import socns.data.Config;

/**
 * @author langhsu
 *
 */
public interface ConfigService {
	List<Config> findAll();
	Map<String, Config> findAll2Map();
	
	void update(List<Config> configs);
}
