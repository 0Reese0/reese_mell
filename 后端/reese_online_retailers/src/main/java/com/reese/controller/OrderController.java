package com.reese.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reese.entity.Order;
import com.reese.entity.OrderDetail;
import com.reese.entity.R;
import com.reese.properties.WeixinpayProperties;
import com.reese.service.IOrderDetailService;
import com.reese.service.IOrderService;
import com.reese.util.*;
import io.jsonwebtoken.Claims;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.util.*;

/*
 * 订单Controller
 * */
@RestController
@RequestMapping("/my/order")
public class OrderController {

    @Autowired
    private IOrderDetailService orderDetailServicel;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private WeixinpayProperties weixinpayProperties;


    //创建订单，返回订单号
    @RequestMapping("/create")
    //保证事务一致
    @Transactional
    public R create(@RequestBody Order order,@RequestHeader (value = "token")String token){
        //通过token获取openid
//         System.out.println("token:"+token);
//         System.out.println("order"+order.toString());
        Claims claims=JwtUtils.validateJWT(token).getClaims();
        if(claims!=null){
            System.out.println("openid:"+claims.getId());
            order.setUserId(claims.getId());
        }
        order.setOrderNo("Reese"+DateUtil.getCurrentDateStr());

        //添加订单到数据库
        order.setCreateDate(new Date());
        OrderDetail[] goods=order.getGoods();
        orderService.save(order);

        //添加订单详情到数据库
        for(int i=0;i<goods.length;i++){
            OrderDetail orderDetail=goods[i];
            orderDetail.setMId(order.getId());
            orderDetailServicel.save(orderDetail);
        }

        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("orderNo",order.getOrderNo());

        return R.ok(resultMap);
    }



    @RequestMapping("/preparePay")
    public R preparePay(@RequestBody String orderNo)throws Exception{
//        System.out.println("orderNo:"+orderNo);
        Order order=orderService.getOne(new QueryWrapper<Order>().eq("orderNo",orderNo));
//        System.out.println(order.toString());
        Map<String,Object> map=new HashMap<>();
        map.put("appid",weixinpayProperties.getAppid());
        map.put("mch_id",weixinpayProperties.getAppid());
        map.put("nonce_str",StringUtil.getRandomString(32));
        map.put("body","Reese_商品购买测试");
        map.put("out_trade_no",orderNo);
        map.put("total_fee",order.getTotalPrice().movePointRight(2));
        map.put("spbill_create_ip","127.0.0.1");
        map.put("notify_url",weixinpayProperties.getNotify_url());
        map.put("trade_type","JSAPI");
        map.put("openid",order.getUserId());
        map.put("sign",getSign(map));

        //参数转xml
        String xml=XmlUtil.genXml(map);

        HttpResponse httpResponse=HttpClientUtil.sendXMLDataByPost(weixinpayProperties.getUrl().toString(),xml);
        String httpEntityContent=HttpClientUtil.getHttpEntityContent(httpResponse);

        Map resultMap = XmlUtil.doXMLParse(httpEntityContent);

//        System.out.println("resultMap="+resultMap);

        if(resultMap.get("result_code").equals("SUCCESS")){
            Map<String,Object> payMap=new HashMap<>();
            payMap.put("appId",resultMap.get("appid"));
            payMap.put("timeStamp",System.currentTimeMillis()/1000+"");
            payMap.put("nonceStr",StringUtil.getRandomString(32));
            payMap.put("package","prepay_id="+resultMap.get("prepay_id"));
            payMap.put("signType","MD5");
            payMap.put("paySign",getSign(payMap));
            payMap.put("orderNo",orderNo);
            return R.ok(payMap);
        }else{
            return R.error(500,"系统报错，请联系管理员！");
        }
    }

    /*** 微信支付签名算法sign */
    private String getSign(Map<String,Object> map) {
        StringBuffer sb = new StringBuffer();
        String[] keyArr = (String[]) map.keySet().toArray(new String[map.keySet().size()]);//获取map中的key转为array
        Arrays.sort(keyArr);//对array排序
        for (int i = 0, size = keyArr.length; i < size; ++i) {
            if ("sign".equals(keyArr[i])) {
                continue;
            }
            sb.append(keyArr[i] + "=" + map.get(keyArr[i]) + "&");
        }
        sb.append("key=" + weixinpayProperties.getKey());
        String sign = string2MD5(sb.toString());
        return sign;

    }

    /*** * MD5加码 生成32位md5码 */
    private String string2MD5(String str){
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(str.getBytes("UTF-8"));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf).toUpperCase();
        } catch (Exception e) {
            return null;
        }
    }

    //订单查询 type  0全部订单  1待付款  2待收货  3退款/退货
    @RequestMapping("/list")
    public R list(Integer type,Integer page,Integer pageSize){
        System.out.println(type);
        List<Order> orders=null;
        Map<String,Object> resultMap=new HashMap<String,Object>();
        Page<Order> pageOrder=new Page<>(page,pageSize);
        if(type==0){
            Page<Order> orderResult=orderService.page(pageOrder,new QueryWrapper<Order>().orderByDesc("id"));
            orders=orderResult.getRecords();
            resultMap.put("total",orderResult.getTotal());
            resultMap.put("totalPage",orderResult.getPages());
        }else{
            Page<Order> orderResult=orderService.page(pageOrder,new QueryWrapper<Order>().eq("status",type).orderByDesc("id"));
            orders=orderResult.getRecords();
            resultMap.put("total",orderResult.getTotal());
            resultMap.put("totalPage",orderResult.getPages());
        }
        resultMap.put("orders",orders);
        return R.ok(resultMap);
    }



}
