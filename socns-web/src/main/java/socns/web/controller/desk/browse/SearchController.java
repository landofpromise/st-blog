/**
 * 
 */
package socns.web.controller.desk.browse;

import mtons.modules.pojos.Paging;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import socns.persist.service.PostService;
import socns.persist.service.TagService;
import socns.web.controller.BaseController;
import socns.web.controller.desk.Views;

/**
 * 文章搜索
 * @author langhsu
 *
 */
@Controller
@RequestMapping("/browse")
public class SearchController extends BaseController {
	@Autowired
	private PostService postService;
	@Autowired
	private TagService tagService;
	
	@RequestMapping("/search")
	public String search(Integer pn, String q, ModelMap model) {
		Paging page = wrapPage(pn);
		try {
			if (StringUtils.isNotEmpty(q)) {
				postService.search(page, q);
				tagService.identityHots(q);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("page", page);
		model.put("q", q);
		return getView(Views.BROWSE_SEARCH);
	}
	
}
