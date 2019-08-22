package cn.pers.qhl.sendfile;

import cn.pers.qhl.sendfile.config.SendFileConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShareService {

    private static final Logger logger = LoggerFactory.getLogger(ShareService.class);

    @Autowired
    private SendFileConfig config;

    synchronized public ShareInfo getShare(String shareId) throws IOException {
        File shareDir = getShareDir(shareId);

        ObjectMapper objectMapper = new ObjectMapper();
//        try {
            ShareInfo shareInfo = objectMapper.readValue(new File(shareDir, Util.SHARE_INFO_FILE), ShareInfo.class);

            Long ttl = shareInfo.getTtlConfig() == -1 ? null : shareInfo.getTtlConfig() - (System.currentTimeMillis() - shareDir.lastModified());

            if (ttl != null && ttl <= 0) {
                logger.debug("删除共享" + shareId + "，已过期");
                deleteShareDir(shareId);
                throw new NotFoundException("未找到共享（" + shareId + "）");
            }

            ArrayList<ShareFile> files = new ArrayList<>();
            File filesDir = new File(shareDir, Util.FILES_DIR);
            for (File file : filesDir.listFiles()) {
                ShareFile shareFile = new ShareFile();
                shareFile.setName(file.getName());
                shareFile.setSize(file.length());
                files.add(shareFile);
            }

//            Long ttl = ((long) (config.getShare().getTtl() * 24 * 60 * 60 * 1000)) - (System.currentTimeMillis() - shareDir.lastModified());

//            Share share = new Share();
        shareInfo.setId(shareId);
        shareInfo.setTtl(ttl);
        shareInfo.setFiles(files);
//            share.setToken(shareInfo.getToken());
        shareInfo.setLastModified(shareDir.lastModified());

            return shareInfo;
//        } catch (IOException e) {
//            logger.error(e.getMessage(), e);
//            throw new ServerException(e);
//        }
    }

    synchronized public File getShareDir(String shareId) {
        if (shareId.length() == 8) {
            String lv1 = shareId.substring(0, 4);
            String lv2 = shareId.substring(4);
            File shareDir = new File(Util.REPO_ROOT, lv1 + "/" + lv2);
            if (!shareDir.exists()) {
                throw new NotFoundException("未找到共享（" + shareId + "）");
            } else {
                return shareDir;
            }
        } else {
            throw new BadRequestException("非法id：" + shareId);
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

    public static class CreateShareResult {
        public String id;
        public File dir;
        public CreateShareResult(String id, File dir) {
            this.id = id;
            this.dir = dir;
        }
    }

    synchronized public List<ShareInfo> getAllShares() {
        ArrayList<ShareInfo> shares = new ArrayList<>();
        File repo = new File(Util.REPO_ROOT);
        if (!repo.exists()) {
            repo.mkdirs();
        }
        for (String lv1Id : repo.list()) {
            File lv1Dir = new File(repo, lv1Id);
            for (String lv2Id : lv1Dir.list()) {
                String shareId = lv1Id + lv2Id;
                try {
                    ShareInfo shareInfo = getShare(shareId);
                    shares.add(shareInfo);
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return shares;
    }
}
