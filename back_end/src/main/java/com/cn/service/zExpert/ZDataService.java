package org.jit.sose.service.zExpert;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.jit.sose.entity.assessment.vo.IndicatorFileVo;
import org.jit.sose.entity.assessment.vo.courseIndicatorFileVo;
import org.jit.sose.entity.zExpert.ZData;
import org.jit.sose.entity.zExpert.vo.PublicFileVo;
import org.jit.sose.entity.zExpert.vo.ScoreVo;
import org.jit.sose.entity.zExpert.vo.ZIndicatorFileVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author qinghua
 * @date 2022/5/14 13:30
 */
public interface ZDataService extends IService<ZData> {

    /**
     * 查询选中的指标的文件
     * @param pageNum
     * @param pageSize
     * @param checkNodesList
     * @return {@link PageInfo< ZData> }
     * @author qinghua
     * @date 2022/5/14 13:32
     */
    PageInfo<ZData> selectCheckedIndicatorFile(Integer pageNum, Integer pageSize, List<Integer> checkNodesList);

    /**
     * 指标对应文件上传
     * @param indicatorFileVoList
     * @param file
     * @return {@link List< Map< String, Object>> }
     * @author qinghua
     * @date 2022/5/14 15:46
     */
    List<Map<String,Object>> insertIndicatorFile(List<ZIndicatorFileVo> indicatorFileVoList, MultipartFile file);

    /**
     * 获取该专家绑定评估指标
     * @param evaluationPlanId
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return {@link PageInfo< List< ZData>> }
     * @author qinghua
     * @date 2022/5/14 20:31
     */
    PageInfo<Map<String, Object>> getEvaluationPlanFile(Integer evaluationPlanId, Integer userId, Integer pageNum, Integer pageSize);

    /**
     * 获取该专家绑定评估指标(对结果进行嵌套)
     * @param evaluationPlanId
     * @param userId
     * @return {@link List< ScoreVo> }
     * @author qinghua
     * @date 2022/5/15 20:32
     */
    PageInfo<ScoreVo> getEvaluationPlanFile2(Integer evaluationPlanId, Integer userId, Integer pageNum, Integer pageSize);

    /**
     * 对结果进行嵌套 (三级指标对应的二级和一级都查出来）
     * @param evaluationPlanId
     * @return {@link List< ScoreVo> }
     * @author qinghua
     * @date 2022/5/15 20:32
     */
    List<ScoreVo> getEvaluationPlanFile3(Integer evaluationPlanId, Integer pageNum, Integer pageSize);

    /**
     * 修改指标对应的文件
     * @param dataId
     * @param indicatorId
     * @param file
     * @author qinghua
     * @date 2022/5/14 22:10
     */
    String editIndicatorFile(Integer dataId, Integer indicatorId, MultipartFile file);

    /**
     * 删除表记录，但不删除文件
     * @param dataId
     * @return {@link String }
     * @author qinghua
     * @date 2022/5/15 20:39
     */
    String delete(Integer dataId);

    /**
     * 根据指标id获取该指标绑定的文件夹
     * @param indicatorIdList  指标id数组
     * @return {@link List< ZData> }
     * @author qinghua
     * @date 2022/5/26 17:59
     */
    List<ZData> getFolderByIndicatorId(List<Integer> indicatorIdList);

    /**
     * 为指标添加文件夹
     * @param indicatorId
     * @param fileName
     * @author qinghua
     * @date 2022/5/26 21:34
     */
    void addFolderByIndicatorId(Integer indicatorId, String fileName);

    /**
     * 获取公共文件夹
     *
     * @return {@link List< ZData> }
     * @author qinghua
     * @date 2022/5/27 10:17
     */
    List<ZData> getPublicFolder();

    /**
     * 添加公共文件夹
     * @param fileName  文件夹名
     * @author qinghua
     * @date 2022/5/27 10:48
     */
    void addPublicFolder(String fileName);

    /**
     * 获取公共资料
     *
     * @return {@link List< ZData> }
     * @author qinghua
     * @date 2022/5/27 10:51
     */
    PageInfo<ZData> getPublicFile(Integer pageNum, Integer pageSize);

    /**
     * 添加公共文件
     * @param folderId
     * @param file
     * @return {@link String }
     * @author qinghua
     * @date 2022/5/27 12:06
     */
    String insertPublicFile(Integer folderId, MultipartFile file);

    List<PublicFileVo> getPublicFileTree();

    List<PublicFileVo> getIndicatorFileTree(Integer indicatorId);
}
