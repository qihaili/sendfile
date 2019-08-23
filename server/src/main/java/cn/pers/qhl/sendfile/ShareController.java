package cn.pers.qhl.sendfile;

import cn.pers.qhl.sendfile.config.SendFileConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/shares")
public class ShareController {

    private static final Logger logger = LoggerFactory.getLogger(ShareController.class);

    @Autowired
    private SendFileConfig config;

    @Autowired
    private ShareService shareService;

    @PostMapping
    ShareWithToken upload(@RequestPart String ttl, @RequestPart(required = false) String password, @RequestPart("file") MultipartFile[] files) {
        try {
            ShareService.CreateShareResult result = shareService.createShareDir();
            File shareDir = result.dir;
            String shareId = result.id;
            File filesDir = new File(shareDir, Util.FILES_DIR);

            String token = Util.genToken();
            ShareInfo shareInfo = new ShareInfo();
            shareInfo.setToken(token);
            shareInfo.setTtlConfig(Util.parseTtlToMillis(ttl));
            shareInfo.setPassword(password);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(shareDir, Util.SHARE_INFO_FILE), shareInfo);

            for (MultipartFile file : files) {
                if (config.getShare().getMaxFileSize() > 0 && file.getSize() > config.getShare().getMaxFileSize().longValue() * 1024 * 1024) {
                    throw new BadRequestException("文件过大");
                }
                File destFile = new File(filesDir, file.getOriginalFilename());
                file.transferTo(destFile.getCanonicalFile());
            }

            ShareInfo shareInfo1 = shareService.getShare(shareId);
            ShareWithToken shareWithToken = new ShareWithToken();
            BeanUtils.copyProperties(shareInfo1, shareWithToken);

            return shareWithToken;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ServerException(e);
        }
    }

    @GetMapping("{shareId}/{filePath}")
    ResponseEntity<InputStreamResource> download(@PathVariable String shareId, @PathVariable String filePath, HttpSession session) {
        ShareInfo shareInfo = shareService.getShare(shareId);
        if (shareInfo != null) {
            if (shareInfo.getPassword() == null || shareAuthorized(session, shareId)) {
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
            } else {
                throw new UnauthorizedException();
            }
        } else {
            throw new BadRequestException("未找到共享：" + shareId);
        }
    }

    @GetMapping("{shareId}")
    Share get(@PathVariable String shareId, @RequestHeader(required = false) String token, @RequestHeader(required = false) String password, HttpSession session) {
        ShareInfo shareInfo = shareService.getShare(shareId);
        if (shareInfo != null) {
            if (shareInfo.getPassword() == null || shareInfo.getToken().equals(token) || shareAuthorized(session, shareId)) {
                Share share = new Share();
                BeanUtils.copyProperties(shareInfo, share);
                return share;
            } else {
                throw new UnauthorizedException();
            }
        } else {
            throw new BadRequestException("未找到共享：" + shareId);
        }
    }

    @DeleteMapping("{shareId}")
    void delete(@PathVariable String shareId, @RequestHeader String token) {
        ShareInfo shareInfo = shareService.getShare(shareId);
        if (shareInfo != null) {
            if (shareInfo.getToken().equals(token)) {
                shareService.deleteShareDir(shareId);
            } else {
                throw new UnauthorizedException();
            }
        } else {
            throw new BadRequestException("未找到共享：" + shareId);
        }
    }

    @PostMapping("{shareId}/authorize")
    void authorized(@PathVariable String shareId, @RequestBody AuthenticateInfo authenticateInfo, HttpSession session) {
        ShareInfo shareInfo = shareService.getShare(shareId);
        if (shareInfo.getPassword() != null) {
            if (shareInfo.getPassword().equals(authenticateInfo.getPassword())) {
                getAuthorizedShares(session).add(shareId);
            } else {
                throw new UnauthorizedException();
            }
        } else {
            throw new BadRequestException("该共享无需验证");
        }
    }

    private boolean shareAuthorized(HttpSession session, String shareId) {
        return getAuthorizedShares(session).contains(shareId);
    }

    private Set<String> getAuthorizedShares(HttpSession session) {
        Set<String> authorizedShares = (Set) session.getAttribute("authorizedShares");
        if (authorizedShares == null) {
            authorizedShares = new HashSet<>();
            session.setAttribute("authorizedShares", authorizedShares);
        }
        return authorizedShares;
    }

}
