package noorg.filesharing.controller;

import noorg.generated.api.FileApi;
import noorg.generated.model.ListFilesResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

@RestController
public class FileController implements FileApi {
    private static final File ROOT_FOLDER;

    static {
        ROOT_FOLDER = Path.of(System.getenv("HOME"), "file-sharing").toFile();
        ROOT_FOLDER.mkdirs();
    }

    @Override
    public ResponseEntity<ListFilesResponse> listFiles() {
        File[] files = ROOT_FOLDER.listFiles(f -> f.isFile() && !f.isHidden());
        ListFilesResponse response = new ListFilesResponse();
        response.setFiles(Arrays.stream(Objects.requireNonNull(files)).map(f -> String.format("%.2fMB %s", f.length() / 1024f / 1024f, f.getName())).toList());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Resource> downloadFile(String filename) {
        File file = new File(ROOT_FOLDER, filename);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new FileSystemResource(file));
    }

    @Override
    public ResponseEntity<Void> uploadFile(String filename, MultipartFile multipartFile) {
        File file = new File(ROOT_FOLDER, filename);
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }
}
