package com.sg.rl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.google.common.collect.Maps;
import com.sg.rl.dto.GetSysConfReq;
import com.sg.rl.entity.Product;
import com.sg.rl.entity.SysConfig;
import com.sg.rl.entity.User;
import com.sg.rl.enums.SexEnums;
import com.sg.rl.framework.components.db.annotation.DataSource;
import com.sg.rl.framework.components.http.RestTemplateComponent;
import com.sg.rl.framework.components.redis.RedisUtil;
import com.sg.rl.framework.components.spring.SpringContextComponent;
import com.sg.rl.framework.components.threadpool.AsyncManager;
import com.sg.rl.mapper.ProductMapper;
import com.sg.rl.mapper.SysConfigMapper;
import com.sg.rl.mapper.UserMapper;
import com.sg.rl.service.IBussinessService;
import com.sg.rl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.sg.rl.common.constants.Constants.DataSourceType.MASTER;
import static com.sg.rl.common.utils.Threads.sleep;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BussinessServiceTest {

    @Autowired
    private IBussinessService bService;

    @Autowired
    private RestTemplateComponent restTemplateUtils;

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductMapper productMapper;

    @Test
    public void test() {
        SysConfig sysConfig = (SysConfig) bService.getSysConfig(1, 2);
        log.info("getSysConfig = {}", sysConfig);
    }

    @Test
    public void testIoc() {
        IBussinessService bean1 = (IBussinessService) SpringContextComponent.getBean(IBussinessService.class);
        IBussinessService bean2 = (IBussinessService) SpringContextComponent.getBean("IBussinessServiceImpl");

        if (bean2 == bean1) {
            System.out.println("getBean suc");
        }

        String[] allBeanNames = SpringContextComponent.getApplicationContext().getBeanDefinitionNames();
        for (String beanName : allBeanNames) {
            System.out.println(beanName);
        }

    }

    @Test
    public void testAsync() {

        for (int i = 0; i < 5; i++) {
            bService.sendMsg();
        }

    }


    @Test
    @DataSource(value = MASTER)
    @Transactional
    public void insertBatch() {
        SysConfig config = new SysConfig();
        for (int i = 0; i < 10; i++) {
            config.setConfigKey("sys.index.skinName");
            config.setConfigName("i" + i);
            config.setConfigValue("xasdxasdsad");
            config.setConfigType("Y");
            config.setCreateTime(new Date());
            sysConfigMapper.insertConfig(config);
        }


    }


    @Test
    public void testRestTemplate() {

        String rsp = "";

        rsp = restTemplateUtils.sendGet("http://www.baidu.com");

        String getUrl = "http://localhost:8020/bussiness/testGet";

        GetSysConfReq req = GetSysConfReq.builder()
                .account("123").userId("888").channel(3).build();

        Map urlParam = JSON.parseObject(JSON.toJSONString(req), Map.class);


        rsp = restTemplateUtils.sendGetWithParms(getUrl, urlParam);

        Map<String, Object> header = new HashMap<>();
        urlParam.put("RL", "123");
        urlParam.put("RL111", 123123213);


        HttpHeaders headers = new HttpHeaders();
        headers.add("RL", "123");
        headers.add("RL1111", "1121x1x2e123");
        rsp = restTemplateUtils.sendGetWithParamsHeaders(getUrl, urlParam, headers);

        String postUrl = "http://localhost:8020/bussiness/testPost";

        rsp = restTemplateUtils.sendPostJson(postUrl, urlParam);
        rsp = restTemplateUtils.sendPostWithJsonHeaders(postUrl, urlParam, headers);

    }


    @Test
    public void testRedisUtil() {
        redisUtil.set("test", "123");
        log.info("after set redisUtil.get {}  !!!!!!", redisUtil.get("test"));

        redisUtil.delete("test");
        log.info("after delete redisUtil.get {}  !!!!!!", redisUtil.get("test"));
    }


    @Test
    public void testRedisLock() {
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                RLock redisLock = redissonClient.getLock("lock1");
                redisLock.lock();
                log.info("task1 lock suc!!!");

                sleep(3000);


                redisLock.unlock();
                log.info("task1 unlock suc!!!");
            }
        };


        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                RLock redisLock = redissonClient.getLock("lock1");
                redisLock.lock();
                log.info("task2 lock  suc!!!");

                sleep(3000);

                redisLock.unlock();
                log.info("task2 unlock suc!!!");

            }
        };

        AsyncManager.me().execute(task1);
        AsyncManager.me().execute(task2);


        sleep(10000);
    }


    @Test
    public void testMp() {
        userMapper.selectList(null).forEach(System.out::println);
    }

    @Test
    public void testMpInsert() {
        User user = new User(null, "张三", 23, "zhangsan@atguigu.com", 0, SexEnums.SEX_MALE);
//INSERT INTO user ( id, name, age, email ) VALUES ( ?, ?, ?, ? )
        int result = userMapper.insert(user);
        System.out.println("受影响行数：" + result);
//1475754982694199298
        System.out.println("id自动获取：" + user.getId());
    }

    @Test
    public void testDeleteById() {
//通过id删除用户信息
//DELETE FROM user WHERE id=?
        int result = userMapper.deleteById(1590696833755897858L);
        System.out.println("受影响行数：" + result);


    }

    @Test
    public void testDeleteByMap() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("age", 23);
        map.put("name", "张三");
        int result = userMapper.deleteByMap(map);
        System.out.println("受影响行数：" + result);
    }


    @Test
    public void testDeleteBatchIds() {
//通过多个id批量删除
//DELETE FROM user WHERE id IN ( ? , ? , ? )
        List<Long> idList = Arrays.asList(1L, 2L, 3L);
        int result = userMapper.deleteBatchIds(idList);
        System.out.println("受影响行数：" + result);
    }

    @Test
    public void testUpdateById() {
        User user = new User(4L, "admin", 22, null, 0, SexEnums.SEX_MALE);
//UPDATE user SET name=?, age=? WHERE id=?
        int result = userMapper.updateById(user);
        System.out.println("受影响行数：" + result);
    }


    @Test
    public void testSelectById() {
//根据id查询用户信息
//SELECT id,name,age,email FROM user WHERE id=?
        User user = userMapper.selectById(4L);
        System.out.println(user);
    }


    @Test
    public void testPage() {
//设置分页参数
        Page<User> page = new Page<>(1, 1);
        userMapper.selectPage(page, null);
//获取分页数据
        List<User> list = page.getRecords();
        list.forEach(System.out::println);
        System.out.println("当前页：" + page.getCurrent());
        System.out.println("每页显示的条数：" + page.getSize());
        System.out.println("总记录数：" + page.getTotal());
        System.out.println("总页数：" + page.getPages());
        System.out.println("是否有上一页：" + page.hasPrevious());
        System.out.println("是否有下一页：" + page.hasNext());
    }


    @Test
    public void testSelectPageVo() {
//设置分页参数
        Page<User> page = new Page<>(1, 5);
        userMapper.selectPageVo(page, 20);
//获取分页数据
        List<User> list = page.getRecords();
        list.forEach(System.out::println);
        System.out.println("当前页：" + page.getCurrent());
        System.out.println("每页显示的条数：" + page.getSize());
        System.out.println("总记录数：" + page.getTotal());
        System.out.println("总页数：" + page.getPages());
        System.out.println("是否有上一页：" + page.hasPrevious());
        System.out.println("是否有下一页：" + page.hasNext());
    }

    @Test
    public void testSelectByMap() {
//通过map条件查询用户信息
//SELECT id,name,age,email FROM user WHERE name = ? AND age = ?
        Map<String, Object> map = new HashMap<>();
        map.put("age", 22);
        map.put("name", "admin");
        List<User> list = userMapper.selectByMap(map);
        list.forEach(System.out::println);
    }

    @Test
    public void testGetCount() {
        long count = userService.count();
        System.out.println(count);
    }

    @Test
    public void testSaveBatch() {
// SQL长度有限制，海量数据插入单条SQL无法实行，
// 因此MP将批量插入放在了通用Service中实现，而不是通用Mapper
        ArrayList<User> users = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setName("ybc" + i);
            user.setAge(20 + i);
            user.setIsDeleted(0);
            //  user.setSex(SexEnums.SEX_MALE);
            users.add(user);
        }
//SQL:INSERT INTO t_user ( username, age ) VALUES ( ?, ? )
        boolean b = userService.saveBatch(users);
        System.out.println(b);
    }


    @Test
    public void testConcurrentUpdate() {
//1、小李
        Product p1 = productMapper.selectById(1L);
        System.out.println("小李取出的价格：" + p1.getPrice());
//2、小王
        Product p2 = productMapper.selectById(1L);
        System.out.println("小王取出的价格：" + p2.getPrice());

//3、小李将价格加了50元，存入了数据库
        p1.setPrice(p1.getPrice() + 50);
        int result1 = productMapper.updateById(p1);
        System.out.println("小李修改结果：" + result1);
//4、小王将商品减了30元，存入了数据库
        p2.setPrice(p2.getPrice() - 30);
        int result2 = productMapper.updateById(p2);
        System.out.println("小王修改结果：" + result2);
//最后的结果
        Product p3 = productMapper.selectById(1L);
//价格覆盖，最后的结果：70
        System.out.println("最后的结果：" + p3.getPrice());
    }


    @Test
    public void testGen() {
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/mybatis_plus?characterEncoding=utf-8&userSSL=false", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("Steven") // 设置作者
//.enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D://mybatis_plus"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.sg") // 设置父包名
                            .moduleName("mybatisplus") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D://mybatis_plus"));
// 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("t_user","t_product") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker 引擎模板，默认的是Velocity引擎模板
                .execute();
    }


}
