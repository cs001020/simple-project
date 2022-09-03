package com.chen.service;

import com.chen.entity.RoleMenu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 角色和菜单关联表(RoleMenu)表服务接口
 *
 * @author makejava
 * @since 2022-09-03 17:52:41
 */
public interface RoleMenuService {

    /**
     * 通过ID查询单条数据
     *
     * @param roleId 主键
     * @return 实例对象
     */
    RoleMenu queryById(Long roleId);

    /**
     * 分页查询
     *
     * @param roleMenu 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<RoleMenu> queryByPage(RoleMenu roleMenu, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param roleMenu 实例对象
     * @return 实例对象
     */
    RoleMenu insert(RoleMenu roleMenu);

    /**
     * 修改数据
     *
     * @param roleMenu 实例对象
     * @return 实例对象
     */
    RoleMenu update(RoleMenu roleMenu);

    /**
     * 通过主键删除数据
     *
     * @param roleId 主键
     * @return 是否成功
     */
    boolean deleteById(Long roleId);

}
