package org.jit.sose.mapper.zExpert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jit.sose.entity.zExpert.ZExpert;

import java.util.List;
import java.util.Map;

/**
 * @Date 2022/5/9 21:22
 * @Author qinghua
 */
@Mapper
public interface ZExpertMapper extends BaseMapper<ZExpert> {

    /**
     * 批量插入
     * @param userIds  userId数组
     * @param planId  计划id
     * @author qinghua
     * @date 2022/5/11 14:59
     */
    void inserts(@Param("userIds") List<Integer> userIds, @Param("planId") Integer planId);

    void deletes(@Param("userIds") List<Integer> userIds, @Param("planId") Integer planId);

    List<Map<String, String>> queryByPlanId(@Param("planId") Integer planId);

    Integer queryId(@Param("userId") Integer userId, @Param("planId") Integer planId);

}
