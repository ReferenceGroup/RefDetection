package com.wzy.paper;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author wzy
 * @Date 2016/11/25 16:55
 */
@RunWith(SpringJUnit4ClassRunner.class)//表示整合JUnit4进行测试
@ContextConfiguration(locations = {"classpath:applicationContext_main.xml"})//加载spring配置文件
public class BaseJunit4Test {
}
