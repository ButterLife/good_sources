package cn.zhuchuangsoft.footstone.service;

import cn.zhuchuangsoft.footstone.entity.warming.JalaLineWarming;
import cn.zhuchuangsoft.footstone.entity.warming.JalaWarming;

public interface IJalaWarmingService {


    void inertJalaWarming(JalaWarming jalaWarming);

    void inertJalaLineWarming(JalaLineWarming jalaLineWarming);
}
