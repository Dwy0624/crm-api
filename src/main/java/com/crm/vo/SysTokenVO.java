package com.crm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author crm
 */
// 修改 SysTokenVO.java，新增部门相关字段
@Data
@AllArgsConstructor
@Schema(description = "用户登录响应")
public class SysTokenVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "access_token")
    private String access_token;

    @Schema(description = "用户所属部门ID")
    private Integer deptId;

    @Schema(description = "用户所属部门名称")
    private String deptName;
}
