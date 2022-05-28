package org.jit.sose.mapper.zExpert;

import org.apache.ibatis.annotations.Mapper;
import org.jit.sose.entity.zExpert.ZPlan;

import java.util.List;
import java.util.Map;

/**
 * @Date 2022/5/8 10:43
 * @Author qinghua
 */
@Mapper
public interface ZPlanMapper {

    List<ZPlan> queryAll();

    /**
     * 查询指标体系对应的制定人姓名
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     * @Author qinghua
     * @Date 2022/5/8 12:20
     **/
    List<Map<String, String>> queryUserName();

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

    void update(ZPlan zPlan);

}
