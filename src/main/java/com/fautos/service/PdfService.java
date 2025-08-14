package com.fautos.service;

import com.fautos.model.Orcamento;
import com.fautos.model.Servico;
import com.fautos.model.Peca;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;

@Service
public class PdfService {

    private final DecimalFormat valorFormat = new DecimalFormat("#,##0.00");
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] gerarPdfOrcamento(Orcamento orcamento) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            PdfFont fontRegular = PdfFontFactory.createFont();
            PdfFont fontBold = PdfFontFactory.createFont("Helvetica-Bold");

            // Cabeçalho
            adicionarCabecalho(document, fontBold);

            // Título
            document.add(new Paragraph("ORÇAMENTO")
                .setFont(fontBold)
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(10)
                .setMarginBottom(10));

            // Dados do Cliente
            adicionarDadosCliente(document, orcamento, fontRegular);

            // Tabela de Serviços
            if (!orcamento.getServicos().isEmpty()) {
                adicionarTabelaServicos(document, orcamento, fontRegular, fontBold);
            }

            // Peças a Comprar
            if (!orcamento.getPecas().isEmpty()) {
                adicionarPecasComprar(document, orcamento, fontRegular, fontBold);
            }

            // Rodapé
            adicionarRodape(document, orcamento, fontRegular);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    private void adicionarCabecalho(Document document, PdfFont fontBold) throws Exception {
        // Cria uma tabela de 2 colunas
        Table headerTable = new Table(2);

        // Define largura da tabela como 60% da página
        headerTable.setWidth(UnitValue.createPercentValue(80));

        // Centraliza a tabela horizontalmente usando setHorizontalAlignment
        headerTable.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);

        // ----- Coluna esquerda: logo -----
        ImageData imageData = ImageDataFactory.create("src/main/resources/static/logo.jpg");
        Image logo = new Image(imageData);

        // Aumenta a logo em 20% em relação ao tamanho original (150x100 -> 180x120)
        logo.scaleToFit(180, 120);

        Cell logoCell = new Cell()
                .add(logo)
                .setBorder(null)
                .setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE); // Alinha verticalmente ao centro
        headerTable.addCell(logoCell);

        // ----- Coluna direita: contato -----
        Cell contactCell = new Cell()
                .add(new Paragraph("(85) 99648-3792").setFontSize(12).setFont(fontBold))
                .add(new Paragraph("Rua Dionísio Leonel Alencar, 1520").setFontSize(10))
                .add(new Paragraph("Jangurussu - Fortaleza/CE").setFontSize(10))
                .setBorder(null)
                .setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE) // Alinha verticalmente ao centro
                .setTextAlignment(TextAlignment.RIGHT); // Alinha à direita
        headerTable.addCell(contactCell);

        // Adiciona tabela ao documento
        document.add(headerTable);


    }

    private void adicionarDadosCliente(Document document, Orcamento orcamento, PdfFont fontRegular) {
        // Cria tabela de 1 coluna para centralizar conteúdo e controlar largura
        Table dadosTable = new Table(1);
        dadosTable.setWidth(UnitValue.createPercentValue(90)); // 80% da página
        dadosTable.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);

        // Cliente
        Paragraph pCliente = new Paragraph()
                .add(new Text("Cliente: ").setFont(fontRegular))
                .add(new Text(orcamento.getCliente().getNome() != null ? orcamento.getCliente().getNome() : "").setFont(fontRegular));
        Cell cellCliente = new Cell().add(pCliente).setBorder(null);
        dadosTable.addCell(cellCliente);

        // Veículo e Placa
        Paragraph pVeiculo = new Paragraph()
                .add(new Text("Veículo: ").setFont(fontRegular))
                .add(new Text(orcamento.getCliente().getModeloVeiculo() != null ? orcamento.getCliente().getModeloVeiculo() : "").setFont(fontRegular))
                .add(new Text(" - Placa: ").setFont(fontRegular))
                .add(new Text(orcamento.getCliente().getPlacaVeiculo() != null ? orcamento.getCliente().getPlacaVeiculo() : "").setFont(fontRegular));
        dadosTable.addCell(new Cell().add(pVeiculo).setBorder(null));

        // Endereço
        Paragraph pEndereco = new Paragraph()
                .add(new Text("Endereço: ").setFont(fontRegular))
                .add(new Text(orcamento.getCliente().getEndereco() != null ? orcamento.getCliente().getEndereco() : "").setFont(fontRegular));
        dadosTable.addCell(new Cell().add(pEndereco).setBorder(null));

        // Bairro e Telefone
        String bairro = orcamento.getCliente().getBairro() != null ? orcamento.getCliente().getBairro() : "";
        String telefone = orcamento.getCliente().getTelefone() != null ? orcamento.getCliente().getTelefone() : "";
        Paragraph pBairroTel = new Paragraph()
                .add(new Text("Bairro: ").setFont(fontRegular))
                .add(new Text(bairro).setFont(fontRegular))
                .add(new Text("    "))
                .add(new Text("Telefone: ").setFont(fontRegular))
                .add(new Text(telefone).setFont(fontRegular));
        dadosTable.addCell(new Cell().add(pBairroTel).setBorder(null));

        // Forma de pagamento
        boolean aVista = "A_VISTA".equals(orcamento.getCliente().getFormaPagamento());
        Paragraph pPagamento = new Paragraph()
                .add(new Text("Forma de pagamento: ").setFont(fontRegular))
                .add(new Text(aVista ? "(X) À vista   ( ) Cartão" : "( ) À vista   (X) Cartão").setFont(fontRegular));
        dadosTable.addCell(new Cell().add(pPagamento).setBorder(null));

        // Adiciona tabela ao documento
        document.add(dadosTable);
        document.add(new Paragraph("\n"));
    }

    private void adicionarTabelaServicos(Document document, Orcamento orcamento, PdfFont fontRegular, PdfFont fontBold) {
        Table table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(90)); // 80% da página
        table.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);
        table.setTextAlignment(TextAlignment.CENTER);

        // Cabeçalho da tabela
        Cell headerQtd = new Cell()
                .add(new Paragraph("Quantidade").setFont(fontBold).setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(ColorConstants.RED)
                .setTextAlignment(TextAlignment.CENTER);
        table.addHeaderCell(headerQtd);

        Cell headerDesc = new Cell()
                .add(new Paragraph("Descrição").setFont(fontBold).setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(ColorConstants.RED)
                .setTextAlignment(TextAlignment.CENTER);
        table.addHeaderCell(headerDesc);

        Cell headerValor = new Cell()
                .add(new Paragraph("Valor (R$)").setFont(fontBold).setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(ColorConstants.RED)
                .setTextAlignment(TextAlignment.CENTER);
        table.addHeaderCell(headerValor);

        // Linhas de serviços
        double total = 0.0;
        for (Servico servico : orcamento.getServicos()) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(servico.getQuantidade())).setFont(fontRegular)).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph(servico.getDescricao()).setFont(fontRegular)).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph("R$ " + valorFormat.format(servico.getValor())).setFont(fontRegular)).setTextAlignment(TextAlignment.CENTER));
            total += servico.getValor();
        }

        // Linha do total
        table.addCell(new Cell(1, 2).add(new Paragraph("TOTAL").setFont(fontBold)).setTextAlignment(TextAlignment.CENTER).setFontColor(ColorConstants.RED));
        table.addCell(new Cell().add(new Paragraph("R$ " + valorFormat.format(total)).setFont(fontBold)).setTextAlignment(TextAlignment.CENTER).setFontColor(ColorConstants.RED));

        // Adiciona tabela ao documento
        document.add(table);
        document.add(new Paragraph().setMarginBottom(20));
    }

    private void adicionarPecasComprar(Document document, Orcamento orcamento, PdfFont fontRegular, PdfFont fontBold) {
        // Título centralizado
        document.add(new Paragraph("PEÇAS A COMPRAR")
                .setFont(fontBold)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(12)
                .setMarginTop(10)
                .setMarginBottom(5));

        // Cria tabela de 1 coluna para centralizar conteúdo e controlar largura
        Table pecasTable = new Table(1);
        pecasTable.setWidth(UnitValue.createPercentValue(90)); // 80% da página
        pecasTable.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);

        // Adiciona cada peça como uma célula
        for (Peca peca : orcamento.getPecas()) {
            Cell cell = new Cell()
                    .add(new Paragraph("• " + peca.getNome() + ": R$ " + valorFormat.format(peca.getValor()))
                            .setFont(fontRegular)
                            .setTextAlignment(TextAlignment.LEFT)) // alinhamento dentro da célula
                    .setBorder(null); // sem borda
            pecasTable.addCell(cell);
        }

        // Adiciona a tabela ao documento
        document.add(pecasTable);
    }

    private void adicionarRodape(Document document, Orcamento orcamento, PdfFont fontRegular) {
        document.add(new Paragraph()
            .add("Documento gerado automaticamente pelo sistema da F. Autos")
            .setFont(fontRegular)
            .setFontSize(8)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginTop(30));

        document.add(new Paragraph()
            .add("Emitido em: " + orcamento.getDataEmissao().format(dateFormat))
            .setFont(fontRegular)
            .setFontSize(8)
            .setTextAlignment(TextAlignment.RIGHT));
    }
}
