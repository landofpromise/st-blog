package socns.shiro.realm;

import mtons.modules.lang.Const;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import socns.data.AccountProfile;
import socns.data.User;
import socns.lang.EnumRole;
import socns.persist.service.UserService;
import socns.shiro.authc.AccountAuthenticationInfo;

public class AccountRealm extends AuthorizingRealm {
    private UserService userService;

    public AccountRealm() {
        super(new AllowAllCredentialsMatcher());
        setAuthenticationTokenClass(UsernamePasswordToken.class);
        
        //FIXME: 暂时禁用Cache
        setCachingEnabled(false);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.fromRealm(getName()).iterator().next();
        if (username != null) {
            User user = userService.get(username);
            if (user != null){
                SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
                EnumRole role = EnumRole.getEnum(user.getRoleId());
                
                info.addRole(role.getKey());
//                for (Role r : user.getRoles()) {
//                    info.addRole(r.getName());
//                    ArrayList<String> ps = new ArrayList<String>();
//                    for(Permission p: r.getPermissions()){
//                        ps.add(p.getName());
//                    }
//                    
//                    info.addStringPermissions(ps);
//                }
                return info;
            }
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    	AccountProfile profile = getAccount(userService, token);
        
        if(profile.getStatus() != Const.STATUS_NORMAL){
            throw new LockedAccountException(profile.getName());
        }
        
        AccountAuthenticationInfo info = new AccountAuthenticationInfo(token.getPrincipal(), token.getCredentials(), getName());
        info.setProfile(profile);
        
        return info;
    }

    public void setUserService(UserService userService) {
		this.userService = userService;
	}

	protected AccountProfile getAccount(UserService userService, AuthenticationToken token){
        UsernamePasswordToken upToken = (UsernamePasswordToken)token;
        return userService.login(upToken.getUsername(), String.valueOf(upToken.getPassword()));
    }
}
