package org.jit.sose.service.zExpert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jit.sose.entity.zExpert.ZExpert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @Date 2022/5/10 15:30
 * @Author qinghua
 */
public interface ZExpertService {


    /**
     * @Description 批量添加专家
     * @Param str （idList,planId)
     * @return void
     * @Author qinghua
     * @Date 2022/5/10 15:28
     **/
    void inserts(String str);

    /**
     * @Description 批量删除专家
     * @Param str （idList,planId)
     * @return void
     * @Author qinghua
     * @Date 2022/5/10 15:29
     **/
    void deletes(String str);

    /**
     * @Description 根据z_plan_id查询专家信息
     * @Param zPlanId
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     *     （userId,expertName)
     * @Author qinghua
     * @Date 2022/5/10 15:32
     **/
    List<Map<String, String>> queryByPlanId(Integer zPlanId);

}
