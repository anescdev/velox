package es.anescdev.velox.context.sumatory.view.commands.print;

import java.awt.Color;

import org.openpdf.text.Rectangle;
import org.openpdf.text.pdf.PdfContentByte;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPCellEvent;
import org.openpdf.text.pdf.PdfPTable;


/**
 * @author AnesCDev
 */
public class RoundedBoxEvent implements PdfPCellEvent {

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        // Get the background/lines layer canvas
        PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
        
        // Configure line appearance
        cb.setColorStroke(Color.BLACK);
        cb.setLineWidth(1f);
        
        // Draw the rounded rectangle: x, y, width, height, corner radius
        float x = position.getLeft();
        float y = position.getBottom();
        float width = position.getWidth();
        float height = position.getHeight();
        float radius = 10f; // Adjust this value to change corner roundness
        
        cb.roundRectangle(x, y, width, height, radius);
        cb.stroke();
    }
    
}
