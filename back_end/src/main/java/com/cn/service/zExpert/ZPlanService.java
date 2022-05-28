package org.jit.sose.service.zExpert;

import com.github.pagehelper.PageInfo;
import org.jit.sose.entity.zExpert.ZPlan;

import java.util.List;

/**
 * @Date 2022/5/9 20:02
 * @Author qinghua
 */
public interface ZPlanService {

    List<ZPlan> queryAll();

    /**
     * 分页查询计划
     * @param pageNum 第几页
     * @param pageSize 数量
     * @return com.github.pagehelper.PageInfo<java.util.List<org.jit.sose.entity.zExpert.ZPlan>>
     * @Author qinghua
     * @Date 2022/5/9 20:05
     **/
    PageInfo<ZPlan> queryZPlan(Integer pageNum,Integer pageSize);

    /**
     * 新增计划
     * @param zPlan 计划
     * @Author qinghua
     * @Date 2022/5/11 11:59
     **/
    void insert(ZPlan zPlan);

    /**
     * 根据id删除计划
     * @param planId 计划id
     * @return java.lang.Integer
     * @Author qinghua
     * @Date 2022/5/11 12:41
     **/
    Integer deleteById(Integer planId);

    /**
     * 修改计划名
     * @param zPlan 计划
     * @Author qinghua
     * @Date 2022/5/11 12:24
     **/
    void update(ZPlan zPlan);

}
