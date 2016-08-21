/**
 * 
 */
package socns.utils;

import org.springframework.beans.BeanUtils;

import socns.data.AccountProfile;
import socns.data.Attach;
import socns.data.Comment;
import socns.data.Post;
import socns.data.Tag;
import socns.data.User;
import socns.persist.entity.AttachPO;
import socns.persist.entity.CommentPO;
import socns.persist.entity.PostPO;
import socns.persist.entity.TagPO;
import socns.persist.entity.UserPO;

/**
 * @author langhsu
 *
 */
public class BeanMapUtils {
	private static String[] USER_IGNORE = new String[]{"password"};
	
	private static String[] COMMENT_IGNORE = new String[]{"author"};
	
	private static String[] POST_IGNORE = new String[]{"author", "snapshot"};
	private static String[] POST_IGNORE_LIST = new String[]{"author", "snapshot", "markdown", "content"};
	
	public static User copy(UserPO po) {
		if (po == null) {
			return null;
		}
		User ret = new User();
		BeanUtils.copyProperties(po, ret, USER_IGNORE);
		return ret;
	}
	
	public static AccountProfile copyPassport(UserPO po) {
		AccountProfile passport = new AccountProfile(po.getId(), po.getUsername());
		passport.setName(po.getName());
		passport.setEmail(po.getEmail());
		passport.setAvatar(po.getAvatar());
		passport.setLastLogin(po.getLastLogin());
		passport.setRoleId(po.getRoleId());
		passport.setStatus(po.getStatus());
		return passport;
	}
	
	public static Comment copy(CommentPO po) {
		Comment ret = new Comment();
		BeanUtils.copyProperties(po, ret, COMMENT_IGNORE);
		
		if (po.getAuthor() != null) {
			ret.setAuthor(copy(po.getAuthor()));
		}
		return ret;
	}
	
	public static Post copy(PostPO po, int level) {
		Post d = new Post();
		if (level > 0) {
			BeanUtils.copyProperties(po, d, POST_IGNORE);
		} else {
			BeanUtils.copyProperties(po, d, POST_IGNORE_LIST);
		}
		
		if (po.getAuthor() != null) {
			User u = new User();
			u.setId(po.getAuthor().getId());
			u.setUsername(po.getAuthor().getUsername());
			u.setName(po.getAuthor().getName());
			u.setAvatar(po.getAuthor().getAvatar());
			d.setAuthor(u);
		}
		if (po.getSnapshot() != null) {
			Attach a = new Attach();
			BeanUtils.copyProperties(po.getSnapshot(), a);
			d.setSnapshot(a);
		}
		return d;
	}
	
	public static Attach copy(AttachPO po) {
		Attach ret = new Attach();
		BeanUtils.copyProperties(po, ret);
		return ret;
	}
	
	public static Tag copy(TagPO po) {
		Tag ret = new Tag();
		BeanUtils.copyProperties(po, ret);
		return ret;
	}
}
