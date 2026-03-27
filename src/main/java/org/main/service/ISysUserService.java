package org.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.main.entity.SystemUser;

public interface ISysUserService extends IService<SystemUser> {

    boolean saveUser(SystemUser systemUser);

    boolean updateUser(Long id, SystemUser systemUser);
}
