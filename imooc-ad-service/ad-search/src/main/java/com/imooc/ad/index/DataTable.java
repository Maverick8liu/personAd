package com.imooc.ad.index;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataTable implements ApplicationContextAware,PriorityOrdered{

    private static ApplicationContext applicationContext;

    public static final Map<Class,Object> dataTable = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DataTable.applicationContext = applicationContext;
    }

    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }

    public static <T> T of(Class<T> clazz){
        T instance = (T) dataTable.get(clazz);
        if(instance != null){
            return instance;
        }
        dataTable.put(clazz,bean(clazz));
        return (T)dataTable.get(clazz);
    }

    private static <T> T bean(String beanName){
        return (T)applicationContext.getBean(beanName);
    }


    private static <T> T bean(Class clazz){
        return (T)applicationContext.getBean(clazz);
    }

}
