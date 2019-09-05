package cn.pers.qhl.sendfile;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class ShareService {

    private static final Logger logger = LoggerFactory.getLogger(ShareService.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MessageSource messageSource;

    synchronized public ShareInfo getShare(String shareId) {
        File shareDir = getShareDir(shareId);
        if (shareDir != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ShareInfo shareInfo = objectMapper.readValue(new File(shareDir, Util.SHARE_INFO_FILE), ShareInfo.class);

                Long ttl = shareInfo.getTtlConfig() == -1 ? null : shareInfo.getTtlConfig() - (System.currentTimeMillis() - shareDir.lastModified());

                if (ttl == null || ttl > 0){
                    ArrayList<ShareFile> files = new ArrayList<>();
                    File filesDir = new File(shareDir, Util.FILES_DIR);
                    for (File file : filesDir.listFiles()) {
                        ShareFile shareFile = new ShareFile();
                        shareFile.setName(file.getName());
                        shareFile.setSize(file.length());
                        files.add(shareFile);
                    }

                    shareInfo.setId(shareId);
                    shareInfo.setTtl(ttl);
                    shareInfo.setFiles(files);
                    shareInfo.setLastModified(shareDir.lastModified());

                    return shareInfo;
                } else {
                    logger.debug("删除共享" + shareId + "，已过期");
                    deleteShareDir(shareId);
                }
            } catch (IOException e) {
                logger.warn("删除共享" + shareId + "，文件已损坏。" + e.getMessage());
                deleteShareDir(shareId);
            }
        }
        return null;
    }

    synchronized public File getShareDir(String shareId) {
        if (shareId.length() == 8) {
            String lv1 = shareId.substring(0, 4);
            String lv2 = shareId.substring(4);
            File shareDir = new File(Util.REPO_ROOT, lv1 + "/" + lv2);
            if (!shareDir.exists()) {
                return null;
//                throw new NotFoundException("未找到共享（" + shareId + "）");
            } else {
                return shareDir;
            }
        } else {
//            throw new BadRequestException("非法id：" + shareId);
            throw new BadRequestException(getI18nMessage("illegalId", shareId));
        }
    }

    synchronized public CreateShareResult createShareDir() {
        while (true) {
            // id为当前时间毫秒数的36进制表示
            String id = Util.genId();

            // 按照 4位-4位 的模式分为两层目录，支持平均每53毫秒创建一个目录（基于每个目录最大32000个文件计算）
            String lv1 = id.substring(0, 4);
            String lv2 = id.substring(4);
            File shareDir = new File(Util.REPO_ROOT, lv1 + "/" + lv2);
            if (!shareDir.exists()) {
                File filesDir = new File(shareDir, Util.FILES_DIR);
                filesDir.mkdirs();
                return new CreateShareResult(id, shareDir);
            }
        }
    }

    synchronized public void deleteShareDir(String shareId) {
        File shareDir = getShareDir(shareId);
        FileSystemUtils.deleteRecursively(shareDir);

        // 若第一层share文件夹为空则删除
        File lv1ShareDir = shareDir.getParentFile();
        if (lv1ShareDir.list().length == 0) {
            FileSystemUtils.deleteRecursively(lv1ShareDir);
        }
    }

    public String getI18nMessage(String messageKey) {
        return getI18nMessage(messageKey, null);
    }

    public String getI18nMessage(String messageKey, String... objects) {
        return messageSource.getMessage(messageKey, objects, RequestContextUtils.getLocale(request));
    }

    public static class CreateShareResult {
        public String id;
        public File dir;
        public CreateShareResult(String id, File dir) {
            this.id = id;
            this.dir = dir;
        }
    }
    
}
