package org.jit.sose.entity.zExpert;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

/**
 * @Date 2022/5/8 10:41
 * @Author qinghua
 */
@Data
public class ZPlan {

    private Integer id;

    private String name;

    @TableLogic
    private String state;

}
