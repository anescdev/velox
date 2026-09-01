package es.anescdev.velox.core.dto;

import java.util.List;
import java.util.Optional;

/**
 * Resultado tipado de un borrado múltiple en {@code AbstractService}: indica cuántas
 * entidades se borraron correctamente y cuáles quedaron pendientes si el proceso se
 * interrumpió a mitad por un error.
 * 
 * @param success indica si se ha borrado con éxito o no
 * @param removedCount contador de elementos borrados con éxito
 * @param notRemoved contador de elementos que no han sido borrados
 */
public record DeleteMultipleEntitiesResult<T>(boolean success, int removedCount, Optional<List<T>> notRemoved) {
    public static <T> DeleteMultipleEntitiesResult<T> success(int removedCount) {
        return new DeleteMultipleEntitiesResult<>(true, removedCount, Optional.empty());
    }
    public static <T> DeleteMultipleEntitiesResult<T> failed(int removedCount, List<T> notRemoved) {
        return new DeleteMultipleEntitiesResult<>(false, removedCount, Optional.of(notRemoved));
    }
}
