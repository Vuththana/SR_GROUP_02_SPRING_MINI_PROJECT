package org.goros.habit_tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.goros.habit_tracker.model.entity.File;
import org.goros.habit_tracker.model.response.ApiResponse;
import org.goros.habit_tracker.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;


    @PostMapping(value = "upload-file",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a file")
    public ResponseEntity<ApiResponse<File>> uploadFile(@RequestParam MultipartFile file){
        File resource = fileService.uploadFile(file);
        ApiResponse<File> response = ApiResponse.<File>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .message("File uploaded successfully")
                .payload(resource)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/preview-file/{file-name}")
    @Operation(summary = "Preview a file")
    public ResponseEntity<Resource> getFileByFileName(@PathVariable("file-name") String fileName){

        Resource resource = fileService.getFileByFileName(fileName);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(resource);
    }

}
