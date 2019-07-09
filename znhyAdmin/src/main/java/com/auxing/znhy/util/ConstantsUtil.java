package com.auxing.znhy.util;


/**
 * Created by GY on 2015/10/16.
 */
public class ConstantsUtil extends ConfigConstantsUtil {

    static {
        init("config/props/config.properties");
    }

    //获取项目名称
    public static String projectName = getValue("system.application.name");

    //获取ONS地址
    public static String onsAddress = getValue("ons.address");

    //获取ONS的Topic
    public static String onsTopic = getValue("ons.topic");

    //获取倍通报告的ONS的Topic
    public static String beitongOnsTopic = getValue("beitong.ons.topic");

    //存活于redis的时间
    public static long liveTime = getLongValue("liveTime");

    //获取token的用户名和密码
    public static String tokenUserName = getValue("token.username");
    public static String tokenPasswd = getValue("token.passwd");

    //获取司法拍卖OSS的ALIBA_ADDR和bucketName
    public static String OssALIBA_ADDR = getValue("Oss.ALIBA_ADDR");
    public static String OssbucketName = getValue("Oss.bucketName");

    public static final String UTF_8 = "UTF-8";

    // URL连接超时时间
    public static final int CONNECTION_TIMEOUT = 30000;

    // URL读取超时时间
    public static final int READ_TIMEOUT = 50000;

    //获取aliaccess的tokend的url
    public static String AliAccessTokenUrl = getValue("AliAccessTokenUrl");
    public static String AliAccessTokenParam = getValue("AliAccessTokenParam");
    public static String OSSEndpoint = getValue("OSSEndpoint");
    public static String OssAuctionBucket = getValue("OssAuctionBucket");
    public static String OssAuctionPath = getValue("OssAuctionPath");
    public static String OssTenderPath = getValue("OssTenderPath");

    public static String OssBucket = getValue("OssBucket");
    public static String OssCpwsPath = getValue("OssCpwsPath");

    // 获取Python程序路径
    public static String PythonSourcsPath = getValue("python.sourcs.path");
    public static String PythonPipelinePath = getValue("python.pipeline.path");

    // 本地仓库路径
    public static String localRepoPath = "C:/CodingNet/spiderprocessor";
    public static String localRepoGitConfig = getValue("localRepoGitConfig");
    public static String remoteRepoURI = "git@gitlab.com:wilson/test.git";
    public static String localCodeDir = "D:/platplat";

}
