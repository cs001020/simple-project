package com.chen.mapper;

import com.chen.entity.OperLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 操作日志(OperLog)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-03 17:52:40
 */
public interface OperLogDao {

    /**
     * 通过ID查询单条数据
     *
     * @param operId 主键
     * @return 实例对象
     */
    OperLog queryById(Integer operId);

    /**
     * 查询指定行数据
     *
     * @param operLog 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<OperLog> queryAllByLimit(OperLog operLog, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param operLog 查询条件
     * @return 总行数
     */
    long count(OperLog operLog);

    /**
     * 新增数据
     *
     * @param operLog 实例对象
     * @return 影响行数
     */
    int insert(OperLog operLog);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<OperLog> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<OperLog> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<OperLog> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<OperLog> entities);

    /**
     * 修改数据
     *
     * @param operLog 实例对象
     * @return 影响行数
     */
    int update(OperLog operLog);

    /**
     * 通过主键删除数据
     *
     * @param operId 主键
     * @return 影响行数
     */
    int deleteById(Integer operId);

}

