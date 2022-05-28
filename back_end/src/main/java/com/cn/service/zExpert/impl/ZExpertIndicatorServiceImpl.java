package org.jit.sose.service.zExpert.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.jit.sose.entity.zExpert.ZExpert;
import org.jit.sose.entity.zExpert.ZExpertIndicator;
import org.jit.sose.entity.zExpert.ZIndicator;
import org.jit.sose.entity.zExpert.vo.AllScoreVo;
import org.jit.sose.mapper.zExpert.ZExpertIndicatorMapper;
import org.jit.sose.mapper.zExpert.ZExpertMapper;
import org.jit.sose.mapper.zExpert.ZIndicatorMapper;
import org.jit.sose.service.zExpert.ZExpertIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Date 2022/5/10 17:44
 * @Author qinghua
 */
@Service
public class ZExpertIndicatorServiceImpl implements ZExpertIndicatorService {

    @Autowired
    private ZExpertIndicatorMapper zExpertIndicatorMapper;

    @Autowired
    private ZExpertMapper zExpertMapper;

    @Override
    public List<Integer> sIndicatorIdListByExpertId(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        Integer userId = jsonObject.getInteger("userId");
        Integer planId = jsonObject.getInteger("planId");
        ZExpert zExpert = new ZExpert();
        zExpert.setUserId(userId);
        zExpert.setPlanId(planId);
        return zExpertIndicatorMapper.sIndicatorIdListByExpertId(zExpert);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void inserts(String str) {
        JSONObject strj = JSON.parseObject(str);
        JSONArray idList = strj.getJSONArray("indicatorIdList");
        List<Integer> list = idList.toJavaList(Integer.class);
        Integer userId = strj.getInteger("userId");
        Integer planId = strj.getInteger("planId");
        // 根据userId获取专家id
        Integer expertId = zExpertMapper.queryId(userId, planId);
        // 先删后增
        zExpertIndicatorMapper.deleteAllByExpertId(expertId);
        zExpertIndicatorMapper.inserts(expertId, list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void expertCopy(String str) {
        JSONObject strj = JSON.parseObject(str);
        Integer planId = strj.getInteger("planId");
        Integer userIdCopied = strj.getInteger("userIdCopied");
        Integer userId = strj.getInteger("userId");
        // 根据userId获取专家id
        Integer expertId = zExpertMapper.queryId(userId, planId);

        // 查询复制的指标数组
        ZExpert zExpert = new ZExpert();
        zExpert.setUserId(userIdCopied);
        zExpert.setPlanId(planId);
        List<Integer> list = zExpertIndicatorMapper.sIndicatorIdListByExpertId(zExpert);

        // 先删后增
        zExpertIndicatorMapper.deleteAllByExpertId(expertId);
        zExpertIndicatorMapper.inserts(expertId, list);

    }

    @Override
    public String giveScore(Integer indicatorId, Integer userId, Integer planId, String score, String opinion) {
        Integer expertId = zExpertMapper.queryId(userId, planId);
        UpdateWrapper<ZExpertIndicator> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("score",score).set("opinion", opinion).eq("indicator_id",indicatorId).eq("expert_id",expertId);
        zExpertIndicatorMapper.update(null, updateWrapper);
        return "ok";
    }

    @Override
    public List<Map<String, Object>> getIndicatorScoreByExpert(Integer userId, Integer planId) {
        return zExpertIndicatorMapper.getIndicatorScoreByExpert(userId, planId);
    }

    @Override
    public List<AllScoreVo> getAllScore(Integer planId) {

        return null;
    }

}
