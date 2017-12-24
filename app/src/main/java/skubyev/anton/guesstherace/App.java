package skubyev.anton.guesstherace;

import android.content.ContextWrapper;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.pixplicity.easyprefs.library.Prefs;

import skubyev.anton.guesstherace.toothpick.DI;
import skubyev.anton.guesstherace.toothpick.module.AppModule;
import skubyev.anton.guesstherace.toothpick.module.DatabaseModule;
import skubyev.anton.guesstherace.toothpick.module.ServerModule;
import timber.log.Timber;
import timber.log.Timber.DebugTree;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.configuration.Configuration;
import toothpick.registries.FactoryRegistryLocator;
import toothpick.registries.MemberInjectorRegistryLocator;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig.Builder;

public class App extends MultiDexApplication {
    public void onCreate() {
        super.onCreate();
        initLogger();
        initToothpick();
        initAppScope();
        initCalligraphy();
        initStetho();
        initPrefs();
    }

    private void initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant((new DebugTree()));
        }
    }

    private void initPrefs() {
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

    private void initToothpick() {
        if (BuildConfig.DEBUG) {
            Toothpick.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes());
        } else {
            Toothpick.setConfiguration(Configuration.forProduction().disableReflection());
            FactoryRegistryLocator.setRootRegistry((new skubyev.anton.guesstherace.FactoryRegistry()));
            MemberInjectorRegistryLocator.setRootRegistry((new skubyev.anton.guesstherace.MemberInjectorRegistry()));
        }
    }

    private void initAppScope() {
        Scope appScope = Toothpick.openScope(DI.APP_SCOPE);
        appScope.installModules((new AppModule(this)));

        Scope dataScope = Toothpick.openScopes(DI.APP_SCOPE, DI.DATA_SCOPE);
        dataScope.installModules((new ServerModule(BuildConfig.ORIGIN_API_ENDPOINT)));
        dataScope.installModules((new DatabaseModule()));
    }

    private void initCalligraphy() {
        CalligraphyConfig.initDefault((new Builder())
                .setFontAttrId(R.string.font_path)
                .build()
        );
    }

    private void initStetho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this)).build());
    }
}

