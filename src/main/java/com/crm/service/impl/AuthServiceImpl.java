package com.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crm.common.exception.ServerException;
import com.crm.entity.Department;
import com.crm.entity.Manager;
import com.crm.mapper.DepartmentMapper;
import com.crm.mapper.ManagerMapper;
import com.crm.security.cache.TokenStoreCache;
import com.crm.security.user.ManagerDetail;
import com.crm.security.utils.TokenUtils;
import com.crm.service.AuthService;
import com.crm.vo.SysAccountLoginVO;
import com.crm.vo.SysTokenVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


/**
 * 认证服务实现
 *
 * @Author crm
 * @Date 2023-05-18 17:31
 */
@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final TokenStoreCache tokenStoreCache;
    private final AuthenticationManager authenticationManager;
    private final ManagerMapper managerMapper;
    private final DepartmentMapper departmentMapper;
    private final PasswordEncoder passwordEncoder;

    // AuthService.java 登录方法实现
    @Override
    public SysTokenVO loginByAccount(SysAccountLoginVO login) {
        // 1. 账号密码校验（原有逻辑）
        Manager manager = managerMapper.selectOne(new LambdaQueryWrapper<Manager>()
                .eq(Manager::getAccount, login.getAccount())
                .eq(Manager::getDeleteFlag, 0));
        if (manager == null || !passwordEncoder.matches(login.getPassword(), manager.getPassword())) {
            throw new ServerException("账号或密码错误");
        }
        if (manager.getStatus() == 0) {
            throw new ServerException("账号已停用");
        }

        // 2. 获取部门信息
        Department dept = departmentMapper.selectById(manager.getDepartId());
        if (dept == null) {
            throw new ServerException("用户未分配部门");
        }

        // 3. 构建用户详情并缓存
        ManagerDetail managerDetail = new ManagerDetail();
        BeanUtils.copyProperties(manager, managerDetail);
        managerDetail.setDeptId(Long.valueOf(dept.getId()));
        managerDetail.setDeptName(dept.getName());
        managerDetail.setDeptParentIds(dept.getParentIds());

        String token = UUID.randomUUID().toString();
        tokenStoreCache.saveUser(token, managerDetail);

        // 4. 返回包含部门信息的token对象
        return new SysTokenVO(token, dept.getId(), dept.getName());
    }

    @Override
    public void logout(String accessToken) {
        // 用户信息
        ManagerDetail manager = tokenStoreCache.getUser(accessToken);

        // 删除用户信息
        tokenStoreCache.deleteUser(accessToken);

    }

}
