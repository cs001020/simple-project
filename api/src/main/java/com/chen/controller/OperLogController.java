package com.chen.controller;

import com.chen.entity.OperLog;
import com.chen.service.OperLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 操作日志(OperLog)表控制层
 *
 * @author makejava
 * @since 2022-09-03 17:52:40
 */
@RestController
@RequestMapping("operLog")
public class OperLogController {
    /**
     * 服务对象
     */
    @Resource
    private OperLogService operLogService;

    /**
     * 分页查询
     *
     * @param operLog 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<OperLog>> queryByPage(OperLog operLog, PageRequest pageRequest) {
        return ResponseEntity.ok(this.operLogService.queryByPage(operLog, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<OperLog> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.operLogService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param operLog 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<OperLog> add(OperLog operLog) {
        operLogService.insert(operLog);
        return ResponseEntity.ok(operLog);
    }

    /**
     * 编辑数据
     *
     * @param operLog 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<OperLog> edit(OperLog operLog) {
        return ResponseEntity.ok(this.operLogService.update(operLog));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.operLogService.deleteById(id));
    }

}

