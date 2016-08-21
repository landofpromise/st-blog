/**
 * 
 */
package socns.web.controller.desk.tags;

import java.util.List;

import mtons.modules.pojos.Paging;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import socns.core.planet.TagPlanet;
import socns.data.Tag;
import socns.persist.service.PostService;
import socns.persist.service.TagService;
import socns.web.controller.BaseController;
import socns.web.controller.desk.Views;
import socns.web.utils.EncodingTool;

/**
 * 发现
 * @author langhsu
 *
 */
@Controller
public class TagsController extends BaseController {
	@Autowired
	private PostService postService;
	@Autowired
	private TagPlanet tagPlanet;
	@Autowired
	private TagService tagService;
	
	private int maxResults = 12;
	
	@RequestMapping("/explore")
	public String view(ModelMap model) {
		List<Tag> tags = tagPlanet.topTags(maxResults, true);
		model.put("tags", tags);
		return getView(Views.TAGS_EXPLORE);
	}
	
	@RequestMapping("/tag/{tag}")
	public String tag(@PathVariable String tag, Integer pn, ModelMap model) {
		tag = EncodingTool.encodeStr(tag);
		Paging page = wrapPage(pn);
		try {
			if (StringUtils.isNotEmpty(tag)) {
				postService.searchByTag(page, tag);
				tagService.identityHots(tag);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("page", page);
		model.put("tag", tag);
		return getView(Views.TAGS_TAG);
	}
	
}
