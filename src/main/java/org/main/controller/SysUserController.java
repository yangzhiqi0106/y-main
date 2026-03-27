package org.main.controller;

import org.main.entity.SystemUser;
import org.main.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;

    @RequestMapping(value = "/queryAll")
    public List<SystemUser> queryAll() {
        return sysUserService.list();
    }

    @RequestMapping(value = "/queryByPage")
    public List<SystemUser> queryByPage() {
        return sysUserService.list();
    }

}
