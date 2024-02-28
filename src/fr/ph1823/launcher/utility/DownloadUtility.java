package fr.ph1823.launcher.utility;

import fr.ph1823.launcher.Main;
import fr.ph1823.launcher.panel.MainPanel;
import org.json.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DownloadUtility {

    private static DownloadUtility downloadUtility;
    private File dirLauncher;
    private final List<URL> download = new LinkedList<>();
    private final List<String> lastVersion = new ArrayList<>();

    private int bytesSize = 0;

    public MainPanel infoWindows;
    private Pattern whitelist;
    private String cmd = "jre/bin";
    private String split = ";";

    private DownloadUtility() {}
    public static DownloadUtility getInstance() {
        if(downloadUtility == null)
            downloadUtility = new DownloadUtility();

        return downloadUtility;
    }
    public String listFiles() throws IOException {
        Path startPath = this.getDir().toPath().resolve("libs");
        List<String> filesToRemove = Arrays.asList(
                "client-1.18.2-20220404.173914-srg.jar",
                "ForgeAutoRenamingTool-0.1.25-all.jar",
                "fastcsv-2.0.0.jar",
                "lwjgl-stb-3.2.2-natives-windows.jar",
                "lwjgl-jemalloc-3.2.2-natives-windows.jar",
                "forge-1.18.2-40.2.17-client.jar",
                "commons-io-2.4.jar",
                "forge-1.18.2-40.2.17-universal.jar",
                "error_prone_annotations-2.1.3.jar",
                "javafmllanguage-1.18.2-40.2.17.jar",
                "forgespi-4.0.15-4.x",
                "commons-text-1.3.jar",
                "opencsv-4.4.jar",
                "client-1.18.2-20220404.173914-mappings.txt",
                "lowcodelanguage-1.18.2-40.2.17.jar",
                "srgutils-0.4.9.jar",
                "client-1.18.2-20220404.173914-slim.jar",
                "mcp_config-1.18.2-20220404.173914.zip",
                "javaxdelta-2.0.1.jar",
                "mcp_config-1.18.2-20220404.173914-mappings-merged.txt",
                "srgutils-0.4.11.jar",
                "mclanguage-1.18.2-40.2.17.jar",
                "SpecialSource-1.11.0.jar",
                "lwjgl-tinyfd-3.2.2-natives-windows.jar",
                "lwjgl-glfw-3.2.2-natives-windows.jar",
                "client-1.18.2-20220404.173914-extra.jar.cache",
                "animal-sniffer-annotations-1.14.jar",
                "client-1.18.2-20220404.173914-extra.jar",
                "commons-collections-3.2.2.jar",
                "mcp_config-1.18.2-20220404.173914-mappings.txt",
                "lwjgl-openal-3.2.2-natives-windows.jar",
                "j2objc-annotations-1.1.jar",
                "lwjgl-3.2.2-natives-windows.jar",
                "checker-qual-2.0.0.jar",
                "client-1.18.2-20220404.173914-slim.jar.cache",
                "jarsplitter-1.1.4.jar",
                "installertools-1.3.0.jar",
                "text2speech-1.12.4-natives-windows.jar",
                "fmlcore-1.18.2-40.2.17.jar",
                "lzma-java-1.3.jar",
                "commons-collections4-4.2.jar",
                "trove-1.0.2.jar",
                "lwjgl-opengl-3.2.2-natives-windows.jar",
                "srgutils-0.4.3.jar",
                "commons-beanutils-1.9.3.jar",
                "binarypatcher-1.0.12.jar",
                "jsr305-3.0.2.jar",
                "trove-1.0.2.jar",
                "mcp_config-1.18.2-20220404.173914.zip",
                "mcp_config-1.18.2-20220404.173914-mappings.txt",
                "mcp_config-1.18.2-20220404.173914-mappings-merged.txt"
                );
        try (Stream<Path> stream = Files.walk(startPath)) {
            return stream
                    .filter(file -> Files.isRegularFile(file) && !filesToRemove.contains(file.toFile().getName()))
                    .map(Path::toString)
                    .collect(Collectors.joining(this.split));
        }
    }

    public void attach(MainPanel panel) {
        this.infoWindows = panel;
    }

    public void launchGame(String username, String ram) throws IOException {
        System.out.println(listFiles());
        /*for 1.18: String p = String.join(this.split, dirLauncher.toPath().resolve("libs/cpw/mods/bootstraplauncher/1.0.0/bootstraplauncher-1.0.0.jar").toFile().getPath(),
                dirLauncher.toPath().resolve("libs/cpw/mods/securejarhandler/1.0.8/securejarhandler-1.0.8.jar").toFile().getPath(),
                dirLauncher.toPath().resolve("libs/org/ow2/asm/asm-commons/9.5/asm-commons-9.5.jar").toFile().getPath(),
                dirLauncher.toPath().resolve("libs/org/ow2/asm/asm-util/9.5/asm-util-9.5.jar").toFile().getPath(),
                dirLauncher.toPath().resolve("libs/org/ow2/asm/asm-analysis/9.5/asm-analysis-9.5.jar").toFile().getPath(),
                dirLauncher.toPath().resolve("libs/org/ow2/asm/asm-tree/9.5/asm-tree-9.5.jar").toFile().getPath(),
                dirLauncher.toPath().resolve("libs/org/ow2/asm/asm/9.5/asm-9.5.jar").toFile().getPath(),
                dirLauncher.toPath().resolve("libs/net/minecraftforge/JarJarFileSystems/0.3.19/JarJarFileSystems-0.3.19.jar").toFile().getPath());
       Process process = Runtime.getRuntime().exec(this.dirLauncher.getPath() + "/" + cmd +"/java -Djava.library.path=" + this.dirLauncher.getPath() +"/natives " +
                "-cp " + listFiles() + " " +
                "-DignoreList=bootstraplauncher,securejarhandler,asm-commons,asm-util,asm-analysis,asm-tree,asm,JarJarFileSystems,client-extra,fmlcore,javafmllanguage,lowcodelanguage,mclanguage,forge-,1.18.2-forge-40.2.17.jar -DmergeModules=jna-5.10.0.jar,jna-platform-5.10.0.jar,java-objc-bridge-1.0.0.jar -DlibraryDirectory=" + dirLauncher.getPath() + "/libs " +
                "-p " + p + " " +
                "--add-modules ALL-MODULE-PATH " +
                "--add-opens java.base/java.util.jar=cpw.mods.securejarhandler --add-opens java.base/java.lang.invoke=cpw.mods.securejarhandler " +
                "--add-exports java.base/sun.security.util=cpw.mods.securejarhandler --add-exports jdk.naming.dns/com.sun.jndi.dns=java.naming " +
                "cpw.mods.bootstraplauncher.BootstrapLauncher --username " + username + " --versionType release --version 1.18.2-forge-40.2.17 " +
                "--gameDir " + this.dirLauncher.getPath() + " --assetsDir " + this.dirLauncher.getPath() + "/assets --assetIndex 1.18 " +
                "--accessToken nope --launchTarget forgeclient --fml.forgeVersion 40.2.17 --fml.mcVersion 1.18.2 --fml.forgeGroup net.minecraftforge --fml.mcpVersion 20220404.173914");*/

        /*BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(process.getErrorStream()));

        // Read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }

        // Read any errors from the attempted command
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }*/
        Runtime.getRuntime()
                .exec( this.dirLauncher.getPath() +"/" + this.cmd + "/java -Djava.library.path="+ this.dirLauncher.getPath() +"/natives -cp " +  listFiles() + this.split +  this.dirLauncher.getPath() +"/minecraft.jar -Dminecraft.client.jar=" + this.dirLauncher.getPath() +"/minecraft.jar -Xms2G -Xmx"+ram+"G net.minecraft.launchwrapper.Launch --username "+ username +" --versionType Forge --version " + Main.NAME +" --gameDir " + this.dirLauncher.getPath() + " --assetsDir "+ this.dirLauncher.getPath() +"/assets --assetIndex 1.12 --accessToken nop --uuid nope --tweakClass net.minecraftforge.fml.common.launcher.FMLTweaker", null, this.dirLauncher);

    }


    public void unzipFile(File file) throws IOException {
        infoWindows.updateState("Décompression de " + file.getName());
        byte[] buffer = new byte[1024];
        ZipInputStream zip = new ZipInputStream(Files.newInputStream(file.toPath()));
        ZipEntry zipEntry = zip.getNextEntry();
        while (zipEntry != null) {
            File newFile = new File(dirLauncher.getPath(), zipEntry.getName());
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }
                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zip.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            if(!newFile.setExecutable(true)) throw new IOException("Can't set executable");
            zipEntry = zip.getNextEntry();
        }

        zip.closeEntry();
        zip.close();
        if(!file.delete()) System.out.println("Cant delete " + file.getName());
    }

    public void update() throws IOException {
        int downloadedBytes = 0;
        int countFile = 0;
        this.infoWindows.initProgressbar(download.size(), this.bytesSize/1000000);
        this.infoWindows.updateState(0, 0);
        for(URL url: download) {
            System.out.println("Download: " + url.getPath());
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/5.0");
            InputStream in = httpcon.getInputStream();

            File f = dirLauncher.toPath().resolve(url.getPath().replaceFirst(Main.PREFIX, "")).toFile();
            OutputStream out = Files.newOutputStream(f.toPath());

            //Prefer buffer like Files.copy because Files.copy doest support big file
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, bytesRead);
                downloadedBytes += bytesRead;
                this.infoWindows.updateState(countFile, downloadedBytes/1000000);
            }

            in.close();
            out.close();

            f.setExecutable(true);
            f.setReadable(true);
            f.setWritable(true);

            countFile++;
            this.infoWindows.updateState(countFile, downloadedBytes/1000000);

            if(url.getPath().contains("jre-") || url.getPath().contains("assets.zip")) unzipFile(dirLauncher.toPath().resolve(url.getPath().replaceFirst(Main.PREFIX, "")).toFile());
        }

        System.out.println("downloadedBytes = " + downloadedBytes);
        System.out.println("bytesSize = " + bytesSize);
    }

    private DownloadSatus checkHash(JSONObject onlineHash, File localFile) {
        this.infoWindows.updateState("Vérification des fichiers");
        String filePath = localFile.getPath().replace(this.dirLauncher.getPath() , "").replaceAll("/", "").replaceAll("\\\\", "");
        Matcher matcher = this.whitelist.matcher(filePath);

        if(matcher.matches()) return DownloadSatus.WHITELIST;


        if(!onlineHash.has(localFile.getName())) return DownloadSatus.NOT_ALLOW;
        // Convert to hexadecimal string if needed
        StringBuilder checksum = new StringBuilder();

        /*
        We build the md5 haash of file chunck by chunck because error in 32 bits version of java
        Method:
         - Get buffer of 1024 bytes
         - Add buffer to message digest
         - while buffer is not -1 we update digest
         - at the end we get full digest in byte array
         - build the checksum with byte array and string builder
         - compare to he online hash
         */
        try (FileInputStream fis = new FileInputStream(localFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            MessageDigest md = MessageDigest.getInstance("MD5");
            while ((bytesRead = fis.read(buffer)) != -1) {
                md.update(buffer, 0, bytesRead);
            }
            byte[] hash = md.digest();
            for (byte b : hash) {
                checksum.append(String.format("%02x", b));
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            System.out.println("Erreur l232 DLU: " + e.getMessage());
        }

        //Convert byte in String checksum and check if hash match with online
        if(onlineHash.getJSONObject(localFile.getName()).getString("hash").contentEquals(checksum))
            return DownloadSatus.LATEST_VERSION;

        return DownloadSatus.NEED_UPDATE;
    }

    public void checkLauncherDir() {
        this.infoWindows.updateState("Vérification de l'existance du dossier " + Main.NAME.toLowerCase());
        String appdata = System.getenv("APPDATA");

        if(appdata == null || appdata.isEmpty()) appdata = System.getenv("LIBRARY_PATH");
        if(appdata == null || appdata.isEmpty()) appdata = System.getenv("HOME");

        String dirLauncher = appdata + "/."  + Main.NAME.toLowerCase();
        File f = new File(dirLauncher);
        if(!f.exists()){
            if(!f.mkdir()) throw new RuntimeException("Can't create dir.");
        }


        if(!f.canWrite() && !f.setWritable(true)) throw new RuntimeException("Can't set writable dir.");
        if(!f.canRead() && !f.setReadable(true)) throw new RuntimeException("Can't set writable dir.");

        this.dirLauncher = f;
    }

    public void checkUpdate() throws Exception {
        infoWindows.updateState("Vérification des fichiers");
        URLConnection httpcon = new URL(Main.URL + "?dir=game").openConnection();
        httpcon.addRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader jsonCheck = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
        String line = jsonCheck.readLine();

        JSONObject serverInfo = new JSONObject(line);
        JSONObject files = serverInfo.getJSONObject("files");
        JSONArray whitelist = serverInfo.getJSONArray("whitelist");

        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < whitelist.length(); i++) {
            stringBuilder.append(whitelist.getString(i));

            if(i != whitelist.length() - 1) stringBuilder.append("|");
        }

        this.whitelist = Pattern.compile(stringBuilder.toString());

        checkRecCurrentFile(this.dirLauncher.listFiles(), files);

        //Check new file
        checkRecOnlineFile(files, dirLauncher.toPath());


        //Remove unecessary zip file jre
        if(System.getProperties().getProperty("os.name").toLowerCase().contains("linux")) {
            bytesSize -= 180000000;
            download.removeIf(url -> url.getPath().contains("jre-windows.zip"));
            download.removeIf(url -> url.getPath().contains("jre-macos.zip"));
            this.split = ":";
        } else if(System.getProperties().getProperty("os.name").toLowerCase().contains("windows")) {
            bytesSize -= 180000000;
            download.removeIf(url -> url.getPath().contains("jre-linux.zip"));
            download.removeIf(url -> url.getPath().contains("jre-macos.zip"));
        } else {
            bytesSize -= 180000000;
            download.removeIf(url -> url.getPath().contains("jre-linux.zip"));
            download.removeIf(url -> url.getPath().contains("jre-windows.zip"));
            this.cmd = "jre/Contents/Home/bin";
            this.split = ":";
        }

        //Remove down jre if its already present
        //dirLauncher.toPath().resolve()
        if(Objects.requireNonNull(dirLauncher.listFiles(file -> file.getName().equals("jre"))).length > 0) {
            download.removeIf(url -> url.getPath().contains("jre-"));
            bytesSize -= 180000000;
        }


        System.out.println("check assets");
        //Remove assets zip
        if(download.stream().noneMatch(url -> url.getPath().contains("download-assets"))) {
            System.out.println("Je passe ici");
            download.removeIf(url -> url.getPath().contains("assets.zip"));
            bytesSize -= 417838156;
        } else download.remove(new URL(Main.URL + "/download-assets"));

    }

    public void checkRecOnlineFile(JSONObject obj, Path parent) throws Exception {
        String natives = ".dll";
        if(System.getProperties().getProperty("os.name").toLowerCase().contains("linux")) natives = ".so";
        else if(System.getProperties().getProperty("os.name").toLowerCase().contains("mac")) natives = ".dylib";
        //Update info to check file
        this.infoWindows.updateState("Vérification des fichiers");
        //Start first check, main file
        for (Iterator<String> it = obj.keys(); it.hasNext();) {
            String name = it.next();
            JSONObject fileObj = obj.getJSONObject(name);
            /*
            If file is dir, we check if dir exist and create if not exist, and check content of this dir
            If file is file, we check the hash of file if doest match with server hash, redownload file
             */
            if(fileObj.getBoolean("dir")) {
                if(fileObj.has("files")) {
                    if (!Files.isDirectory(parent.resolve(name))) Files.createDirectories(parent.resolve(name));
                    this.checkRecOnlineFile(fileObj.getJSONObject("files"), parent.resolve(name));
                }
            }
            else {
                URL url = fileObj.getBoolean("download") ? new URL(fileObj.getString("url")) : new URL(Main.URL + "/download-assets");
                if(url.getPath().contains("natives") && !(url.getPath().endsWith(natives)  || url.getPath().contains(System.getProperties().getProperty("os.name").split(" ")[0].toLowerCase()))) continue;
                if(!this.lastVersion.contains(fileObj.getString("url")) && !download.contains(url)) {
                    download.add(url);
                    if(fileObj.getBoolean("download")) bytesSize += fileObj.getInt("size");
                }
            }
        }
    }

    public void checkRecCurrentFile(File[] listFile, JSONObject obj) throws IOException {
        //Check if list is not null
        if(listFile == null) return;

        //Check if current file is update to date
        for(File file: listFile)
        {
            /*
            Check if is dir
            If its dir: we call to rec metho
            Else: we check the server hash and local file hash and if file exist in server
             */
            if(file.isDirectory()) {
                if(obj.has(file.getName())) {
                    if(obj.getJSONObject(file.getName()).has("files"))
                        checkRecCurrentFile(file.listFiles(), obj.getJSONObject(file.getName()).getJSONObject("files"));
                } else if(!this.deleteRec(file)) throw new IOException("Can't delete dir: " + file.getName());
            }
            else {
                switch (checkHash(obj, file)) {
                    case LATEST_VERSION:
                        this.lastVersion.add(obj.getJSONObject(file.getName()).getString("url"));
                        break;
                    case WHITELIST:
                        System.out.println(file + " is whitelist");
                        break;
                    default:
                        System.out.println("try delete: " + file.getName());
                        if (!file.delete()) throw new IOException("Can't delete file: " + file.getName());
                        break;
                }
            }
        }
    }

    private boolean deleteRec(File dir) {
        File[] listFile = dir.listFiles();
        if(listFile != null) {
            for (File f : listFile) {
                if(f.isDirectory()) {
                    return deleteRec(f);
                } else return f.delete();
            }
        }
        return dir.delete();
    }

    public File getDir() {
        return this.dirLauncher;
    }
}
