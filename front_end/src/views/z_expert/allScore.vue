<template>
  <div class="layout-main">
    <div class="evaluationPlan">
      <!-- 计划 -->
      <div style="margin-bottom:10px">选择评估计划：</div>
      <el-select v-model="evaluationPlan" placeholder="请选择评估计划" style="width:250px">
        <el-option
          v-for="item in evaluationPlanList"
          :key="'evaluationPlan'+item.id"
          :label="item.name"
          :value="item.id">
        </el-option>
      </el-select>
    </div>
    <div class="itable" style="margin-top:20px">
      <i-table
          row-key="id"
          class="table"
          ref="table"
          border
          height="auto"
          width="auto"
          :highlight-row="true"
          size="large"
          :columns="column"
          :data="tableData"
          :loading="loading">
      </i-table>
      <el-button type="primary" style="float:right;margin-top:30px" @click="exportStatistics">导出参评结果</el-button>
    </div>
  </div>
</template>
<script>

import dataFormat from '../../util/dataFormatUtil'

export default {
  name:"allScore",
  data(){
    return{
      course_plan_name:[],//被评审的课程名称
      course_plan:[],//被评审的课程信息
      indicator:[],//被评审的二级指标信息
      nowData:[],//指标分数
      nowData2:[],//筛选指标分数
      tableData:[],//itable数据
      firstLevelList:[],//一级指标列表
      column:[],//i-table列表属性
      loading:true,
      temp:[],
      evaluationPlanList:[],//评估计划列表
      evaluationPlan:'',//评估计划
    }
  },

  mounted(){

    this.getEvaluationPlanList();    
  },

    watch:{
    evaluationPlan:{
      handler(newVal,oldVal){
        if(newVal != '' || newVal != null){
          this.column = [];
          let promise = this.filter();
          let that = this;
          promise.then(()=>{
            console.log("课程列表",this.course_plan);
            console.log("指标列表",this.indicator);
            console.log("table数据",this.tableData);
            this.selectFirstLevel().then(()=>{
              that.setTableTitle()
            })
          })
        }
      },
      deep:true
    },
  },

  //计算属性
  computed:{

  },
   
   methods:{
    //获取评估计划列表
    getEvaluationPlanList(){
      let url = "/assessment/indicatorFile/selectEvaluationPlan";
      let data = {};
      this.$request.post(url,data).then(data => {
        console.log("评估计划列表",data);
        this.evaluationPlanList = data.data.obj;
      })
    },


     //查询课程、指标及审评分数
     filter(){
       return new Promise((resolve,reject) => {
        let url = "/assessment/detailedStatistics/selectScore";
        let data = {
          evaluationPlanId:this.evaluationPlan
        };
        this.$request.post(url,data).then(data => {
          this.nowData = data.data.obj;
          console.log("审评数据",this.nowData)
          for(let item1 of this.nowData){
            if(this.course_plan_name.indexOf(item1.course_name) < 0){
              this.course_plan_name.push(item1.course_name)
              this.course_plan.push({"course_plan_id":item1.coursePlanId,"course_name":item1.course_name})
            }
          }
          for(let item2 of this.nowData){
            if(this.indicator.indexOf(item2.indicatorName) < 0){
              this.indicator.push(item2.indicatorName);
              this.tableData.push({"indicator_id":item2.indicatorId,"indicator_name":item2.indicatorName,"last_id":item2.last_id,"level":item2.level,"content":item2.content})
            }
          }
          resolve();
        })
       })
     },

     //查询一级指标
     selectFirstLevel(){
       return new Promise((resolve,reject) => {
          let url = "/assessment/indicator/FirstLevelList";
          let data = {};
          this.$request.post(url, data).then((data) => {
            this.firstLevelList = data.data.obj;
            console.log("一级指标数据",this.firstLevelList)
            for(let item of this.tableData){
              for(let item2 of this.firstLevelList){
                if(item.last_id == item2.id){
                  this.$set(item,"firstLevel",item2.name);
                }
              }
            }
            for(let item4 of this.tableData){
              for(let item3 of this.nowData){
                if(Number(item3.indicatorId) == Number(item4.indicator_id)){
                  this.$set(item4,"course"+item3.coursePlanId,item3.score)
                }
              }
            }
            resolve();
          });
       })
     },

    /**
     * 设置表头
     */
    setTableTitle() {
      
      let render = {
        render: (h, param) => {
          // 表格显示的文字
          let texts = '';
          // 内容不为空
          let key = "content";
          if (!dataFormat.IsEmpty(param.row[key])) {
              // 长度小于15
              if (param.row[key].length <= 30) {
                  texts = param.row[key];
                  return h("span", {
                      domProps:{
                          innerHTML: texts,
                      }
                  })
              } else {
                  texts = (param.row[key]).substring(0, 30) + "...";
                  // 鼠标移入时显示的文字
                  let str = param.row[key].replace(/<[^>]+>/g,"");
                  // let str = param.row[key];
                  return h(
                          "tooltip", {
                          props: {
                              placement: "top-start",
                              transfer: true,
                              theme: "light",
                          },
                      },
                      [
                          h("div",{
                              domProps:{
                                  innerHTML:texts,
                              }
                          }),
                          h("div", {
                              slot: "content",
                              style: {
                                  fontSize: 19,
                                  whiteSpace: 'normal',
                                  wordBreak: 'break'
                              },
                              // 控制文字样式，可以换行显示
                          },str)
                      ]
                  )
              }
          }
        },
      }
      this.column.push({"fixed":"left","key":"firstLevel","title":"一级指标","minWidth":150});
      this.column.push({"fixed":"left","key":"indicator_name","title":"二级指标","minWidth":150});
      this.column.push({"fixed":"left","key":"content","title":"内容","minWidth":150,"render":render.render});
      console.log("table数据",this.tableData)
      for(let item of this.course_plan){
        this.column.push({"key":"course"+item.course_plan_id,"title":item.course_name,"minWidth":100,"align":"center"})
      }
      this.loading = false;
      this.collapseFlag = true;
      for(let item of this.column){
        // if(item.)
      }
      console.log("column",this.column)
    },

    exportStatistics(){
      let column = this.column;
      console.log("column",column)
      this.$refs.table.exportCsv({
        filename:"专家评审统计",
        original:false,
        columns:column,
        data:this.tableData
      })
    }
   },
}
</script>