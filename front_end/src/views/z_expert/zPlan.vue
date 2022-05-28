<template>
  <div class="layout-main">
    <div>
      <div class="evaluation_plan">
        <el-collapse v-model="activeNames">
          <el-collapse-item :title="collapse_name" name="1">
            请输入评估计划：
            <el-input
              v-model="zPlanName"
              placeholder="请输入评估计划"
              style="margin-top:20px;width:200px"
            ></el-input>
            <el-button
              style="float: right; margin-right: 20px;"
              size="small"
              v-if="collapse_name == '新增评估计划'"
              @click="clearInput"
            >
              重置
            </el-button>
            <el-button
              style="float: right; margin-right: 20px;"
              size="small"
              v-else
              @click="clearInput"
            >
              取消编辑
            </el-button>
            <el-button
              style="float: right; margin-right: 20px;"
              type="primary"
              size="small"
              v-if="collapse_name == '新增评估计划'"
              @click="addEvaluationPlan"
            >
              新增
            </el-button>
            <el-button
              style="float: right; margin-right: 20px;"
              type="primary"
              size="small"
              v-else
              @click="editEvaluationPlan"
            >
              确认编辑
            </el-button>
          </el-collapse-item>
        </el-collapse>

        <i-table
          row-key="id"
          class="table"
          border
          height="auto"
          width="auto"
          :highlight-row="true"
          size="large"
          :columns="column"
          :data="nowData"
          :loading="loading"
          @on-selection-change="onSelectionChange"
        >
          <template slot-scope="{ row }" slot="action">
            <el-button type="primary" @click="edit(row)" size="small">编辑</el-button>
            <el-button type="primary" @click="openIndicator(row)" size="small">指标</el-button>
            <el-button type="primary" @click="openExpert(row)" size="small">专家</el-button>
            <el-button type="danger" @click="deleteEvaluation(row)" size="small">删除</el-button>
          </template>
        </i-table>
        <Page
          style="float:right;"
          :current="pageNum"
          :total="totalNum"
          :page-size="pageSize"
          :page-size-opts="[5, 10, 20, 50]"
          @on-change="onPageChange"
          @on-page-size-change="onPageSizeChange"
          show-total
          show-elevator
          show-sizer
        ></Page>
        <div class="div-inline-left">
          <span v-show="selection.length > 0">已勾选 {{ selection.length }}条</span>
        </div>
      </div>

      <!-- 指标信息管理 -->
      <div style="margin-top: 50px" v-show="indicatorFlag">
        <zIndicator :zPlanId="manageRow.id"></zIndicator>
      </div>

      <!-- 专家信息管理 -->
      <div style="margin-top: 50px" v-show="expertFlag">
        <zExpert :zPlanId="manageRow.id"></zExpert>
      </div>
    </div>
  </div>
</template>

<script>
import zIndicator from '@/views/z_expert/zIndicator'
import zExpert from '@/views/z_expert/zExpert'
export default {
  name: 'zPlan',
  components: {
    zIndicator,
    zExpert
  },
  data() {
    return {
      loading: false, //加载提示，默认是关闭的
      zPlanName: '', //评估计划
      column: [
        {
          title: '计划名称',
          key: 'name'
        }
      ],
      collapse_name: '新增评估计划',
      selection: [],
      transferFlag: false,
      indicatorFlag: false,
      expertFlag: false,
      nowData: [],
      totalNum: 0,
      pageNum: 1,
      pageSize: 5, // 分页参数
      activeNames: '',
      editRow: '',
      manageRow: '',
      dialogVisible: false,
      evaluationPlanList: [],
      row: []
    }
  },
  mounted() {
    this.selectZPlan()
    this.setTableTitle()
  },
  methods: {

    //新增评估计划
    addEvaluationPlan() {
      if (this.zPlanName == '' || this.zPlanName == null) {
        this.$jitsoseVue.MessageWarning('请输入评估计划')
        return
      } else {
        let url = '/zExpert/zPlan/insert'
        let data = {
          name: this.zPlanName
        }
        this.$request.post(url, data).then(data => {
          this.$jitsoseVue.MessageSuccess('添加成功')
          this.selectZPlan()
          this.clearInput()
        })
      }
    },

    //查询评估计划
    selectZPlan() {
      let url = '/zExpert/zPlan/selectZPlanList'
      let data = {
        pageNum: this.pageNum,
        pageSize: this.pageSize
      }
      this.$request.post(url, data).then(data => {
        console.log('data', data)
        this.nowData = data.data.obj.list
        this.totalNum = data.data.obj.total
        // 再次设置当前页码
        this.pageNum = data.data.obj.pageNum === 0 ? 1 : data.data.obj.pageNum
      })
    },

    //编辑操作
    edit(row) {
      console.log('row', row)
      this.collapse_name = '编辑评估计划'
      this.activeNames = '1'
      this.editRow = row
      this.zPlanName = row.name
    },

    //编辑评估计划
    editEvaluationPlan() {
      if (this.zPlanName == '' || this.zPlanName == null) {
        this.$jitsoseVue.MessageWarning('请先输入评估计划名称')
        return
      } else {
        let url = '/zExpert/zPlan/update'
        let data = {
          id: this.editRow.id,
          name: this.zPlanName
        }
        this.$request.post(url, data).then(data => {
          this.$jitsoseVue.MessageSuccess('编辑成功！')
          this.clearInput()
          this.selectZPlan()
        })
      }
    },

    //删除评估计划
    deleteEvaluation(row) {
      this.$confirm('此操作将删除该评估计划，是否确认删除？', '提示')
        .then(() => {
          let url = '/zExpert/zPlan/deleteById/' + row.id
          this.$request.get(url).then(data => {
            if (data.data.obj > 0) {
              this.$jitsoseVue.MessageSuccess('删除成功！')
              this.selectZPlan()
            }
          })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
    },

    // 打开指标管理
    openIndicator(row) {
      this.expertFlag = false
      if (row.id === this.manageRow.id) {
        this.indicatorFlag = !this.indicatorFlag
      } else {
        this.manageRow = row
        this.indicatorFlag = true
      }
    },

    // 打开专家管理
    openExpert(row) {
      this.indicatorFlag = false
      if (row.id === this.manageRow.id) {
        this.expertFlag = !this.expertFlag
      } else {
        this.manageRow = row
        this.expertFlag = true
      }
    },

    //重置搜索
    clearInput() {
      this.collapse_name = '新增评估计划'
      this.zPlanName = ''
      this.editRow = ''
    },

    /**
     * 在多选模式下有效，只要选中项发生变化时就会触发
     *
     * @param selection
     * 已选项数据
     */
    onSelectionChange(selection) {
      this.selection = selection
    },

    /**
     * 改变页码
     *
     * @param pageNum
     * 改变后的页码
     */
    onPageChange(pageNum) {
      this.pageNum = pageNum
      this.selectZPlan()
    },

    /**
     * 切换每页条数
     * @param pageSize
     * 换后的每页条数
     */
    onPageSizeChange(pageSize) {
      this.pageSize = pageSize
      this.selectZPlan()
    },

    /**
     * 设置表头
     */
    setTableTitle() {
      // // 添加自定义slot-scope
      this.column.push(this.$tableTitleUtil.HeadActionSlot(true, 400))
      console.log('this.column', this.column)
      // // 添加多选
      // this.column.unshift(this.$tableTitleUtil.HeadSelection(true));
      //   console.log(this.column);
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
