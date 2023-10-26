//导入request请求工具类
import {
  getBaseUrl,
  requestUtil
} from "../../utils/requestUtil";
import regeneratorRuntime from "../../lib/runtime/runtime";
Page({

  /**
   * 页面的初始数据
   */
  data: {
    baseUrl: '',
    productObj:{},
    activeIndex:0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    const baseUrl = getBaseUrl();
    this.setData({
      baseUrl
    });
    this.getProductDetail(options.id);
  },
  handleBuy(e){
    this.setCartadd();
    wx.switchTab({
      url: '/pages/cart/index',
    })
  },
  //Tap点击事件
  handleItemTap(e){
    const {
      index
    } = e.currentTarget.dataset;
    this.setData({
      activeIndex:index
    })
  },
  productInfo:{

  },
  //商品添加购物车点击事件
  handleCartAdd(e){
    this.setCartadd();
    wx.showToast({
      title: '加入成功',
      icon:'success',
      mask:true
    })
  },
  //加入购物车
  setCartadd(){
    let cart=wx.getStorageSync('cart')||[];
    // console.log(cart)
    let index=cart.findIndex(v=>v.id===this.productInfo.id);
    if(index===-1){   //购物车里当前商品不存在
      this.productInfo.num=1;
      this.productInfo.checked=true;
      cart.push(this.productInfo)
    }else{   //购物车里当前商品存在
      cart[index].num++;
    }
    wx.setStorageSync('cart',cart);
  },
  //获取商品详情
  async getProductDetail(id) {
    const result = await requestUtil({
      url: '/product/detail',
      data:{id},
      method: "GET"
    });
    this.productInfo=result.message;
    this.setData({
      productObj: result.message
    })
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

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})