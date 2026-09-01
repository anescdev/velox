package es.anescdev.velox.context.sumatory.view.commands.print;

import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfContentByte;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfPageEventHelper;
import org.openpdf.text.pdf.PdfWriter;

import lombok.RequiredArgsConstructor;

import java.awt.Color;


@RequiredArgsConstructor
/**
 * Comando de la capa de vista del dominio sumatory: encapsula una acción disparada desde
 * la interfaz (ver {@code Command}/{@code FeatherCommand}), coordinando el ViewModel,
 * los diálogos y la navegación necesarios para completarla.
 */
public class PdfEvent extends PdfPageEventHelper {

    private final String docTitle;

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        float leftMargin = document.left();
        float rightMargin = document.right();

        // 1. Draw a clean horizontal separation line using OpenPDF canvas
        cb.saveState();
        cb.setColorStroke(Color.BLACK);
        cb.setLineWidth(1.0f);
        // Position the line slightly above the footer text
        float lineY = document.bottom();
        cb.moveTo(leftMargin, lineY);
        cb.lineTo(rightMargin, lineY);
        cb.stroke();
        cb.restoreState();

        // 2. Render the split table (Left String, Right Page Number)
        PdfPTable table = new PdfPTable(2);
        try {
            table.setTotalWidth(rightMargin - leftMargin);
            table.setLockedWidth(true);

            // Left String Cell
            PdfPCell cellLeft = new PdfPCell(new Phrase(this.docTitle));
            cellLeft.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellLeft.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cellLeft);

            // Right Page Number Cell
            PdfPCell cellRight = new PdfPCell(new Phrase(Integer.toString(writer.getPageNumber())));
            cellRight.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cellRight.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cellRight);

            // Render the table below the line
            table.writeSelectedRows(0, -1, leftMargin, document.bottom() - 5, cb);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
