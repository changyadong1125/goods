package com.goods.system.service;


import com.goods.common.error.SystemException;
import com.goods.common.model.system.Menu;
import com.goods.common.vo.system.MenuNodeVO;
import com.goods.common.vo.system.MenuVO;

import java.util.List;

public interface MenuService {
    /**
     * 获取菜单树
     * @return
     */
    List<MenuNodeVO> findMenuTree();

    /**
     * 添加菜单
     * @param menuVO
     */
    Menu add(MenuVO menuVO);

    /**
     * 删除节点
     * @param id
     */
    void delete(Long id) throws SystemException;

    /**
     * 编辑节点
     * @param id
     * @return
     */
    MenuVO edit(Long id) throws SystemException;

    /**
     * 更新节点
     * @param id
     */
    void update(Long id, MenuVO menuVO) throws SystemException;

    /**
     * 所有展开菜单的ID
     * @return
     */
    List<Long> findOpenIds();


    /**
     * 获取所有菜单
     * @return
     */
    List<Menu> findAll();

}
