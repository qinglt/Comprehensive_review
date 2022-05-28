package org.jit.sose.mapper.zExpert;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jit.sose.entity.zExpert.ZIndicator;
import org.springframework.security.core.parameters.P;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qinghua
 * @since 2022-05-07
 */
@Mapper
public interface ZIndicatorMapper{

    /**
     * 通过id更新记录
     * @param record
     */
    void updateById(ZIndicator record);

    /**
     * 单条插入
     * @param record
     */
    void insert(ZIndicator record);

    /**
     * @Description 查询（同一个计划的）一级指标集合，用作新增记录时选定上级指标
     * @Param planId 计划id
     * @return java.util.List<org.jit.sose.entity.zExpert.ZIndicator>
     * @Author qinghua
     * @Date 2022/5/10 16:40
     **/
    List<ZIndicator> firstLevelList(@Param("planId") Integer planId);

    /**
     * @Description 以树的形式（同一个计划的）查询一级二级指标集合
     * @Param planId 计划id
     * @return java.util.List<org.jit.sose.entity.zExpert.ZIndicator>
     * @Author qinghua
     * @Date 2022/5/10 16:39
     **/
    List<ZIndicator> selectListAndList2(@Param("planId") Integer planId);

}
