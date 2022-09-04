package com.chen.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * 角色和菜单关联表(RoleMenu)实体类
 *
 * @author makejava
 * @since 2022-09-03 17:52:41
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private  int page;
    private int size;
    private Sort sort;
}

