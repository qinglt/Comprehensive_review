package org.jit.sose.controller.zExpert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.jit.sose.entity.zExpert.ZPlan;
import org.jit.sose.mapper.zExpert.ZPlanMapper;
import org.jit.sose.service.zExpert.ZPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Date 2022/5/9 19:54
 * @Author qinghua
 */
@RestController
@RequestMapping("/zExpert/zPlan")
public class ZPlanController {

    @Autowired
    private ZPlanService zPlanService;

    /**
     * @return java.util.List<org.jit.sose.entity.zExpert.ZPlan>
     * @Description 查询全部计划
     * @Param
     * @Author qinghua
     * @Date 2022/5/9 20:11
     **/
    @GetMapping("/getZPlanList")
    public List<ZPlan> getZPlanList() {
        return zPlanService.queryAll();
    }

    @PostMapping("/selectZPlanList")
    public PageInfo<ZPlan> selectZPlanList(@RequestBody String str) {
        JSONObject strj = JSON.parseObject(str);
        Integer pageNum = strj.getInteger("pageNum");
        Integer pageSize = strj.getInteger("pageSize");
        return zPlanService.queryZPlan(pageNum, pageSize);
    }

    @PostMapping("/insert")
    public void insert(@RequestBody ZPlan zPlan){
        zPlanService.insert(zPlan);
    }

    @GetMapping("/deleteById/{zPlanId}")
    public Integer deleteById(@PathVariable("zPlanId") Integer zPlanId) {
        return zPlanService.deleteById(zPlanId);
    }

    @PostMapping("/update")
    public void update(@RequestBody ZPlan zPlan){
        zPlanService.update(zPlan);
    }

}
