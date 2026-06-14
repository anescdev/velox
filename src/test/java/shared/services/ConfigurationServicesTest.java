/**
 * @author AnesCDev
 */
package shared.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.MissingResourceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.anescdev.shared.services.ConfigurationService;
import es.anescdev.shared.services.ConfigurationService.ConfigurationKey;

/**
 * @author AnesCDev
 */
@DisplayName("Test for ConfigurationService")
public class ConfigurationServicesTest {
    private final static ConfigurationKey<String> TEST_STRING = new ConfigurationKey<>("testString", String.class);
    private final static ConfigurationKey<Double> TEST_DOUBLE = new ConfigurationKey<>("testDouble", Double.class);
    private final static ConfigurationKey<Float> TEST_FLOAT = new ConfigurationKey<>("testFloat", Float.class);
    private final static ConfigurationKey<Integer> TEST_INTEGER = new ConfigurationKey<>("testInteger", Integer.class);
    private final static ConfigurationKey<Character> TEST_CHARACTER = new ConfigurationKey<>("testCharacter",
            Character.class);

    private ConfigurationService config;

    @BeforeEach
    public void setupTest() {
        this.config = new ConfigurationService();
    }

    @Test
    @DisplayName("Test if data is included correctly")
    public void testIncludeDataOnConfig() {
        this.config.set(TEST_STRING, "quimbara");
        this.config.set(TEST_DOUBLE, 23.4d);
        this.config.set(TEST_FLOAT, 23.42f);
        this.config.set(TEST_INTEGER, 2);
        this.config.set(TEST_CHARACTER, 's');
        assertTrue(this.config.has(TEST_STRING));
        assertTrue(this.config.has(TEST_DOUBLE));
        assertTrue(this.config.has(TEST_FLOAT));
        assertTrue(this.config.has(TEST_INTEGER));
        assertTrue(this.config.has(TEST_CHARACTER));
        assertEquals("quimbara", this.config.<String>get(TEST_STRING));
        assertEquals(23.4d, this.config.<Double>get(TEST_DOUBLE));
        assertEquals(23.42f, this.config.<Float>get(TEST_FLOAT));
        assertEquals(2, this.config.<Integer>get(TEST_INTEGER));
        assertEquals('s', this.config.<Character>get(TEST_CHARACTER));
    }

    @Test
    @DisplayName("Test if the service let the user set null values")
    public void testCannotSetNullValues() {
        IllegalArgumentException exception = assertThrowsExactly(IllegalArgumentException.class,
                () -> this.config.set(TEST_DOUBLE, null));
        assertEquals("The key or value cannot be null", exception.getMessage());
        exception = assertThrowsExactly(IllegalArgumentException.class, () -> this.config.set(null, 2));
        assertEquals("The key or value cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Test if throws MissingResourceException when the key is not added")
    public void testIfGetThrowsMissingResourceException() {
        assertThrowsExactly(MissingResourceException.class, () -> this.config.get(TEST_DOUBLE));
    }

    @Test
    @DisplayName("Test if the data can be deleted")
    public void testIfDataIsRemoved() {
        this.config.set(TEST_DOUBLE, 23d);
        assertTrue(this.config.has(TEST_DOUBLE));
        this.config.remove(TEST_DOUBLE);
        assertFalse(this.config.has(TEST_DOUBLE));
    }
}
