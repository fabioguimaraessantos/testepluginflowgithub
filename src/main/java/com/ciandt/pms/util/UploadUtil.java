package com.ciandt.pms.util;

import com.ciandt.pms.Constants;
import com.ciandt.pms.csv.util.CsvUtil;
import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.UploadArquivo;
import org.richfaces.model.UploadItem;

import java.io.IOException;
import java.util.List;

/**
 * A classe utilitaria {@link UploadUtil} fornece metodos para manipular
 * arquivos de upload.
 * 
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * @since 23/10/2013
 **/
public class UploadUtil {

	/**
	 * Retorna uma lista de objetos contendo os elementos extraidos do arquivo
	 * CSV passado por parametro.
	 * 
	 * @param csv
	 *            arquivo CSV obtido no upload
	 * @param pattern
	 *            {@link PadraoArquivo}
	 * @param classOf
	 *            Tipo da classe a ser manipulada
	 * @return List<T>
	 */
	public static <T> List<T> csvFileToElementList(final UploadItem csv,
			final PadraoArquivo pattern, final Class<T> classOf) {
		return CsvUtil.getElementList(new String(csv.getData()), classOf,
				CsvUtil.getCsvConfig(pattern));
	}

	/**
	 * Retorna o caminho de destino dos uploads.
	 * 
	 * @return retorna uma string q representa o destino do upload.
	 */
	private static String retrieveEnvironmentUploadPathDestination(
			final String destination) {
		String server = System.getProperty(Constants.SERVER_ENVIRONMENT_KEY);
		if (Constants.SERVER_ENVIRONMENT_PROD.equals(server)) {
			return destination;
		}
		return "";
	}

	/**
	 * Salva os dados importados do arquivo e salva o arquivo no file system.
	 * 
	 * @param uploadArquivo
	 *            - contem os dados do arquivo importados
	 * @param path
	 *            - diretorio onde sera salvo o arquivo
	 * @param fileBytes
	 *            - bytes dos arquivos lidos
	 * @throws IOException
	 *             - lança a exception de I/O
	 */
	public static void saveUploadFile(final UploadArquivo uploadArquivo,
			final String destination, final String fileName,
			final byte[] fileBytes) throws IOException {
		String path = UploadUtil
				.retrieveEnvironmentUploadPathDestination(destination);
		FileUtil.saveFile(path, fileName, fileBytes);
	}

}