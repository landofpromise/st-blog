/**
 * 
 */
package socns.web.controller.desk.user;

import mtons.modules.pojos.Paging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import socns.core.planet.PostPlanet;
import socns.data.User;
import socns.persist.entity.STUserPO;
import socns.persist.service.UserService;
import socns.web.controller.BaseController;
import socns.web.controller.desk.Views;

/**
 * @author langhsu
 *
 */
@Controller
public class UserHomeController extends BaseController {
	@Autowired
	private PostPlanet postPlanet;
	@Autowired
	private UserService userService;
	
	@RequestMapping("/user/{uid}")
	public String home(@PathVariable Long uid, Integer pn, ModelMap model) {
		User user = userService.get(uid);
		Paging page = wrapPage(pn);
		page = postPlanet.pagingByUserId(page, uid);
		//add st user info
		String username = user.getUsername();

		STUserPO stUser = userService.getSTUser(username);
		if(stUser != null){
			model.put("birthday", stUser.getBirthYear());
			model.put("hometown", stUser.getHometown());
			model.put("discuz", stUser.getDisUsername());
			model.put("muqu",stUser.getMuqu());
			model.put("realname", stUser.getName());
			if(stUser.getSex() > 0) {
				model.put("sex", "弟兄");
			}else if(stUser.getSex() == 0){
				model.put("sex", "姐妹");
			}
			model.put("zz",stUser.getZzName());
			model.put("isDzz", stUser.getIsDzz());
		}
		
		model.put("user", user);
		model.put("page", page);

		return getView(Views.USER_HOME);
	}
}
