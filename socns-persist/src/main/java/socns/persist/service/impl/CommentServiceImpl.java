/**
 * 
 */
package socns.persist.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mtons.modules.pojos.Paging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import socns.data.Comment;
import socns.persist.dao.CommentDao;
import socns.persist.dao.UserDao;
import socns.persist.entity.CommentPO;
import socns.persist.service.CommentService;
import socns.utils.BeanMapUtils;

/**
 * @author langhsu
 *
 */
public class CommentServiceImpl implements CommentService {
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private UserDao userDao;
	
	@Override
	@Transactional(readOnly = true)
	public void paging(Paging paging, String key) {
		List<CommentPO> list = commentDao.paging(paging, key);
		List<Comment> rets = new ArrayList<Comment>();
		for (CommentPO po : list) {
			rets.add(BeanMapUtils.copy(po));
		}
		paging.setResults(rets);
	}
	
	@Override
	@Transactional(readOnly = true)
	public void paging(Paging paging, long toId) {
		List<CommentPO> list = commentDao.paging(paging, toId);
		paging.setMaxResults(99999);
		paging.setResults(null);
		int size=list.size();
		List<CommentPO> listTemp = new ArrayList<CommentPO>();
		for(int i=0;i<size;i++){
			CommentPO po =list.get(i);
			List<CommentPO>  listTemp2 = commentDao.paging(paging,po.getId(),toId);
			//list.addAll(listTemp2);
			listTemp.add(po);
			listTemp.addAll(listTemp2);
		}
		
		List<Comment> rets = new ArrayList<Comment>();
		for (CommentPO po : listTemp) {
			rets.add(BeanMapUtils.copy(po));
		}
		paging.setResults(rets);
	}
	
	@Override
	@Transactional
	public long post(Comment comment) {
		CommentPO po = new CommentPO();
		
		po.setAuthor(userDao.get(comment.getAuthorId()));
		po.setToId(comment.getToId());
		po.setContent(comment.getContent());
		po.setCreated(new Date());
		po.setToPid(comment.getToPid());
		commentDao.save(po);
		
		return po.getId();
	}

	@Override
	@Transactional
	public void delete(List<Long> ids) {
		for (Long id : ids) {
			commentDao.deleteById(id);
		}
	}

}
