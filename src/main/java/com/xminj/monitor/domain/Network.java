package com.xminj.monitor.domain;


public class Network {

    /**
     * 网络接口
     */
    private String networkInterface;

    /**
     * ip v4
     */
    private String iPv4addr;

    /**
     * 上传
     */
    private Long networkSent;

    /**
     * 下载
     */
    private Long networkRecv;


    public String getiPv4addr() {
        return iPv4addr;
    }

    public void setiPv4addr(String iPv4addr) {
        this.iPv4addr = iPv4addr;
    }

    public String getNetworkInterface() {
        return networkInterface;
    }

    public void setNetworkInterface(String networkInterface) {
        this.networkInterface = networkInterface;
    }

    public Long getNetworkRecv() {
        return networkRecv;
    }

    public void setNetworkRecv(Long networkRecv) {
        this.networkRecv = networkRecv;
    }

    public Long getNetworkSent() {
        return networkSent;
    }

    public void setNetworkSent(Long networkSent) {
        this.networkSent = networkSent;
    }
}
