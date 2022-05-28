<template>
  <div :style="{ width: screenWidth + 'px', height: screenHeight + 'px', overflow: 'auto' }">
    <h1 style="position: absolute; left: 1%;">{{ indicatorDetails.indicatorName3 }}</h1>
    <el-button type="primary" style="position: absolute; left: 3%;top: 5%" @click="backToScore">返回评价</el-button>
    <div style="width: 10%;height: 100%; float: left;"></div>
    <div style="width: 30%;height: 100%; float: left;">
      <div class="text_top">
        <center>
          <el-button @click="changeShowText('guide')">导向要求</el-button>
          <el-button @click="changeShowText('limit')">限定</el-button>
          <el-button @click="changeShowText('standard')">建设标准</el-button>
        </center>
      </div>

      <div class="text_bottom">
        <div style="margin: 20px;font: 1.5em sans-serif;">&emsp;&emsp;{{ showText }}</div>
      </div>
    </div>
    <div style="width: 10%;height: 100%; float: right;"></div>
    <div style="width: 20%; float: right;">
      <el-card style="margin: 50px 50px 50px 10px;" shadow="hover">
        <h5 style="font: 1.5em sans-serif;">公共资料</h5>
        <!--树展示-->
        <el-tree
          :data="treeData"
          :props="treeProps"
          node-key="id"
          show-checkbox
          ref="publicFileTree"
          @check="handleCheck"
          :default-expand-all="true"
          :expand-on-click-node="false"
          style="margin:30px 0px 0px 30px;"
        ></el-tree>
        <el-button type="primary" style="margin: 10px 10px 10px 150px" @click="openPublicFile">
          查看
        </el-button>
      </el-card>
    </div>
    <div style="width: 30%; float: right;">
      <el-card style="margin: 50px 10px 50px 10px;" shadow="hover">
        <h5 style="font: 1.5em sans-serif;">（一）自评报告</h5>
        <h5 style="font: 1.5em sans-serif;">（二）佐证材料</h5>
        <!--树展示-->
        <el-tree
          :data="treeData2"
          :props="treeProps"
          node-key="id"
          show-checkbox
          ref="indicatorFileTree"
          @check="handleCheck2"
          :default-expand-all="true"
          :expand-on-click-node="false"
          style="margin:10px 0px 0px 30px;"
        ></el-tree>
        <el-button type="primary" style="margin: 10px 10px 10px 100px" @click="openIndicatorFile" v-show="openIndicatorFileFlag">
          查看
        </el-button>
        <h5 style="font: 1.5em sans-serif;">（三）数据表单</h5>
        <h5 style="font: 1.5em sans-serif;">（四）工作记录</h5>
        <table style="border: 1px solid grey;width: 100%;" v-show="indicatorDetails.ascription">
          <tr style="border: 1px solid grey; height: 10px;">
            <th style="border: 1px solid grey;width: 20%;">等级</th>
            <th style="border: 1px solid grey">意见</th>
          </tr>
          <tr style="border: 1px solid grey;">
            <td style="border: 1px solid grey" align="center">
              {{indicatorDetails.score == 'X' ? '' : indicatorDetails.score}}
            </td>
            <td style="border: 1px solid grey">
              {{indicatorDetails.opinion == 'X' ? '' : indicatorDetails.opinion}}
            </td>
          </tr>
        </table>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'zIndicatorDetails',
  components: {},
  data() {
    return {
      evaluationPlan: '',
      indicatorDetails: {},
      showText: '',
      level: '',
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
      opinion: '',
      treeData: [],
      treeProps: {
        children: 'childList',
        label: 'fileName'
      },
      treeData2: [],

      checkedPublicFile: [],
      checkedIndicatorFile: [],
      openIndicatorFileFlag: false,
    }
  },
  computed: {
    screenHeight() {
      return window.innerHeight
    },
    screenWidth() {
      return window.screen.width
    }
  },
  mounted() {
    this.initPage()
    this.getPublicFileTree()
    this.getIndicatorFileTree()
  },
  methods: {
    changeShowText(arg) {
      if (arg == 'guide') {
        this.showText = this.indicatorDetails.guide
      } else if (arg == 'limit') {
        this.showText = this.indicatorDetails.limit
      } else if (arg == 'standard') {
        this.showText = this.indicatorDetails.standard
      }
    },

    getPublicFileTree() {
      let url = '/zExpert/zData/getPublicFileTree'
      this.$request.get(url).then(res => {
        this.treeData = res.data.obj
      })
    },

    getIndicatorFileTree() {
      let url = '/zExpert/zData/getIndicatorFileTree/' + this.indicatorDetails.indicatorId
      this.$request.get(url).then(res => {
        this.treeData2 = res.data.obj
      })
    },

    handleCheck(data, checked) {
      console.log(data, checked)
      this.checkedPublicFile = []
      checked.checkedNodes.forEach(item => {
        if (item.level == 1) {
          this.checkedPublicFile.push(item)
        }
      })
      console.log(this.checkedPublicFile)
    },

    handleCheck2(data, checked) {
      this.checkedIndicatorFile = []
      checked.checkedNodes.forEach(item => {
        if (item.level == 1) {
          this.checkedIndicatorFile.push(item)
        }
      })
      this.openIndicatorFileFlag = true
      console.log(this.checkedIndicatorFile)
    },

    openPublicFile() {
      if (this.checkedPublicFile.length == 0) {
        this.$message({
          message: '请选择文件',
          type: 'warning'
        })
        return
      }
      this.checkedPublicFile.forEach(item => {
        window.open(window.location.origin + '/api' + item.accessUrl)
      })
    },

    openIndicatorFile() {
      if (this.checkedIndicatorFile.length == 0) {
        this.$message({
          message: '请选择文件',
          type: 'warning'
        })
        return
      }
      this.checkedIndicatorFile.forEach(item => {
        window.open(window.location.origin + '/api' + item.accessUrl)
      })
    },
    backToScore() {
      this.$router.push({
        path: '/zExpert/score',
        query: {
          evaluationPlan: this.evaluationPlan
        }
      })
    },

    initPage() {
      this.indicatorDetails = JSON.parse(this.$route.query.row)
      this.evaluationPlan = this.$route.query.evaluationPlan
      console.log(this.indicatorDetails)
      this.showText = this.indicatorDetails.guide
    }
  }
}
</script>

<style scoped>
.text_top {
  display: block;
  height: 5%;
  border: 1px solid grey;
  border-radius: 5px 5px 0px 0px;
  margin: 50px 10px 0px 50px;
}
.text_bottom {
  display: block;
  height: 75%;
  border: 1px solid grey;
  border-radius: 0px 0px 5px 5px;
  margin: 0px 10px 50px 50px;
  overflow: auto;
}
</style>
