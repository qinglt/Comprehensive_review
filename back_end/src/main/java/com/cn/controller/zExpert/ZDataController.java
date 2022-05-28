package org.jit.sose.controller.zExpert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.Get;
import org.jit.sose.config.security.entity.JwtUser;
import org.jit.sose.entity.zExpert.ZData;
import org.jit.sose.entity.zExpert.vo.PublicFileVo;
import org.jit.sose.entity.zExpert.vo.ScoreVo;
import org.jit.sose.entity.zExpert.vo.ZIndicatorFileVo;
import org.jit.sose.service.zExpert.ZDataService;
import org.jit.sose.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author qinghua
 * @date 2022/5/14 13:17
 */
@RestController
@RequestMapping("/zExpert/zData")
public class ZDataController {

    @Autowired
    private ZDataService zDataService;

    @PostMapping("/selectCheckedIndicatorFile")
    public PageInfo<ZData> selectCheckedIndicatorFile(@RequestBody String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        Integer pageNum = jsonObject.getInteger("pageNum");
        Integer pageSize = jsonObject.getInteger("pageSize");
        JSONArray list = jsonObject.getJSONArray("checkedNodes");
        List<Integer> indicatorIds = list.toJavaList(Integer.class);
        return zDataService.selectCheckedIndicatorFile(pageNum, pageSize, indicatorIds);
    }

    @PostMapping("/insertIndicatorFile")
    public String insertIndicatorFile(@RequestParam("list") String str,
                                      @RequestParam("file") MultipartFile file) {
        if (file == null) {
            return ResponseUtil.error("uploadFileError");
        } else {
            JSONArray jsonArray = JSON.parseArray(str);
            List<ZIndicatorFileVo> zIndicatorFileVoList = jsonArray.toJavaList(ZIndicatorFileVo.class);
            return ResponseUtil.success(zDataService.insertIndicatorFile(zIndicatorFileVoList, file));
        }
    }

    @PostMapping("/getEvaluationPlanFile")
    public PageInfo<Map<String, Object>> getEvaluationPlanFile(@RequestBody String string, @AuthenticationPrincipal JwtUser user) {
        JSONObject strj = JSON.parseObject(string);
        Integer evaluationPlanId = strj.getInteger("evaluationPlanId");
        Integer pageNum = strj.getIntValue("pageNum");
        Integer pageSize = strj.getIntValue("pageSize");
        Integer userId = user.getId();
        return zDataService.getEvaluationPlanFile(evaluationPlanId, userId, pageNum, pageSize);
    }

    /**
     * 数据传到前端属性zDataVoList 变成了 zdataVoList
     */
    @PostMapping("/getEvaluationPlanFile2")
    public PageInfo<ScoreVo> getEvaluationPlanFile2(@RequestBody String string, @AuthenticationPrincipal JwtUser user) {
        JSONObject strj = JSON.parseObject(string);
        Integer evaluationPlanId = strj.getInteger("evaluationPlanId");
        Integer pageNum = strj.getIntValue("pageNum");
        Integer pageSize = strj.getIntValue("pageSize");
        Integer userId = user.getId();
        return zDataService.getEvaluationPlanFile2(evaluationPlanId, userId, pageNum, pageSize);
    }

    /**
     * 数据传到前端属性zDataVoList 变成了 zdataVoList (取消分页）
     */
    @PostMapping("/getEvaluationPlanFile3")
    public List<ScoreVo> getEvaluationPlanFile3(@RequestBody String string) {
        JSONObject jsonObject = JSON.parseObject(string);
        Integer evaluationPlanId = jsonObject.getInteger("evaluationPlanId");
//        Integer pageNum = jsonObject.getIntValue("pageNum");
//        Integer pageSize = jsonObject.getIntValue("pageSize");
        return zDataService.getEvaluationPlanFile3(evaluationPlanId, 1, 1);
    }

    @PostMapping("/editIndicatorFile")
    public String editIndicatorFile(@RequestParam("dataId") Integer dataId,
                                    @RequestParam("indicatorId") Integer indicatorId,
                                    @RequestParam("file") MultipartFile file) {
        if (file == null) {
            return ResponseUtil.error("uploadFileError");
        } else {
            return ResponseUtil.success(zDataService.editIndicatorFile(dataId, indicatorId, file));
        }
    }

    @GetMapping("/delete/{dataId}")
    public String delete(@PathVariable("dataId") Integer dataId) {
        return ResponseUtil.success(zDataService.delete(dataId));
    }

    @PostMapping("/getFolderByIndicatorId")
    public List<ZData> getFolderByIndicatorId(@RequestBody String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONArray jsonArray = jsonObject.getJSONArray("indicatorIdList");
        List<Integer> list = jsonArray.toJavaList(Integer.class);
        return zDataService.getFolderByIndicatorId(list);
    }

    @PostMapping("/addFolderByIndicatorId")
    public void addFolderByIndicatorId(@RequestBody String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        Integer indicatorId = jsonObject.getInteger("indicatorId");
        String fileName = jsonObject.getString("fileName");
        zDataService.addFolderByIndicatorId(indicatorId, fileName);
    }

    @GetMapping("/getPublicFolder")
    public List<ZData> getPublicFolder() {
        return zDataService.getPublicFolder();
    }

    @PostMapping("/addPublicFolder")
    public void addPublicFolder(@RequestBody String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        String fileName = jsonObject.getString("fileName");
        zDataService.addPublicFolder(fileName);
    }

    @GetMapping("/getPublicFile/{pageNum}/{pageSize}")
    public PageInfo<ZData> getPublicFile(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        return zDataService.getPublicFile(pageNum, pageSize);
    }

    @PostMapping("/insertPublicFile")
    public String insertPublicFile(@RequestParam("folderId") Integer folderId, @RequestParam("file") MultipartFile file) {
        return ResponseUtil.success(zDataService.insertPublicFile(folderId, file));
    }

    @GetMapping("/deletePublicFileById/{id}")
    public void deletePublicFileById(@PathVariable("id") Integer zDataId) {
        zDataService.removeById(zDataId);
    }

    @GetMapping("/getPublicFileTree")
    public List<PublicFileVo> getPublicFileTree(){
        return zDataService.getPublicFileTree();
    }

    @GetMapping("/getIndicatorFileTree/{id}")
    public List<PublicFileVo> getIndicatorFileTree(@PathVariable("id") Integer indicatorId) {
        return zDataService.getIndicatorFileTree(indicatorId);
    }
}
