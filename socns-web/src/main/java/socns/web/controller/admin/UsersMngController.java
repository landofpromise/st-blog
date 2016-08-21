/**
 * 
 */
package socns.web.controller.admin;

import mtons.modules.lang.Const;
import mtons.modules.pojos.Data;
import mtons.modules.pojos.Paging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import socns.data.User;
import socns.lang.EnumRole;
import socns.persist.service.UserService;
import socns.web.controller.BaseController;

/**
 * @author langhsu
 *
 */
@Controller
@RequestMapping("/admin/users")
public class UsersMngController extends BaseController {
	@Autowired
	private UserService userService;
	
	@RequestMapping("/list")
	public String list(Integer pn, String key, ModelMap model) {
		Paging page = wrapPage(pn);
		userService.paging(page, key);
		model.put("page", page);
		model.put("key", key);
		return "/admin/users/list";
	}
	
	@RequestMapping("/view")
	public String view(Long id, ModelMap model) {
		User view = userService.get(id);
		model.put("view", view);
		model.put("roles", EnumRole.values());
		return "/admin/users/view";
	}
	
	@RequestMapping("/update_role")
	public String update(long id, int roleId) {
		userService.updateRole(id, roleId);
		return "redirect:/admin/users/list";
	}
	
	@RequestMapping(value = "/pwd", method = RequestMethod.GET)
	public String pwsView(Long id, ModelMap model) {
		User ret = userService.get(id);
		model.put("view", ret);
		return "/admin/users/pwd";
	}
	
	@RequestMapping(value = "/pwd", method = RequestMethod.POST)
	public String pwd(Long id, String newPassword, ModelMap model) {
		User ret = userService.get(id);
		model.put("view", ret);

		try {
			userService.updatePassword(id, newPassword);
			model.put("message", "修改成功");
		} catch (IllegalArgumentException e) {
			model.put("message", e.getMessage());
		}
		return "/admin/users/pwd";
	}
	
	@RequestMapping("/open")
	public @ResponseBody Data open(Long id) {
		userService.updateStatus(id, Const.STATUS_NORMAL);
		Data data = Data.success("操作成功");
		return data;
	}
	
	@RequestMapping("/close")
	public @ResponseBody Data close(Long id) {
		userService.updateStatus(id, Const.STATUS_CLOSED);
		Data data = Data.success("操作成功");
		return data;
	}
}
