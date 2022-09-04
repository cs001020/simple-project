package com.chen.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.chen.configuration.Constant;
import com.chen.configuration.RedisTemplate;
import com.chen.entity.LoginUser;
import com.chen.entity.User;
import com.chen.exception.UserNotFoundException;
import com.chen.exception.UserPasswordInaccuracyException;
import com.chen.mapper.UserDao;
import com.chen.service.UserService;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

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
        redisTemplate.setObj(Constant.TOKEN_KEY+token,loginUser,Constant.TOKEN_TIME);
        return loginUser;
    }

    @Override
    public void logOut() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        //redis删除token
        Long remove = redisTemplate.remove(Constant.TOKEN_KEY + request.getHeader(Constant.HAND_AUTHORIZATION));
    }
}
