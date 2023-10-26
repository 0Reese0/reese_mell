<template>
  <el-dialog
      modal-value="dialogVisible"
      title="订单商品详情"
      width="40%"
      @close="handleClose"
  >

    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column type="index" width="50"/>
      <el-table-column prop="goodsPic" label="商品图片" width="200">
        <template v-slot="scope">
          <img :src="getServerUrl()+'/image/product/'+scope.row.goodsPic" width="80" height="80">
        </template>
      </el-table-column>
      <el-table-column prop="goodsName" label="商品名称" />
      <el-table-column prop="goodsPrice" label="商品价格" width="100"/>
      <el-table-column prop="goodsNumber" label="商品数量" width="100"/>
    </el-table>

  </el-dialog>

</template>

<script setup>
import {defineEmits, defineProps, watch,ref} from 'vue'
import axios,{getServerUrl} from "@/util/axios";

const tableData=ref([])

const props=defineProps({
    id:{
      type:Number,
      default:-1,
      required:true
    }
  }
)

watch(
    ()=>props.id,
    ()=>{
      let id =props.id;
      if(id!=-1){
        initOrderDetailData(props.id);
      }
    }
)

const initOrderDetailData=async(id)=>{
  const res=await axios.get("admin/orderDetail/list/"+id)
  tableData.value=res.data.list
}

const emits=defineEmits(['update:modelValue'])

const handleClose=()=>{
  emits("update:modelValue",false)
}

</script>

<style lang="scss" scoped>

</style>