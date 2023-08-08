package com.goods.system.service;


import com.goods.common.error.SystemException;
import com.goods.common.model.system.Role;
import com.goods.common.vo.system.PageVO;
import com.goods.common.vo.system.RoleVO;

import java.util.List;


public interface RoleService {

    /**
     * 角色列表
     * @param pageNum
     * @param pageSize
     * @param roleVO
     * @return
     */
    PageVO<RoleVO> findRoleList(Integer pageNum, Integer pageSize, RoleVO roleVO);

    /**
     * 添加角色
     * @param roleVO
     */
    void add(RoleVO roleVO) throws SystemException;

    /**
     * 删除角色
     * @param id
     */
    void deleteById(Long id) throws SystemException;

    /**
     * 编辑角色
     * @param id
     * @return
     */
    RoleVO edit(Long id) throws SystemException;

    /**
     * 更新角色
     * @param id
     * @param roleVO
     */
    void update(Long id, RoleVO roleVO) throws SystemException;

    /**
     * 根据角色状态
     * @param id
     * @param status
     */
    void updateStatus(Long id, Boolean status) throws SystemException;

    /**
     * 查询所有的角色
     * @return
     */
    List<Role> findAll();

    /**
     * 查询角色拥有的菜单权限id
     * @param id
     * @return
     */
    List<Long> findMenuIdsByRoleId(Long id) throws SystemException;

    /**
     * 角色授权
     * @param mids
     */
    void authority(Long id,Long[] mids) throws SystemException;
}
