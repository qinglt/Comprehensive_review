package org.jit.sose.service.zExpert.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.jit.sose.entity.zExpert.ZData;
import org.jit.sose.entity.zExpert.ZExpert;
import org.jit.sose.entity.zExpert.ZExpertIndicator;
import org.jit.sose.entity.zExpert.vo.PublicFileVo;
import org.jit.sose.entity.zExpert.vo.ScoreVo;
import org.jit.sose.entity.zExpert.vo.ZIndicatorFileVo;
import org.jit.sose.mapper.zExpert.ZDataMapper;
import org.jit.sose.mapper.zExpert.ZExpertIndicatorMapper;
import org.jit.sose.mapper.zExpert.ZExpertMapper;
import org.jit.sose.service.zExpert.ZDataService;
import org.jit.sose.util.FileEquals;
import org.jit.sose.util.ZIndicatorFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qinghua
 * @date 2022/5/14 13:30
 */
@Slf4j
@Service
public class ZDataServiceImpl extends ServiceImpl<ZDataMapper, ZData> implements ZDataService {

    @Autowired
    private ZDataMapper zDataMapper;

    @Autowired
    private ZIndicatorFileUtil zIndicatorFileUtil;

    @Autowired
    private FileEquals fileEquals;

    @Override
    public PageInfo<ZData> selectCheckedIndicatorFile(Integer pageNum, Integer pageSize, List<Integer> indicatorIds) {
        QueryWrapper<ZData> wrapper = new QueryWrapper<>();
        wrapper.eq("level", 1).in("indicator_id", indicatorIds).orderByAsc("indicator_id");
        List<ZData> zDataList = zDataMapper.selectList(wrapper);
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<ZData> zDataPageInfo = new PageInfo<>(zDataList);
        return zDataPageInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Map<String, Object>> insertIndicatorFile(List<ZIndicatorFileVo> zIndicatorFileVoList, MultipartFile file) {
        //将文件存入服务器
        ZData zData = zIndicatorFileUtil.uploadIndicatorFile(file);

        // 复制一份zIndicatorFileVoList
        List<ZIndicatorFileVo> zIndicatorFileVoListCopy = new ArrayList<>(zIndicatorFileVoList);

        // 返回上传了相同文件的指标
        List<Map<String, Object>> result = new ArrayList<>();

        // 判断哪些指标上传了相同文件
        for (ZIndicatorFileVo zIndicatorFileVo : zIndicatorFileVoList) {
            // 查询该指标已有的文件
            QueryWrapper<ZData> wrapper = new QueryWrapper<>();
            wrapper.select("indicator_id", "access_url").eq("indicator_id", zIndicatorFileVo.getId()).eq("level", 1);
            List<ZData> zDataList = zDataMapper.selectList(wrapper);

            // 判断文件是否相同
            for (ZData data : zDataList) {
                File file2 = new File(zIndicatorFileUtil.getFileLocalPath(
                        data.getAccessUrl().substring(0, data.getAccessUrl().length() - "pdf".length()) + zData.getType()));
                if (file2.exists()){
                    if (fileEquals.fileEqualsMD5(
                            zData.getAccessUrl(),
                            data.getAccessUrl().substring(0, data.getAccessUrl().length() - "pdf".length()) + zData.getType())
                    ) {
                        // 进行数据清洗
                        for (int k = 0; k < zIndicatorFileVoListCopy.size(); k++) {
                            if (zIndicatorFileVoListCopy.get(k).getId().equals(data.getIndicatorId())) {
                                zIndicatorFileVoListCopy.remove(k);
                            }
                        }
                        // 获取不需要上传文件的指标，用作前端提示
                        Map<String, Object> map = new HashMap<>();
                        map.put("indicatorId", data.getIndicatorId());
                        result.add(map);
                    }
                }
            }
        }

        if (zIndicatorFileVoListCopy.size() > 0) {

            zIndicatorFileUtil.uploadFileToPdf(zData);

            List<ZData> zDataList2 = new ArrayList<>();

            zData.setLevel(1);
            zData.setFileName(zData.getFileName().substring(0, zData.getFileName().length() - zData.getType().length()) + "pdf");
            zData.setAccessUrl(zData.getAccessUrl().substring(0, zData.getAccessUrl().length() - zData.getType().length()) + "pdf");
            zData.setType("pdf");

            for (ZIndicatorFileVo zIndicatorFileVo : zIndicatorFileVoListCopy) {
                // 需要深克隆，不然指向的都是同一个地址
                ZData zData2 = (ZData) zData.clone();
                zData2.setIndicatorId(zIndicatorFileVo.getId());
                if (zIndicatorFileVo.getFolderId() != 0){
                    zData2.setLastId(zIndicatorFileVo.getFolderId());
                }
                zDataList2.add(zData2);
            }
            // 批量插入
            saveBatch(zDataList2);
        }

        return result;
    }

    @Override
    public PageInfo<Map<String, Object>> getEvaluationPlanFile(Integer evaluationPlanId,
                                                               Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String, Object>> evaluationPlanFile = zDataMapper.getEvaluationPlanFile(evaluationPlanId, userId);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(evaluationPlanFile);
        return pageInfo;
    }

    @Override
    public PageInfo<ScoreVo> getEvaluationPlanFile2(Integer evaluationPlanId, Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ScoreVo> evaluationPlanFile2 = zDataMapper.getEvaluationPlanFile2(evaluationPlanId, userId);
        PageInfo<ScoreVo> pageInfo = new PageInfo<>(evaluationPlanFile2);
        return pageInfo;
    }

    @Override
    public List<ScoreVo> getEvaluationPlanFile3(Integer evaluationPlanId, Integer pageNum, Integer pageSize) {
//        PageHelper.startPage(pageNum, pageSize);
        List<ScoreVo> evaluationPlanFile3 = zDataMapper.getEvaluationPlanFile3(evaluationPlanId);
//        PageInfo<ScoreVo> pageInfo = new PageInfo<>(evaluationPlanFile3);
        return evaluationPlanFile3;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String editIndicatorFile(Integer dataId, Integer indicatorId, MultipartFile file) {
        // 先删除原来的文件
        zDataMapper.deleteById(dataId);
        // 将文件存入服务器
        ZData zData = zIndicatorFileUtil.uploadIndicatorFile(file);
        zData.setIndicatorId(indicatorId);
        zDataMapper.insert(zData);
        return "ok";
    }

    @Override
    public String delete(Integer dataId) {
        zDataMapper.deleteById(dataId);
        return "ok";
    }

    @Override
    public List<ZData> getFolderByIndicatorId(List<Integer> indicatorIdList) {
        List<ZData> zDataFolderList = zDataMapper.selectList(
                new QueryWrapper<ZData>()
                        .select("id", "file_name", "indicator_id")
                        .eq("level", 0)
                        .in("indicator_id", indicatorIdList)
        );
        return zDataFolderList;
    }

    @Override
    public void addFolderByIndicatorId(Integer indicatorId, String fileName) {
        ZData zData = new ZData();
        zData.setIndicatorId(indicatorId);
        zData.setFileName(fileName);
        zDataMapper.insert(zData);
    }

    @Override
    public List<ZData> getPublicFolder() {
        return zDataMapper.selectList(
                new QueryWrapper<ZData>()
                        .select("id", "file_name")
                        .eq("data_type", 1)
                        .eq("level", 0)
        );
    }

    @Override
    public void addPublicFolder(String fileName) {
        ZData zData = new ZData();
        zData.setDataType(1);
        zData.setFileName(fileName);
        zDataMapper.insert(zData);
    }

    @Override
    public PageInfo<ZData> getPublicFile(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ZData> zDataList = zDataMapper.selectList(
                new QueryWrapper<ZData>()
                        .select("id", "file_name")
                        .eq("data_type", 1)
                        .eq("level", 1)
        );
        return new PageInfo<>(zDataList);
    }

    @Override
    public String insertPublicFile(Integer folderId, MultipartFile file) {
        //将文件存入服务器
        ZData zData = zIndicatorFileUtil.uploadIndicatorFile(file);
        List<ZData> zDataList = zDataMapper.selectList(
                new QueryWrapper<ZData>()
                        .eq("data_type", 1)
                        .eq("level", 1)
        );
        for (ZData data : zDataList) {
            File file2 = new File(zIndicatorFileUtil.getFileLocalPath(data.getAccessUrl().substring(0, data.getAccessUrl().length() - 3) + zData.getType()));
            if (file2.exists()){
                if (fileEquals.fileEqualsMD5(
                        zData.getAccessUrl(),
                        data.getAccessUrl().substring(0, data.getAccessUrl().length() - 3) + zData.getType())
                ) {
                    return "文件已存在";
                }
            }
        }
        zIndicatorFileUtil.uploadFileToPdf(zData);
        zData.setDataType(1);
        zData.setLevel(1);
        if (folderId != 0) {
            zData.setLastId(folderId);
        }
        zData.setFileName(zData.getFileName().substring(0, zData.getFileName().length() - zData.getType().length()) + "pdf");
        zData.setAccessUrl(zData.getAccessUrl().substring(0, zData.getAccessUrl().length() - zData.getType().length()) + "pdf");
        zData.setType("pdf");
        zDataMapper.insert(zData);
        return "上传成功";
    }

    @Override
    public List<PublicFileVo> getPublicFileTree() {
        return zDataMapper.getPublicFileTree();
    }

    @Override
    public List<PublicFileVo> getIndicatorFileTree(Integer indicatorId) {
        return zDataMapper.getIndicatorFileTree(indicatorId);
    }

}
