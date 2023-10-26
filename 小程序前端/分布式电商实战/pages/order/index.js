//导入request请求工具类
import {
  getBaseUrl,
  requestUtil,
  getWxLogin,
  getUserProfile,
  requestPay
} from "../../utils/requestUtil";
import regeneratorRuntime from "../../lib/runtime/runtime";
Page({

  /**
   * 页面的初始数据
   */
  data: {
    orders:[],
    tabs: [{
        id: 0,
        value: "全部订单",
        isActive: true
      },
      {
        id: 1,
        value: "待付款",
        isActive: false
      },
      {
        id: 2,
        value: "待收货",
        isActive: false
      },
      {
        id: 3,
        value: "退款/退货",
        isActive: false
      },
    ]
  },
  //接口参数
  QueryParams:{
    type:0,
    page:1,
    pageSize:10
  },
  //总页数
  totalPage:1,
  //tap点击事件
  handleTabsItemChange(e){
   const {index}=e.detail;
    this.changeTitleByIndex(index);
    this.QueryParams.type=index;
    this.QueryParams.page=1;
    this.setData({
      orders:[]
    })
    this.getOrder();
  },
  //根据标题索引来激活选中的数据
  changeTitleByIndex(index){
    let {tabs}=this.data;
    tabs.forEach((v,i)=>i==index?v.isActive=true:v.isActive=false);
    this.setData({
      tabs
    })
  },


  //获取参数
  async getOrder(){
    const res=await requestUtil({url:'/my/order/list',data: this.QueryParams});
    this.totalPage=res.totalPage;
    this.setData({
      orders:[...this.data.orders,...res.orders]
    })
  },
 
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    let pages=getCurrentPages();
    let currentPage=pages[pages.length-1];
    const {type}=currentPage.options;
    this.changeTitleByIndex(type);
    this.QueryParams.type=type;
    this.QueryParams.page=1;
    this.setData({
      orders:[]
    })
    this.getOrder();
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {
    this.QueryParams.page=1;
    this.setData({
      orders:[]
    })
    this.getOrder();
    wx.stopPullDownRefresh({
    })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {
    if(this.QueryParams.page>=this.totalPage){
      wx.showToast({
        title: '没有下一页数据了',
        icon:'none'
      })
    }else{
      this.QueryParams.page++;
      this.getOrder();
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})