package org.goros.habit_tracker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class File {
    private String fileName;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
}
