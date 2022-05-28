<template>
  <div class="layout-main">
    <!-- 搜索及按钮区域 -->
    当前选择评估计划：{{ planList[planId] }}
    <div>
      <el-collapse class="collapseStyle" v-model="activeNames" @change="handleChange" accordion>
        <el-collapse-item title="新增" name="2">
          <!-- 编辑模块 -->
          <div class="edit" v-if="addOrEdit === 0">
            <el-card shadow="hover">
              <div class="addSpan">
                <span>修改指标</span>
              </div>
              <div class="tableOut">
                <div class="tableIn">
                  <table style="text-align: left">
                    <el-row>
                      <el-col :span="8">
                        <td>指标名：</td>
                      </el-col>
                      <el-col :span="16">
                        <el-input
                          class="input"
                          type="text"
                          v-model.trim="indicator.name"
                          placeholder=""
                        ></el-input>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="8">
                        <td>上级指标:</td>
                      </el-col>
                      <el-col :span="16">
                        <el-select
                          v-model="indicator.lastId"
                          placeholder=""
                          filterable
                          class="select_input"
                        >
                          <el-option
                            v-for="item in lastLevelList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                          ></el-option>
                        </el-select>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="8">
                        <td>导向要求:</td>
                      </el-col>
                      <el-col :span="16">
                        <el-input
                          class="input"
                          type="textarea"
                          v-model.trim="indicator.guide"
                          placeholder=""
                          maxlength="500"
                          :autosize="{ minRows: 2, maxRows: 8 }"
                          show-word-limit
                        ></el-input>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="8">
                        <td>限定:</td>
                      </el-col>
                      <el-col :span="16">
                        <el-input
                          class="input"
                          type="textarea"
                          v-model.trim="indicator.limit"
                          placeholder=""
                          maxlength="500"
                          :autosize="{ minRows: 2, maxRows: 8 }"
                          show-word-limit
                        ></el-input>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="8">
                        <td>建设标准:</td>
                      </el-col>
                      <el-col :span="16">
                        <el-input
                          class="input"
                          type="textarea"
                          v-model.trim="indicator.standard"
                          placeholder=""
                          maxlength="500"
                          :autosize="{ minRows: 2, maxRows: 8 }"
                          show-word-limit
                        ></el-input>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="8">
                        <td>牵头单位:</td>
                      </el-col>
                      <el-col :span="16">
                        <el-input
                          class="input"
                          type="text"
                          v-model.trim="indicator.company"
                          placeholder=""
                          maxlength="20"
                        ></el-input>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="8">
                        <td>制定人:</td>
                      </el-col>
                      <el-col :span="16">
                        <el-input
                          class="input"
                          type="text"
                          v-model.trim="indicatorExpertMap[indicator.id]"
                          placeholder=""
                          maxlength="20"
                          disabled
                        ></el-input>
                      </el-col>
                    </el-row>
                  </table>
                </div>
              </div>
              <div class="addSure">
                <i-button
                  type="primary"
                  size="large"
                  style="margin-right: 50px"
                  @click="editTermInformation"
                >
                  确定
                </i-button>
                <i-button
                  type="primary"
                  size="large"
                  style="margin-right: 50px"
                  @click="cancelEditTerm"
                >
                  取消
                </i-button>
                <i-button
                  v-if="addOrEdit === 0"
                  type="primary"
                  size="large"
                  style="margin-right: 50px"
                  @click="deleteTermInformation"
                >
                  删除
                </i-button>
              </div>
            </el-card>
          </div>
          <!-- 新增模块 -->
          <div class="add" v-else>
            <el-card shadow="hover">
              <div class="addSpan">
                <span>指标</span>
              </div>
              <div class="tableOut">
                <div class="tableIn">
                  <table style="text-align: left">
                    <el-row>
                      <el-col :span="8">
                        <td>指标名：</td>
                      </el-col>
                      <el-col :span="16">
                        <el-input
                          class="input"
                          type="text"
                          v-model.trim="indicator.name"
                          placeholder=""
                        ></el-input>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="8">
                        <td>上级指标:</td>
                      </el-col>
                      <el-col :span="16">
                        <el-select
                          v-model="indicator.lastId"
                          placeholder=""
                          filterable
                          class="select_input"
                        >
                          <el-option
                            v-for="item in lastLevelList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                          ></el-option>
                        </el-select>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="8">
                        <td>导向要求:</td>
                      </el-col>
                      <el-col :span="16">
                        <el-input
                          class="input"
                          type="textarea"
                          v-model.trim="indicator.guide"
                          placeholder=""
                          maxlength="500"
                          :autosize="{ minRows: 2, maxRows: 8 }"
                          show-word-limit
                        ></el-input>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="8">
                        <td>限定:</td>
                      </el-col>
                      <el-col :span="16">
                        <el-input
                          class="input"
                          type="textarea"
                          v-model.trim="indicator.limit"
                          placeholder=""
                          maxlength="500"
                          :autosize="{ minRows: 2, maxRows: 8 }"
                          show-word-limit
                        ></el-input>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="8">
                        <td>建设标准:</td>
                      </el-col>
                      <el-col :span="16">
                        <el-input
                          class="input"
                          type="textarea"
                          v-model.trim="indicator.standard"
                          placeholder=""
                          maxlength="500"
                          :autosize="{ minRows: 2, maxRows: 8 }"
                          show-word-limit
                        ></el-input>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="8">
                        <td>牵头单位:</td>
                      </el-col>
                      <el-col :span="16">
                        <el-input
                          class="input"
                          type="text"
                          v-model.trim="indicator.company"
                          placeholder=""
                          maxlength="20"
                        ></el-input>
                      </el-col>
                    </el-row>
                  </table>
                </div>
              </div>
              <div class="addSure">
                <i-button type="primary" size="large" style="margin-right: 120px" @click="addTerm">
                  新增
                </i-button>
                <i-button
                  type="primary"
                  size="large"
                  style="margin-right: 30px"
                  @click="cancelAddTerm"
                >
                  取消
                </i-button>
              </div>
            </el-card>
          </div>
        </el-collapse-item>
      </el-collapse>
    </div>
    <!--树展示-->
    <div>
      <el-tree
        :data="nowData"
        :props="treeProps"
        @node-click="editLocale"
        node-key="id"
        :default-expand-all="true"
        :expand-on-click-node="false"
      ></el-tree>
    </div>
  </div>
</template>
<script>
export default {
  name: 'zIndicator',
  props: {
    zPlanId: Number
  },
  components: {},
  data() {
    return {
      planId: '',
      nowData: [], //从后端获取的数据
      activeNames: '', //当前激活的面板
      indicator: {
        id: '',
        lastId: '',
        name: '',
        content: '',
        level: '',
        company: '',
        planId: '',
        guide: '',
        limit: '',
        standard: ''
      },
      lastLevelList: [], //所有上级指标集合
      zPlanList: [], //所有计划集合
      planList: [],
      indicatorExpertMap: [], //所有专家集合
      treeProps: {
        children: 'childList',
        label: 'name'
      }, //树组件参数
      addOrEdit: '', //控制编辑和新增框的显示，1为新增，0为编辑
      loading: false //加载提示，默认是关闭的
    }
  },
  watch: {
    zPlanId(val) {
      this.planId = val
      this.getExpertName()
      this.getPlanList()
      this.filter()
      this.activeNames = ''
    }
  },
  mounted() {},
  methods: {
    // 获取指定人列表
    getExpertName() {
      let url = '/zExpert/indicator/getExpertName'
      this.$request.get(url).then(data => {
        console.log('专家', data)
        for (let i = 0; i < data.data.obj.length; i++) {
          this.$set(
            this.indicatorExpertMap,
            data.data.obj[i].indicatorId,
            data.data.obj[i].expertName
          )
        }
      })
    },

    //  获取所有计划
    getPlanList() {
      let url = '/zExpert/zPlan/getZPlanList'
      this.$request.get(url).then(data => {
        this.zPlanList = data.data.obj
        for (let i = 0; i < data.data.obj.length; i++) {
          this.$set(this.planList, data.data.obj[i].id, data.data.obj[i].name)
        }
      })
    },

    // 删除数据
    deleteTermInformation() {
      console.log('有没有删除按钮', this.addOrEdit)
      let that = this
      this.loadingMsg = this.$jitsoseVue.MessageLoading() //打开加载提示
      let data = {
        id: this.indicator.id,
        state: 'X'
      }
      let url = '/zExpert/indicator/updateById'
      this.$request.post(url, data).then(data => {
        that.$jitsoseVue.CloseMessageLoading(that.loadingMsg) //关闭加载提示
        that.$jitsoseVue.MessageSuccess('删除成功')
        that.cancelEditTerm()
        that.filter()
      })
    },
    // 修改数据
    editTermInformation() {
      let that = this
      this.loadingMsg = this.$jitsoseVue.MessageLoading() //打开加载提示
      let data = {
        id: this.indicator.id,
        name: this.indicator.name,
        content: this.indicator.content,
        level: this.indicator.level,
        lastId: this.indicator.lastId,
        company: this.indicator.company,
        planId: this.indicator.planId,
        guide: this.indicator.guide,
        limit: this.indicator.limit,
        standard: this.indicator.standard
      }
      let url = '/zExpert/indicator/updateById'
      this.$request.post(url, data).then(data => {
        that.$jitsoseVue.CloseMessageLoading(that.loadingMsg) //关闭加载提示
        that.$jitsoseVue.MessageSuccess('修改成功')
        that.cancelEditTerm()
        that.filter()
      })
    },
    // 取消修改数据
    cancelEditTerm() {
      this.indicator.name = ''
      this.indicator.content = ''
      this.indicator.level = ''
      this.indicator.lastId = ''
      this.indicator.company = ''
      this.indicator.planId = ''
      this.indicator.guide = ''
      this.indicator.limit = ''
      this.indicator.standard = ''
      this.activeNames = ''
    },
    // 点击编辑框，展示原来数据
    editLocale(data) {
      console.log('点击编辑框，展示原来数据', data)
      this.activeNames = '2'
      this.addOrEdit = 0
      this.indicator.id = data.id
      this.indicator.name = data.name
      this.indicator.content = data.content
      this.indicator.level = data.level
      this.indicator.lastId = data.lastId
      this.indicator.company = data.company
      this.indicator.planId = data.planId
      this.indicator.guide = data.guide
      this.indicator.limit = data.limit
      this.indicator.standard = data.standard
    },
    // 以树的形式查询一级二级指标集合
    filter() {
      let that = this
      let url = '/zExpert/indicator/selectListAndList2/' + this.planId
      this.loading = true
      this.$request.get(url).then(data => {
        that.loading = false
        //获取后台数据
        that.nowData = data.data.obj
        that.lastLevelList = []
        for (let i = 0; i < that.nowData.length; i++) {
          that.lastLevelList.push({
            id: that.nowData[i].id,
            name: that.nowData[i].name,
            level: that.nowData[i].level
          })
          for (let j = 0; j < that.nowData[i].childList.length; j++) {
            that.lastLevelList.push({
              id: that.nowData[i].childList[j].id,
              name: that.nowData[i].childList[j].name,
              level: that.nowData[i].childList[j].level
            })
          }
        }
      })
    },
    // 新增数据
    addTerm() {
      if (this.indicator.name === '' || this.indicator.name === null) {
        this.$jitsoseVue.MessageWarning('指标名不能为空')
        return
      }
      let that = this
      this.loadingMsg = this.$jitsoseVue.MessageLoading() //打开加载提示
      // 设置指标级别
      for (let i = 0; i < this.lastLevelList.length; i++) {
        if (this.indicator.lastId === this.lastLevelList[i].id) {
          this.indicator.level = this.lastLevelList[i].level + 1
        }
      }
      let data = {
        name: this.indicator.name,
        content: this.indicator.content,
        level: this.indicator.level,
        lastId: this.indicator.lastId,
        company: this.indicator.company,
        planId: this.planId,
        guide: this.indicator.guide,
        limit: this.indicator.limit,
        standard: this.indicator.standard
      }
      let url = '/zExpert/indicator/insert/'
      this.$request.post(url, data).then(data => {
        that.$jitsoseVue.CloseMessageLoading(that.loadingMsg) //关闭加载提示
        that.$jitsoseVue.MessageSuccess('添加成功')
        that.cancelAddTerm()
        that.activeNames = ''
        that.filter()
        that.getExpertName()
      })
    },
    // 取消新增
    cancelAddTerm() {
      this.indicator.name = ''
      this.indicator.content = ''
      this.indicator.level = ''
      this.indicator.lastId = ''
      this.indicator.company = ''
      this.indicator.planId = ''
      this.indicator.guide = ''
      this.indicator.limit = ''
      this.indicator.standard = ''
      this.activeNames = ''
    },
    // 展开框状态改变触发事件
    handleChange(val) {
      // 展示新增框
      this.addOrEdit = 1
    }
  }
}
</script>
<style lang="less" scoped>
.el-row {
  margin-bottom: 10px;
}

#term {
  border: 24px solid white;
  padding: 5px 5px 70px 5px;
  /*设置div的高度height为calc(100vh)即可,100vh = 视窗高度的100%*/
  height: calc(100vh);
  overflow-y: auto;
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
</style>
