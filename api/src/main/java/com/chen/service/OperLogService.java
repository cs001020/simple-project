package com.chen.service;

import com.chen.entity.OperLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 操作日志(OperLog)表服务接口
 *
 * @author makejava
 * @since 2022-09-03 17:52:41
 */
public interface OperLogService {

    /**
     * 通过ID查询单条数据
     *
     * @param operId 主键
     * @return 实例对象
     */
    OperLog queryById(Integer operId);

    /**
     * 分页查询
     *
     * @param operLog 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<OperLog> queryByPage(OperLog operLog, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param operLog 实例对象
     * @return 实例对象
     */
    OperLog insert(OperLog operLog);

    /**
     * 修改数据
     *
     * @param operLog 实例对象
     * @return 实例对象
     */
    OperLog update(OperLog operLog);

    /**
     * 通过主键删除数据
     *
     * @param operId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer operId);

}
