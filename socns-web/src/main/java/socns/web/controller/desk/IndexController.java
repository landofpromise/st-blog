/**
 * 
 */
package socns.web.controller.desk;

import mtons.modules.pojos.Paging;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import socns.core.planet.PostPlanet;
import socns.web.controller.BaseController;

/**
 * @author langhsu
 *
 */
@Controller
public class IndexController extends BaseController{
	@Autowired
	private PostPlanet postPlanet;
	
	private String defaultOrder = "newest";
	
	@RequestMapping(value= {"/", "/index"})
	public String root(Integer pn, String ord, ModelMap model) {
		Paging page = wrapPage(pn);
		String order = defaultOrder;
		if (StringUtils.isNotBlank(ord)) {
			order = ord;
		}
		page.setMaxResults(50);
		page = postPlanet.paging(page, order);
		model.put("page", page);
		model.put("ord", order);
		return getView(Views.INDEX);
	}
	
	@RequestMapping("/post_list")
	public @ResponseBody Paging indexJson(Integer pn) {
		Paging page = wrapPage(pn);
		page = postPlanet.paging(page, defaultOrder);
		return page;
	}
	
}
