//定义请求根路径baseUrl
const baseUrl = "http://localhost:8080";

//同时并发请求的次数
let ajaxTime = 0;

//返回请求根路径
export const getBaseUrl = () => {
  return baseUrl;
}
//wx login封装
export const getWxLogin = () => {
  return new Promise((resolve, reject) => {
    wx.login({
      timeout: 5000,
      success: (res) => {
        resolve(res)
      },
      fail: (err) => {
        resolve(err)
      }
    })
  });
}
//wx getUserProfile封装
export const getUserProfile = () => {
  return new Promise((resolve, reject) => {
    wx.getUserProfile({
      desc: '获取用户信息',
      success: (res) => {
        resolve(res)
      },
      fail: (err) => {
        resolve(err)
      }
    })
  });
}

//promise形式 微信小程序支付
export const requestPay = (pay) => {
  return new Promise((resolve, reject) => {
    wx.requestPayment({
      ...pay,
      success: (res) => {
        resolve(res)
      },
      fail: (err) => {
        resolve(err)
      }
    })
  });
}

// 后端请求工具类
export const requestUtil = (params) => {

  //判断url中是否带有my （私有路径）带上herder token
  let header ={...params.header};
  if(params.url.includes("/my/")){
    //拼接header带上token
    header["token"]=wx.getStorageSync('token');
  }
  //模拟网络延迟加载
  var start = new Date().getTime();
  while (true) {
    if (new Date().getTime() - start > 1 * 500) break;
  }
  ajaxTime++;
  wx.showLoading({
    title: '加载中...',
    mask: true
  })

  return new Promise((resolve, reject) => {
    wx.request({
      ...params,
      header,
      url: baseUrl + params.url,
      success: (result) => {
        resolve(result.data);
      },
      fail: (err) => {
        reject(err);
      },
      complete: () => {
        ajaxTime--;
        if (ajaxTime == 0) {
          wx.hideLoading();
        }
      }
    })
  });
}