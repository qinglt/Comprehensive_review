package org.jit.sose.controller.zExpert;

import org.jit.sose.config.redis.RedisUtil;
import org.jit.sose.config.security.entity.JwtUser;
import org.jit.sose.entity.zExpert.ZIndicator;
import org.jit.sose.mapper.zExpert.ZIndicatorMapper;
import org.jit.sose.mapper.zExpert.ZPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qinghua
 * @since 2022-05-07
 */
@RestController
@RequestMapping("/zExpert/indicator")
public class ZIndicatorController {

    @Autowired
    private ZIndicatorMapper zIndicatorMapper;

    @Autowired
    private ZPlanMapper zPlanMapper;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/selectListAndList2/{zPlanId}")
    public List<ZIndicator> selectListAndList2(@PathVariable("zPlanId") Integer zPlanId) {
        return zIndicatorMapper.selectListAndList2(zPlanId);
    }

    @PostMapping("/updateById")
    public void updateById(@RequestBody ZIndicator record) {
        zIndicatorMapper.updateById(record);
    }

    @PostMapping("/insert")
    public void insert(@RequestBody ZIndicator record, @AuthenticationPrincipal JwtUser jwtUser) {
        //从redis中获取用户信息
        HashMap<String,Object> onlineUser = (HashMap<String, Object>) redisUtil.hget("onlineUser", jwtUser.getUsername());
        Integer userId = (Integer) onlineUser.get("id");
        record.setUserId(userId);
        zIndicatorMapper.insert(record);
    }

    @GetMapping("/firstLevelList/{zPlanId}")
    public List<ZIndicator> firstLevelList(@PathVariable("zPlanId") Integer zPlanId) {
        return zIndicatorMapper.firstLevelList(zPlanId);
    }

    /**
     * @Description 查询指标指定人的姓名
     * @Param
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     * @Author qinghua
     * @Date 2022/5/9 19:54
     **/
    @GetMapping("getExpertName")
    public List<Map<String, String>> getExpertName() {
        return zPlanMapper.queryUserName();
    }

}

