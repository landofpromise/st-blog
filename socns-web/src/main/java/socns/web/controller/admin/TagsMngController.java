/**
 * 
 */
package socns.web.controller.admin;

import mtons.modules.pojos.Data;
import mtons.modules.pojos.Paging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import socns.core.planet.TagPlanet;
import socns.data.Tag;
import socns.persist.service.TagService;
import socns.web.controller.BaseController;

/**
 * @author langhsu
 *
 */
@Controller
@RequestMapping("/admin/tags")
public class TagsMngController extends BaseController {
	@Autowired
	private TagService tagService;
	@Autowired
	private TagPlanet tagPlanet;
	
	@RequestMapping("/list")
	public String list(Integer pn, String key, String order, ModelMap model) {
		Paging page = wrapPage(pn);
		tagService.paging(page, key, order);
		model.put("page", page);
		model.put("key", key);
		model.put("order", order);
		return "/admin/tags/list";
	}
	
	@RequestMapping("/view")
	public String view(Long id, ModelMap model) {
		Tag tag = tagService.get(id);
		model.put("view", tag);
		return "/admin/tags/view";
	}
	
	@RequestMapping("/update")
	public String update(Tag tag) {
		if (tag != null) {
			tagService.update(tag);
		}
		return "redirect:/admin/tags/list";
	}
	
	@RequestMapping("/featured")
	public @ResponseBody Data featured(Long id, int status) {
		Data data = Data.failure("操作失败");
		if (id != null) {
			try {
				tagService.updateFeatured(id, status);
				data = Data.success("操作成功");
			} catch (Exception e) {
				data = Data.failure(e.getMessage());
			}
		}
		return data;
	}
	
	@RequestMapping("/lock")
	public @ResponseBody Data lock(Long id, int status) {
		Data data = Data.failure("操作失败");
		if (id != null) {
			try {
				tagService.updateLock(id, status);
				data = Data.success("操作成功");
			} catch (Exception e) {
				data = Data.failure(e.getMessage());
			}
		}
		return data;
	}
	
	@RequestMapping("/delete")
	public @ResponseBody Data delete(Long id) {
		Data data = Data.failure("操作失败");
		if (id != null) {
			try {
				tagPlanet.delete(id);
				data = Data.success("操作成功");
			} catch (Exception e) {
				data = Data.failure(e.getMessage());
			}
		}
		return data;
	}
	
}
