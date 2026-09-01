package es.anescdev.velox.context.sumatory.view.commands.print;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.Rectangle;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.openpdf.text.pdf.draw.LineSeparator;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.cod.model.entities.Cod;
import es.anescdev.velox.context.sumatory.data.key.SumatoryEntryKey;
import es.anescdev.velox.context.sumatory.model.entity.Sumatory;
import es.anescdev.velox.context.sumatory.model.entity.SumatoryEntry;
import es.anescdev.velox.context.sumatory.model.service.SumatoryEntryService;
import es.anescdev.velox.core.command.FeatherCommand;
import es.anescdev.velox.core.view.utils.DescriptiveDurationConverter;
import es.anescdev.velox.core.view.utils.DurationStringConverter;

/**
 * @author AnesCDev
 */
public class GeneratePDFCommand extends FeatherCommand<Void> {

    private final Sumatory sumatory;
    private final File pdfFile;
    private final String docTitle;
    private final DateTimeFormatter dateSumatoryFormatter;

    private static final Color HEADER_BACKGROUND_COLOR = new Color(215, 215, 215);
    private static final Color HEADER_TEXT_COLOR = new Color(40, 40, 40);
    private static final Color ODD_ROW_COLOR = new Color(235, 235, 235);

    private static final Font dayFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.BLACK);
    private static final Font TABLE_TITLE_PILL_FONT = FontFactory.getFont("JetBrains Mono SemiBold", 10, Color.WHITE);
    private static final Font TITLE_FONT = FontFactory.getFont("Plus Jakarta Sans Bold", 24, Color.BLACK);
    private static final Font DATE_TITLE_FONT = FontFactory.getFont("Inter 18pt SemiBold", 16, Color.DARK_GRAY);
    private static final Font TOTAL_LABEL_FONT = FontFactory.getFont("Inter 18pt SemiBold", 12, Color.DARK_GRAY);
    private static final Font TOTAL_VALUE_FONT = FontFactory.getFont("JetBrains Mono Bold", 16, Font.BOLD);
    private static final Font TABLE_HEADER_FONT = FontFactory.getFont("Inter 18pt SemiBold", 11, HEADER_TEXT_COLOR);
    private static final Font COD_TABLE_FONT = FontFactory.getFont("JetBrains Mono Bold", 11, Color.BLACK);
    private static final Font DESCRIPTION_TABLE_FONT = FontFactory.getFont("Inter 18pt Regular", 11, Color.BLACK);
    private static final Font TIME_WORKED_TABLE_FONT = FontFactory.getFont("JetBrains Mono Regular", 11, Color.DARK_GRAY);

    @Inject
    private DurationStringConverter durationParser;

    @Inject
    private DescriptiveDurationConverter descriptiveDurationConverter;

    @Inject
    private SumatoryEntryService sumatoryEntryService;

    /**
     * @param sumatory
     */
    public GeneratePDFCommand(Sumatory sumatory, File pdfFile) {
        this.sumatory = sumatory;
        this.pdfFile = pdfFile;
        this.dateSumatoryFormatter = DateTimeFormatter
                .ofPattern(App.instance().getMessage("pdf.sumatory.date.formatter"));
        this.docTitle = App.instance().getMessage("%pdf.sumatory.title " + this.sumatory.getEmployee().getName() + " - "
                + dateSumatoryFormatter.format(this.sumatory.getFullDate()));
    }

    @Override
    public Void executeCommand() {
        App.instance().getLogger().info("Print sumatory " + this.sumatory.getId());
        try {
            if (pdfFile.exists() && !pdfFile.delete())
                return null;
            Document document = new Document(PageSize.A4);
            var writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            writer.setPageEvent(new PdfEvent(this.docTitle));
            this.prepareDocument(document);
            for (Entry<Byte, Collection<PdfSumatoryEntry>> pdfDayTable : this.groupByDays(this.findEntries())
                    .entrySet()) {
                this.createTable(document, pdfDayTable.getKey(), pdfDayTable.getValue());
            }
            document.add(new LineSeparator(1, 15, Color.BLACK, Element.ALIGN_RIGHT, -15));
            document.add(new Paragraph(" "));
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            App.instance().getLogger().severe(e.getMessage());
        }
        return null;
    }

    private List<SumatoryEntry> findEntries() {
        var result = this.sumatoryEntryService
                .searchEntity(new SumatoryEntryKey(-1L, new Cod(), this.sumatory, (byte) 1));
        if (result.success())
            return result.entities();
        else
            return List.of();
    }

    private void prepareDocument(Document document) throws DocumentException {
        document.open();
        var titleParts = this.docTitle.split(" - ");

        Phrase titlePhrase = new Paragraph();
        titlePhrase.setFont(GeneratePDFCommand.TITLE_FONT);
        titlePhrase.add(titleParts[0]);
        document.add(titlePhrase);

        Phrase datePhrase = new Phrase();
        datePhrase.setFont(GeneratePDFCommand.DATE_TITLE_FONT);
        datePhrase.add(titleParts[1]);
        document.add(datePhrase);

        this.createTotalPdf(document);

    }

    private void createTable(Document document, Byte day, Collection<PdfSumatoryEntry> entries) {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 1.5f, 5f, 2f });
        table.setHeaderRows(1);
        table.setSplitRows(false);
        table.setSpacingBefore(8);

        // --- CABECERAS ---
        PdfPCell codHeaderCell = new PdfPCell(
                new Paragraph(App.instance().getMessage("pdf.sumatory.table.header.cod"), TABLE_HEADER_FONT));
        codHeaderCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        codHeaderCell.setBorder(Rectangle.TOP | Rectangle.LEFT | Rectangle.BOTTOM);
        codHeaderCell.setBackgroundColor(GeneratePDFCommand.HEADER_BACKGROUND_COLOR);
        codHeaderCell.setPadding(3);
        table.addCell(codHeaderCell);

        PdfPCell descriptionHeaderCell = new PdfPCell(
                new Paragraph(App.instance().getMessage("pdf.sumatory.table.header.description"), TABLE_HEADER_FONT));
        descriptionHeaderCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        descriptionHeaderCell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
        descriptionHeaderCell.setBackgroundColor(GeneratePDFCommand.HEADER_BACKGROUND_COLOR);
        descriptionHeaderCell.setPadding(3);
        table.addCell(descriptionHeaderCell);

        PdfPCell timeWorkedHeaderCell = new PdfPCell(
                new Paragraph(App.instance().getMessage("pdf.sumatory.table.header.timeworked"), TABLE_HEADER_FONT));
        timeWorkedHeaderCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        timeWorkedHeaderCell.setBorder(Rectangle.TOP | Rectangle.BOTTOM | Rectangle.RIGHT);
        timeWorkedHeaderCell.setBackgroundColor(GeneratePDFCommand.HEADER_BACKGROUND_COLOR);
        timeWorkedHeaderCell.setPadding(3);
        table.addCell(timeWorkedHeaderCell);

        Duration totalDayDuration = Duration.ZERO;

        // --- FILAS DE DATOS ---
        boolean isOdd = false;
        for (PdfSumatoryEntry sumatoryEntry : entries) {
            totalDayDuration = totalDayDuration.plus(sumatoryEntry.getTimeWorkedPerDay());
            // 1. Columna Código
            PdfPCell codCell = new PdfPCell(new Phrase(sumatoryEntry.getCod(), GeneratePDFCommand.COD_TABLE_FONT));
            codCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            codCell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
            codCell.setPadding(3);

            // 2. Columna Descripción
            PdfPCell descriptionCell = new PdfPCell(
                    new Phrase(sumatoryEntry.getDescription(), GeneratePDFCommand.DESCRIPTION_TABLE_FONT));
            descriptionCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            descriptionCell.setBorder(Rectangle.BOTTOM);
            descriptionCell.setPadding(3);

            // 3. Columna Tiempo trabajado
            PdfPCell durationCell = new PdfPCell(
                    new Phrase(this.durationParser.toString(sumatoryEntry.getTimeWorkedPerDay()),
                            GeneratePDFCommand.TIME_WORKED_TABLE_FONT));
            durationCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            durationCell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
            durationCell.setPadding(3);

            if (isOdd) {
                codCell.setBackgroundColor(ODD_ROW_COLOR);
                descriptionCell.setBackgroundColor(ODD_ROW_COLOR);
                durationCell.setBackgroundColor(ODD_ROW_COLOR);
            }
            isOdd = !isOdd;

            table.addCell(codCell);
            table.addCell(descriptionCell);
            table.addCell(durationCell);
        }
        this.addDayTitle(document, day, totalDayDuration);
        document.add(table);
    }

    private void addDayTitle(Document document, int day, Duration totalDayDuration) throws DocumentException {

        // ---- Barra de acento + "DÍA N" ----
        String dayLabel = App.instance().getMessage("pdf.sumatory.title.day") + String.format("%02d", day);
        Phrase dayText = new Phrase(dayLabel, GeneratePDFCommand.dayFont);

        PdfPCell dayCell = new PdfPCell();
        dayCell.setBorder(Rectangle.LEFT);
        dayCell.setBorderWidthLeft(3f);
        dayCell.setBorderColorLeft(Color.BLACK);
        dayCell.setPaddingLeft(8f);
        dayCell.setUseAscender(true);
        dayCell.setUseDescender(true);
        dayCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        dayCell.setLeading(1, 2);
        dayCell.addElement(dayText);

        // ---- Píldora con el total del día ----
        var pillText = new Paragraph(
                this.descriptiveDurationConverter.toString(totalDayDuration),
                GeneratePDFCommand.TABLE_TITLE_PILL_FONT);
        pillText.setAlignment(Element.ALIGN_CENTER);

        PdfPCell totalDayCell = new PdfPCell();
        totalDayCell.setBorder(Rectangle.NO_BORDER);
        totalDayCell.setCellEvent(new TotalDayPillEvent());
        totalDayCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        totalDayCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalDayCell.setUseAscender(true);
        totalDayCell.setUseDescender(true);
        totalDayCell.setPadding(6f);
        totalDayCell.addElement(pillText);

        PdfPTable tableTitle = new PdfPTable(2);
        tableTitle.setWidthPercentage(100);
        // el label ocupa más ancho que la píldora, igual que en el ejemplo
        tableTitle.setWidths(new float[] { 3f, .5f });
        tableTitle.setSpacingBefore(18f);
        tableTitle.setSpacingAfter(4f);
        tableTitle.addCell(dayCell);
        tableTitle.addCell(totalDayCell);

        document.add(tableTitle);
    }

    private Map<Byte, Collection<PdfSumatoryEntry>> groupByDays(List<SumatoryEntry> entries) {
        Collections.sort(entries, (entryA, entryB) -> Byte.compare(entryA.getDay(), entryB.getDay()));
        LinkedHashMap<Byte, Collection<PdfSumatoryEntry>> pdfEntries = new LinkedHashMap<>();
        for (SumatoryEntry sumatoryEntry : entries) {
            PdfSumatoryEntry mappedSumatoryEntry = new PdfSumatoryEntry(
                    sumatoryEntry.getCod().getCompleteCod(),
                    sumatoryEntry.getDay(),
                    sumatoryEntry.getCod().getDescription(),
                    sumatoryEntry.getCod().getTimeWorked());
            if (pdfEntries.containsKey(sumatoryEntry.getDay())) {
                pdfEntries.get(sumatoryEntry.getDay()).add(mappedSumatoryEntry);
                continue;
            } else {
                List<PdfSumatoryEntry> listEntries = new LinkedList<>();
                listEntries.add(mappedSumatoryEntry);
                pdfEntries.put(sumatoryEntry.getDay(), listEntries);
            }
        }
        return pdfEntries;
    }

    private void createTotalPdf(Document document) {
        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100f);
        outerTable.setSpacingBefore(0);
        outerTable.setSpacingAfter(26f);

        /*
         * CONTENIDO INTERIOR
         */
        PdfPTable content = new PdfPTable(2);
        content.setWidthPercentage(100f);
        content.setWidths(new float[] { 1f, 1f });

        /*
         * LABEL
         */
        PdfPCell labelCell = new PdfPCell(
                new Phrase(
                        App.instance().getMessage("pdf.sumatory.total.label"),
                        GeneratePDFCommand.TOTAL_LABEL_FONT));

        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        labelCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        labelCell.setPaddingTop(5f);
        labelCell.setPaddingBottom(5f);
        labelCell.setPaddingLeft(10f);

        /*
         * VALOR
         */
        PdfPCell valueCell = new PdfPCell(
                new Phrase(
                        this.parseDuration(this.sumatory.getTotal()),
                        GeneratePDFCommand.TOTAL_VALUE_FONT));

        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        valueCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        valueCell.setPaddingTop(5f);
        valueCell.setPaddingBottom(5f);
        valueCell.setPaddingRight(10f);

        content.addCell(labelCell);
        content.addCell(valueCell);

        /*
         * CAJA EXTERIOR
         */
        PdfPCell boxCell = new PdfPCell();
        boxCell.setBorder(Rectangle.NO_BORDER);
        boxCell.setPadding(7f);
        boxCell.addElement(content);

        boxCell.setCellEvent(new RoundedBoxEvent());

        outerTable.addCell(boxCell);

        document.add(outerTable);
    }

    private String parseDuration(Duration duration) {
        var hours = duration.toHours();
        var minutes = duration.toMinutes() - (hours * 60);
        return hours + App.instance().getMessage("pdf.sumatory.total.duration." + (hours > 1 ? "hours" : "hour")) +
                App.instance().getMessage("pdf.sumatory.total.duration.separator") +
                minutes + App.instance().getMessage("pdf.sumatory.total.duration." + (minutes > 1 ? "hours" : "hour"));
    }
}
