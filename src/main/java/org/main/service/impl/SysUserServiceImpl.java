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

    @Override
    public LoginUserVO login(String username, String password) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new IllegalArgumentException("用户名和密码不能为空");
        }

        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUser::getUsername, username);
        SystemUser systemUser = this.getOne(queryWrapper);
        if (systemUser == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        String encryptedPassword = GmCryptoUtil.sm3Hex(password);
        if (!encryptedPassword.equals(systemUser.getPassword())) {
            throw new IllegalArgumentException("密码错误");
        }

        LoginUserVO vo = new LoginUserVO();
        vo.setId(systemUser.getId());
        vo.setUserno(systemUser.getUserno());
        vo.setUsername(systemUser.getUsername());
        vo.setPhone(systemUser.getPhone());
        vo.setEmail(systemUser.getEmail());
        vo.setSex(systemUser.getSex());
        vo.setAvatar(systemUser.getAvatar());
        vo.setStatus(systemUser.getStatus());
        vo.setCreateTime(systemUser.getCreateTime());
        vo.setUpdateTime(systemUser.getUpdateTime());
        return vo;
    }

    private void encodePassword(SystemUser systemUser) {
        if (StringUtils.hasText(systemUser.getPassword())) {
            systemUser.setPassword(GmCryptoUtil.sm3Hex(systemUser.getPassword()));
        }
    }
}
