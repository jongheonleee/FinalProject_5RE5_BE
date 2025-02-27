package com.oreo.finalproject_5re5_be.project.service;

import com.oreo.finalproject_5re5_be.project.dto.response.ProjectResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ProjectService {
    /** 1. 프로젝트 조회 2. 프로젝트 생성 3. 프로젝트 이름 변경 4. 프로젝트 삭제 */
    List<ProjectResponse> findAllProject(Long memberSeq);

    Long saveProject(Long memberSeq);

    void updateProject(@Valid @NotNull Long projectSeq, @Valid @NotNull String projectName);

    void deleteProject(@Valid @NotNull List<Long> projectSeq);

    boolean checkProject(Long memberSeq, Long projectSeq);

    boolean checkProject(Long memberSeq, List<Long> projectSeq);
}
