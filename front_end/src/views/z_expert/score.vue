<template>
  <div class="layout-main">
    <div class="giveScore">
      <div class="evaluationPlan">
        <!-- 计划 -->
        <div style="margin-bottom:10px">选择评估计划：</div>
        <el-select v-model="evaluationPlan" placeholder="请选择评估计划" style="width:250px">
          <el-option
            v-for="item in evaluationPlanList"
            :key="'evaluationPlan' + item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </div>

      <div class="table" style="margin-top:50px;">
        <div style="margin-bottom:20px">专家评审数据</div>
        <i-table
          row-key="id"
          class="table"
          border
          height="auto"
          width="auto"
          :highlight-row="true"
          size="large"
          :columns="column"
          :data="fileData"
          :loading="loading"
          :span-method="handleSpan"
        >
          <template slot-scope="{ row }" slot="action">
            <el-button icon="el-icon-search" size="mini" circle @click="openFile(row)"></el-button>
            <el-button
              type="primary"
              size="mini"
              @click="giveScore(row)"
              v-if="row.score == 'X' && row.ascription == true"
            >
              开始评测
            </el-button>
            <el-button
              type="info"
              size="mini"
              @click="editScore(row)"
              v-if="row.score != 'X' && row.ascription == true"
            >
              重新测评
            </el-button>
          </template>
        </i-table>
        <!-- <Page
          style="float:right;"
          :current="pageNum"
          :total="totalNum"
          :page-size="pageSize"
          @on-change="onPageChange"
          @on-page-size-change="onPageSizeChange"
          show-total
          show-elevator
          show-sizer
        ></Page> -->
      </div>
    </div>
    <el-dialog
      title="评测"
      :visible.sync="dialogVisible"
      width="30%"
      :close-on-click-modal="false"
      :before-close="handleClose"
    >
      <div class="dialogContent">
        <div class="level" style="text-align:center">
          等级：
          <el-select v-model="level" placeholder="请选择等级">
            <el-option
              v-for="item in levelList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </div>
        <div class="opinion">
          评审意见：
          <el-input
            v-model="opinion"
            placeholder="请输入评审意见"
            :rows="10"
            type="textarea"
            style="margin-top:10px"
            maxlength="1000"
            show-word-limit
          ></el-input>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="commitScore">确 定</el-button>
        <!-- <el-button type="primary" @click="commitEdit" v-else>确认编辑</el-button> -->
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'score',
  data() {
    return {
      evaluationPlan: '', //选中的评估计划
      evaluationPlanList: [], //评估计划
      treeDataMap: [], //指标树map(key:id, value:name)
      fileData: [], //table数据
      fileDataCopy: [], //评测数据
      column: [
        {
          title: '一级指标',
          key: 'indicatorName1'
        },
        {
          title: '二级指标',
          key: 'indicatorName2'
        },
        {
          title: '三级指标',
          key: 'indicatorName3'
        },
        {
          title: '评审文件',
          key: 'zdataVoList',
          tooltip: true
        },
        {
          title: 'Action',
          slot: 'action',
          width: 240,
          align: 'center'
        }
      ], //table列表头参数
      loading: false,
      totalNum: 0,
      selection: [],
      pageNum: 1,
      pageSize: 10,
      dialogVisible: false,
      levelList: [
        {
          value: 'A',
          label: 'A'
        },
        {
          value: 'B',
          label: 'B'
        },
        {
          value: 'C',
          label: 'C'
        }
      ],
      level: '', //等级
      opinion: '', //建议
      row: [],
      editFlag: false
    }
  },

  watch: {
    evaluationPlan: {
      handler(newVal, oldVal) {
        if (newVal != '' || newVal != null) {
          this.getEvaluationPlanFile()
        }
      },
      deep: true
    }
  },

  mounted() {
    this.getEvaluationPlanList()
    this.initPage()
  },

  methods: {
    //获取评估计划列表
    getEvaluationPlanList() {
      let url = '/zExpert/zPlan/getZPlanList'
      this.$request.get(url).then(data => {
        this.evaluationPlanList = data.data.obj
      })
    },

    //获取待评估的材料
    getEvaluationPlanFile() {
      let that = this
      if (this.evaluationPlan == '' || this.evaluationPlan == null) {
        this.$jitsoseVue.MessageWarning('请先选择评估计划！')
        return
      }
      let url = '/zExpert/zData/getEvaluationPlanFile3'
      let data = {
        evaluationPlanId: this.evaluationPlan
        // pageNum: this.pageNum,
        // pageSize: this.pageSize
      }
      this.$request.post(url, data).then(data => {
        this.fileData = data.data.obj
        this.fileDataCopy = JSON.parse(JSON.stringify(this.fileData)) // 复制一份fileData
        // 拼接文件名：文件1，文件2，......
        for (let i = 0; i < this.fileData.length; i++) {
          this.fileData[i].zdataVoList = ''
        }
        for (let i = 0; i < this.fileData.length; i++) {
          for (let j = 0; j < this.fileDataCopy[i].zdataVoList.length; j++) {
            this.fileData[i].zdataVoList =
              this.fileData[i].zdataVoList + this.fileDataCopy[i].zdataVoList[j].fileName + ', '
          }
        }
        // end

        // 合并单元格
        this.fileData = this.assembleData(this.fileData)
        // this.totalNum = data.data.obj.total
        // 再次设置当前页码
        // this.pageNum = data.data.obj.pageNum === 0 ? 1 : data.data.obj.pageNum
        // end

        // 获取打分
        let url2 = '/zExpert/zExpertIndicator/getIndicatorScoreByExpert'
        let data2 = {
          evaluationPlanId: this.evaluationPlan
        }
        this.$request.post(url2, data2).then(data => {
          for (let i = 0; i < that.fileData.length; i++) {
            // 设置标识符：该指标是否分配于该专家
            that.$set(that.fileData[i], 'ascription', false)
            for (let j = 0; j < data.data.obj.length; j++) {
              if (that.fileData[i].indicatorId == data.data.obj[j].indicatorId) {
                that.fileData[i].score = data.data.obj[j].score
                that.fileData[i].opinion = data.data.obj[j].opinion
                that.fileData[i].ascription = true
              }
            }
          }
          console.log(that.fileData)
        })
      })
    },

    // 合并单元格
    handleSpan({ row, column, rowIndex, columnIndex }) {
      //合并第1列,这里columnIndex==1 根据具体业务要在前端写死
      if (columnIndex == 0) {
        //计算合并的行数列数
        let x = row.mergeCol == 0 ? 0 : row.mergeCol
        let y = row.mergeCol == 0 ? 0 : 1
        //console.log(x , y)
        return [x, y]
      }
      if (columnIndex == 1) {
        //计算合并的行数列数
        let x = row.mergeCol2 == 0 ? 0 : row.mergeCol2
        let y = row.mergeCol2 == 0 ? 0 : 1
        //console.log(x , y)
        return [x, y]
      }
    },

    assembleData(data) {
      // 先合并第一列
      let names = []
      //筛选出不重复的 name值,将其放到 names数组中
      data.forEach(e => {
        if (!names.includes(e.indicatorName1)) {
          names.push(e.indicatorName1)
        }
      })
      let nameNums = []
      //将names数组中的 name值设置默认合并0个单元格,放到 nameNums中
      names.forEach(e => {
        nameNums.push({ name: e, num: 0 })
      })
      //计算每种 name值所在行需要合并的单元格数
      data.forEach(e => {
        nameNums.forEach(n => {
          if (e.indicatorName1 == n.name) {
            n.num++
          }
        })
      })
      //将计算后的合并单元格数整合到 data中
      data.forEach(e => {
        nameNums.forEach(n => {
          if (e.indicatorName1 == n.name) {
            if (names.includes(e.indicatorName1)) {
              e.mergeCol = n.num
              //删除已经设置过的值(防止被合并的单元格进到这个 if 语句中)
              names.splice(names.indexOf(n.name), 1)
            } else {
              //被合并的单元格设置为 0
              e.mergeCol = 0
            }
          }
        })
      })
      // end

      // 再合并第二列
      names = []
      //筛选出不重复的 name值,将其放到 names数组中
      data.forEach(e => {
        if (!names.includes(e.indicatorName2)) {
          names.push(e.indicatorName2)
        }
      })
      nameNums = []
      //将names数组中的 name值设置默认合并0个单元格,放到 nameNums中
      names.forEach(e => {
        nameNums.push({ name: e, num: 0 })
      })
      //计算每种 name值所在行需要合并的单元格数
      data.forEach(e => {
        nameNums.forEach(n => {
          if (e.indicatorName2 == n.name) {
            n.num++
          }
        })
      })
      //将计算后的合并单元格数整合到 data中
      data.forEach(e => {
        nameNums.forEach(n => {
          if (e.indicatorName2 == n.name) {
            if (names.includes(e.indicatorName2)) {
              e.mergeCol2 = n.num
              //删除已经设置过的值(防止被合并的单元格进到这个 if 语句中)
              names.splice(names.indexOf(n.name), 1)
            } else {
              //被合并的单元格设置为 0
              e.mergeCol2 = 0
            }
          }
        })
      })
      // end

      //将整理后的数据交给表格渲染
      return data
    },

    //打开文件
    openFile(row) {
      this.$router.push({
        path: '/zExpert/zIndicatorDetails',
        query: {
          row: JSON.stringify(row),
          evaluationPlan: this.evaluationPlan
        }
      })
      // let routerUrl = this.$router.resolve({
      //   path: '/zExpert/zIndicatorDetails',
      //   query: {
      //     row: JSON.stringify(row)
      //   }
      // })
      // window.open(routerUrl.href, '_blank')
    },

    //评测分数
    giveScore(row) {
      this.row = row
      this.dialogVisible = true
      console.log('row', row)
    },

    //重新编辑测评分数
    editScore(row) {
      this.level = row.score
      this.opinion = row.opinion
      this.row = row
      this.dialogVisible = true
    },

    //确认提交
    commitScore() {
      console.log('level', this.level)
      if (this.level == '' || this.level == null) {
        this.$jitsoseVue.MessageWarning('请先选择等级！')
        return
      }
      if (this.opinion == '' || this.opinion == null) {
        this.$jitsoseVue.MessageWarning('请先填写评语！')
        return
      }
      let url = '/zExpert/zExpertIndicator/giveScore'
      let data = {
        score: this.level,
        opinion: this.opinion,
        indicatorId: this.row.indicatorId,
        planId: this.evaluationPlan
      }
      this.$request.post(url, data).then(data => {
        console.log('data', data)
        if (data.data === 'ok') {
          this.dialogVisible = false
          this.level = ''
          this.row = []
          this.getEvaluationPlanFile()
          this.$jitsoseVue.MessageSuccess('评估成功！')
        }
      })
    },
    initPage(){
      if(this.$route.query.evaluationPlan != undefined){
        this.evaluationPlan = this.$route.query.evaluationPlan
      }
      console.log('evaluationPlan', this.evaluationPlan)
    },
    //关闭对话框的提示
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          this.dialogVisible = false
          this.level = ''
          this.opinion = ''
          this.row = []
        })
        .catch(_ => {})
    },

    /**
     * 改变页码
     *
     * @param pageNum
     * 改变后的页码
     */
    onPageChange(pageNum) {
      this.pageNum = pageNum
      this.getEvaluationPlanFile()
    },

    /**
     * 切换每页条数
     * @param pageSize
     * 换后的每页条数
     */
    onPageSizeChange(pageSize) {
      this.pageSize = pageSize
      this.getEvaluationPlanFile()
    }

    /**
     * 设置表头
     */
    // setTableTitle() {
    //   // // 添加自定义slot-scope
    //   this.column.push(this.$tableTitleUtil.HeadActionSlot(true, 200))
    // }
  }
}
</script>
<style lang="less" scoped></style>
