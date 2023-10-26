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
    nickName: '',
    avatarUrl: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    const token = wx.getStorageSync('token');
    if (!token) {
      wx.showModal({
        title: '友情提示',
        content: '微信授权登录后，才可进入个人中心！',
        success: (res) => {
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
            this.setData({
              nickName: wx.getStorageSync('nickName'),
              avatarUrl: wx.getStorageSync('avatarUrl')
            })
          })
        }
      })
    } else {
      this.setData({
        nickName: wx.getStorageSync('nickName'),
        avatarUrl: wx.getStorageSync('avatarUrl')
      })
    }

  },
  async wxlogin(loginParam) {
    const result = await requestUtil({
      url: "/user/wxlogin",
      data: loginParam,
      method: "POST"
    });
    // console.log(result);
    const token = result.token;
    if (result.code === 0) {
      wx.setStorageSync('token', token)
    }
  },
  handleEditAddress(e){
    wx.chooseAddress({
      success: (result) => {},
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