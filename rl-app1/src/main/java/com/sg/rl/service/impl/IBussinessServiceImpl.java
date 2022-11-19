package com.sg.rl.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.sg.rl.entity.SysConfig;
import com.sg.rl.framework.components.db.annotation.DataSource;
import com.sg.rl.framework.components.redis.RedisUtil;
import com.sg.rl.framework.components.userLog.annotation.SysLog;
import com.sg.rl.mapper.SysConfigMapper;
import com.sg.rl.service.IBussinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.PostConstruct;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.sg.rl.common.constants.Constants.DataSourceType.SLAVE;
import static com.sg.rl.common.utils.Threads.sleep;


@Service
public class IBussinessServiceImpl implements IBussinessService {

    @Value(value = "${device.msg.parse.cache.device.expire:600}")
    int expireSecond;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysConfigMapper sysConfigMapper;

    private Cache<Object, Object> roomsCache;

    @Autowired
    private RedisUtil redisUtil;

    @PostConstruct
    public void init(){
        log.info("IBussinessServiceImpl init suc!!!!!!!!!!");
        log.info("sysConfigMapper {} " , sysConfigMapper);

        roomsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(expireSecond, TimeUnit.SECONDS)
                .initialCapacity(500)
                .build();

    }

    @Override
    @SysLog(value="获取系统信息Service")
    @DataSource(SLAVE)
    @Transactional
    public Object getSysConfig(int i, int i1) {


        String testKey = "testKey";
        String testVal = "testVal";
        if(null == roomsCache.getIfPresent(testKey)){
            roomsCache.put(testKey,testVal);
            log.info("put key {} val {}",testKey,testVal);
        }

        if(null != roomsCache.getIfPresent(testKey)){
            roomsCache.invalidate(testKey);
            log.info("invalidate key {} val {}",testKey,testVal);
        }

        //int i = 3/0;


/*
        IBussinessService config = (IBussinessService)SpringContextComponent.getBean("IBussinessServiceImpl");
*/
/*        IBussinessService config = (IBussinessService) (AopContext.currentProxy());

        for(int i=0;i<5;i++){
            config.sendMsg();
        }*/

        SysConfig config1 = new SysConfig();
        config1.setConfigId(1L);
        sysConfigMapper.selectConfig(config1);
        SysConfig config = new SysConfig();
        // config.setConfigId(1L);
        config.setConfigKey("sys.index.skinName");
        return sysConfigMapper.selectConfigList(config);
    }




    @Async("threadPoolTaskExecutor")
    @Override
    public void sendMsg(){
        log.info("begin to send msg");
        sleep(1000);
    }

//批量操作
   /* @Transactional(rollbackFor = Exception.class)
    public void publish(PublishTicketParam param) {
        BeanValidator.check(param);
        TrainNumber trainNumber = trainNumberMapper.findByName(param.getTrainNumber());
        if (trainNumber == null) {
            throw new BusinessException("车次不存在");
        }
        List<Long> trainSeatIdList = StringUtil.splitToListLong(param.getTrainSeatIds());
        List<List<Long>> idPartitionList = Lists.partition(trainSeatIdList, 1000);
        for(List<Long> partitionList : idPartitionList) {
            int count = trainSeatMapper.batchPublish(trainNumber.getId(), partitionList);
            if (count != partitionList.size()) {
                throw new BusinessException("部分座位不满足条件，请重新查询[初始]状态的座位进行放票");
            }
        }
    }*/


}
