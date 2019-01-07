package com.github.willisaway.user.service;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.willisaway.core.base.BaseService;
import com.github.willisaway.core.base.ModuleReturn;
import com.github.willisaway.core.support.Assert;
import com.github.willisaway.core.support.login.ThirdPartyUser;
import com.github.willisaway.core.util.RedissonUtil;
import com.github.willisaway.user.api.SmUserClient;
import com.github.willisaway.user.dao.SmUserMapper;
import com.github.willisaway.user.dao.SysUserThirdpartyMapper;
import com.github.willisaway.user.model.SmRole;
import com.github.willisaway.user.model.SmUser;
import com.github.willisaway.user.model.SmUserRole;
import com.github.willisaway.user.model.SysUserThirdparty;

/**
 * SysUser服务实现类
 *
 * @author ShenHuaJie
 * @version 2016-08-27 22:39:42
 */
@Service
@CacheConfig(cacheNames = "SmUser")
public class SmUserService extends BaseService<SmUserMapper, SmUser> {
    @Autowired
    private SysUserThirdpartyMapper thirdpartyMapper;
//    @Autowired
//    private SysDicClient sysDicProvider;
//    @Autowired
//    private SmOrgClient mdDeptClient;
    @Autowired
    private SmUserMapper sysUserMapper;
    @Autowired
    SysAuthorizeService sysAuthorizeService;
    @Autowired
	SmUserClient userClient;
    @Autowired
    SmUserRoleService smUserRoleService;
    @Autowired
    SmRoleService smRoleService;
    
    public Page<SmUser> query(Map<String, Object> params) {
        Page<Long> page = getPage(params);
        page.setRecords(baseMapper.selectIdByMap(page, params));
        return getPage(page);
    }

    @Transactional
    public void register(SmUser sysUser) {
    	sysUser.setPassword(encryptPassword(sysUser.getPassword()));
		SmUserRole smUserRole = new SmUserRole();
		smUserRole.setUserId(sysUser.getRowId());
		//默认为1
		long roleId = 1;
		smUserRole.setRoleId(roleId);
		smUserRoleService.update(smUserRole);
    }
    /**
     * 修改用户信息
     */
    public void updateUserInfo(SmUser sysUser) {
        Assert.notNull(sysUser.getRowId(), "USER_ID");
        Assert.isNotBlank(sysUser.getUserCode(), "ACCOUNT");
        Assert.length(sysUser.getUserCode(), 3, 15, "ACCOUNT");
        SmUser user = this.queryById(sysUser.getRowId());
        Assert.notNull(user, "USER", sysUser.getRowId());
        if (StringUtils.isBlank(sysUser.getPassword())) {
            sysUser.setPassword(user.getPassword());
        }
        sysUser.setUpdateBy(userClient.getCurrentUserId());
        this.update(sysUser);
    }

    /**
     * 修改密码
     */
    public void updatePassword(Long id, String password) {
        Assert.notNull(id, "USER_ID");
        Assert.isNotBlank(password, "PASSWORD");
        SmUser sysUser = this.queryById(id);
        Assert.notNull(sysUser, "USER", id);
        //Long userId = userClient.getCurrentUserId();
//        if (!id.equals(userId)) {
//            SysUser user = this.queryById(userId);
//            if (user.getUserType() == 1) {
//                throw new UnauthorizedException();
//            }
//        }
        sysUser.setPassword(encryptPassword(password));
//       sysUser.setUpdateBy(userClient.getCurrentUserId());
        this.update(sysUser);
    }

    public SmUser queryByName(String loginName) {
        SmUser returnObj = null;
        Map<String, Object> params = new HashMap();
        params.put("loginName", loginName);
        List<SmUser> userList = queryAll(params);
        if (userList != null && userList.size() > 0) {
            returnObj = userList.get(0);
        }
        return returnObj;
    }

    /**
     * 查询第三方帐号用户Id
     */
    @Cacheable
    public Long queryUserIdByThirdParty(String openId, String provider) {
        return thirdpartyMapper.queryUserIdByThirdParty(provider, openId);
    }

    // 加密
    public String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);//SecurityUtil.encryptMd5(SecurityUtil.encryptSHA(password));
    }

    /**
     * 保存第三方帐号
     */
    @Transactional
    public SmUser insertThirdPartyUser(ThirdPartyUser thirdPartyUser) {
        SmUser sysUser = new SmUser();
        sysUser.setSex("1");
        sysUser.setUserType("1");
        sysUser.setPassword(this.encryptPassword("123456"));
        sysUser.setUserName(thirdPartyUser.getUserName());
        // 初始化第三方信息
        SysUserThirdparty thirdparty = new SysUserThirdparty();
        thirdparty.setProvider(thirdPartyUser.getProvider());
        thirdparty.setOpenId(thirdPartyUser.getOpenid());
        thirdparty.setCreateTime(new Date());

        this.update(sysUser);
        sysUser.setUserCode(thirdparty.getProvider() + sysUser.getRowId());
        this.update(sysUser);
        thirdparty.setUserId(sysUser.getRowId());
        thirdpartyMapper.insert(thirdparty);
        return sysUser;
    }

    public void init() {
        List<Long> list = sysUserMapper.selectIdByMap(Collections.<String, Object>emptyMap());
        for (Long id : list) {
            RedissonUtil.set(getCacheKey(id), baseMapper.selectById(id));
        }
    }

    public Set<GrantedAuthority> getAuthorities(Long userId) {
        Set<GrantedAuthority> userAuthotities = new HashSet<>();
//        List<String> list = sysAuthorizeService.queryPermissionByUserId(userId);
//        for (String permission : list) {
//            if (StringUtils.isNotBlank(permission)) {
//                // 添加基于Permission的权限信息
//                userAuthotities.add(new SimpleGrantedAuthority(permission));
//            }
//        }
//        // 添加用户角色权限
//        List<SmRole> roles = queryUserRoles(userId);
//        for(SmRole role:roles) {
//        	userAuthotities.add(new SimpleGrantedAuthority(role.getRoleCode()));
//        }
        userAuthotities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return userAuthotities;
    }

    /*
     * 查询用户角色
     */
    public List<SmRole> queryUserRoles(Long userId){
    	List<SmRole> roles = new ArrayList();
    	List<SmUserRole> smUserRoles = smUserRoleService.queryUserRoles(userId);
    	for(SmUserRole smUserRole:smUserRoles) {
    		SmRole role = smRoleService.queryById(smUserRole.getRoleId());
    		roles.add(role);
    	}
    	return roles;
    }

    /**
     * 查询用户的所有权限
     *
     * @param userId
     * @return
     */
    public List<String> queryAllPerms(Long userId) {
        return sysUserMapper.queryAllPerms(userId);
    }

    /**
     * 查询用户的所有菜单ID
     *
     * @param userId
     * @return
     */
    public List<Long> queryAllMenuId(Long userId) {
        return sysUserMapper.queryAllMenuId(userId);
    }

    /**
     * 根据userId和roleId保存中间表
     *
     * @param userId
     * @param roleId
     * @return
     */
    public int saveSmUserRoleByUserId(String userId, String roleId) {
        return sysUserMapper.updateBySql(userId, roleId);
    }


    /**
     * 根据用户名，保存角色权限
     *
     * @param roleIds
     * @return
     */
    public int saveGrant(String roleIds) {
        String roleId[] = roleIds.split(",");
        List<Map<String, String>> list = new ArrayList<>();
        for (String rid : roleId) {

        }
        return 1;
    }

    /**
     * 查询所有用户
     *
     * @return
     */
    public List<SmUser> queryUserList() {

        return sysUserMapper.queryUserList();
    }

    /**
     * 删除用户角色
     *
     * @param roleId
     * @param selectUserIds
     * @return
     */
    public Boolean deleteUseRole(String roleId, String selectUserIds) {
        return true;
    }

	public Page<SmUser> queryPage(Map<String, Object> param) {
		Page<SmUser> smUserList = this.query(param);
		for(SmUser smUser : smUserList.getRecords()){
			param.put("userId", smUser.getRowId());
			smUser.setSmUserRole(smUserRoleService.queryAll(param));
			for(SmUserRole smUserRole : smUser.getSmUserRole()){
				smUserRole.setUserRoleName(smRoleService.queryById(smUserRole.getRoleId()).getRoleName());
			}
		}
		return smUserList;
	}

	public ModuleReturn newPassword(String phone, String password, String msg,
			Long sendRowId) {
		ModuleReturn objRtn = new ModuleReturn(1);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("phone", phone);
		SmUser sysUser = this.queryOne(param);
		if(true){
			this.updatePassword(sysUser.getRowId(), password);
			objRtn.setReturnValue(1,"重置成功");
		}else{
			objRtn.setReturnValue(-1,"验证码错误");
		}
		return objRtn;
	}


}
