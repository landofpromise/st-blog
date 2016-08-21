/**
 * 
 */
package socns.core.planet;

import java.util.List;

import socns.data.Tag;

/**
 * @author langhsu
 *
 */
public interface TagPlanet {
	List<Tag> topTags(int maxResutls, boolean loadPost);
	void delete(long id);
	boolean cacheFlush();
}
