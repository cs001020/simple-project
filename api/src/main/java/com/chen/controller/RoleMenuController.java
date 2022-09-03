package com.chen.controller;

import com.chen.entity.RoleMenu;
import com.chen.service.RoleMenuService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 角色和菜单关联表(RoleMenu)表控制层
 *
 * @author makejava
 * @since 2022-09-03 17:52:41
 */
@RestController
@RequestMapping("roleMenu")
public class RoleMenuController {
    /**
     * 服务对象
     */
    @Resource
    private RoleMenuService roleMenuService;

    /**
     * 分页查询
     *
     * @param roleMenu 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<RoleMenu>> queryByPage(RoleMenu roleMenu, PageRequest pageRequest) {
        return ResponseEntity.ok(this.roleMenuService.queryByPage(roleMenu, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<RoleMenu> queryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.roleMenuService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param roleMenu 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<RoleMenu> add(RoleMenu roleMenu) {
        return ResponseEntity.ok(this.roleMenuService.insert(roleMenu));
    }

    /**
     * 编辑数据
     *
     * @param roleMenu 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<RoleMenu> edit(RoleMenu roleMenu) {
        return ResponseEntity.ok(this.roleMenuService.update(roleMenu));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Long id) {
        return ResponseEntity.ok(this.roleMenuService.deleteById(id));
    }

}

