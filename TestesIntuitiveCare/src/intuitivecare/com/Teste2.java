package intuitivecare.com;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import java.io.*;
import java.util.zip.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripperByArea;


public class Teste2 {
	//Escrever tabela de Strings em .csv
	public static void writeCsv(String[] quadro, String name) throws IOException{
		
		//Cria o arquivo csv
		FileWriter writer = new FileWriter(name+".csv", StandardCharsets.ISO_8859_1);
		StringBuilder sb = new StringBuilder();
		
		//Escreve nome das colunas
		sb.append(quadro[1]);
		sb.append('\n');
		sb.append("Código");
		sb.append(';');
		sb.append("Descrição da categoria");
		sb.append('\n');
		
		//Insere os dados
		for (int i = 3; i < quadro.length; i++) {
			try {
				Scanner sc = new Scanner(quadro[i]);
				int number = sc.nextInt(); //Pega valor da primeira coluna
				sc.close();
				sb.append(number);
				quadro[i] = quadro[i].replace(String.valueOf(number)+" ", "");
				
				if (quadro[i].equals("\r")) {  //Caso a informação da segunda coluna ocupe duas linhas
					sb.append(';');
					sb.append(quadro[i+1].replace("\r", " ")+quadro[i+2]);
					i+=2;
				}
				else {
					sb.append(';');
					sb.append(quadro[i]);
				}
			} catch (Exception e) { //Pular linhas vazias
			}
			
		}
		
		//Passa os dados para o arquivo
	    writer.write(sb.toString());
        writer.close();
		
	}
	public static void main(String[] args) throws IOException {
		
		//Carrega a tabela
		File file = new File("padrao_tiss_componente_organizacional.pdf");
		PDDocument document = PDDocument.load(file);
		int numPages = document.getNumberOfPages();
		
		//Recorta cabeçalho e roda pé
		Rectangle2D region = new Rectangle2D.Double(0, 79, 550, 664);
        String regionName = "region";
        PDFTextStripperByArea stripper;
        PDPageTree page = document.getPages();
        
        //Passa os textos do pdf para uma String
        String text="";
        for (int i = 0; i < numPages; i++) {
        	PDPage pageTemp = page.get(i);
        	stripper = new PDFTextStripperByArea();
        	stripper.addRegion(regionName, region);
            stripper.extractRegions(pageTemp);
            text = text+stripper.getTextForRegion(regionName);
		}
        
        //Recorta a String para cada tabela
		String[] aux = text.split("Quadro 30 – Tabela de tipo de demandante ");
		String[] aux2 = aux[1].split("Fonte");
		String[] aux3 = aux[1].split("Quadro 31 – Tabela de categoria do Padrão TISS ");
		String[] aux4 = aux3[1].split("Fonte");
		String[] aux5 = aux3[1].split("TISS. \r\n" + " ");
		String[] aux6 = aux5[1].split(" \r\n" + "Processo de Alteração do Padrão TISS ");
		String[] quadro30 = aux2[0].split("\n");
		String[] quadro31 = aux4[0].split("\n");
		String[] quadro32 = aux6[0].split("\n");

		//Cria e escreve os arquivos csv
		writeCsv(quadro30, "quadro30");
		writeCsv(quadro31, "quadro31");
		writeCsv(quadro32, "quadro32");
		
		//Compacta as tabelas em um unico arquivo
		List<String> tabelas = Arrays.asList("quadro30.csv", "quadro31.csv", "quadro32.csv");
		FileOutputStream fileOutputStream = new FileOutputStream("Teste_Intuitive_Care_Gabriel_Ovidio_Vieira.zip");
		ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
		for (String srcFile: tabelas) {
            File tabela = new File(srcFile);
            FileInputStream fileInputStream = new FileInputStream(tabela);
            ZipEntry zipEntry = new ZipEntry(tabela.getName());
            zipOutputStream.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fileInputStream.read(bytes)) >= 0) {
            	zipOutputStream.write(bytes, 0, length);
            }
            fileInputStream.close();
        }
		
		//Final do código
		System.out.println("Arquivo baixado!");
		zipOutputStream.close();
        fileOutputStream.close();
		document.close();
		
	}

}
