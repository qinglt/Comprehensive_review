package org.jit.sose.entity.zExpert;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

/**
 * @Date 2022/5/9 21:03
 * @Author qinghua
 */
@Data
public class ZExpert {

    private Integer id;

    private Integer userId;

    private Integer planId;

    @TableLogic
    private String state;

}
