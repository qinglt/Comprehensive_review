package org.jit.sose.mapper.zExpert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jit.sose.entity.zExpert.ZData;
import org.jit.sose.entity.zExpert.vo.PublicFileVo;
import org.jit.sose.entity.zExpert.vo.ScoreVo;

import java.util.List;
import java.util.Map;

/**
 * @author qinghua
 * @date 2022/5/14 12:31
 */
@Mapper
public interface ZDataMapper extends BaseMapper<ZData> {

    /**
     * 没有嵌套结果
     * @param evaluationPlanId
     * @param userId
     * @return {@link List< Map< String, Object>> }
     * @author qinghua
     * @date 2022/5/15 20:32
     */
    List<Map<String, Object>> getEvaluationPlanFile(@Param("planId") Integer evaluationPlanId, @Param("userId") Integer userId);

    /**
     * 对结果进行嵌套
     * @param evaluationPlanId
     * @param userId
     * @return {@link List< ScoreVo> }
     * @author qinghua
     * @date 2022/5/15 20:32
     */
    List<ScoreVo> getEvaluationPlanFile2(@Param("planId") Integer evaluationPlanId, @Param("userId") Integer userId);

    /**
     * 对结果进行嵌套 (三级指标对应的二级和一级都查出来）
     * @param evaluationPlanId
     * @return {@link List< ScoreVo> }
     * @author qinghua
     * @date 2022/5/15 20:32
     */
    List<ScoreVo> getEvaluationPlanFile3(@Param("planId") Integer evaluationPlanId);

    List<PublicFileVo> getPublicFileTree();

    List<PublicFileVo> getIndicatorFileTree(@Param("indicatorId") Integer indicatorId);

}
