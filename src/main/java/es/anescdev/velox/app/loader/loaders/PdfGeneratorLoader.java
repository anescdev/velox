package es.anescdev.velox.app.loader.loaders;

import javax.inject.Singleton;

import org.openpdf.text.FontFactory;

import es.anescdev.velox.app.App;
import es.anescdev.velox.app.loader.LoadException;
import es.anescdev.velox.app.loader.Loader;
import es.anescdev.velox.app.loader.events.LoadEvent;
import javafx.event.EventType;

/**
 * @author AnesCDev
 */
@Singleton
public class PdfGeneratorLoader implements Loader {
    public final static EventType<LoadEvent> LOAD_EVENT = new EventType<>("PDF_GENERATOR");
    public final static String LOADING_MESSAGE = "load.pdfgenerator.message";

    @Override
    public void load() throws LoadException {
        String[] fonts = {
                // Plus Jakarta Sans
                "/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-Bold.ttf",
                "/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-BoldItalic.ttf",
                "/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-ExtraBold.ttf",
                "/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-ExtraBoldItalic.ttf",
                "/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-ExtraLight.ttf",
                "/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-ExtraLightItalic.ttf",
                "/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-Italic.ttf",
                "/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-Light.ttf",
                "/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-LightItalic.ttf",
                "/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-Medium.ttf",
                "/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-MediumItalic.ttf",
                "/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-Regular.ttf",
                "/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-SemiBold.ttf",
                "/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-SemiBoldItalic.ttf",

                // Inter
                "/fonts/Inter/static/Inter_18pt-Bold.ttf",
                "/fonts/Inter/static/Inter_18pt-BoldItalic.ttf",
                "/fonts/Inter/static/Inter_18pt-ExtraBold.ttf",
                "/fonts/Inter/static/Inter_18pt-ExtraBoldItalic.ttf",
                "/fonts/Inter/static/Inter_18pt-ExtraLight.ttf",
                "/fonts/Inter/static/Inter_18pt-ExtraLightItalic.ttf",
                "/fonts/Inter/static/Inter_18pt-Italic.ttf",
                "/fonts/Inter/static/Inter_18pt-Light.ttf",
                "/fonts/Inter/static/Inter_18pt-LightItalic.ttf",
                "/fonts/Inter/static/Inter_18pt-Medium.ttf",
                "/fonts/Inter/static/Inter_18pt-MediumItalic.ttf",
                "/fonts/Inter/static/Inter_18pt-Regular.ttf",
                "/fonts/Inter/static/Inter_18pt-SemiBold.ttf",
                "/fonts/Inter/static/Inter_18pt-SemiBoldItalic.ttf",
                "/fonts/Inter/static/Inter_18pt-Thin.ttf",
                "/fonts/Inter/static/Inter_18pt-ThinItalic.ttf",

                // JetBrains Mono
                "/fonts/JetBrainsMono/static/JetBrainsMono-Bold.ttf",
                "/fonts/JetBrainsMono/static/JetBrainsMono-BoldItalic.ttf",
                "/fonts/JetBrainsMono/static/JetBrainsMono-ExtraBold.ttf",
                "/fonts/JetBrainsMono/static/JetBrainsMono-ExtraBoldItalic.ttf",
                "/fonts/JetBrainsMono/static/JetBrainsMono-ExtraLight.ttf",
                "/fonts/JetBrainsMono/static/JetBrainsMono-ExtraLightItalic.ttf",
                "/fonts/JetBrainsMono/static/JetBrainsMono-Italic.ttf",
                "/fonts/JetBrainsMono/static/JetBrainsMono-Light.ttf",
                "/fonts/JetBrainsMono/static/JetBrainsMono-LightItalic.ttf",
                "/fonts/JetBrainsMono/static/JetBrainsMono-Medium.ttf",
                "/fonts/JetBrainsMono/static/JetBrainsMono-MediumItalic.ttf",
                "/fonts/JetBrainsMono/static/JetBrainsMono-Regular.ttf",
                "/fonts/JetBrainsMono/static/JetBrainsMono-SemiBold.ttf",
                "/fonts/JetBrainsMono/static/JetBrainsMono-SemiBoldItalic.ttf",
                "/fonts/JetBrainsMono/static/JetBrainsMono-Thin.ttf",
                "/fonts/JetBrainsMono/static/JetBrainsMono-ThinItalic.ttf"
        };

        for (String font : fonts) {
            FontFactory.register(font);
            App.instance().getLogger().config("Loaded font " + font.substring(font.lastIndexOf("/")) + " for OpenPDF");
        }
    }

    @Override
    public String getLoadingMessage() {
        return LOADING_MESSAGE;
    }

    @Override
    public EventType<LoadEvent> eventType() {
        return LOAD_EVENT;
    }

}
