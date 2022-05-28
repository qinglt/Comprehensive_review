package org.jit.sose.controller.zExpert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.jit.sose.config.security.entity.JwtUser;
import org.jit.sose.entity.zExpert.vo.ScoreVo;
import org.jit.sose.service.zExpert.ZExpertIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Date 2022/5/10 17:14
 * @Author qinghua
 */
@RestController
@RequestMapping("/zExpert/zExpertIndicator")
public class ZExpertIndicatorController {

    @Autowired
    private ZExpertIndicatorService zExpertIndicatorService;

    @PostMapping("/sIndicatorIdListByExpertId")
    public List<Integer> sIndicatorIdListByExpertId(@RequestBody String str) {
        return zExpertIndicatorService.sIndicatorIdListByExpertId(str);
    }

    @PostMapping("/inserts")
    public void inserts(@RequestBody String str) {
        zExpertIndicatorService.inserts(str);
    }

    @PostMapping("/expertCopy")
    public void expertCopy(@RequestBody String str) {
        zExpertIndicatorService.expertCopy(str);
    }

    @PostMapping("/giveScore")
    public String giveScore(@RequestBody String str, @AuthenticationPrincipal JwtUser jwtUser) {
        JSONObject jsonObject = JSON.parseObject(str);
        Integer indicatorId = jsonObject.getInteger("indicatorId");
        Integer planId = jsonObject.getInteger("planId");
        String score = jsonObject.getString("score");
        String opinion = jsonObject.getString("opinion");
        Integer userId = jwtUser.getId();
        return zExpertIndicatorService.giveScore(indicatorId,userId,planId,score,opinion);
    }

    @PostMapping("/getIndicatorScoreByExpert")
    public List<Map<String, Object>> getIndicatorScoreByExpert(@RequestBody String string, @AuthenticationPrincipal JwtUser user) {
        JSONObject strj = JSON.parseObject(string);
        Integer evaluationPlanId = strj.getInteger("evaluationPlanId");
        Integer userId = user.getId();
        return zExpertIndicatorService.getIndicatorScoreByExpert(userId,evaluationPlanId);
    }
}
