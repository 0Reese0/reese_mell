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
    address: {},
    baseUrl: '',
    cart: [],
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
  //订单支付
  async handleOrderPay() {
    // let res1=await getWxLogin();
    // let res2=await getUserProfile();
    const token = wx.getStorageSync('token');
    if (!token) {
      Promise.all([getWxLogin(), getUserProfile()]).then((res) => {
        // console.log(res)
        let loginParam = {
          code: res[0].code,
          nickName: res[1].userInfo.nickName,
          avatarUrl: res[1].userInfo.avatarUrl
        }
        wx.setStorageSync('nickName', res[1].userInfo.nickName);
        wx.setStorageSync('avatarUrl', res[1].userInfo.avatarUrl);
        this.wxlogin(loginParam)
      })
    } else {
      this.createOrder();
    }
  },
  //创建订单
  async createOrder() {

    try {
      const totalPrice = this.data.totalPrice;
      const address = this.data.address.provinceName + this.data.address.cityName + this.data.address.countyName + this.data.address.detailInfo;
      const consignee = this.data.address.userName;
      const telNumber = this.data.address.telNumber;
      const goods = [];
      this.data.cart.forEach(v => goods.push({
        goodsId: v.id,
        goodsNumber: v.num,
        goodsPrice: v.price,
        goodsName: v.name,
        goodsPic: v.proPic
      }))
      const orderParam = {
        totalPrice,
        address,
        consignee,
        telNumber,
        goods
      }
      const res = await requestUtil({
        url: "/my/order/create",
        data: orderParam,
        method: "POST"
      })
      // console.log(res);
      let orderNo = res.orderNo;
      //调用统一下单，预支付
      const preparePayRes = await requestUtil({
        url: "/my/order/preparePay",
        data: orderNo,
        method: "POST"
      })
      // console.log(preparePayRes)

      let payRes = await requestPay(preparePayRes);
      // console.log(payRes)

      //删除缓存中，已经支付的商品
      let newCart = wx.getStorageSync('cart');
      newCart = newCart.filter(v => !v.checked);
      wx.setStorageSync('cart', newCart);
      wx.showToast({
        title: '支付成功',
        icon: 'none'
      })
      wx.navigateTo({
        url: '/pages/order/index?type=0',
      })
    } catch (error) {
      console.log(error)
      wx.showToast({
        title: '支付失败，请稍后重试',
        icon: 'none'
      })
    }
  },
  //请求获取后端用户token
  async wxlogin(loginParam) {
    const result = await requestUtil({
      url: "/user/wxlogin",
      data: loginParam,
      method: "POST"
    });
    // console.log(result);
    const token = result.token;
    if (result.code === 0) {
      wx.setStorageSync('token', token);
      this.createOrder();
    }
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    const address = wx.getStorageSync('address');
    let cart = wx.getStorageSync('cart') || [];
    cart = cart.filter(v => v.checked);
    let allChecked = true;
    let totalPrice = 0;
    let totalNum = 0;
    cart.forEach(v => {
      totalPrice += v.num * v.price;
      totalNum += v.num;
    })
    this.setData({
      totalPrice,
      totalNum,
      cart,
      address
    });
  }
})