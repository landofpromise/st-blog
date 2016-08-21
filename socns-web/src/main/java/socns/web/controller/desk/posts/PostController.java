/**
 * 
 */
package socns.web.controller.desk.posts;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import mtons.modules.pojos.Data;
import mtons.modules.pojos.UserProfile;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import socns.core.context.AppContext;
import socns.core.event.LogEvent;
import socns.core.planet.PostPlanet;
import socns.data.Attach;
import socns.data.Post;
import socns.lang.EnumLog;
import socns.persist.service.PostService;
import socns.web.controller.BaseController;
import socns.web.controller.desk.Views;
import socns.web.upload.Repository;

/**
 * @author langhsu
 *
 */
@Controller
@RequestMapping("/post")
public class PostController extends BaseController {
	@Autowired
	private PostService postService;
	@Autowired
	private PostPlanet postPlanet;
	@Autowired
	private AppContext appContext;
	@Autowired
	private Repository fileRepository;
	@Autowired
	private ApplicationContext applicationContext;
	
	@RequestMapping(value = "/new/{type}", method = RequestMethod.GET)
	public String view(@PathVariable String type, ModelMap model) {
		model.put("type", type);
		return getView(Views.BLOG_POST + type);
	}
	
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String post(Post blog) {
		
		if (blog != null && StringUtils.isNotBlank(blog.getTitle())) {
			UserProfile up = getSubject().getProfile();
			
			handleAlbums(blog.getAlbums());
			blog.setAuthorId(up.getId());
			
			postPlanet.post(blog);
		}
		return "redirect:/home";
	}
	
	@RequestMapping("/delete/{id}")
	public @ResponseBody Data delete(@PathVariable Long id) {
		Data data = Data.failure("操作失败");
		if (id != null) {
			UserProfile up = getSubject().getProfile();
			try {
				postPlanet.delete(id, up.getId());
				data = Data.success("操作成功");
			} catch (Exception e) {
				data = Data.failure(e.getMessage());
			}
		}
		return data;
	}
	
	@RequestMapping("/heart")
	public @ResponseBody Data heart(Long id, HttpServletRequest request) {
		Data data = Data.failure("操作失败");
		if (id != null) {
			try {
				UserProfile up = getSubject().getProfile();
				
				LogEvent evt = new LogEvent("source");
				
				if (up != null) {
					evt.setUserId(up.getId());
				}
				evt.setTargetId(id);
				evt.setType(EnumLog.LIKED);
				evt.setIp(getIpAddr(request));
				applicationContext.publishEvent(evt);
				
				data = Data.success("操作成功,感谢您的支持!");
			} catch (Exception e) {
				data = Data.failure(e.getMessage());
			}
		}
		return data;
	}
	
	private void handleAlbums(List<Attach> albums) {
		if (albums == null) {
			return;
		}
		for (Attach alb : albums) {
			createPic(alb);
		}
	}

	private void createPic(Attach album) {
		String root = getRealPath("/");
		File temp = new File(root + album.getOriginal());
		
		try {
			// 保存原图
			String orig = fileRepository.storeScale(temp, appContext.getOrigDir(), 700);
			album.setOriginal(orig);
			
			// 创建缩放图片
			String preview = fileRepository.storeScale(temp, appContext.getThumbsDir(), 360);
			album.setPreview(preview);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (temp != null) {
				temp.delete();
			}
		}
	}
	
}
