package org.jit.sose.service.zExpert;

import org.apache.ibatis.annotations.Param;
import org.jit.sose.entity.zExpert.vo.AllScoreVo;

import java.util.List;
import java.util.Map;

/**
 * @Date 2022/5/10 17:44
 * @Author qinghua
 */
public interface ZExpertIndicatorService {

    /**
     * 查询专家对应的指标
     * @param str （userId, planId)
     * @return java.util.List<java.lang.Integer>
     * @Author qinghua
     * @Date 2022/5/10 17:46
     **/
    List<Integer> sIndicatorIdListByExpertId(String str);

    /**
     * 批量插入专家对应的指标
     * @param str （List<Integer> ids, expertId)
     * @Author qinghua
     * @Date 2022/5/10 18:01
     **/
    void inserts(String str);

    /**
     * 复制专家指标
     * @param str (planId,expertIdCopied,expertId)
     * @return void
     * @Author qinghua
     * @Date 2022/5/10 21:00
     **/
    void expertCopy(String str);


    /**
     * 打分
     * @param indicatorId
     * @param userId
     * @param planId
     * @param score
     * @return {@link String }
     * @author qinghua
     * @date 2022/5/15 18:52
     */
    String giveScore(Integer indicatorId, Integer userId, Integer planId, String score, String opinion);

    /**
     * {indicatorId: , score: }
     * @param userId
     * @param planId
     * @return {@link List<  Map < String, Object>> }
     * @author qinghua
     * @date 2022/5/21 17:35
     */
    List<Map<String, Object>> getIndicatorScoreByExpert(Integer userId, Integer planId);

    /**
     * 评审结果汇总
     * @param planId
     * @return {@link List< AllScoreVo> }
     * @author qinghua
     * @date 2022/5/15 22:51
     */
    List<AllScoreVo> getAllScore(Integer planId);

}
