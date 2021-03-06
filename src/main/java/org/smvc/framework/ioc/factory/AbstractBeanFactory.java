/**
 * @filename AbstractBeanFactory.java
 * @createtime 2015年8月2日
 * @author dingxiangyong
 * @comment 
 */
package org.smvc.framework.ioc.factory;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.smvc.framework.ioc.bean.BeanDefinition;

/**
 * Provide creating , shipping beans.
 * 
 * @author Big Martin
 *
 */
public abstract class AbstractBeanFactory implements IBeanFactory {
    /**
     * Default map capacity.
     */
    private static final int INITIAL_MAP_CAPACITY = 50;
    
    private static Logger logger = Logger.getLogger(AbstractBeanFactory.class);
    
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<String, BeanDefinition>(INITIAL_MAP_CAPACITY);

    public Object getBean(String name) {
        BeanDefinition beanDefinition = beanDefinitions.get(name);

        // if not exist, then return
        if (beanDefinition == null) {
            return null;
        }

        try {
            // lazy loading
            Object beanInstance = beanDefinition.getBean();
            if (beanInstance == null) {
                beanInstance = createBean(beanDefinition);

                // set properties
                beanDefinition.setBean(beanInstance);
            }
            return beanInstance;
        } catch (Exception ex) {
            logger.warn("Got an exception when get bean from IOC container.", ex);
            return null;
        }

    }

    /**
     * Instance a bean
     * 
     * @throws Exception
     */
    public abstract Object createBean(BeanDefinition beanDefinition) throws Exception;

    /**
     * regist a bean
     * 
     * @param beanDefinition
     */
    public void registBean(String name, BeanDefinition beanDefinition) {
        beanDefinitions.put(name, beanDefinition);
    }

}
