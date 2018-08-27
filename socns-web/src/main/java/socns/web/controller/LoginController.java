package socns.web.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mtons.modules.utils.MD5Helper;
import socns.persist.service.UserService;
import socns.web.controller.desk.Views;
import socns.web.utils.UserInfoCache;

/**
 * 
 * @author langhsu
 *
 */
@Controller
public class LoginController extends BaseController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String view() {
		return getView(Views.LOGIN);
	}
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(String username, String password, ModelMap model) {
		String ret = getView("/login");
	    if ((StringUtils.isBlank(username)) || (StringUtils.isBlank(password))) {
	      return ret;
	    }
	    String md5Psd = MD5Helper.md5(password);
	    AuthenticationToken token = new UsernamePasswordToken(username, MD5Helper.md5(password), true);
	    if (token == null)
	    {
	      model.put("message", "用户名或密码错误");
	      return ret;
	    }
	    try
	    {
	      SecurityUtils.getSubject().login(token);
	      UserInfoCache.put(username, md5Psd);
	      
	      ret = "redirect:/home";
	    }
	    catch (AuthenticationException e)
	    {
	      if ((e instanceof UnknownAccountException))
	      {
	        model.put("message", "用户不存在");
	      }
	      else if ((e instanceof LockedAccountException))
	      {
	        model.put("message", "用户被禁用");
	      }
	      else
	      {
	        Object obj = SecurityUtils.getSubject().getPrincipal();
	        if (obj == null) {
	          obj = "";
	        }
	        model.put("message", "用户名或密码错误");
	      }
	    }
	    return ret;
	}

	protected AuthenticationToken createToken(String username, String password) {
        return new UsernamePasswordToken(username, MD5Helper.md5(password), true);
    }
}
