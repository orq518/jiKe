package com.topad.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ${todo}<我的需求详情实体>
 *
 * @author lht
 * @data: on 15/10/26 11:06
 */
public class MyNeedListBean implements Serializable {
    /**product
     * serialVersionUID:TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = 4768927122317982665L;
    /** 状态码 **/
    protected int status;
    /** error信息 **/
    protected String msg;
    /** data **/
    public ArrayList<MyNeedBean> data = new ArrayList<MyNeedBean>();

    @Override
    public String toString() {
        return "MyNeedListBean{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
