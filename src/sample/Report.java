package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.xwpf.usermodel.*;
import sample.tables.Cheque;
import sample.tables.Status;
import sample.tables.Worker;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Report {
    private ObservableList<Cheque> cheques = FXCollections.observableArrayList();
    private Worker worker;

    public Report(ObservableList<Cheque> cheques, Worker worker) {
        this.cheques = cheques;
        this.worker = worker;
        try {
            create();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void create() throws IOException {
        String fileName = "C:\\Users\\efime\\IdeaProjects\\Barbershop\\src\\Report.docx";
        InputStream fis = new FileInputStream(fileName);
        XWPFDocument document = new XWPFDocument(fis);

        List<XWPFParagraph> paragraphs = document.getParagraphs();

        for (int x=0; x<paragraphs.size();x++)
        {
            XWPFParagraph paragraph = paragraphs.get(x);
            System.out.println("Para " + x);
            System.out.println(paragraph.getParagraphText());
        }
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MMMM.yyyy");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yy");
        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    System.out.println("Run");
                    System.out.println(r.getText(0));

                    if (text != null && text.contains("day")) {
                        text = text.replace("day", dateFormat.format(date).split("\\.")[0]);
                        r.setText(text, 0);
                    }

                    if (text != null && text.contains("month")) {
                        text = text.replace("month", dateFormat.format(date).split("\\.")[1]);
                        r.setText(text, 0);
                    }

                    if (text != null && text.contains("year")) {
                        text = text.replace("year", dateFormat.format(date).split("\\.")[2]);
                        r.setText(text, 0);
                    }

                    if (text != null && text.contains("admin")) {
                        text = text.replace("admin", worker.getFullName());
                        r.setText(text, 0);
                    }

                }
            }
        }

        XWPFTable table = document.getTableArray(0);

        int allCost = 0;

        for (int i = 0; i < cheques.size(); i++) {

            if (cheques.get(i).getDateStr().equals(dateFormat1.format(date)) && cheques.get(i).getStatus() == Status.END) {
                XWPFTableRow tableRowOne = table.createRow();

                tableRowOne.getCell(0).setText(cheques.get(i).getCustomer().getName());
                tableRowOne.getCell(1).setText(cheques.get(i).getWorker().getFullName());
                tableRowOne.getCell(2).setText(cheques.get(i).getNameService().getName());
                tableRowOne.getCell(3).setText(cheques.get(i).getCost());

//            table.addRow(tableRowOne, table.getRows().size() - 2);
                allCost += Integer.parseInt(cheques.get(i).getCost());
            }


        }

        table.removeRow(1);

        XWPFTableRow row = table.createRow();
        row.getCell(2).setText("Итого");
        row.getCell(3).setText(String.valueOf(allCost));
        row.getCell(3).getParagraphs().get(0).getRuns().get(0).setBold(true);
        row.getCell(2).getParagraphs().get(0).getRuns().get(0).setBold(true);

        OutputStream out = new FileOutputStream("C:\\Users\\efime\\IdeaProjects\\Barbershop\\src\\OutputReport.docx");
        document.write(out);
        out.close();

        try {
            Runtime.getRuntime().exec("C:\\Program Files\\Microsoft Office\\root\\Office16\\WINWORD.EXE C:\\Users\\efime\\IdeaProjects\\Barbershop\\src\\OutputReport.docx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
