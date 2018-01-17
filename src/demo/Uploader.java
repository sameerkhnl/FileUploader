package demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import java.util.*;

@MultipartConfig
public class Uploader extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Uploader() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		if (!ServletFileUpload.isMultipartContent(request)) {
			out.println("Nothing uploaded");
			return;
		}
		// doUpload(request, response);
		ServletFileUpload sf = new ServletFileUpload(new DiskFileItemFactory());
		List<FileItem> multifiles = new ArrayList<>();
		try {
			multifiles = sf.parseRequest(new ServletRequestContext(request));

			for (FileItem item : multifiles) {

				item.write(new File("/Users/sameerkhanal/Desktop/" + item.getName()));
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to upload file");
			out.println("Failed to upload file");
			return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Can't save file");
			out.println("Can't save file");
		}
		System.out.println("file uploaded");
		out.println("file(s) uploaded");
	}
	
	//alternatively this method can be used for the upload
	private void doUpload(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		for (Part part : request.getParts()) {
			String name = part.getName();
			InputStream inputStream = request.getPart(name).getInputStream();
			String fileName = getUploadedFileName(part);
			FileOutputStream fileOutputStream = new FileOutputStream("/Users/sameerkhanal/Desktop/" + fileName);
			int data = 0;
			while ((data = inputStream.read()) != -1) {
				fileOutputStream.write(data);
			}
			fileOutputStream.close();
			inputStream.close();

		}
	}

	private String getUploadedFileName(Part part) {
		String file = "", header = "Content-Disposition";
		String[] strArray = part.getHeader(header).split(";");
		for (String split : strArray) {
			if (split.trim().startsWith("filename")) {
				file = split.substring(split.indexOf('=') + 1);
				file = file.trim().replace("\"", "");
				System.out.println("File name: " + file);
			}
		}
		return file;
	}

}
