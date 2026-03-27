package org.main.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.main.entity.SystemUser;
import org.main.mapper.SystemUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SysUserDetailsService implements UserDetailsService {

    @Autowired
    private SystemUserMapper systemUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!StringUtils.hasText(username)) {
            throw new UsernameNotFoundException("用户名不能为空");
        }

        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUser::getUserno, username);
        SystemUser systemUser = systemUserMapper.selectOne(queryWrapper);
        if (systemUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        if (systemUser.getStatus() != null && systemUser.getStatus() != 1) {
            throw new UsernameNotFoundException("用户已禁用");
        }

        return new User(systemUser.getUserno(), systemUser.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
