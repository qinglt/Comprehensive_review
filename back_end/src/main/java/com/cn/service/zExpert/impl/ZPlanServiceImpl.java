package org.jit.sose.service.zExpert.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.jit.sose.entity.assessment.EvaluationPlan;
import org.jit.sose.entity.zExpert.ZPlan;
import org.jit.sose.mapper.zExpert.ZPlanMapper;
import org.jit.sose.service.zExpert.ZPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Date 2022/5/9 20:02
 * @Author qinghua
 */
@Service
public class ZPlanServiceImpl implements ZPlanService {

    @Autowired
    private ZPlanMapper zPlanMapper;

    @Override
    public List<ZPlan> queryAll() {
        return zPlanMapper.queryAll();
    }

    @Override
    public PageInfo<ZPlan> queryZPlan(Integer pageNum,Integer pageSize) {
        //设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        List<ZPlan> zPlanList = zPlanMapper.queryAll();
        PageInfo<ZPlan> pageInfo = new PageInfo<>(zPlanList);
        //解决删除完整页的数据后不跳转到上一页的bug
        if (pageInfo.getSize() == 0){
            pageNum = pageNum -1;
            PageHelper.startPage(pageNum, pageSize);
            zPlanList = zPlanMapper.queryAll();
            pageInfo = new PageInfo<>(zPlanList);
        }
        System.out.println(pageInfo);
        return pageInfo;
    }

    @Override
    public void insert(ZPlan zPlan) {
        zPlanMapper.insert(zPlan);
    }

    @Override
    public Integer deleteById(Integer planId) {
        return zPlanMapper.deleteById(planId);
    }

    @Override
    public void update(ZPlan zPlan) {
        zPlanMapper.update(zPlan);
    }
}
