<template>
  <div class="layout-main" style="display: flex">
    <div class="left" style="width:20%;">
      <div>
        <div class="evaluationPlan" style="margin-top:10px">
          <!-- 计划 -->
          <div style="margin-bottom:10px">选择评估计划：</div>
          <el-select v-model="evaluationPlan" placeholder="请选择评估计划" style="width:250px">
            <el-option
              v-for="item in evaluationPlanList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </div>

        <div style="margin-top:10px" v-show="indicatorFlag">
          <div style="margin-bottom:10px">选择指标：</div>
          <!-- 指标 -->
          <!--树展示-->
          <el-tree
            :data="treeData"
            :props="treeProps"
            @node-click="editLocale"
            node-key="id"
            show-checkbox
            ref="indicatorTree"
            @check="handleCheckChange"
            :default-expand-all="true"
            :expand-on-click-node="false"
            style="margin-left:30px"
          ></el-tree>
        </div>
      </div>
    </div>

    <div class="left" style="width:15%;">
      <div style="margin-top:50px" v-show="folderFlag">
        <el-button @click="dialogVisible2 = true">添加文件夹</el-button>
        <div style="margin-bottom:10px">选择上传文件夹（可不选）：</div>
        <!--树展示-->
        <el-tree
          :data="folderData"
          :props="treeProps1"
          node-key="id"
          show-checkbox
          ref="folderTree"
          @check="handleCheck"
          :default-expand-all="true"
          :expand-on-click-node="false"
          style="margin-left:30px"
        ></el-tree>
      </div>
      <el-dialog
        title="添加文件夹"
        :visible.sync="dialogVisible2"
        width="20%"
        :before-close="handleClose"
      >
        <div style="width: 100%; margin: 10px;">
          <label>文件夹名称：</label>
          <el-input v-model="addFolder.fileName" style="width: 203px;"></el-input>
        </div>
        <div style="width: 100%; margin: 10px;">
          <label>选择指标：</label>
          <el-select v-model="addFolder.indicatorId">
            <el-option
              v-for="item in checkedIndicators"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible2 = false">取 消</el-button>
          <el-button type="primary" @click="addFolderByIndicatorId">确 定</el-button>
        </span>
      </el-dialog>
    </div>

    <div class="right" style="width:25%;">
      <div class="upload" style="width:100%;margin-top:10px;">
        <center>
          <div style="margin-bottom:10px">上传指标资料：</div>
          <el-upload
            class="uploadDemo"
            :limit="1"
            drag
            accept=".pdf,.doc,.docx,.xls,.xlsx"
            :on-exceed="handleExceedFileLimit"
            :on-change="handleChangeFile"
            :on-remove="handleRemoveFile"
            :before-remove="beforeRemoveFile"
            :file-list="uploadFileList"
            :auto-upload="false"
            action=""
          >
            <i class="el-icon-upload"></i>
            <center>
              <div class="el-upload__text">
                将文件拖到此处，或
                <em>点击上传文件</em>
              </div>
              <div slot="tip" class="el-upload__tip">
                支持上传WORD，EXCEL，PDF文件
                <br />
                一次只允许上传一个文件且文件大小不超过{{ maxFileSize }}MB)
              </div>
            </center>
          </el-upload>
          <div v-if="uploadFlag" style="color: red;font-size: 20px">
            请选择需要上传的文件！
          </div>
          <div>
            <el-button type="info">取消</el-button>
            <el-button type="primary" style="margin-left:40px" @click="confirmUpload">
              确认
            </el-button>
          </div>
        </center>
      </div>
      <!-- 公共文件夹 -->
      <div style="margin-top:160px;margin-left:30px">
        <div>选择上传公共文件夹（可不选）：</div>
        <el-select v-model="publicFolderId" placeholder="请选择公共文件夹" style="width:250px">
          <el-option
            v-for="item in publicFolderList"
            :key="item.id"
            :label="item.fileName"
            :value="item.id"
          ></el-option>
        </el-select>
        <el-button type="primary" @click="dialogVisible4 = true">添加文件夹</el-button>
        <el-dialog
          title="添加文件夹"
          :visible.sync="dialogVisible4"
          width="20%"
          :before-close="handleClose"
        >
          <label>文件夹名称：</label>
          <el-input
            v-model="publicFolderName"
            style="width: 203px;"
            @keyup.enter.native="addPublicFolder"
          ></el-input>
          <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible4 = false">取 消</el-button>
            <el-button type="primary" @click="addPublicFolder">确 定</el-button>
          </span>
        </el-dialog>
      </div>
      <div class="upload" style="width:100%;">
        <center>
          <div style="margin-bottom:10px">上传公共资料：</div>
          <el-upload
            class="uploadDemo"
            :limit="1"
            drag
            accept=".pdf,.doc,.docx,.xls,.xlsx"
            :on-exceed="handleExceedFileLimit"
            :on-change="handleChangeFile2"
            :on-remove="handleRemoveFile2"
            :before-remove="beforeRemoveFile"
            :file-list="uploadFileList2"
            :auto-upload="false"
            action=""
          >
            <i class="el-icon-upload"></i>
            <center>
              <div class="el-upload__text">
                将文件拖到此处，或
                <em>点击上传文件</em>
              </div>
              <div slot="tip" class="el-upload__tip">
                支持上传WORD，EXCEL，PDF文件
                <br />
                一次只允许上传一个文件且文件大小不超过{{ maxFileSize }}MB)
              </div>
            </center>
          </el-upload>
          <div>
            <el-button type="primary" v-show="uploadFileList2.length > 0" @click="confirmUpload2">
              上传
            </el-button>
          </div>
        </center>
      </div>
    </div>

    <div style="width:40%;margin-top:0px">
      <div style="margin-bottom:0px">指标已绑定文件：</div>
      <i-table
        row-key="id"
        class="table"
        border
        height="350"
        width="auto"
        :highlight-row="true"
        size="large"
        :columns="column"
        :data="fileData"
        :loading="loading"
      >
        <template slot-scope="{ row }" slot="action">
          <el-button type="primary" @click="editFile(row)" size="small">编辑</el-button>
          <el-button type="danger" @click="deleteFile(row)" size="small">删除</el-button>
        </template>
      </i-table>
      <Page
        style="float:right;"
        :current="pageNum"
        :total="totalNum"
        :page-size="pageSize"
        @on-change="onPageChange"
        @on-page-size-change="onPageSizeChange"
        show-total
        show-elevator
        show-sizer
      ></Page>
      <!-- ---------------------------------------------------------- -->
      <div style="margin-top:50px">公共文件：</div>
      <i-table
        row-key="id"
        class="table"
        border
        height="350"
        width="auto"
        :highlight-row="true"
        size="large"
        :columns="column2"
        :data="publicFileData"
        :loading="loading"
      >
        <template slot-scope="{ row }" slot="action">
          <el-button type="danger" @click="deletePublicFile(row)" size="small">删除</el-button>
        </template>
      </i-table>
      <Page
        style="float:right;"
        :current="pageNum2"
        :total="totalNum2"
        :page-size="pageSize2"
        @on-change="onPageChange2"
        @on-page-size-change="onPageSizeChange2"
        show-total
        show-elevator
        show-sizer
      ></Page>
    </div>

    <div>
      <el-dialog title="编辑" :visible.sync="dialogVisible" width="30%" :before-close="handleClose">
        <div class="upload" style="width:100%;margin-top:50px;">
          <center>
            <div style="margin-bottom:10px">上传资料：</div>
            <el-upload
              class="uploadDemo"
              :limit="1"
              drag
              accept=".pdf,.doc,.docx,.xls,.xlsx"
              :on-exceed="handleExceedFileLimit"
              :on-change="handleChangeFile"
              :on-remove="handleRemoveFile"
              :before-remove="beforeRemoveFile"
              :file-list="uploadFileList"
              :auto-upload="false"
              action=""
            >
              <i class="el-icon-upload"></i>
              <center>
                <div class="el-upload__text">
                  将文件拖到此处，或
                  <em>点击上传文件</em>
                </div>
                <div class="el-upload__tip">
                  支持上传WORD,EXCEL,PDF文件
                  <br />
                  一次只允许上传一个文件且文件大小不超过{{ maxFileSize }}MB)
                  <div v-if="uploadFlag" style="color: red;font-size: 20px">
                    请选择需要上传的文件！
                  </div>
                </div>
              </center>
            </el-upload>
          </center>
        </div>

        <span slot="footer" class="dialog-footer">
          <el-button @click="handleClose">取 消</el-button>
          <el-button type="primary" @click="confirmEdit" v-show="uploadFileList.length > 0">
            确 定
          </el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>
<script>
export default {
  name: 'indicatorFile',
  //数据
  data() {
    return {
      evaluationPlan: '', //选中的评估计划
      evaluationPlanList: [], //评估计划列表
      courseList: '', //课程列表
      indicatorFlag: false,
      treeData: [], //指标树
      treeDataMap: [], //指标树map
      folderData: [], //文件夹
      checkedFolders: [], //选中的文件夹
      checkedFoldersRepeat: false, //选中的文件夹重复
      folderFlag: false,
      addFolder: {
        fileName: '', //文件名
        indicatorId: '' //指标id
      },
      treeProps: {
        children: 'childList',
        label: 'name'
      }, //树组件参数
      treeProps1: {
        children: 'childList',
        label: 'name'
      },
      maxFileSize: 500, //文件最大的大小
      uploadFileList: [], //上传指标文件预览列表
      uploadFileList2: [], //上传公共文件预览列表
      uploadFlag: false,
      fileFormData: new FormData(), //文件表单对象

      data: [], //用户数据
      chooseData: [], //专家数据
      indicatorData: [], //指标数据
      loading: false, //加载提示，默认是关闭的

      treeData: [],
      dialogVisible: false, // 修改文件弹出框
      dialogVisible2: false, // 添加文件夹弹出框
      dialogVisible3: false, // 添加公共文件弹出框
      dialogVisible4: false, // 添加公共文件夹弹出框
      checkedIndicators: [], // 选中的指标

      //table属性
      totalNum: 0,
      pageNum: 1,
      pageSize: 10, // 分页参数、
      totalNum2: 0,
      pageNum2: 1,
      pageSize2: 10, // 分页参数、
      fileData: [], //文件信息
      column: [
        {
          title: '指标参数',
          key: 'indicatorName'
        },
        {
          title: '参评资料',
          key: 'fileName',
          tooltip: true
        }
      ],
      publicFolderId: '', //公共文件夹
      publicFolderName: '', //公共文件夹
      publicFolderList: [], //公共文件夹列表
      publicFileData: [], //公共文件信息
      column2: [
        {
          title: '文件名',
          key: 'fileName'
        }
      ],
      editRow: []
    }
  },
  //计算属性
  computed: {},

  mounted() {
    this.getEvaluationPlanList()
    this.setTableTitle()
    this.setTableTitle2()
    this.getPublicFolder()
    this.getPublicFile()
  },

  watch: {
    evaluationPlan: {
      handler(newVal, oldVal) {
        if (newVal != '' || newVal != null) {
          this.getIndicatorTree()
          this.indicatorFlag = true
        }
      },
      deep: true
    }
  },

  methods: {
    //关闭前的确认
    handleClose() {
      this.$confirm('确认关闭？')
        .then(_ => {
          this.uploadFileList = []
          this.dialogVisible = false
          this.dialogVisible2 = false
          this.dialogVisible3 = false
          this.dialogVisible4 = false
          this.fileFormData = new FormData()
          done()
        })
        .catch(_ => {})
    },

    //确认编辑
    confirmEdit() {
      console.log('uploadFileList', this.uploadFileList)
      this.fileFormData = new FormData() //文件表单对象
      if (this.uploadFileList.length != 0) {
        if (this.uploadFileList[0].size > this.maxFileSize * 1024 * 1024) {
          this.uploadFileList = []
          this.$jitsoseVue.MessageWarning('文件过大，请重新上传')
          return
        }
        this.fileFormData.append('dataId', this.editRow.id)
        this.fileFormData.append('indicatorId', this.editRow.indicatorId)
        this.fileFormData.append('file', this.uploadFileList[0].raw)
      }
      let url = '/zExpert/zData/editIndicatorFile'
      this.$request.post(url, this.fileFormData).then(data => {
        this.uploadFileList = []
        this.dialogVisible = false
        this.fileFormData = new FormData()
        this.editRow = []
        this.handleCheckChange()
        // this.selectTree();
      })
    },

    //获取评估计划列表
    getEvaluationPlanList() {
      let url = '/zExpert/zPlan/getZPlanList'
      this.$request.get(url).then(data => {
        this.evaluationPlanList = data.data.obj
      })
    },

    //获取指标树
    getIndicatorTree() {
      let that = this
      let url = '/zExpert/indicator/selectListAndList2/' + this.evaluationPlan
      this.loading = true
      this.$request.get(url).then(data => {
        that.loading = false
        //获取后台数据
        this.treeData = data.data.obj

        // 将treeData对象转换为map
        for (let i = 0; i < this.treeData.length; i++) {
          this.$set(this.treeDataMap, this.treeData[i].id, this.treeData[i].name)
          for (let j = 0; j < this.treeData[i].childList.length; j++) {
            this.$set(
              this.treeDataMap,
              this.treeData[i].childList[j].id,
              this.treeData[i].childList[j].name
            )
            for (let k = 0; k < this.treeData[i].childList[j].childList.length; k++) {
              this.$set(
                this.treeDataMap,
                this.treeData[i].childList[j].childList[k].id,
                this.treeData[i].childList[j].childList[k].name
              )
            }
          }
        }
      })
    },

    //编辑资料
    editFile(row) {
      this.dialogVisible = true
      this.uploadFileList = []
      this.editRow = row
    },

    //删除文件
    deleteFile(row) {
      this.$confirm('确认删除？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        let url = '/zExpert/zData/delete/' + row.id
        this.$request.get(url).then(data => {
          this.handleCheckChange()
        })
      })
    },

    //获取当前节点的数据
    editLocale(data) {
      console.log('data', data)
    },

    /**
     * 确认是否提交上传
     */
    confirmUpload() {
      let checkedNode = this.$refs.indicatorTree.getCheckedNodes()
      let indicatorList = []
      for (let item of checkedNode) {
        if (
          item.level == '3' ||
          (item.level == '2' && item.childList.length == 0) ||
          (item.level == '1' && item.childList.length == 0)
        ) {
          indicatorList.push({
            id: item.id,
            folderId: this.checkedFolders[item.id] == undefined ? 0 : this.checkedFolders[item.id]
          })
        }
      }
      console.log('指标列表', indicatorList)

      if (indicatorList.length == 0) {
        this.$message.warning('请选择指标')
        return
      }

      if (this.checkedFoldersRepeat) {
        this.$message.warning('同一指标不要选择多个文件夹')
        return
      }

      this.$confirm('确认上传', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      })
        .then(() => {
          this.fileFormData = new FormData() //文件表单对象
          if (this.uploadFileList.length != 0) {
            console.log('上传文件', this.uploadFileList)
            if (this.uploadFileList[0].size > this.maxFileSize * 1024 * 1024) {
              this.uploadFileList = []
              this.$jitsoseVue.MessageWarning('文件过大，请重新上传')
              return
            }
            const loading = this.$loading({
              lock: true,
              text: 'Loading',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            })

            this.fileFormData.append('list', JSON.stringify(indicatorList))
            this.fileFormData.append('file', this.uploadFileList[0].raw)
            let url = '/zExpert/zData/insertIndicatorFile'
            this.$request.post(url, this.fileFormData).then(data => {
              for (let item of data.data.obj) {
                this.$jitsoseVue.MessageWarning(
                  '通过MD5加密获取哈希值对比，' +
                    '指标：' +
                    this.treeDataMap[item.indicatorId] +
                    '已存在相同文件！'
                )
              }
              loading.close()
              this.fileFormData = new FormData()
              this.uploadFileList = []
              this.uploadFlag = false
              this.$refs.indicatorTree.setCheckedKeys([])
              this.$refs.indicatorTree.setCheckedKeys([])
              this.handleCheckChange()
              // this.selectTree();
            })
          } else {
            this.uploadFlag = true
          }
        })
        .catch(error => {
          console.log(error)
          this.$message({
            type: 'info',
            message: '已取消上传'
          })
        })
    },

    /**
     * 确认是否提交上传
     */
    confirmUpload2() {
      this.$confirm('确认上传', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      })
        .then(() => {
          const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          })
          let formData = new FormData() //文件表单对象
          if (this.uploadFileList2[0].size > this.maxFileSize * 1024 * 1024) {
            this.uploadFileList2 = []
            this.$jitsoseVue.MessageWarning('文件过大，请重新上传')
            return
          }
          if (this.publicFolderId == '') {
            this.publicFolderId = 0
          }
          formData.append('folderId', this.publicFolderId)
          formData.append('file', this.uploadFileList2[0].raw)
          let url = '/zExpert/zData/insertPublicFile'
          this.$request.post(url, formData).then(res => {
            this.$message({
              type: 'info',
              message: res.data.obj
            })
            this.publicFolderId = ''
            this.uploadFileList2 = []
            this.getPublicFile()
            loading.close()
          })
        })
        .catch(error => {
          console.log(error)
          this.$message({
            type: 'info',
            message: '已取消上传'
          })
        })
    },

    //选中节点发生改变
    handleCheckChange(data, checked) {
      //获取当前树所有被选中的节点
      let checkedNodes = []
      Array.from(this.$refs.indicatorTree.getCheckedNodes()).forEach(item => {
        if (item.level != 1 && item.level != 2) {
          checkedNodes.push(item.id)
        }
      })

      for (let i = 0; i < checkedNodes.length; i++) {
        this.checkedIndicators.push({
          id: checkedNodes[i],
          name: this.treeDataMap[checkedNodes[i]]
        })
      }

      if (checkedNodes.length > 0) {
        let url = '/zExpert/zData/selectCheckedIndicatorFile'
        let data = {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          checkedNodes: checkedNodes
        }
        this.$request.post(url, data).then(res => {
          this.fileData = res.data.obj.list

          // 为文件列表添加指标名称
          for (let i = 0; i < this.fileData.length; i++) {
            this.$set(
              this.fileData[i],
              'indicatorName',
              this.treeDataMap[this.fileData[i].indicatorId]
            )
          }

          console.log('指标绑定资料', this.fileData)
          this.totalNum = res.data.obj.total
          // 再次设置当前页码
          this.pageNum = res.data.obj.pageNum === 0 ? 1 : res.data.obj.pageNum
        })
        this.getFolderByIndicatorId(checkedNodes)
      } else {
        this.fileData = []
        this.totalNum = 0
      }
    },

    getFolderByIndicatorId(indicatorIdList) {
      let url = '/zExpert/zData/getFolderByIndicatorId'
      let data = {
        indicatorIdList: indicatorIdList
      }
      this.$request.post(url, data).then(res => {
        this.folderData = []
        for (let i = 0; i < indicatorIdList.length; i++) {
          let data = {
            id: indicatorIdList[i],
            name: this.treeDataMap[indicatorIdList[i]],
            isFolder: false,
            childList: []
          }
          res.data.obj.forEach(item => {
            if (item.indicatorId == indicatorIdList[i]) {
              data.childList.push({
                id: item.id,
                name: item.fileName,
                isFolder: true,
                indicatorId: item.indicatorId
              })
            }
          })

          this.folderData.push(data)
        }
        this.folderFlag = true
        console.log('指标绑定文件夹', this.folderData)
      })
    },
    addFolderByIndicatorId() {
      if (this.addFolder.fileName == '') {
        this.$message({
          type: 'warning',
          message: '请输入文件夹名称'
        })
        return
      }
      if (this.addFolder.indicatorId == '') {
        this.$message({
          type: 'warning',
          message: '请选择指标'
        })
        return
      }
      let isExist = false
      this.folderData.forEach(item => {
        if (item.id == this.addFolder.indicatorId) {
          item.childList.forEach(item2 => {
            if (item2.name == this.addFolder.fileName) {
              isExist = true
              return
            }
          })
        }
        if (isExist) {
          return
        }
      })
      if (isExist) {
        this.$message({
          type: 'warning',
          message: '文件夹已存在'
        })
        return
      }
      let url = '/zExpert/zData/addFolderByIndicatorId'
      let data = this.addFolder
      this.$request.post(url, data).then(res => {
        this.$message({
          type: 'success',
          message: '添加成功'
        })
        this.addFolder = {
          indicatorId: '',
          fileName: ''
        }
        let checkedNodes = []
        this.checkedIndicators.forEach(item => {
          checkedNodes.push(item.id)
        })
        this.getFolderByIndicatorId(checkedNodes)
        this.dialogVisible2 = false
      })
    },
    getPublicFolder() {
      let url = '/zExpert/zData/getPublicFolder'
      this.$request.get(url).then(res => {
        this.publicFolderList = res.data.obj
      })
    },
    addPublicFolder() {
      if (this.publicFolderName == '') {
        this.$message({
          type: 'warning',
          message: '请输入文件夹名称'
        })
        return
      }
      let isExist = false
      this.publicFolderList.forEach(item => {
        if (item.fileName == this.publicFolderName) {
          isExist = true
          return
        }
      })
      if (isExist) {
        this.$message({
          type: 'warning',
          message: '文件夹已存在'
        })
        return
      }
      let url = '/zExpert/zData/addPublicFolder'
      let data = {
        fileName: this.publicFolderName
      }
      this.$request.post(url, data).then(res => {
        this.$message({
          type: 'success',
          message: '添加成功'
        })
        this.publicFolderName = ''
        this.getPublicFolder()
        this.dialogVisible4 = false
      })
    },
    getPublicFile() {
      let url = '/zExpert/zData/getPublicFile/' + this.pageNum2 + '/' + this.pageSize2
      this.$request.get(url).then(res => {
        this.publicFileData = res.data.obj.list
        this.totalNum2 = res.data.obj.total
        // 再次设置当前页码
        this.pageNum2 = res.data.obj.pageNum === 0 ? 1 : res.data.obj.pageNum
      })
    },
    deletePublicFile(row) {
      let url = '/zExpert/zData/deletePublicFileById/' + row.id
      this.$request.get(url).then(res => {
        this.$message({
          type: 'success',
          message: '删除成功'
        })
        this.getPublicFile()
      })
    },
    handleCheck(data, checked) {
      console.log(data, checked)
      this.checkedFolders = []
      checked.checkedNodes.forEach(item => {
        if (item.isFolder) {
          this.checkedFolders.push({
            id: item.id,
            indicatorId: item.indicatorId,
            fileName: item.name
          })
        }
      })
      console.log(this.checkedFolders)
      let repeat = []
      this.checkedFolders.forEach(item => {
        this.$set(repeat, item.indicatorId, item.id)
      })
      console.log(Object.keys(repeat).length)
      console.log(this.checkedFolders.length)
      if (Object.keys(repeat).length < this.checkedFolders.length) {
        console.log('重复')
        this.$message({
          type: 'warning',
          message: '同一指标不要选择多个文件夹'
        })
        this.checkedFoldersRepeat = true
      } else {
        console.log('不重复')
        this.checkedFoldersRepeat = false
      }
      this.checkedFolders = repeat
      console.log(this.checkedFolders)
    },

    /**
     * 文件超出个数限制
     */
    handleExceedFileLimit() {
      this.$jitsoseVue.MessageWarning('只能上传一个文件')
    },

    /**
     * 文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用
     * @param file 当前文件
     * @param fileList 文件列表
     */
    handleChangeFile(file, fileList) {
      //将文件信息存入对应的fileList
      console.log('file', file)
      this.uploadFileList.push(file)
      this.uploadFlag = false
      console.log('文件列表', this.uploadFileList)
    },

    handleChangeFile2(file, fileList) {
      //将文件信息存入对应的fileList
      console.log('file', file)
      this.uploadFileList2.push(file)
      console.log('文件列表2', this.uploadFileList)
    },

    /**
     * 文件列表移除文件时的钩子
     * @param file 移除的图片
     * @param fileList 图片列表
     */
    handleRemoveFile(file, fileList) {
      //文件移除
      this.uploadFileList = []
      console.log('文件列表', this.uploadFileList)
    },

    handleRemoveFile2(file, fileList) {
      //文件移除
      this.uploadFileList2 = []
      console.log('文件列表2', this.uploadFileList)
    },

    /**
     * 删除文件之前的钩子，参数为上传的文件和文件列表，
     * 若返回 false 或者返回 Promise 且被 reject，则停止删除。
     * @param file 移除的图片
     * @param fileList 图片列表
     */
    beforeRemoveFile(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`)
    },

    /**
     * 改变页码
     *
     * @param pageNum
     * 改变后的页码
     */
    onPageChange(pageNum) {
      this.pageNum = pageNum
      this.handleCheckChange()
    },
    onPageChange2(pageNum2) {
      this.pageNum2 = pageNum2
      this.handleCheckChange2()
    },

    /**
     * 切换每页条数
     * @param pageSize
     * 换后的每页条数
     */
    onPageSizeChange(pageSize) {
      this.pageSize = pageSize
      this.handleCheckChange()
    },
    onPageSizeChange2(pageSize2) {
      this.pageSize2 = pageSize2
      this.handleCheckChange2()
    },

    /**
     * 设置表头
     */
    setTableTitle() {
      // // 添加自定义slot-scope
      this.column.push(this.$tableTitleUtil.HeadActionSlot(true, 200))
      // // 添加多选
    },
    setTableTitle2() {
      // // 添加自定义slot-scope
      this.column2.push(this.$tableTitleUtil.HeadActionSlot(true, 150))
    }
  }
}
</script>
<style lang="less">
// .el-select .el-input__inner {
//   width: 200px
// }
</style>
