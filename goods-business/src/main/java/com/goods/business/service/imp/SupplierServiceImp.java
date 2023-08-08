package com.goods.business.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.business.mapper.SupplierMapper;
import com.goods.business.service.SupplierService;
import com.goods.common.model.business.Supplier;
import com.goods.common.vo.business.SupplierVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * project:goods
 * package:com.goods.business.service.imp
 * class:SupplierServiceImp
 *
 * @author: smile
 * @create: 2023/8/7-15:57
 * @Version: v1.0
 * @Description:
 */
@Service
public class SupplierServiceImp implements SupplierService {
    @Resource
    private SupplierMapper supplierMapper;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据条件查询物资来源
     */

    @Override
    public PageVO<Supplier> findSupplierList(Integer pageNum, Integer pageSize, SupplierVO supplierVO) {
        //设置分页
        PageHelper.startPage(pageNum, pageSize);
        //封装条件查询数据
        Example E = new Example(Supplier.class);
        Example.Criteria C = E.createCriteria();
        if (null != supplierVO && !StringUtils.isEmpty(supplierVO.getName())) {
            C.andLike("name", "%" + supplierVO.getName() + "%");
        }
        if (null != supplierVO && !StringUtils.isEmpty(supplierVO.getContact())) {
            C.andLike("contact", "%" + supplierVO.getContact() + "%");
        }
        if (null != supplierVO && !StringUtils.isEmpty(supplierVO.getAddress())) {
            C.andLike("address", "%" + supplierVO.getAddress() + "%");
        }
        List<Supplier> supplierList = supplierMapper.selectByExample(E);
        PageInfo<Supplier> supplierPageInfo = new PageInfo<>(supplierList);
        return new PageVO<>(supplierPageInfo.getTotal(), supplierList);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:添加物资来源
     */
    @Override
    public void addSupplier(Supplier supplier) {
        supplier.setCreateTime(new Date());
        supplier.setCreateTime(new Date());
        supplierMapper.insert(supplier);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回显物资来源
     */
    @Override
    public Supplier findSupplierById(Long id) {
        return supplierMapper.selectByPrimaryKey(id);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:修改物资来源
     */
    @Override
    public void update(Long id, Supplier supplier) {
        supplier.setModifiedTime(new Date());
        supplierMapper.updateByPrimaryKey(supplier);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:删除物资来源
     */
    @Override
    public void delete(Long id) {
        supplierMapper.deleteByPrimaryKey(id);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:查询所有
     */
    @Override
    public List<Supplier> findAll() {
        return supplierMapper.selectAll();
    }
}
