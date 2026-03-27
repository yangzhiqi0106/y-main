package org.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.main.entity.SystemUser;
import org.main.mapper.SystemUserMapper;
import org.main.service.ISysUserService;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements ISysUserService {
}
