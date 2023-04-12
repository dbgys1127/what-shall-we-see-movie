package shallwe.movie.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marvin.image.MarvinImage;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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
        String s3FileName = UUID.randomUUID().toString().substring(0,6) + "-" + originFile;
        String s3FileFormat = multipartFile.getContentType().substring(multipartFile.getContentType().lastIndexOf("/")+1);

        //파일 리사이징
        MultipartFile resizedImage = resizer(s3FileName, s3FileFormat, multipartFile, 250);

        //S3에 파일의 사이즈를 알려주는 용도
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(resizedImage.getSize());
        objMeta.setContentType(resizedImage.getContentType());

        //S3 파일 업로드
        amazonS3.putObject(new PutObjectRequest(bucket, s3FileName, resizedImage.getInputStream(), objMeta));

        //DB 저장용 URL
        return amazonS3.getUrl(bucket, s3FileName).toString();
    }

    @Transactional
    public MultipartFile resizer(String s3FileName, String s3FileFormat, MultipartFile multipartFile,int width) {
        try {
            BufferedImage image = ImageIO.read(multipartFile.getInputStream());

            int originWidth = image.getWidth();
            int originHeight = image.getHeight();

            if (originWidth < width) {
                return multipartFile;
            }

            MarvinImage marvinImage = new MarvinImage(image);

            Scale scale = new Scale();
            scale.load();
            scale.setAttribute("newWidth",width);
            scale.setAttribute("newHeight",width*originHeight/originWidth);
            scale.process(marvinImage.clone(),marvinImage,null,null,false);

            BufferedImage imageNoAlpha = marvinImage.getBufferedImageNoAlpha();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(imageNoAlpha, s3FileFormat, byteArrayOutputStream);
            byteArrayOutputStream.flush();

            return new CustomMultipartFile(s3FileName, s3FileFormat, multipartFile.getContentType(), byteArrayOutputStream.toByteArray());

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"파일 리사이징 실패");
        }
    }
}
