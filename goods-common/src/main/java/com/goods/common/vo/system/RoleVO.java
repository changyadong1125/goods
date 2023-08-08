package com.goods.common.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;


@Data
public class RoleVO {

    private Long id;

    @NotBlank(message = "角色名必填")
    private String roleName;

    @NotBlank(message = "角色描述信息必填")
    private String remark;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;

    private Date modifiedTime;

    private Boolean status;
}
