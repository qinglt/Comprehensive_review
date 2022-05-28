package org.jit.sose.controller.zExpert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jit.sose.entity.zExpert.ZExpert;
import org.jit.sose.mapper.zExpert.ZExpertMapper;
import org.jit.sose.service.zExpert.ZExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Date 2022/5/9 21:35
 * @Author qinghua
 */
@RestController
@RequestMapping("/zExpert/zExpert")
public class ZExpertController {

    @Autowired
    private ZExpertService zExpertService;

    /**
     * @Description 批量添加专家
     * @Param str （idList,planId)
     * @return void
     * @Author qinghua
     * @Date 2022/5/10 15:28
     **/
    @PostMapping("/insert")
    public void insert(@RequestBody String str) {
        zExpertService.inserts(str);
    }

    /**
     * @Description 批量删除专家
     * @Param str （idList,planId)
     * @return void
     * @Author qinghua
     * @Date 2022/5/10 15:29
     **/
    @PostMapping("/delete")
    public void delete(@RequestBody String str){
        zExpertService.deletes(str);
    }

    @GetMapping("/queryByPlanId/{zPlanId}")
    public List<Map<String, String>> queryByPlanId(@PathVariable("zPlanId") Integer zPlanId){
        return zExpertService.queryByPlanId(zPlanId);
    }

}
