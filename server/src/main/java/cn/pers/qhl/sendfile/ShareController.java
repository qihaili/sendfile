package cn.pers.qhl.sendfile;

import cn.pers.qhl.sendfile.config.SendFileConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/shares")
public class ShareController {

    private static final Logger logger = LoggerFactory.getLogger(ShareController.class);

    @Autowired
    private ShareService shareService;

    @Autowired
    private SendFileConfig config;

    @PostMapping
    Share create() {
        String shareId = Util.genId();
        File shareDir = new File(Util.REPO_ROOT, shareId);
        shareDir.mkdirs();

        try {
            String token = Util.genToken();
            ShareInfo shareInfo = new ShareInfo();
            shareInfo.setToken(token);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(shareDir, ".share"), shareInfo);

            return shareService.getShare(shareId);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ServerException(e);
        }
    }

    @PostMapping("{shareId}")
    Share uploadFile(@PathVariable String shareId, @RequestPart MultipartFile file) {
        if (config.getShare().getMaxFileSize() > 0 && file.getSize() > config.getShare().getMaxFileSize().longValue() * 1024 * 1024) {
            throw new BadRequestException("文件过大");
        } else {
            File shareDir = new File(Util.REPO_ROOT, shareId);
            File destFile = new File(shareDir, file.getOriginalFilename());
            try {
                file.transferTo(destFile.getCanonicalFile());
                return shareService.getShare(shareId);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new ServerException(e);
            }
        }
    }
}
