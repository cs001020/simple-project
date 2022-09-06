package com.chen.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.chen.configuration.Constant;
import com.chen.configuration.RedisTemplate;
import com.chen.entity.LoginUser;
import com.chen.entity.Menu;
import com.chen.entity.Role;
import com.chen.entity.User;
import com.chen.exception.UserNotFoundException;
import com.chen.exception.UserPasswordInaccuracyException;
import com.chen.mapper.UserDao;
import com.chen.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户信息表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-09-03 17:52:42
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    @Override
    public User queryById(Long userId) {
        return this.userDao.queryById(userId);
    }

    /**
     * 分页查询
     *
     * @param user 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<User> queryByPage(User user, PageRequest pageRequest) {
        long total = this.userDao.count(user);
        return new PageImpl<>(this.userDao.queryAllByLimit(user, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User insert(User user) {
        this.userDao.insert(user);
        return user;
    }

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User update(User user) {
        this.userDao.update(user);
        return this.queryById(user.getUserId());
    }

    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long userId) {
        return this.userDao.deleteById(userId) > 0;
    }

    @Override
    public LoginUser login(String userName, String password) {
        //登陆，使用用户名查询用户是否存在
        User user = userDao.queryByUserName(userName);
        if(user==null){
            throw new UserNotFoundException("用户：｛"+userName+"}未找到！");
        }
        //存在用户，比较密码
        if (!password.equals(user.getPassword())){
            throw new UserPasswordInaccuracyException("用户：｛"+userName+"}密码匹配！");
        }
        //认证成功
        //生成token
        String token = UUID.randomUUID().toString();
        //获取操作系统，浏览器
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        UserAgent userAgent=new UserAgent(request.getHeader("User-Agent"));
        //获取登陆地址
        String body = HttpRequest.get("https://whois.pconline.com.cn/ipJson.jsp?ip="+request.getRemoteHost()+"&json=true").execute().body();
        JSONObject entries = JSONUtil.parseObj(body);
        //封装loginUser存入redis
        LoginUser loginUser = LoginUser.builder()
                .user(user)
                .userId(user.getUserId())
                .loginTime(new Date())
                .token(token)
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .loginLocation((String) entries.get("addr"))
                .ipaddr((String) entries.get("ip"))
                .build();
        //根据用户名字生成key前缀
        String keyPrefix = Constant.TOKEN_KEY + userName+":";
        //查询key是否存在
        Set<String> keys = redisTemplate.keys(keyPrefix + "*");
        //存在时删除
        if (keys!=null&&keys.size()!=0){
            keys.forEach(key->redisTemplate.remove(key));
        }
        //新增登陆用户
        redisTemplate.setObj(keyPrefix+token,loginUser,Constant.TOKEN_TIME);
        return loginUser;
    }

    @Override
    public void logOut() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        //redis删除token
        String token = request.getHeader(Constant.HAND_AUTHORIZATION);
        Set<String> keys1 = redisTemplate.keys(Constant.TOKEN_KEY + "*" + token);
        Set<String> keys2 = redisTemplate.keys(Constant.PERM_PREFIX + "*" + token);
        Set<String> keys3 = redisTemplate.keys(Constant.ROLE_PREFIX + "*" + token);
        String key1 = (String) keys1.toArray()[0];
        String key2 = (String) keys2.toArray()[0];
        String key3 = (String) keys3.toArray()[0];
        redisTemplate.remove(key1);
        redisTemplate.remove(key2);
        redisTemplate.remove(key3);
    }

    @Override
    public Map<String, List<String>> getInfo() {
        //获取登陆用户
        LoginUser loginUser = getLoginUser();
        //获取登陆用户的信息Permissions
        User info = userDao.getInfo(loginUser.getUserId());
        //删除
        Set<String> keys = redisTemplate.keys(Constant.ROLE_PREFIX +info.getUserName()+":"+"*");
        Set<String> keys1 = redisTemplate.keys(Constant.PERM_PREFIX +info.getUserName()+":"+"*" );
        if (keys!=null&&keys.size()!=0){
            keys.forEach(redisTemplate::remove);
        }
        if (keys1!=null&&keys1.size()!=0){
            keys1.forEach(redisTemplate::remove);
        }
        //处理权限和角色相关信息
        List<String> roleTags = info.getRoles().stream().map(Role::getRoleTag).collect(Collectors.toList());
        redisTemplate.setObj(Constant.ROLE_PREFIX+info.getUserName()+":"+loginUser.getToken(),roleTags,Constant.TOKEN_TIME);
        List<String> prems=new ArrayList<>();
        info.getRoles().stream().map(Role::getMenus).forEach(menus -> {
            prems.addAll(menus.stream().map(Menu::getPerms).collect(Collectors.toList()));
        });
        redisTemplate.setObj(Constant.PERM_PREFIX+info.getUserName()+":"+loginUser.getToken(),prems,Constant.TOKEN_TIME);
        //整合数据
        Map<String,List<String>> data=new HashMap<>();
        data.put("roles",roleTags);
        data.put("perms",prems);
        return data;
    }
    private LoginUser getLoginUser(){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String token = request.getHeader(Constant.HAND_AUTHORIZATION);
        if (token==null){
            throw new RuntimeException("当前用户未登陆");
        }
        //使用token去redis中查看有没对应的key
        Set<String> keys = redisTemplate.keys(Constant.TOKEN_KEY + "*" + token);
        if (keys==null||keys.size()==0){
           throw new RuntimeException("当前用户未登陆");
        }
        String key = (String) keys.toArray()[0];
        //使用token去redis中查看有没对应的loginUser
        LoginUser loginUser = redisTemplate.getObj(key, new TypeReference<LoginUser>() {});
        if (loginUser==null){
            throw new RuntimeException("当前用户未登陆");
        }
        return loginUser;
    }
}
