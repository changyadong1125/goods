package com.goods.system.converter;

import com.goods.common.model.system.Department;
import com.goods.common.vo.system.DepartmentVO;
import org.springframework.beans.BeanUtils;


public class DepartmentConverter {


    /**
     * 转vo
     * @return
     */
    public static DepartmentVO converterToDepartmentVO(Department department){
        DepartmentVO departmentVO = new DepartmentVO();
        BeanUtils.copyProperties(department,departmentVO);
        return departmentVO;
    }
}
