package org.jit.sose.entity.zExpert;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

/**
 * @Date 2022/5/10 17:01
 * @Author qinghua
 */
@Data
public class ZExpertIndicator {

    private Integer id;

    private Integer indicatorId;

    private Integer expertId;

    private String score;

    private String opinion;

    @TableLogic
    private String state;
}
