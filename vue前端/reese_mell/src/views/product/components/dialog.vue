<template>
  <el-dialog
      modal-value="dialogVisible"
      :title="dialogTitle"
      width="50%"
      @close="handleClose"
  >

    <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
    >
      <el-form-item label="商品名称" prop="name">
        <el-input v-model="form.name" style="width: 400px"/>
      </el-form-item>

      <el-form-item label="商品价格" prop="price">
        <el-input v-model="form.price" style="width: 100px"/>
      </el-form-item>

      <el-form-item label="商品库存" prop="stock">
        <el-input v-model="form.stock" style="width: 100px"/>
      </el-form-item>

      <el-form-item label="商品类别">
        <el-select v-model="bigTypeId" placeholder="请选择商品大类" class="m-2" @change="handleBigTypeChange">
          <el-option
              v-for="item in bigTypeSelectOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"></el-option>
        </el-select>
        &nbsp;&nbsp;
        <el-select v-model="form.type.id" placeholder="请选择商品小类" class="m-2">
          <el-option
              v-for="item in smallTypeSelectOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="商品描述" prop="description">
        <el-input v-model="form.description" type="textarea" :rows="4"/>
      </el-form-item>
      <el-form-item label="商品介绍">
      </el-form-item>
      <QuillEditor
          v-model:content="form.productIntroImgs"
          contentType="html"
          toolbar="full"
          theme="snow"
          style="height:200px"
      />

      <el-form-item label="商品参数">
      </el-form-item>

      <QuillEditor
          v-model:content="form.productParaImgs"
          contentType="html"
          toolbar="full"
          theme="snow"
          style="height:200px"
      />
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button  @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleConfirm">确认</el-button>
      </span>
    </template>

  </el-dialog>
</template>

<script setup>
import {defineEmits, defineProps, watch,ref} from 'vue'
import axios from "@/util/axios";
import {ElMessage} from "element-plus";
import {QuillEditor} from "@vueup/vue-quill";

const bigTypeSelectOptions=ref([])
const smallTypeSelectOptions=ref([])

const props=defineProps({
      id:{
        type:Number,
        default:-1,
        required:true
      },
      dialogTitle:{
        type:String,
        default:'',
        required:true
      },
      dialogVisible:{
        type:Boolean,
        default:false,
        required:true
      }
    }
)

watch(
    ()=>props.dialogVisible,
    ()=>{
      console.log("id:"+props.id)
      let id =props.id;
      if(id!=-1){
        initFormData(props.id);
      }else {
        form.value={
          id:-1,
          name:'',
          price:null,
          stock:null,
          type:{
            id:''
          },
          description:'',
          productIntroImgs:'',
          productParaImgs:''
        }
      }
      console.log(form.value)
    }
)



const emits=defineEmits(['update:modelValue','initProductList'])

const handleClose=()=>{
  emits('update:modelValue',false)
}

const form=ref({
  id:-1,
  name:'',
  price:null,
  stock:null,
  type:{
    id:''
  },
  description:'',
  productIntroImgs:'',
  productParaImgs:''
})
const bigTypeId=ref("");


const rules=ref({
  name:[{required: true, message: '请输入商品小类名称！'}],
  price:[
    { required: true, message: '请输入商品价格!' },
    { type: 'number', message: '商品价格是数值类型！',transform: (value) => Number(value) },
  ],
  stock:[
    { required: true, message: '请输入商品库存!' },
    { type: 'number', message: '商品库存是数值类型！',transform: (value) => Number(value) },
  ],
  description:[{required: true, message: '请输入商品小类描述！'},],
})

const formRef=ref(null)

const initBigTypeSelectList=async ()=>{
  const res=await axios.get("admin/bigType/listAll")
  // console.log(res)
  bigTypeSelectOptions.value=res.data.bigTypeList
}
initBigTypeSelectList()
const initSmallTypeSelectList=async (bigTypeId)=>{
  form.value.type.id=""
  const res=await axios.get("admin/smallType/listAll/"+bigTypeId);
  smallTypeSelectOptions.value=res.data.smallTypeList;
}

const initFormData=async (id)=>{
  const res=await axios.get("admin/product/"+id);
  bigTypeId.value=res.data.product.type.bigType.id
  initSmallTypeSelectList(bigTypeId.value)
  form.value=res.data.product;
}

const handleBigTypeChange=async (bigTypeId)=>{
  initSmallTypeSelectList(bigTypeId)
}

const handleConfirm=()=> {
  formRef.value.validate(async (valid) => {
        if (valid) {
          if(form.value.type.id==''){
            ElMessage.error("请选择商品类别！")
            return;
          }
          let result = await axios.post("admin/product/save", form.value)
          let data = result.data;
          if (data.code == 0) {
            ElMessage.success("执行成功！");
            formRef.value.resetFields();
            emits("initProductList");
            handleClose()
          } else {
            ElMessage.error(data.msg)
          }
        } else {
          console.log("fail")
        }
      }
  )

}




</script>

<style lang="scss" scoped>

</style>