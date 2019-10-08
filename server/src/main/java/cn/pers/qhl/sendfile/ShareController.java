package cn.pers.qhl.sendfile;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/shares")
public class ShareController {

    private static final Logger logger = LoggerFactory.getLogger(ShareController.class);

    @Autowired
    private ShareService shareService;

    @Autowired
    private HttpSession session;

    @GetMapping
    List<ShareWithToken> list() {
        if (isAdmin()) {
            List<ShareWithToken> shareWithTokens = new ArrayList<>();
            for (ShareInfo shareInfo : shareService.getAllShares()) {
                ShareWithToken shareWithToken = new ShareWithToken();
                BeanUtils.copyProperties(shareInfo, shareWithToken);
                shareWithTokens.add(shareWithToken);
            }
            return shareWithTokens;
        } else {
            throw new UnauthorizedException();
        }
    }

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
//                if (config.getShare().getMaxFileSize() > 0 && file.getSize() > config.getShare().getMaxFileSize().longValue() * 1024 * 1024) {
//                    throw new BadRequestException("文件过大");
//                }
                File destFile = new File(filesDir, file.getOriginalFilename());
                file.transferTo(destFile.getCanonicalFile());
            }

            ShareInfo shareInfo1 = shareService.getShare(shareId);
            ShareWithToken shareWithToken = new ShareWithToken();
            BeanUtils.copyProperties(shareInfo1, shareWithToken);

            getOwnShares().add(shareId);

            return shareWithToken;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ServerException(e);
        } catch (IllegalUnitException e) {
            logger.error(e.getMessage(), e);
            throw new BadRequestException(shareService.getI18nMessage("illegalUnit: " + e.getMessage()));
        }
    }

    @GetMapping("{shareId}/{filePath}")
    ResponseEntity<InputStreamResource> download(@PathVariable String shareId, @PathVariable String filePath) {
        ShareInfo shareInfo = shareService.getShare(shareId);
        if (shareInfo != null) {
            // 下载文件同样需要验证密码或Token
            if (shareInfo.getPassword() == null || isShareOwner(shareId) || isShareViewer(shareId) || isAdmin()) {
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
                    throw new NotFoundException(shareService.getI18nMessage("notFound.file", filePath), e);
                }
            } else {
                throw new UnauthorizedException();
            }
        } else {
            throw new NotFoundException(shareService.getI18nMessage("notFound.share", shareId));
        }
    }

    @GetMapping("{shareId}")
    Share get(@PathVariable String shareId) {
        ShareInfo shareInfo = shareService.getShare(shareId);
        if (shareInfo != null) {
            if (shareInfo.getPassword() == null || isShareOwner(shareId) || isShareViewer(shareId)) {
                Share share = new Share();
                BeanUtils.copyProperties(shareInfo, share);
                return share;
            } else {
                throw new UnauthorizedException();
            }
        } else {
            throw new NotFoundException(shareService.getI18nMessage("notFound.share", shareId));
        }
    }

    @DeleteMapping("{shareId}")
    void delete(@PathVariable String shareId, @RequestHeader String token) {
        ShareInfo shareInfo = shareService.getShare(shareId);
        if (shareInfo != null) {
            if (isShareOwner(shareId) || isAdmin()) {
                shareService.deleteShareDir(shareId);
            } else {
                throw new UnauthorizedException();
            }
        } else {
            throw new NotFoundException(shareService.getI18nMessage("notFound.share", shareId));
        }
    }

//    @PostMapping("viewer/authorize")
//    void authorizeViewer(@RequestBody ShareWithPassword[] shareWithPasswords) {
//        for (ShareWithPassword share : shareWithPasswords) {
//            if (shareService.getShare(share.getId()).getPassword().equals(share.getPassword())) {
//                getCanReadShares().add(share.getId());
//            } else {
//                throw new UnauthorizedException();
//            }
//        }
//    }
//
//    @PostMapping("owner/authorize")
//    void authorizeOwner(@RequestBody ShareWithToken[] shareTokens) {
//        for (ShareWithToken share : shareTokens) {
//            String shareId = share.getId();
//            if (!StringUtils.isEmpty(shareId)) {
//                ShareInfo shareInfo = shareService.getShare(shareId);
//                if (shareInfo != null) {
//                    if (shareInfo.getToken().equals(share.getToken())) {
//                        getOwnShares().add(shareId);
//                    } else {
//                        throw new UnauthorizedException();
//                    }
//                }
//            }
//        }
//    }

    @PostMapping("authorize")
    void authorize(@RequestBody ShareCredential share) {
        String shareId = share.getId();
        if (!StringUtils.isEmpty(shareId)) {
            ShareInfo shareInfo = shareService.getShare(shareId);
            if (shareInfo != null) {
                if (shareInfo.getToken().equals(share.getToken())) {
                    getOwnShares().add(shareId);
                } else if (shareInfo.getPassword().equals(share.getPassword())) {
                    getCanReadShares().add(shareId);
                } else {
                    throw new UnauthorizedException();
                }
            }
        }
    }

    private boolean isShareOwner(String shareId) {
        Set<String> ownShares = getOwnShares();
        return ownShares.contains(shareId);
    }

    private Set<String> getOwnShares() {
        Set<String> ownShares = (Set) session.getAttribute("ownShares");
        if (ownShares == null) {
            ownShares = new HashSet<>();
            session.setAttribute("ownShares", ownShares);
        }
        return ownShares;
    }


    // 是否有查看权限
    private boolean isShareViewer(String shareId) {
        Set<String> canReadShares = getCanReadShares();
        return canReadShares.contains(shareId);
    }

    private Set<String> getCanReadShares() {
        Set<String> canReadShares = (Set) session.getAttribute("canReadShares");
        if (canReadShares == null) {
            canReadShares = new HashSet<>();
            session.setAttribute("canReadShares", canReadShares);
        }
        return canReadShares;
    }

    private boolean isAdmin() {
        return session.getAttribute("authenticated") != null;
    }

}
