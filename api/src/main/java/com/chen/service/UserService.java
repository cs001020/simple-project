package com.chen.service;

import com.chen.entity.LoginUser;
import com.chen.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 用户信息表(User)表服务接口
 *
 * @author makejava
 * @since 2022-09-03 17:52:41
 */
public interface UserService {

    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    User queryById(Long userId);

    /**
     * 分页查询
     *
     * @param user 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<User> queryByPage(User user, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    User insert(User user);

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    User update(User user);

    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 是否成功
     */
    boolean deleteById(Long userId);

    /**
     * 登陆认证
     * @param userName
     * @param password
     * @return
     */
    LoginUser login(String userName, String password);

    /**
     * 退出登陆
     */
    void logOut();

    /**
     * @return 获取用户的所用信息
     */
    Map<String, List<String>> getInfo();
}
