package org.jit.sose.mapper.zExpert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jit.sose.entity.assessment.Expert;
import org.jit.sose.entity.zExpert.ZExpert;
import org.jit.sose.entity.zExpert.ZExpertIndicator;
import org.jit.sose.entity.zExpert.vo.AllScoreVo;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Map;

/**
 * @Date 2022/5/10 17:11
 * @Author qinghua
 */
@Mapper
public interface ZExpertIndicatorMapper extends BaseMapper<ZExpertIndicator> {

    /**
     * 通过专家标识查询指标标识集合
     * @param record 专家信息
     * @return
     */
    List<Integer> sIndicatorIdListByExpertId(ZExpert record);

    /**
     * @Description 批量插入
     * @Param expertId 专家id
     * @Param indicatorIds 指标id
     * @return void
     * @Author qinghua
     * @Date 2022/5/10 19:14
     **/
    void inserts(@Param("expertId") Integer expertId, @Param("indicatorIds") List<Integer> indicatorIds);

    /**
     * 删除该专家绑定所有指标
     * @param expertId 专家id
     * @return void
     * @Author qinghua
     * @Date 2022/5/10 19:16
     **/
    void deleteAllByExpertId(@Param("expertId") Integer expertId);

    /**
     * {indicatorId: , score: }
     * @param userId
     * @param planId
     * @return {@link List< Map< String, Object>> }
     * @author qinghua
     * @date 2022/5/21 17:35
     */
    List<Map<String, Object>> getIndicatorScoreByExpert(@Param("userId") Integer userId, @Param("planId") Integer planId);

}
