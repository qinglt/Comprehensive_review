package org.jit.sose.service.zExpert.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jit.sose.entity.zExpert.ZExpert;
import org.jit.sose.mapper.zExpert.ZExpertMapper;
import org.jit.sose.service.zExpert.ZExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Date 2022/5/10 15:30
 * @Author qinghua
 */
@Service
public class ZExpertServiceImpl implements ZExpertService {

    @Autowired
    private ZExpertMapper zExpertMapper;

    /**
     * @Description 批量添加专家
     * @Param str （idList,planId)
     * @return void
     * @Author qinghua
     * @Date 2022/5/10 15:28
     **/
    @Override
    public void inserts(String str) {
        JSONObject strj = JSON.parseObject(str);
        JSONArray idList = strj.getJSONArray("idList");
        List<Integer> list = idList.toJavaList(Integer.class);
        Integer planId = strj.getInteger("planId");
        zExpertMapper.inserts(list, planId);
    }

    /**
     * @Description 批量删除专家
     * @Param str （idList,planId)
     * @return void
     * @Author qinghua
     * @Date 2022/5/10 15:29
     **/
    @Override
    public void deletes(String str){
        JSONObject strj = JSON.parseObject(str);
        JSONArray idList = strj.getJSONArray("idList");
        List<Integer> list = idList.toJavaList(Integer.class);
        Integer planId = strj.getInteger("planId");
        zExpertMapper.deletes(list, planId);
    }

    @Override
    public List<Map<String, String>> queryByPlanId(Integer zPlanId){
        return zExpertMapper.queryByPlanId(zPlanId);
    }
}
