package com.ciandt.pms.util;

import com.ciandt.pms.exception.DirectoryNotFoundException;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class FileUtil {

	/**
	 * Realiza o download de um arquivo que contido no servidor.
	 * 
	 * @param path
	 *            - caminho do arquivo no file system
	 * @param fileName
	 *            - nome do arquivo a ser baixado
	 * @param contentType
	 *            - tipo do arquivo
	 */
	public static void downloadFile(final String path, final String fileName,
			final String contentType) {
		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();

		response.setHeader("Content-disposition", "inline; filename=\""
				+ fileName + "\"");

		response.setContentType(contentType);

		try {
			File file;
			// Linux
			file = new File(path + fileName);

			// Windows para testes
			// file = new File("c:\\" + fileName);

			FileInputStream fileIn = new FileInputStream(file);

			ServletOutputStream out = response.getOutputStream();

			byte[] outputByte = new byte[4096];
			// copy binary contect to output stream
			while (fileIn.read(outputByte, 0, 4096) != -1) {
				out.write(outputByte, 0, 4096);
			}
			fileIn.close();
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		FacesContext.getCurrentInstance().responseComplete();
	}

	/**
	 * Salva um aquivo no servidor da aplicacao.
	 * 
	 * @param path
	 *            - caminho do arquivo no file system
	 * @param fileName
	 *            - nome do arquivo
	 * @param bytes
	 *            - bytes do arquivo
	 * @throws IOException
	 */
	public static void saveFile(final String path, final String fileName,
			final byte[] bytes) throws IOException {
		FileOutputStream fos = new FileOutputStream(path + fileName);
		fos.write(bytes);
		fos.flush();
		fos.close();
	}

	/**
	 * Remove o arquivo especificado em {@code filename} e {@code directory}.
	 * 
	 * @param fileName
	 * @param directory
	 * 
	 * @throws FileNotFoundException
	 * @throws DirectoryNotFoundException
	 * @throws IOException
	 */
	public static void deteleFileAtLocation(String fileName, String directory)
			throws FileNotFoundException, DirectoryNotFoundException,
			IOException {

		if (!new File(directory).exists()) {
			throw new DirectoryNotFoundException();
		} else if (!new File(directory + fileName).exists()) {
			throw new FileNotFoundException();
		}

		File file = new File(directory + fileName);

		if (!file.delete()) {
			throw new IOException();
		}
	}
}
