/**
 * 
 */
package socns.web.controller.desk.account;

import mtons.modules.pojos.Data;
import mtons.modules.pojos.UserProfile;

import mtons.modules.utils.MD5Helper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RequestParam;
import socns.data.User;
import socns.lang.Consts;
import socns.persist.entity.STUserPO;
import socns.persist.service.UserService;
import socns.web.controller.BaseController;
import socns.web.controller.desk.Views;

/**
 * @author langhsu
 *
 */
@Controller
public class RegController extends BaseController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/reg", method = RequestMethod.GET)
	public String view(ModelMap model) {
//		UserProfile profile = getSubject().getProfile();
//		if (profile != null) {
//			return "redirect:/home";
//		}
//		return getView(Views.REG);

		return "redirect:/home";
	}

	@RequestMapping(value = "/import", method = RequestMethod.GET)
	public String importST(@RequestParam("token")String token, ModelMap model) {
		if(StringUtils.isBlank(token)){
			model.put("error", "token is null");
			return "redirect:/error";
		}

		STUserPO stUser = userService.getSTUser(token);
		if(null == stUser){
			model.put("error", "st user isn't exist");
			return "redirect:/error";
		}

		User userInfo = userService.get(token);
		if(null != userInfo){
			model.put("message", "ST用户已导入，请用账号密码登录");
			return getView(Views.LOGIN);
		}

		User user = new User();
		String phone = stUser.getPhone();
		user.setAvatar(Consts.AVATAR);
		user.setMobile(phone);
		user.setName(stUser.getName());
		user.setUsername(stUser.getPhone());

		String password = phone.substring(phone.length() - 6,phone.length());
		user.setPassword(password);
		userService.register(user);

//		if (profile != null) {
//			return "redirect:/home";
//		}

		model.put("message", "ST用户默认密码为手机后六位:" + password);
		return getView(Views.LOGIN);
	}
	
	@RequestMapping(value = "/reg", method = RequestMethod.POST)
	public String reg(User user, ModelMap model) {
//		Data data = Data.failure("注册失败");
//		String ret = getView(Views.REG);
//
//		try {
//			user.setAvatar(Consts.AVATAR);
//			userService.register(user);
//			data = Data.success("恭喜您! 注册成功");
//			ret = getView(Views.REG_RESULT);
//
//		} catch (Exception e) {
//			data = Data.failure(e.getMessage());
//		}
		Data data = Data.failure("ST仅支持导入");
		model.put("data", data);
		return getView(Views.REG);
	}

	public static void  main(String[] args){
		String token = "18698564908";
		String password = token.substring(token.length() - 6,token.length());
		System.out.println(password);
		System.out.println(MD5Helper.md5(password));
	}

}
