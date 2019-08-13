package cn.pers.qhl.sendfile;

import cn.pers.qhl.sendfile.config.SendFileConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class ShareService {

    private static final Logger logger = LoggerFactory.getLogger(ShareService.class);

    @Autowired
    private SendFileConfig config;

    public Share getShare(String shareId) {
        File shareDir = Util.getShareDir(shareId);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Share shareInfo = objectMapper.readValue(new File(shareDir, ".share"), Share.class);

            ArrayList<ShareFile> files = new ArrayList<>();
            for (File file : shareDir.listFiles()) {
                if (!".share".equals(file.getName())) {
                    ShareFile shareFile = new ShareFile();
                    shareFile.setName(file.getName());
                    shareFile.setSize(file.length());
                    files.add(shareFile);
                }
            }

            Long ttl = ((long) (config.getShare().getTtl() * 24 * 60 * 60 * 1000)) - (System.currentTimeMillis() - shareDir.lastModified());

            Share share = new Share();
            share.setId(shareId);
            share.setTtl(ttl);
            share.setFiles(files);
            share.setToken(shareInfo.getToken());

            return share;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ServerException(e);
        }
    }
}
