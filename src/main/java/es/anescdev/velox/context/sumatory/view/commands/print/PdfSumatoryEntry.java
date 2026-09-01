package es.anescdev.velox.context.sumatory.view.commands.print;

import java.time.Duration;

import lombok.Getter;

/**
 * Comando de la capa de vista del dominio sumatory: encapsula una acción disparada desde
 * la interfaz (ver {@code Command}/{@code FeatherCommand}), coordinando el ViewModel,
 * los diálogos y la navegación necesarios para completarla.
 */
public class PdfSumatoryEntry {
    @Getter
    private final String cod;

    private final byte day;
    @Getter
    private final String description;
    @Getter
    private final Duration timeWorkedPerDay;

    /**
     * @param cod
     * @param description
     * @param timeWorkedPerDay
     */
    public PdfSumatoryEntry(String cod, byte day, String description, Duration timeWorkedPerDay) {
        this.cod = cod;
        this.day = day;
        this.description = description;
        this.timeWorkedPerDay = timeWorkedPerDay;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cod == null) ? 0 : cod.hashCode());
        result = prime * result + day;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PdfSumatoryEntry other = (PdfSumatoryEntry) obj;
        if (cod == null) {
            if (other.cod != null)
                return false;
        } else if (!cod.equals(other.cod))
            return false;
        if (day != other.day)
            return false;
        return true;
    }

}
