/**
 * 
 */
package socns.web.controller.desk.comment;

import mtons.modules.pojos.Data;
import mtons.modules.pojos.Paging;
import mtons.modules.pojos.UserProfile;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import socns.data.Comment;
import socns.persist.service.CommentService;
import socns.persist.service.PostService;
import socns.web.controller.BaseController;

/**
 * @author langhsu
 *
 */
@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {
	@Autowired
	private CommentService commentService;
	@Autowired
	private PostService postService;
	
	@RequestMapping("/list/{toId}")
	public @ResponseBody Paging view(Integer pn, @PathVariable Long toId) {
		Paging page = wrapPage(pn);
		commentService.paging(page, toId);
		int totalCount = page.getResults().size();
		page.setTotalCount(totalCount);
		return page;
	}
	
	@RequestMapping("/submit")
	public @ResponseBody Data post(Long toId,Long toPid, String text) {
		Data data = Data.failure("操作失败");
		
		if (!SecurityUtils.getSubject().isAuthenticated()) {
			data = Data.failure("请先登录在进行操作");
			
			return data;
		}
		if (toId > 0 && StringUtils.isNotEmpty(text)) {
			UserProfile up = getSubject().getProfile();
			
			Comment c = new Comment();
			c.setToId(toId);
			c.setContent(HtmlUtils.htmlEscape(text));
			c.setAuthorId(up.getId());
			c.setToPid(toPid);
			commentService.post(c);
			// 自增评论数
			postService.identityComments(c.getToId());
			data = Data.success("发表成功!");
		}
		return data;
	}
	
}
