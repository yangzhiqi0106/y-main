package org.main.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        String formatted = now.format(FORMATTER);
        strictInsertFill(metaObject, "createTime", String.class, formatted);
        strictInsertFill(metaObject, "updateTime", String.class, formatted);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        strictUpdateFill(metaObject, "updateTime", String.class, LocalDateTime.now().format(FORMATTER));
    }
}
