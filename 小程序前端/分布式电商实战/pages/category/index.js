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
    leftMenuList: [], //左侧菜单数据
    rightContent: [], //右侧商品数据
    currentIndex: 0, //当前选中左侧菜单的索引
    scrollTop: 0 //设置滚动条位置
  },
  Cates: [],
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    const baseUrl = getBaseUrl();
    this.setData({
      baseUrl
    });
    this.getCates();
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    // console.log("onShow");
    const app = getApp();
    const {
      index
    } = app.globalData;
    if (index != -1) { //从首页跳转过来的
      this.getCates2(index);
      app.globalData.index = -1; //重置index
    }
  },
  async getCates() {
    const result = await requestUtil({
      url: '/bigType/findCategories',
      method: "GET"
    });
    this.Cates = result.message;
    let leftMenuList = this.Cates.map(v => v.name);
    let rightContent = this.Cates[0].smallTypeList;
    this.setData({
      leftMenuList,
      rightContent
    })
  },

  async getCates2(index) {
    const result = await requestUtil({
      url: '/bigType/findCategories',
      method: "GET"
    });
    this.Cates = result.message;
    let leftMenuList = this.Cates.map(v => v.name);
    let rightContent = this.Cates[index].smallTypeList;
    const baseUrl = getBaseUrl();
    this.setData({
      baseUrl,
      leftMenuList,
      rightContent,
      currentIndex: index,
      scrollTop: 0
    })
  },
  //左侧菜单点击切换事件
  handleMenuItemChange(e) {
    // console.log(e)
    const {
      index
    } = e.currentTarget.dataset;
    let rightContent = this.Cates[index].smallTypeList;
    this.setData({
      currentIndex: index,
      rightContent,
      scrollTop: 0
    })
  }
})