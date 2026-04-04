package org.goros.habit_tracker.service;

import org.goros.habit_tracker.model.entity.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    File uploadFile(MultipartFile file);

    Resource getFileByFileName(String fileName);
}
