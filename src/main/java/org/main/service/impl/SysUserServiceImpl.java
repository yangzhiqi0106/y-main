package org.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.main.entity.SystemUser;
import org.main.mapper.SystemUserMapper;
import org.main.service.ISysUserService;
import org.main.util.GmCryptoUtil;
import org.main.vo.LoginUserVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SysUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements ISysUserService {

    @Override
    public boolean saveUser(SystemUser systemUser) {
        systemUser.setId(null);
        encodePassword(systemUser);
        return this.save(systemUser);
    }

    @Override
    public boolean updateUser(Long id, SystemUser systemUser) {
        systemUser.setId(id);
        encodePassword(systemUser);
        return this.updateById(systemUser);
    }

    private void encodePassword(SystemUser systemUser) {
        if (StringUtils.hasText(systemUser.getPassword())) {
            systemUser.setPassword(GmCryptoUtil.sm3Hex(systemUser.getPassword()));
        }
    }
}
