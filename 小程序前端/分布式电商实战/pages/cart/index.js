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
    address: {},
    baseUrl: '',
    cart: [],
    allChecked: false,
    totalPrice: 0,
    totalNum: 0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    const baseUrl = getBaseUrl();
    this.setData({
      baseUrl
    })
  },
  handlePay(e) {
    const { address, totalNum } = this.data;
    if (!address) {
      wx.showToast({
        title: '您还没有选择收获地址',
        icon: 'none'
      })
      return;
    }
    if (totalNum === 0) {
      wx.showToast({
        title: '您还没有选购商品',
        icon: 'none'
      })
      return;
    }
    wx.navigateTo({
      url: '/pages/pay/index',
    })
  },
  handleItemNumEdit(e) {
    const { operation, id } = e.currentTarget.dataset;
    let { cart } = this.data;
    let index = cart.findIndex(v => v.id === id);
    if (cart[index].num === 1 && operation === -1) {
      console.log(1111)
      wx.showModal({
        title: '系统提示',
        content: '您是否要删除？',
        cancelColor: 'cancelColor',
        complete: (res) => {
          if (res.confirm) {
            cart.splice(index, 1);
            this.setCart(cart);
          }
        }
      })
    } else {
      cart[index].num += operation;
      this.setCart(cart)
    }
  },
  handleItemAllChange(e) {
    let { cart, allChecked } = this.data;
    allChecked = !allChecked;
    cart.forEach(v => v.checked = allChecked);
    this.setCart(cart);
  },
  handleItemChange(e) {
    // console.log(e)
    const { id } = e.currentTarget.dataset;
    let { cart } = this.data;
    let index = cart.findIndex(v => v.id === id);
    cart[index].checked = !cart[index].checked;
    this.setCart(cart);
  },
  handleChooseAddress() {
    wx.chooseAddress({
      success: (result) => {
        // console.log(result);
        wx.setStorageSync('address', result);
      }
    })
  },
  setCart(cart) {
    let allChecked = true;
    let totalPrice = 0;
    let totalNum = 0;
    cart.forEach(v => {
      if (v.checked) {
        totalPrice += v.num * v.price;
        totalNum += v.num;
      } else {
        allChecked = false;
      }
    })
    allChecked = cart.length != 0 ? allChecked : false;
    this.setData({
      totalPrice,
      totalNum,
      allChecked,
      cart
    });
    wx.setStorageSync('cart', cart)
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
    const address = wx.getStorageSync('address');
    const cart = wx.getStorageSync('cart') || [];
    this.setData({
      address
    });
    this.setCart(cart);
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