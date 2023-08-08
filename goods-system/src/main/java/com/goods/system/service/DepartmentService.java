package com.goods.system.service;


import com.goods.common.error.SystemException;
import com.goods.common.model.system.Department;
import com.goods.common.vo.system.DeanVO;
import com.goods.common.vo.system.DepartmentVO;
import com.goods.common.vo.system.PageVO;

import java.util.List;


public interface DepartmentService {
    /**
     * 部门列表
     * @param pageNum
     * @param pageSize
     * @param departmentVO
     * @return
     */
    PageVO<DepartmentVO> findDepartmentList(Integer pageNum, Integer pageSize, DepartmentVO departmentVO);

    /**
     * 查询所有部门主任
     * @return
     */
    List<DeanVO> findDeanList();

    /**
     * 添加院部门
     * @param departmentVO
     */
    void add(DepartmentVO departmentVO);

    /**
     * 编辑院部门
     * @param id
     * @return
     */
    DepartmentVO edit(Long id) throws SystemException;

    /**
     * 更新院部门
     * @param id
     * @param departmentVO
     */
    void update(Long id, DepartmentVO departmentVO) throws SystemException;

    /**
     * 删除院部门
     * @param id
     */
    void delete(Long id) throws SystemException;

    /**
     * 所有部门
     * @return
     */
    List<DepartmentVO> findAllVO();


    /**
     * 全部部门
     * @return
     */
    List<Department> findAll();

}
