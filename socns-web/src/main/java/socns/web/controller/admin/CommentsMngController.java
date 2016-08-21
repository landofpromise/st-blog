/**
 * 
 */
package socns.web.controller.admin;

import java.util.List;

import mtons.modules.pojos.Data;
import mtons.modules.pojos.Paging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import socns.persist.service.CommentService;
import socns.web.controller.BaseController;

/**
 * @author langhsu
 *
 */
@Controller
@RequestMapping("/admin/comments")
public class CommentsMngController extends BaseController {
	@Autowired
	private CommentService commentService;
	
	@RequestMapping("/list")
	public String list(Integer pn, String key, ModelMap model) {
		Paging page = wrapPage(pn);
		commentService.paging(page, key);
		model.put("page", page);
		model.put("key", key);
		return "/admin/comments/list";
	}
	
	@RequestMapping("/delete")
	public @ResponseBody Data delete(@RequestParam("id") List<Long> id) {
		Data data = Data.failure("操作失败");
		if (id != null) {
			try {
				commentService.delete(id);
				data = Data.success("操作成功");
			} catch (Exception e) {
				data = Data.failure(e.getMessage());
			}
		}
		return data;
	}
}
