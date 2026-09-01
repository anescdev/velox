package es.anescdev.velox.context.sumatory.view.commands.print;

import java.awt.Color;

import org.openpdf.text.Rectangle;
import org.openpdf.text.pdf.PdfContentByte;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPCellEvent;
import org.openpdf.text.pdf.PdfPTable;

/**
 * Comando de la capa de vista del dominio sumatory: encapsula una acción disparada desde
 * la interfaz (ver {@code Command}/{@code FeatherCommand}), coordinando el ViewModel,
 * los diálogos y la navegación necesarios para completarla.
 */
public class TotalDayPillEvent implements PdfPCellEvent {

    private static final float RADIUS = 12f;
    private static final float INSET = 3f;

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        PdfContentByte cb = canvases[PdfPTable.BACKGROUNDCANVAS];

        cb.saveState();
        cb.setColorFill(Color.BLACK);
        cb.roundRectangle(
                position.getLeft() + INSET,
                position.getBottom() + INSET,
                position.getWidth() - INSET * 2,
                position.getHeight() - INSET * 2,
                RADIUS);
        cb.fill();
        cb.restoreState();
    }

}
