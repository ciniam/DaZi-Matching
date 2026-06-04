package com.xiejun.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Component
public class QiniuOssUtils {

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.access-key}")
    private String accessKey;

    @Value("${qiniu.secret-key}")
    private String secretKey;

    @Value("${qiniu.base-url}")
    private String baseUrl;

    private UploadManager upload;
    private BucketManager bucketManager;

    public QiniuOssUtils() {
        Configuration cfg = new Configuration(Region.huanan());
        this.upload = new UploadManager(cfg);
    }

    private Auth getAuth() {
        return Auth.create(accessKey, secretKey);
    }

    private String getToken() {
        return getAuth().uploadToken(bucket);
    }

    public String upload(File file, String key) {
        try {
            return upload(new FileInputStream(file), key);
        } catch (FileNotFoundException | QiniuException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String upload(InputStream is, String key) throws QiniuException {
        Response response = upload.put(is, key, getToken(), null, null);
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return getSignedUrl(baseUrl + "/" + putRet.key);
    }

    public String getSignedUrl(String rawUrl) {
        if (rawUrl == null || rawUrl.isEmpty() || !rawUrl.contains(baseUrl)) {
            return rawUrl;
        }
        long expireSeconds = 365L * 24 * 60 * 60;
        return getAuth().privateDownloadUrl(rawUrl, expireSeconds);
    }

    public void delete(String key) {
        if (bucketManager == null) {
            bucketManager = new BucketManager(getAuth(), new Configuration(Region.huanan()));
        }
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }
}
