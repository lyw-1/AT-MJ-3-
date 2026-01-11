package com.mold.digitalization.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * MyBatis Plus配置绫?
 * 配置MyBatis Plus鐨勬彃浠跺拰鍏ㄥ眬配置锛岃В鍐砽ambda缂撳瓨闂
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * MyBatis Plus鎷︽埅鍣ㄩ厤缃?
     * 配置鍒嗛〉鎻掍欢鍜屼箰瑙傞攣鎻掍欢
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 鍒嗛〉鎻掍欢
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setMaxLimit(1000L);
        paginationInnerInterceptor.setOverflow(true);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        
        // 涔愯閿佹彃浠?
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        
        return interceptor;
    }

    /**
     * 鍏ㄥ眬配置
     * 配置ID鐢熸垚鍣ㄣ€佺紦瀛樿缃拰Lambda琛ㄨ揪开忕紦瀛樻敮鎸?
     */
    @Bean
    @Primary
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        
        // 配置ID鐢熸垚鍣?
        globalConfig.setIdentifierGenerator(new DefaultIdentifierGenerator());
        
        // 配置数据搴撻厤缃?
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        // 设置琛ㄥ悕涓嬪垝绾胯浆椹煎嘲
        dbConfig.setTableUnderline(true);
        // 设置逻辑删除瀛楁鍚?
        dbConfig.setLogicDeleteField("deleted");
        // 设置逻辑删除鍊?
        dbConfig.setLogicDeleteValue("1");
        // 设置逻辑鏈垹闄ゅ€?
        dbConfig.setLogicNotDeleteValue("0");
        
        globalConfig.setDbConfig(dbConfig);
        
        return globalConfig;
    }

    /**
     * 鑷畾涔塈D鐢熸垚鍣?
     * 鐢ㄤ簬鐢熸垚鍒嗗竷开忕幆澧冧笅鐨勫敮涓€ID
     */
    @Bean
    public IdentifierGenerator identifierGenerator() {
        return new DefaultIdentifierGenerator();
    }
}
