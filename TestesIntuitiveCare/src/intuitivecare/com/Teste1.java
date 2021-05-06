package intuitivecare.com;

import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

//import org.apache.commons.io.FileUtils;

public class Teste1 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//Encontrar o link mais recente de download do arquivo  Padrão TISS:
		Document ansPage = Jsoup.connect("http://www.ans.gov.br/prestadores/tiss-troca-de-informacao-de-saude-suplementar").get();
		String pathButton = "body > div:nth-child(9) > div > div.col-xs-12.col-sm-8.col-md-9 > div.item-page > div:nth-child(6) > a";	
		String filePageLink = "http://www.ans.gov.br/"+ansPage.select(pathButton).attr("href");
		Document filePage = Jsoup.connect(filePageLink).get();
		String pathPdfFile = "body > div:nth-child(9) > div > div.col-xs-12.col-sm-8.col-md-9 > div.item-page > div.table-responsive > table > tbody > tr:nth-child(1) > td:nth-child(3) > a";
		String fileLink = "http://www.ans.gov.br"+filePage.select(pathPdfFile).attr("href");
		
		//Arrumar erro de link invalido por conta do acento:
		fileLink = fileLink.replace("ã", "%C3%A3");		

		//Criar arquivo onde vai ser colocado o Padrão TISS:
		URL url = new URL(fileLink);
		String diretorio = "padrao_tiss_componente_organizacional.pdf"; //Decide nome e onde vai ser salvo o arquivo
		File file = new File(diretorio);

		//Passar as informações do link para o arquivo pdf:
		System.out.println("Baixando arquivo...");
		InputStream inputStream = url.openStream();
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		int bytes = 0;
		while ((bytes = inputStream.read()) != -1) {
			fileOutputStream.write(bytes);
	    }//O arquivo sera criado no próprio projeto onde neste caso ele ja foi criado.
		
		//Final do código:
		System.out.println("Arquivo baixado!");
		inputStream.close();
		fileOutputStream.close();
	}

}
