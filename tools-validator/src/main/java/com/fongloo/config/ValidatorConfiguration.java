package com.fongloo.config;

import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * 验证器配置
 */
public class ValidatorConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Validator validator() {
        return Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .addProperty("hibernate.validator.fail_fast", "true")
                .buildValidatorFactory()
                .getValidator();
    }

    @Autowired
    private Validator validator;

    /**
     * 开启快速返回模式
     * @return
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        processor.setValidator(validator);
        return processor;
    }
}
