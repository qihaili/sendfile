package cn.pers.qhl.sendfile;

import cn.pers.qhl.sendfile.config.SendFileConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/api/shares")
public class ShareController {

    private static final Logger logger = LoggerFactory.getLogger(ShareController.class);

    @Autowired
    private SendFileConfig config;

    @Autowired
    private ShareService shareService;

    @PostMapping
    Share upload(@RequestPart("ttl") String ttl, @RequestPart("file") MultipartFile[] files) {
        try {
            ShareService.CreateShareResult result = shareService.createShareDir();
            File shareDir = result.dir;
            String shareId = result.id;
            File filesDir = new File(shareDir, Util.FILES_DIR);

            String token = Util.genToken();
            ShareInfo shareInfo = new ShareInfo();
            shareInfo.setToken(token);
            shareInfo.setTtlConfig(Util.parseTtlToMillis(ttl));

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(shareDir, Util.SHARE_INFO_FILE), shareInfo);

            for (MultipartFile file : files) {
                if (config.getShare().getMaxFileSize() > 0 && file.getSize() > config.getShare().getMaxFileSize().longValue() * 1024 * 1024) {
                    throw new BadRequestException("文件过大");
                }
                File destFile = new File(filesDir, file.getOriginalFilename());
                file.transferTo(destFile.getCanonicalFile());
            }

            return shareService.getShare(shareId);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ServerException(e);
        }
    }

    @GetMapping("{shareId}/{filePath}")
    ResponseEntity<InputStreamResource> download(@PathVariable String shareId, @PathVariable String filePath) {
        File file = new File(new File(shareService.getShareDir(shareId), Util.FILES_DIR), filePath);
        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment;fileName=" + file.getName())
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new NotFoundException("未找到文件（" + filePath + "）", e);
        }
    }

    @GetMapping("{shareId}")
    Share get(@PathVariable String shareId) {
        Share share = shareService.getShare(shareId);
        share.setToken(null);
        return share;
    }

    @DeleteMapping("{shareId}")
    void delete(@PathVariable String shareId, @RequestHeader String token) {
        Share share = shareService.getShare(shareId);
        if (share.getToken().equals(token)) {
            shareService.deleteShareDir(shareId);
        } else {
            throw new UnauthorizedException();
        }
    }

}
