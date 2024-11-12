package com.oreo.finalproject_5re5_be.audio.service;

import com.mpatric.mp3agic.Mp3File;
import com.oreo.finalproject_5re5_be.audio.dto.response.AudioFileInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;

@Slf4j
@Component
public class AudioInfo {
    //파일 이름,길이,크기,확장자 추출 메서드
    public AudioFileInfo extractAudioFileInfo(MultipartFile audioFile) {
        String fileName = audioFile.getOriginalFilename();
        String fileSize = String.valueOf(audioFile.getSize());

        // 파일 확장자 추출
        String fileExtension = "";
        if (fileName != null && fileName.contains(".")) {
            fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        }

        long fileLength = 0L;

        // 임시 파일로 변환하여 파일 길이 추출
        try {
            File tempFile = File.createTempFile("temp", "." + fileExtension);
            audioFile.transferTo(tempFile);

            if ("mp3".equalsIgnoreCase(fileExtension)) {
                // mp3 파일 길이 추출
                Mp3File mp3File = new Mp3File(tempFile);
                fileLength = mp3File.getLengthInSeconds();
            } else if ("wav".equalsIgnoreCase(fileExtension)) {
                // wav 파일 길이 추출
                fileLength = getWavFileDuration(tempFile);
            }

            tempFile.delete(); // 임시 파일 삭제
        } catch (Exception e) {
            log.error("오디오 파일 정보를 추출하는 중 오류 발생: ", e);
        }
        return AudioFileInfo.builder()
                .name(fileName)
                .size(fileSize)
                .length(fileLength)
                .extension(fileExtension)
                .build();
    }
    //wav 파일 일경우 파일 길이 추출하는 메서드
    private long getWavFileDuration(File wavFile) {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile)) {
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            return (long) (frames / format.getFrameRate()); // 초 단위 길이 반환
        } catch (Exception e) {
            log.error("WAV 파일 길이 추출 중 오류 발생: ", e);
            return 0L;
        }
    }
}
