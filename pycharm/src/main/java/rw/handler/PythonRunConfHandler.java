package rw.handler;

import com.google.gson.Gson;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.ex.temp.TempFileSystem;
import com.jetbrains.python.run.AbstractPythonRunConfiguration;
import org.jetbrains.annotations.Nullable;
import rw.action.RunType;
import rw.audit.RwSentry;
import rw.consts.Const;
import rw.icons.IconPatcher;
import rw.pkg.NativeFileSystem;
import rw.preferences.Preferences;
import rw.preferences.PreferencesState;
import rw.quickconfig.QuickConfigStateFactory;
import rw.settings.ProjectSettings;
import rw.settings.ProjectState;
import rw.util.EnvUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PythonRunConfHandler extends RunConfHandler {
    public static final String IDE_NAME_ENV = "RW_IDE_NAME";
    public static final String IDE_VERSION_ENV = "RW_IDE_VERSION";
    public static final String IDE_PLUGIN_VERSION_ENV = "RW_IDE_PLUGINVERSION";
    public static final String IDE_TYPE_ENV = "RW_IDE_TYPE";
    public static final String IDE_SERVER_PORT_ENV = "RW_IDE_SERVERPORT";
    public static final String VERBOSE_ENV = "RW_VERBOSE";
    public static final String CACHE_ENV = "RW_CACHE";
    public static final String PRINT_LOGO_ENV = "RW_PRINTLOGO";
    public static final String WATCHCWD_ENV = "RW_WATCHCWD";
    public static final String WATCH_FILES_WITH_BREAKPOINTS_ENV = "RW_WATCHFILESWITHBREAKPOINTS";
    public static final String EXTRA_WATCHED_FILES_ENV = "RW_EXTRAWATCHEDFILES";
    public static final String RELOADIUMPATH_ENV = "RELOADIUMPATH";
    public static final String RELOADIUMIGNORE_ENV = "RELOADIUMIGNORE";
    public static final String QUICK_CONFIG_ENV = "RW_QUICKCONFIG";

    public PythonRunConfHandler(RunConfiguration runConf) {
        super(runConf);
    }

    @Override
    public void beforeRun(RunType runType) {
        super.beforeRun(runType);

        PreferencesState preferences = Preferences.getInstance().getState();
        String command;

        this.runType = runType;

        if (runType == RunType.DEBUG) {
            command = "pydev_proxy";
        } else {
            command = "run";
        }

        String pathSep = System.getProperty("path.separator");

        this.runConf.setInterpreterOptions(String.format("-m %s %s", Const.get().launcherName, command));

        this.session.start();

        // Set Envs
        this.runConf.getEnvs().put(this.IDE_NAME_ENV, ApplicationInfo.getInstance().getFullApplicationName());
        this.runConf.getEnvs().put(this.IDE_PLUGIN_VERSION_ENV, Const.get().version);
        this.runConf.getEnvs().put(this.IDE_VERSION_ENV, ApplicationInfo.getInstance().getFullVersion());
        this.runConf.getEnvs().put(this.IDE_TYPE_ENV, Const.get().pycharm);

        ProjectState state = ProjectSettings.getInstance(this.runConf.getProject()).getState();

        this.runConf.getEnvs().put(this.VERBOSE_ENV, EnvUtils.boolToEnv(state.verbose));
        this.runConf.getEnvs().put(this.CACHE_ENV, EnvUtils.boolToEnv(state.cache));
        this.runConf.getEnvs().put(this.PRINT_LOGO_ENV, EnvUtils.boolToEnv(state.printLogo));
        this.runConf.getEnvs().put(this.WATCHCWD_ENV, EnvUtils.boolToEnv(state.watchCwd));
        this.runConf.getEnvs().put(this.WATCH_FILES_WITH_BREAKPOINTS_ENV, EnvUtils.boolToEnv(state.watchFilesWithBreakpoints));
        this.runConf.getEnvs().put(this.IDE_SERVER_PORT_ENV, String.valueOf(this.session.getPort()));
        this.runConf.getEnvs().put("PYDEVD_USE_CYTHON", "NO");
        this.runConf.getEnvs().put("RW_STAGE", Const.get().stage.value);

        if (this.extraEnvsSetter != null) {
            this.extraEnvsSetter.setEnvs(this.runConf.getEnvs());
        }

        Gson gson = new Gson();
        String quickConfigPayload = gson.toJson(QuickConfigStateFactory.create());
        this.runConf.getEnvs().put(this.QUICK_CONFIG_ENV, quickConfigPayload);

        if (state.watchOpenFiles) {
            FileEditorManager fileEditorManager = FileEditorManager.getInstance(this.project);
            List<String> openFiles = Arrays.stream(fileEditorManager.getOpenFiles())
                    .filter(f -> f.getExtension() != null && f.getExtension().equals("py"))
                    .map(f -> this.convertPathToRemote(f.getPath(), false))
                    .toList();
            this.runConf.getEnvs().put(this.EXTRA_WATCHED_FILES_ENV, String.join(pathSep, openFiles));
        }

        this.runConf.getEnvs().put(this.WATCH_FILES_WITH_BREAKPOINTS_ENV, EnvUtils.boolToEnv(state.watchFilesWithBreakpoints));

        List<String> reloadiumPath = new ArrayList<>(state.reloadiumPath);
        if (state.watchSourceRoots) {
            @Nullable Module module = this.runConf.getModule();
            if (module != null) {
                VirtualFile[] sourceRootsRaw = ModuleRootManager.getInstance(module).getSourceRoots(false);

                for (VirtualFile f : sourceRootsRaw) {
                    if (TempFileSystem.class.isAssignableFrom(f.getFileSystem().getClass())) {
                        continue;
                    }
                    reloadiumPath.add(this.convertPathToRemote(f.toNioPath().toString(), false));
                }
            }
        }

        if (reloadiumPath.isEmpty() && this.runConf.getWorkingDirectory().isEmpty() && this.project.getBasePath() != null) {
            reloadiumPath.add(this.convertPathToRemote(this.project.getBasePath(), false));
        }

        List<String> reloadiumPathRemote = this.getRemotePaths(reloadiumPath);

        this.runConf.getEnvs().put(this.RELOADIUMPATH_ENV, String.join(pathSep, reloadiumPathRemote));

        List<String> reloadiumIgnore = new ArrayList<>(state.reloadiumIgnore);
        List<String> reloadiumIgnoreRemote = this.getRemotePaths(reloadiumIgnore);
        this.runConf.getEnvs().put(this.RELOADIUMIGNORE_ENV, String.join(pathSep, reloadiumIgnoreRemote));

        // Set PYTHONPATH
        String pythonpath = this.runConf.getEnvs().getOrDefault("PYTHONPATH", "");

        if (!pythonpath.isBlank()) {
            pythonpath = String.format("%s%s%s", pythonpath, pathSep, System.getenv("PYTHONPATH"));
        } else {
            pythonpath = System.getenv("PYTHONPATH");

            if (pythonpath == null) {
                pythonpath = "";
            }
        }

        pythonpath = pythonpath; //  # RwRender: pythonpath = {{ ctx.pythonpath }}; //

        String launcherPath = String.valueOf(this.getPackagesRootDir());

        if (!pythonpath.isBlank()) {
            pythonpath = String.format("%s%s%s", launcherPath, pathSep, pythonpath);
        } else {
            pythonpath = launcherPath;
        }
        this.runConf.getEnvs().put("PYTHONPATH", pythonpath);
    }

    private List<String> getRemotePaths(List<String> paths) {
        List<String> ret = new ArrayList<>();

        for (String p : paths) {
            try {
                ret.add(this.convertPathToRemote(p, false));
            } catch (Exception e) {
                RwSentry.get().captureException(e, true);
            }
        }
        return ret;
    }

    protected File getPackagesRootDir() {
        return NativeFileSystem.get().getPackagesRootDir();
    }

    @Override
    public void onProcessExit() {
        super.onProcessExit();
        this.session.close();
        IconPatcher.refresh(this.project);
    }

    public boolean isReloadiumActivated() {
        boolean ret = this.runConf.getInterpreterOptions().contains(Const.get().launcherName);
        return ret;
    }
}
