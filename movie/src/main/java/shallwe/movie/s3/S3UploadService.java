package shallwe.movie.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3UploadService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String upload(MultipartFile multipartFile) throws IOException {
        //확장자 확인
        String originFile = multipartFile.getOriginalFilename();
        if (originFile.equals("")) {
            return "";
        }
        //파일이름 중복방지
        String s3FileName = UUID.randomUUID() + "-" + originFile;

        //S3에 파일의 사이즈를 알려주는 용도
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());

        //S3 파일 업로드
        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);

        //DB 저장용 URL
        return amazonS3.getUrl(bucket, s3FileName).toString();
    }
}
