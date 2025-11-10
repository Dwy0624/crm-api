package com.crm.service;

import com.crm.common.result.PageResult;
import com.crm.entity.Contract;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.query.ContractQuery;
import com.crm.query.ContractTrendQuery;
import com.crm.vo.ContractTrendPieVO;
import com.crm.vo.ContractVO;

import java.rmi.ServerException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author crm
 * @since 2025-10-12
 */
public interface ContractService extends IService<Contract> {
    //合同列表
    PageResult<ContractVO> getPage(ContractQuery query);
    void saveOrUpdate(ContractVO contractVO) throws ServerException;

    // 新增：按合同状态统计饼图数据
    List<ContractTrendPieVO> getContractStatusPieData();
}