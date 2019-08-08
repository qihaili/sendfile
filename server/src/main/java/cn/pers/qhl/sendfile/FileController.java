package cn.pers.qhl.sendfile;

import cn.pers.qhl.sendfile.config.SendFileConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private SendFileConfig config;

    @PostMapping(value = "upload")
    Share upload(@RequestPart("file") MultipartFile[] files) {
//        String shareId = UUID.randomUUID().toString();
        String shareId = Util.genId();
        File shareDir = new File(Util.REPO_ROOT, shareId);
        shareDir.mkdirs();

        ArrayList<ShareFile> shareFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            if (config.getShare().getMaxFileSize() > 0 && file.getSize() > config.getShare().getMaxFileSize().longValue() * 1024 * 1024) {
                throw new BadRequestException("文件过大");
            }
            File destFile = new File(shareDir, file.getOriginalFilename());

            try {
                file.transferTo(destFile.getCanonicalFile());

                ShareFile shareFile = new ShareFile();
                shareFile.setName(file.getOriginalFilename());
                shareFile.setSize(file.getSize());
                shareFiles.add(shareFile);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new ServerException(e);
            }
        }

        try {
            String token = Util.genToken();
            ShareInfo shareInfo = new ShareInfo();
            shareInfo.setToken(token);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(shareDir, ".share"), shareInfo);

            Long ttl = ((long) (config.getShare().getTtl() * 24 * 60 * 60 * 1000)) - (System.currentTimeMillis() - shareDir.lastModified());

            Share share = new Share();
            share.setId(shareId);
            share.setTtl(ttl);
            share.setFiles(shareFiles);
            share.setToken(token);

            return share;
        } catch (IOException e) {
            throw new ServerException(e);
        }
    }

    @GetMapping("{shareId}/{filePath}")
    ResponseEntity<InputStreamResource> download(@PathVariable String shareId, @PathVariable String filePath) {
        File file = new File(Util.getShareDir(shareId), filePath);
        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment;fileName=" + file.getName())
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        } catch (FileNotFoundException e) {
            throw new NotFoundException("未找到文件（" + filePath + "）");
        }
    }

    @GetMapping("{shareId}")
    Share get(@PathVariable String shareId) {
        File shareDir = Util.getShareDir(shareId);

        ArrayList<ShareFile> files = new ArrayList<>();
        for (File file : shareDir.listFiles()) {
            ShareFile shareFile = new ShareFile();
            shareFile.setName(file.getName());
            shareFile.setSize(file.length());
            files.add(shareFile);
        }

        Long ttl = ((long) (config.getShare().getTtl() * 24 * 60 * 60 * 1000)) - (System.currentTimeMillis() - shareDir.lastModified());

        Share share = new Share();
        share.setId(shareId);
        share.setTtl(ttl);
        share.setFiles(files);

        return share;
    }

    @DeleteMapping("{shareId}")
    void delete(@PathVariable String shareId, @RequestHeader String token) {
        File shareDir = Util.getShareDir(shareId);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ShareInfo shareInfo = objectMapper.readValue(new File(shareDir, ".share"), ShareInfo.class);
            if (shareInfo.getToken().equals(token)) {
                FileSystemUtils.deleteRecursively(shareDir);
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            } else {
                throw new UnauthorizedException();
            }
        } catch (IOException e) {
            throw new ServerException(e);
        }
    }

}
