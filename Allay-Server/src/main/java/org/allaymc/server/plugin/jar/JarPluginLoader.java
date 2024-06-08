package org.allaymc.server.plugin.jar;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.allaymc.api.i18n.I18n;
import org.allaymc.api.i18n.I18nLoader;
import org.allaymc.api.i18n.LangCode;
import org.allaymc.api.i18n.TrKeys;
import org.allaymc.api.plugin.Plugin;
import org.allaymc.api.plugin.PluginContainer;
import org.allaymc.api.plugin.PluginDescriptor;
import org.allaymc.api.plugin.PluginException;
import org.allaymc.api.plugin.PluginLoader;
import org.allaymc.api.server.Server;
import org.allaymc.api.utils.JSONUtils;
import org.allaymc.server.i18n.AllayI18n;
import org.allaymc.server.i18n.AllayI18nLoader;
import org.allaymc.server.plugin.SimplePluginDescriptor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.allaymc.api.plugin.PluginContainer.createPluginContainer;
import static org.allaymc.server.plugin.DefaultPluginSource.getOrCreateDataFolder;

/**
 * Allay Project 2024/2/8
 *
 * @author daoge_cmd
 */
@Slf4j
public class JarPluginLoader implements PluginLoader {

    @Getter
    protected Path pluginPath;
    protected FileSystem jarFileSystem;
    protected PluginDescriptor descriptor;

    @SneakyThrows
    public JarPluginLoader(Path pluginPath) {
        this.pluginPath = pluginPath;
        this.jarFileSystem = FileSystems.newFileSystem(pluginPath);
    }

    @SneakyThrows
    @Override
    public PluginDescriptor loadDescriptor() {
        descriptor = JSONUtils.from(Files.newBufferedReader(jarFileSystem.getPath("plugin.json")), SimplePluginDescriptor.class);
        PluginDescriptor.checkDescriptorValid(descriptor);
        return descriptor;
    }

    @SneakyThrows
    @Override
    public PluginContainer loadPlugin() {
        // Load the main class
        Class<?> mainClass = findMainClass();
        if (!Plugin.class.isAssignableFrom(mainClass)) {
            throw new PluginException(I18n.get().tr(TrKeys.A_PLUGIN_JAR_ENTRANCE_TYPEINVALID, descriptor.getName()));
        }
        // Try to construct plugin instance
        Plugin pluginInstance;
        try {
            pluginInstance = (Plugin) mainClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new PluginException(I18n.get().tr(TrKeys.A_PLUGIN_CONSTRUCT_INSTANCE_ERROR, descriptor.getName()));
        }
        return createPluginContainer(
                pluginInstance,
                descriptor, this,
                getOrCreateDataFolder(descriptor.getName()),
                new AllayI18n(new JarPluginI18nLoader(), Server.SETTINGS.genericSettings().language())
        );
    }

    protected Class<?> findMainClass() {
        try {
            // noinspection resource: No need to try-with-resources, as we want to keep the class loader alive until server shutdown
            var classLoader = new JarPluginClassLoader(new URL[]{pluginPath.toUri().toURL()});
            return classLoader.loadClass(descriptor.getEntrance());
        } catch (ClassNotFoundException e1) {
            throw new PluginException(I18n.get().tr(TrKeys.A_PLUGIN_ENTRANCE_MISSING, descriptor.getName()));
        } catch (MalformedURLException e2) {
            throw new PluginException("Invalid URL: " + pluginPath.toUri());
        }
    }

    public class JarPluginI18nLoader implements I18nLoader {

        @Override
        public Map<String, String> getLangMap(LangCode langCode) {
            try {
                var str = Files.readString(jarFileSystem.getPath("lang/" + langCode.name() + ".json"));
                TypeToken<HashMap<String, String>> typeToken = new TypeToken<>() {};
                return JSONUtils.fromLenient(str,typeToken);
            } catch (NoSuchFileException e) {
                return Collections.emptyMap();
            } catch (IOException e) {
                log.error("Error while loading plugin language file", e);
                return Collections.emptyMap();
            }
        }
    }

    public static class JarPluginLoaderFactory implements PluginLoaderFactory {

        protected static final PathMatcher PATH_MATCHER = FileSystems.getDefault().getPathMatcher("glob:**.jar");

        @Override
        public boolean canLoad(Path pluginPath) {
            return PATH_MATCHER.matches(pluginPath) && Files.isRegularFile(pluginPath);
        }

        @Override
        public PluginLoader create(Path pluginPath) {
            return new JarPluginLoader(pluginPath);
        }
    }
}
