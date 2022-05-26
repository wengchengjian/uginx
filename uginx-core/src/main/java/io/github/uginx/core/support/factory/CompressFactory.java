package io.github.uginx.core.support.factory;

import io.github.uginx.core.constants.CompressEnum;
import io.github.uginx.core.utils.ApplicationContextUtil;
import io.github.uginx.core.utils.compress.Compress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 压缩器工厂
 * @Author 翁丞健
 * @Date 2022/4/28 23:09
 * @Version 1.0.0
 */
@Slf4j
public class CompressFactory {

    private final ConcurrentHashMap<Byte, Compress> compressMap = new ConcurrentHashMap<>();

    @Autowired
    private ApplicationContextUtil applicationContextUtil;

    @PostConstruct
    public void init(){
        ApplicationContext context = applicationContextUtil.getContext();

        Map<String, Compress> allCompresses = context.getBeansOfType(Compress.class);

        for(Compress compress : allCompresses.values()){
            CompressEnum compressType = compress.getCompressType();
            if(compressType == null){
                log.error("错误的压缩类型");
                throw new RuntimeException("错误的压缩类型");
            }
            byte key = (byte) compressType.getCode();
            if(compressMap.containsKey(key)){
                log.error("存在多个压缩器实现了{}压缩器",compressType.getName());
                throw new IllegalArgumentException("存在多个压缩器实现了同一种类型的压缩器");
            }
            compressMap.put(key,compress);
        }
    }

    public final Compress getInstance(String name){
        return compressMap.get(Objects.requireNonNull(CompressEnum.getEnumByName(name)).getCode());
    }

    public final Compress getInstance(CompressEnum codec){
        return compressMap.get(codec.getCode());
    }

    public final Compress getInstance(byte code){
        return compressMap.get(code);
    }
}
