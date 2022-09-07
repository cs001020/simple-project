package com.chen.controller;

import com.chen.annotation.Log;
import com.chen.annotation.Permissions;
import com.chen.annotation.Repeat;
import com.chen.annotation.Roles;
import com.chen.entity.User;
import com.chen.service.UserService;
import com.chen.util.DeleteFlagEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户信息表(User)表控制层
 *
 * @author makejava
 * @since 2022-09-03 17:52:41
 */
@RestController
@RequestMapping("user")
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    /**
     * 分页查询
     *
     * @param user 筛选条件
     * @return 查询结果
     */
    @GetMapping
    @Log(title = "查询用户",businessType = "用户操作")
    @Repeat(1)
    public ResponseEntity<Page<User>> queryByPage(User user) {
        return ResponseEntity.ok(this.userService.queryByPage(user,PageRequest.of(user.getPage(),user.getSize())));
    }
    @GetMapping("/getInfo")
    public ResponseEntity<Map<String, List<String>>> getInfo() {
        return ResponseEntity.ok(this.userService.getInfo());
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @Permissions("system:user:query")
    @Roles("admin")
    public ResponseEntity<User> queryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.userService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param user 实体
     * @return 新增结果
     */
    @PostMapping
    @Log(title="创建用户",businessType="用户操作")
    public ResponseEntity<User> add(@RequestBody User user, HttpServletRequest request) {
        user.setLoginIp(request.getRemoteHost());
        user.setCreateTime(new Date());
        user.setCreateBy("admin");
        user.setStatus("");
        user.setDelFlag(DeleteFlagEnum.NO.getValue());

        return ResponseEntity.ok(this.userService.insert(user));
    }

    /**
     * 编辑数据
     *
     * @param user 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<User> edit(@RequestBody User user) {
        return ResponseEntity.ok(this.userService.update(user));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.userService.deleteById(id));
    }

}

