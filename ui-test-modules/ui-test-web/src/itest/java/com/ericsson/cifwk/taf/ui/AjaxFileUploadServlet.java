package com.ericsson.cifwk.taf.ui;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class AjaxFileUploadServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            return;
        }

        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Configure a repository (to ensure a secure temp location is used)
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = new File(System.getProperty("java.io.tmpdir"));

        factory.setRepository(repository);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // Parse the request
        try {
            List<FileItem> items = upload.parseRequest(request);
            FileItem uploadedOne = items.get(0);
            if (!uploadedOne.isFormField()) {
                String fileName = uploadedOne.getName();
                int dotIdx = fileName.lastIndexOf(".");
                String name = fileName.substring(0, dotIdx);
                String ext = fileName.substring(dotIdx);
                Path uploadedFile = Files.createTempFile(name, ext);
                uploadedOne.write(uploadedFile.toFile());

                String path = uploadedFile.toAbsolutePath().toString();
                response.getWriter().write(path);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
