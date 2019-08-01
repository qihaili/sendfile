package cn.pers.qhl.sendfile;

import cn.pers.qhl.sendfile.config.SendFileConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private SendFileConfig config;

    @PostMapping(value = "upload")
    String upload(@RequestPart("file") MultipartFile[] files) {
//        String shareId = UUID.randomUUID().toString();
        String shareId = Util.genId();
        File shareDir = new File(Util.REPO_ROOT, shareId);
        shareDir.mkdirs();
        for (MultipartFile file : files) {
            if (config.getShare().getMaxFileSize() > 0 && file.getSize() > config.getShare().getMaxFileSize().longValue() * 1024 * 1024) {
                throw new BadRequestException("文件过大");
            }
            File destFile = new File(shareDir, file.getOriginalFilename());

            try {
                file.transferTo(destFile.getCanonicalFile());
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new ServerException(e);
            }
        }
        return shareId;
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
    List<ShareFile> get(@PathVariable String shareId) {
        File shareDir = Util.getShareDir(shareId);

        ArrayList<ShareFile> files = new ArrayList<>();
        for (String fileName : shareDir.list()) {
            ShareFile file = new ShareFile();
            file.setName(fileName);
            files.add(file);
        }

        return files;
    }

}
