package org.main.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.main.entity.SystemUser;
import org.main.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;

    @GetMapping
    public List<SystemUser> queryAll() {
        return sysUserService.list();
    }

    @GetMapping(value = "/page")
    public IPage<SystemUser> queryByPage(@RequestParam(defaultValue = "1") long current,
                                         @RequestParam(defaultValue = "10") long size,
                                         @RequestParam(required = false) String username) {
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(username)) {
            queryWrapper.like(SystemUser::getUsername, username);
        }
        queryWrapper.orderByDesc(SystemUser::getId);
        return sysUserService.page(new Page<>(current, size), queryWrapper);
    }

    @GetMapping(value = "/{id}")
    public SystemUser queryById(@PathVariable Long id) {
        return sysUserService.getById(id);
    }

    @PostMapping
    public boolean save(@RequestBody SystemUser systemUser) {
        return sysUserService.saveUser(systemUser);
    }

    @PutMapping(value = "/{id}")
    public boolean update(@PathVariable Long id, @RequestBody SystemUser systemUser) {
        return sysUserService.updateUser(id, systemUser);
    }

    @DeleteMapping(value = "/{id}")
    public boolean delete(@PathVariable Long id) {
        return sysUserService.removeById(id);
    }

}
