<template>
  <div class="layout-main" style="display: flex">
    <el-transfer
      v-model="expertData"
      :data="userData"
      :titles="['专家库', '参评专家']"
      @change="handleChange"
      :props="{ key: 'id', label: 'username' }"
    ></el-transfer>

    <div>
      <div>
        <div style="display: flex;margin-left: 100px;margin-bottom:20px">
          <!-- 计划 -->
          <div style="margin-bottom:10px">
            当前选择评估计划：{{ planList[planId] }}
          </div>
        </div>
      </div>
      <div class="el2" style="display: flex;margin-left: 100px">
        <!--    一列专家按钮-->
        <div class="list">
          <List border header="选择专家" size="small">
            <template v-for="item in expertList">
              <ListItem v-bind:key="item.id" style="text-align: center">
                <el-button style="margin: auto" size="small" @click="getIndicatorTree(item)">
                  {{ item.expertName }}
                </el-button>
              </ListItem>
            </template>
          </List>
        </div>

        <!--    指标树卡片-->
        <div class="tab" style="width: 400px">
          <el-tabs v-model="activeName" type="border-card">
            <el-tab-pane label="选择指标" name="选择指标" key="选择指标">
              <!--指标树-->
              <el-tree
                :data="indicatorData"
                show-checkbox
                default-expand-all
                node-key="id"
                :default-checked-keys="indicatorIdList"
                highlight-current
                :props="treeProps"
                ref="tree"
              ></el-tree>
              <div class="tab-foot">
                <i-button type="primary" size="large" @click="add" style="float:right">
                  确定
                </i-button>
                <i-button
                  type="primary"
                  size="large"
                  @click="copy"
                  style="float:right;margin-right:20px"
                >
                  快速复制
                </i-button>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>
    <el-dialog
      title="复制计划课程"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose"
    >
      <div>
        <!-- 计划 -->
        <div style="margin-bottom:10px">选择复制的专家：</div>
        <el-select v-model="expertCopy" placeholder="请选择专家" style="width:250px">
          <el-option
            v-for="item in expertList"
            :key="item.userId"
            :label="item.expertName"
            :value="item.userId"
            :disabled="item.disabled"
          ></el-option>
        </el-select>
        <div style="color:red;margin-top:10px">警告：确认复制将清除该专家已有的绑定指标</div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="confirmCopy">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: 'zExpert',
  props: {
    zPlanId: Number
  },
  components: {},
  data() {
    return {
      planId: '', // 计划id
      evaluationPlanList: [], //评估计划列表
      planList: [], //评估计划列表 (id: name)
      userData: [], //用户数据
      expertData: [], //专家的userId集合
      expertList: [], //专家集合 (userId，expertName)
      indicatorData: [], //指标数据
      dialogVisible: false,
      expertCopy: '',
      row: [],
      treeProps: {
        children: 'childList',
        label: 'name'
      },
      loading: false, //加载提示，默认是关闭的
      activeName: '选择指标', //选中选项卡的name
      userId: '', // userId
      indicatorIdList: [] //通过专家查找到的已绑定的指标标识集合
    }
  },
  watch: {
    zPlanId(val) {
      this.planId = val
      this.UserFilter()
      this.selectAllExperts()
      this.getIndicatorData()
      this.getEvaluationPlanList()
    }
  },
  mounted() {
    this.getEvaluationPlanList()
  },
  methods: {
    //复制出新的计划课程副本
    copy() {
      if (this.userId == null || this.userId == '') {
        this.$jitsoseVue.MessageWarning('请选择复制的对象！')
        return
      }
      this.dialogVisible = true
      // 设置禁用选项
      for(let i = 0; i < this.expertList.length; i++) {
        this.$set(this.expertList[i], 'disabled', false)
        if(this.expertList[i].userId === this.userId) {
          this.$set(this.expertList[i], 'disabled', true)
        }
      }
    },

    //确认复制
    confirmCopy() {
      if (this.expertCopy == null || this.expertCopy == '') {
        this.$jitsoseVue.MessageWarning('请选择被复制的对象！')
        return
      }
      let url = '/zExpert/zExpertIndicator/expertCopy'
      let data = {
        userIdCopied: this.expertCopy, //被复制的
        planId: this.planId, //计划id
        userId: this.userId //复制到的
      }
      console.log(data)
      this.$request.post(url, data).then(data => {
        if (data.data.msg == 'Success') {
          this.$jitsoseVue.MessageSuccess('复制成功！')
        }
        this.userId = ''
        this.expertCopy = ''
        this.dialogVisible = false
        this.$refs.tree.setCheckedKeys([])
      })
    },

    //关闭绑定前的确认
    handleClose() {
      this.$confirm('确认关闭？')
        .then(_ => {
          this.userId = ''
          this.expertCopy = ''
          this.dialogVisible = false
          this.$refs.tree.setCheckedKeys([])
          done()
        })
        .catch(_ => {})
    },

    //获取评估计划列表
    getEvaluationPlanList() {
      let url = '/zExpert/zPlan/getZPlanList'
      this.$request.get(url).then(data => {
        this.evaluationPlanList = data.data.obj
        for (let i = 0; i < this.evaluationPlanList.length; i++) {
          this.$set(this.planList, this.evaluationPlanList[i].id, this.evaluationPlanList[i].name)
        }
      })
    },

    //点击专家按钮触发
    getIndicatorTree(item) {
      this.$refs.tree.setCheckedKeys([])
      console.log('点击专家按钮触发:', item)
      this.userId = item.userId
      this.sIndicatorIdListByExpertId()
    },

    //通过专家标识查询指标标识集合
    sIndicatorIdListByExpertId() {
      let data = {
        userId: this.userId,
        planId: this.planId
      }
      console.log('通过专家标识查询指标标识集合:', data)
      let url = '/zExpert/zExpertIndicator/sIndicatorIdListByExpertId'
      this.$request.post(url, data).then(data => {
        console.log('通过专家标识查询指标标识集合:', data.data.obj)
        this.indicatorIdList = data.data.obj
      })
    },

    //给专家分配指标
    add() {
      if (this.userId === null || this.userId === '') {
        this.$jitsoseVue.MessageWarning('请选择专家')
        return
      }
      // 删除全选时选中的一级指标和二级指标
      let indicatorList = []
      for (let item of this.$refs.tree.getCheckedNodes()) {
        if (
          item.level == '3' ||
          (item.level == '2' && item.childList.length == 0) ||
          (item.level == '1' && item.childList.length == 0)
        ) {
          indicatorList.push(item.id)
        }
      }
      let data = {
        userId: this.userId,
        indicatorIdList: indicatorList,
        planId: this.planId
      }
      let msg = this.$jitsoseVue.MessageLoading()
      let url = '/zExpert/zExpertIndicator/inserts'
      this.$request.post(url, data).then(data => {
        this.$jitsoseVue.CloseMessageLoading(msg)
        this.$jitsoseVue.MessageSuccess('设置成功')
        this.$refs.tree.setCheckedKeys([])
      })
    },

    // 以树的形式查询一级二级指标集合
    getIndicatorData() {
      let url = '/zExpert/indicator/selectListAndList2/' + this.planId
      this.$request.get(url).then(data => {
        console.log('以树的形式查询一级二级指标集合：', data.data.obj)
        //获取后台数据
        this.indicatorData = data.data.obj
      })
    },

    //查询所有专家数据
    selectAllExperts() {
      let url = '/zExpert/zExpert/queryByPlanId/' + this.planId
      this.$request.get(url).then(data => {
        this.expertData = []
        for (let i = 0; i < data.data.obj.length; i++) {
          this.expertData.push(data.data.obj[i].userId)
        }
        this.expertList = data.data.obj
      })
    },

    // 批量添加/删除专家
    handleChange(value, direction, movedKeys) {
      if (direction === 'right') {
        let url = '/zExpert/zExpert/insert'
        let data = {
          idList: movedKeys,
          planId: this.planId
        }
        this.$request.post(url, data).then(data => {
          this.selectAllExperts()
        })
      } else {
        let url = '/zExpert/zExpert/delete'
        let data = {
          idList: movedKeys,
          planId: this.planId
        }
        this.$request.post(url, data).then(data => {
          this.selectAllExperts()
        })
      }
    },

    /**
     * 查询所有绑定老师的账户信息
     */
    UserFilter() {
      let data = {}
      let url = '/user/selectUserAndStaff'
      this.loading = true
      this.$request.post(url, data).then(data => {
        // 拼接数据 => 账号(姓名)
        for (let i = 0; i < data.data.obj.length; i++) {
          data.data.obj[i].username =
            data.data.obj[i].username + '(' + data.data.obj[i].staffName + ')'
        }
        this.loading = false
        this.userData = data.data.obj
      })
    }
  }
}
</script>
<style lang="less" scoped>
#locale {
  border: 24px solid white;
  padding: 5px 5px 70px 5px;
  /*设置div的高度height为calc(100vh)即可,100vh = 视窗高度的100%*/
  height: calc(100vh);
  overflow-y: auto;
}

.list {
  /* background: red; */
  display: inline;
  float: left;
  width: 17%;
  min-width: 150px;
  max-width: 200px;
}
.tab {
  display: inline;
  float: left;
  margin-left: 3px;
  width: 81%;
  height: 700px;
}

.search-card {
  height: 70px;
}

.search-text {
  font-size: 16px;
  margin-left: 10px;
}

.search-input {
  width: 200px;
}

.bufRight {
  width: 200px;
  display: block;
  float: right;
}

.add-card {
  height: 500px;
  width: 800px;
  margin: auto;
}

.addSpan {
  text-align: center;
  font-size: 18px;
  color: #17233d;
  font-weight: 200;
}

.tableOut {
  text-align: center;
}

.tableIn {
  display: inline-block;
  width: 400px;
  font-size: 14px;
}

.input {
  margin-left: 20px;
  margin-top: 5px;
}

.addSure {
  text-align: center;
}

.button {
  margin-bottom: 5px;
}

.bufLeft {
  width: 200px;
  margin-top: 20px;
  /* margin-left: 50px; */
  display: inline-block;
  margin-left: 50px;
}

.table1 {
  width: 22%;
}

.table2 {
  width: 22%;
}

.table3 {
  width: 22%;
}
</style>
