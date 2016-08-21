package socns.persist.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mtons.modules.exception.MtonsException;
import mtons.modules.lang.Const;
import mtons.modules.lang.EntityStatus;
import mtons.modules.pojos.Paging;
import mtons.modules.utils.MD5Helper;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import socns.data.AccountProfile;
import socns.data.User;
import socns.persist.dao.STUserDao;
import socns.persist.dao.UserDao;
import socns.persist.entity.STUserPO;
import socns.persist.entity.UserPO;
import socns.persist.service.UserService;
import socns.utils.BeanMapUtils;

public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;

	@Autowired
	private STUserDao stUserDao;
	
	@Override
	@Transactional(readOnly = true)
	public AccountProfile login(String username, String password) {
		UserPO po = userDao.get(username);
		AccountProfile u = null;
		if (po != null) {
			if (po.getStatus() == Const.STATUS_CLOSED) {
				throw new MtonsException("您的账户已被封禁");
			}
			if (StringUtils.equals(po.getPassword(), password)) {
				po.setLastLogin(Calendar.getInstance().getTime());
				u = BeanMapUtils.copyPassport(po);
			}
		}
		return u;
	}

	@Override
	@Transactional
	public void register(User user) {
		Assert.notNull(user, "Parameter user can not be null!");
		
		Assert.hasLength(user.getUsername(), "用户名不能为空!");
		Assert.hasLength(user.getPassword(), "密码不能为空!");
		
		UserPO check = userDao.get(user.getUsername());
		Assert.isNull(check, "用户名已经存在!");
		
		UserPO po = new UserPO();
		
		BeanUtils.copyProperties(user, po);
		
		Date current = Calendar.getInstance().getTime();
		po.setPassword(MD5Helper.md5(user.getPassword()));
		po.setStatus(EntityStatus.ENABLED);
		po.setCreated(current);
		po.setUpdated(current);
		userDao.save(po);
	}

	@Override
	@Transactional
	public AccountProfile update(User user) {
		UserPO po = userDao.get(user.getId());
		if (null != po) {
			po.setEmail(user.getEmail());
			po.setName(user.getName());
		}
		
		return BeanMapUtils.copyPassport(po);
	}
	
	@Override
	@Transactional
	public User get(long id) {
		UserPO po = userDao.get(id);
		User ret = null;
		if (po != null) {
			ret = BeanMapUtils.copy(po);
		}
		return ret;
	}
	
	@Override
	@Transactional
	public User get(String username) {
		UserPO po = userDao.get(username);
		User ret = null;
		if (po != null) {
			ret = BeanMapUtils.copy(po);
		}
		return ret;
	}
	
	@Override
	@Transactional
	public AccountProfile updateAvatar(long id, String path) {
		UserPO po = userDao.get(id);
		if (po != null) {
			po.setAvatar(path);
			po.setUpdated(new Date());
		}
		return BeanMapUtils.copyPassport(po);
	}
	
	@Override
	@Transactional
	public void updatePassword(long id, String newPassword) {
		UserPO po = userDao.get(id);
		
		Assert.hasLength(newPassword, "密码不能为空!");
		
		if (null != po) {
			po.setPassword(MD5Helper.md5(newPassword));
		}
	}
	
	@Override
	@Transactional
	public void updatePassword(long id, String oldPassword, String newPassword) {
		UserPO po = userDao.get(id);
		
		Assert.hasLength(newPassword, "密码不能为空!");
		
		if (po != null) {
			Assert.isTrue(MD5Helper.md5(oldPassword).equals(po.getPassword()), "当前密码不正确");
			po.setPassword(MD5Helper.md5(newPassword));
		}
	}
	
	@Override
	@Transactional
	public void updateStatus(long id, int status) {
		UserPO po = userDao.get(id);
		
		if (po != null) {
			po.setStatus(status);
		}
	}
	
	@Override
	@Transactional
	public void updateRole(long id, int roleId) {
		UserPO po = userDao.get(id);
		
		if (po != null) {
			po.setRoleId(roleId);
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public void paging(Paging paging, String key) {
		List<UserPO> list = userDao.paging(paging, key);
		List<User> rets = new ArrayList<User>();
		
		for (UserPO po : list) {
			User u = BeanMapUtils.copy(po);
			rets.add(u);
		}
		paging.setResults(rets);
	}

	@Override
	public void updateExtend(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional(readOnly = true)
	public STUserPO getSTUser(String mobile) {
		return stUserDao.get(mobile);
	}
}
