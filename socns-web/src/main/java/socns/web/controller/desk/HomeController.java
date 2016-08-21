/**
 * 
 */
package socns.web.controller.desk;

import mtons.modules.pojos.Paging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import socns.persist.service.PostService;
import socns.shiro.authc.AccountSubject;
import socns.web.controller.BaseController;

/**
 * @author langhsu
 *
 */
@Controller
public class HomeController extends BaseController {
	@Autowired
	private PostService postService;
	
	@RequestMapping("/home")
	public String home(Integer pn, ModelMap model) {
		Paging page = wrapPage(pn);
		AccountSubject subject = getSubject();
		postService.pagingByUserId(page, subject.getProfile().getId());
		
		model.put("page", page);
		
		return getView(Views.HOME);
	}
	
}
