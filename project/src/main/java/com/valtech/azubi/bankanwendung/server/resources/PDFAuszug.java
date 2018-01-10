package com.valtech.azubi.bankanwendung.server.resources;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.valtech.azubi.bankanwendung.model.core.KontoNichtGefundenException;
import com.valtech.azubi.bankanwendung.model.entity.Eintrag;
import com.valtech.azubi.bankanwendung.model.entity.Konto;
import com.valtech.azubi.bankanwendung.model.entity.Kunde;
import com.valtech.azubi.bankanwendung.server.services.KontoService;
import com.valtech.azubi.bankanwendung.server.services.UserService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.valtech.azubi.bankanwendung.server.services.DBService.closeSession;
import static com.valtech.azubi.bankanwendung.server.services.DBService.openSession;

public class PDFAuszug {

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private static PdfWriter write;
    private String auftraggeber;
    private String datum = (new SimpleDateFormat("dd.MM.yyyy 'um' H:mm:ss").format(new Date())).toString();
    private Konto konto;
    private Kunde kunde;
    private String DEST;
    private Document document = new Document();
    private UserService userService = UserService.getInstance();
    private KontoService kontoService = KontoService.getInstance();

    public PDFAuszug(Konto konto) {
        this.konto = konto;
        kunde = konto.getKunde();

        DEST = "/Users/patrickbruno/Aufgaben/Java Training/BankanwendungProjekt/BankanwendungServer/Auszug/";
        String dir = +kunde.getBenutzerNr() + "/";

        Path path = Paths.get(DEST);


        System.out.println(new File(DEST).mkdir());
//            Files.createDirectory(path);

        DEST += konto.getClass().getSimpleName().charAt(0) + ":" + konto.getKontoNr() + datum + ".pdf";
        try {
            write = PdfWriter.getInstance(document, new FileOutputStream(DEST));
            document.open();
            addTitle();
            addInformation();
            createTable();
            //write.setOpenAction(new PdfAction(PdfAction.PRINTDIALOG));
            document.close();
            // Desktop.getDesktop().open(new File(DEST));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            openSession();
            new PDFAuszug(KontoService.getInstance().getKontoByID(7));
            closeSession();
        } catch (KontoNichtGefundenException e) {
            e.printStackTrace();
        }
    }

    public Document getDocument() {
        return document;
    }

    public String getDEST() {
        return DEST;
    }

    public void addTitle() throws DocumentException {
        Paragraph title = new Paragraph();
        title.add(new Paragraph("Kontoauszug", catFont));
        document.add(title);
        // document.newPage();
    }

    public void addInformation() {
        Paragraph info = new Paragraph("", smallBold);
        info.add(new Paragraph("  "));
        info.add(new Paragraph("Benutzer: " + kunde.getBenutzerNr() + " " + kunde.getVorname() + " " + kunde.getName()));
        info.add(new Paragraph("vom " + datum));
        info.setAlignment(Element.ALIGN_LEFT);
        Paragraph infokonto = new Paragraph("Konto: (" + konto.toString() + ")", smallBold);
        infokonto.setAlignment(Element.ALIGN_RIGHT);
        info.add(infokonto);

        try {
            document.add(info);
        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }

    public void createTable() throws DocumentException {
        Paragraph tabsatz = new Paragraph();
        tabsatz.add(new Paragraph(" "));

        String[] theader = new String[]{"Nr", "Datum", "Bezeichnung", "Verwendungszweck", "von_Konto", "an_Konto", "Betrag"};
        PdfPTable table = new PdfPTable(theader.length);
        table.setWidthPercentage(100);
        //  table.setSplitRows(false);
        table.setPaddingTop(4);
        table.setSpacingAfter(4);
        table.calculateHeights();

        Font font = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
        for (int i = 0; i < theader.length; i++) {
            PdfPCell cell = new PdfPCell(new Phrase(theader[i], new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new BaseColor(174, 0, 0));
            cell.setBorder(1);
            cell.setBorderWidthTop(1f);
            cell.setPaddingBottom(4f);
            cell.setBorderWidthBottom(1f);
            table.addCell(cell);
        }
        table.setWidths(new int[]{theader[0].length(), theader[1].length(), theader[2].length(),
                theader[3].length(), theader[4].length(), theader[5].length(), theader[6].length()});

        List<Eintrag> eintragList = konto.getProtokoll().getEintraege();

        for (Eintrag e : eintragList) {

            table.addCell(createCell(String.valueOf(e.getE_id()), font));
            table.addCell(createCell(e.getDatum().toString(), font));
            table.addCell(createCell(String.valueOf(e.getBezeichnung()), font));
            table.addCell(createCell(String.valueOf(e.getVzweck()), font));
            table.addCell(createCell(String.valueOf(e.getVonKonto()), font));
            table.addCell(createCell(String.valueOf(e.getZielKonto()), font));
            table.addCell(createCell(String.valueOf(e.getBetrag()) + " €", font));
        }

        table.setHeaderRows(1);

        tabsatz.add(table);
        document.add(tabsatz);

    }

    public PdfPCell createCell(String bezeichnung, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(bezeichnung, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(1);
        return cell;

    }

}
