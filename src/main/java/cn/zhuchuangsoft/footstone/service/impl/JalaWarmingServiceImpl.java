package cn.zhuchuangsoft.footstone.service.impl;

import cn.zhuchuangsoft.footstone.entity.warming.JalaLineWarming;
import cn.zhuchuangsoft.footstone.entity.warming.JalaWarming;
import cn.zhuchuangsoft.footstone.mappers.JalaWarmingMapper;
import cn.zhuchuangsoft.footstone.service.IJalaWarmingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JalaWarmingServiceImpl implements IJalaWarmingService {
    @Autowired
    private JalaWarmingMapper jalaWarmingMapper;

    @Override
    public void inertJalaWarming(JalaWarming jalaWarming) {
        jalaWarmingMapper.inertJalaWarming(jalaWarming);
    }

    @Override
    public void inertJalaLineWarming(JalaLineWarming jalaLineWarming) {
        jalaWarmingMapper.inertJalaLineWarming(jalaLineWarming);
    }
}
