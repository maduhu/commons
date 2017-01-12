package sos.marshalling;

import java.io.File;
import java.util.StringTokenizer;

import sos.connection.SOSConnection;
import sos.util.SOSArguments;
import sos.marshalling.SOSExport;
import sos.util.SOSStandardLogger;

/** @author Robert Ehrlich */
public class SOSExportProcessor {

    private SOSConnection _sosConnection = null;
    private SOSStandardLogger _sosLogger = null;
    private File _configFile = null;
    private File _logFile = null;
    private int _logLevel = 0;
    private File _outputFile = null;
    private String _tableNames = null;
    private String _executeQuery = null;
    private String _keys = null;
    private boolean enableTableParametr = true;
    private boolean enableExecuteParametr = true;

    public SOSExportProcessor(File configFile, File logFile, int logLevel, File outputFile, String tableNames, String executeQuery, String keys) throws Exception {
        if (configFile == null) {
            throw new NullPointerException("Export: Parameter config == null!");
        }
        if (outputFile == null) {
            throw new NullPointerException("Export: Parameter output == null!");
        }
        try {
            _configFile = configFile;
            _logFile = logFile;
            _logLevel = logLevel;
            _outputFile = outputFile;
            _tableNames = tableNames;
            _executeQuery = executeQuery;
            _keys = keys;
            if (_configFile != null && !_configFile.getName().isEmpty() && !_configFile.exists()) {
                throw new Exception("configuration file not found: " + _configFile);
            }
            if ((_tableNames != null && !"".equals(tableNames)) && (_executeQuery != null && !"".equals(_executeQuery))) {
                throw new Exception("-tables and -execute may not be indicated together");
            }
            if (_logLevel != 0 && "".equals(_logFile.toString())) {
                throw new Exception("log file is not defined");
            }
        } catch (Exception e) {
            throw new Exception("error in SOSExportProcessor: " + e.getMessage());
        }
    }

    public SOSExportProcessor() {
        System.out.println("Syntax");
        System.out.println("Optionen :");
        System.out.println("        -config     Namen der Konfigurationsdatei f�r die DB Verbindung angeben.");
        System.out.println("                    Default : sos_settings.ini");
        System.out.println("        -output     Namen der Export XML-Datei angeben.");
        System.out.println("                    Default : sos_export.xml ");
        System.out.println("        -tables     Tabellennamen f�r den Export.");
        System.out.println("                    Es werden alle Daten der jeweiligen Tabelle exportiert.");
        System.out.println("                    Mehrere Tabellen durch + Zeichen getrennt");
        System.out.println("        -keys    	Schl�sselfelder f�r eine bzw mehreren Tabellen angeben.");
        System.out.println("           			Wird im Zusammenhang mit der Option -tables ber�cksichtigt.");
        System.out.println("                    Schl�sselfelder sind wichtig, wenn eine Tabelle CLOB bzw BLOB enth�lt.");
        System.out.println("                    mehrere Schl�ssel f�r eine Tabelle - durch Komma getrennt.");
        System.out.println("                    f�r mehreren Tabellen durch + Zeichen getrennt.");
        System.out.println("                    f�r mehreren Tabellen : die Reihenfolge wie bei -tables.");
        System.out.println("        -execute    eigene SQL-Statement f�r eine Tabelle angeben.");
        System.out.println("                    SQL-Statement in doppelten Hochkommas.");
        System.out.println("        -log        Namen der Log-Datei angeben.");
        System.out.println("                    Default : sos_export.log");
        System.out.println("        -log-level  Loglevel angeben.");
        System.out.println("                    Default : 0  keine Log-Datei schreiben");
        System.out.println("");
        System.out.println("");
        System.out.println("Notiz : -execute und -tables d�rfen nicht zusammen angegeben werden.");
        System.out.println("        eine von beiden Optionen muss angegeben sein");
        System.out.println("");
        System.out.println("");
        System.out.println("Beispiel 1 : alle Daten der Tabelle t1 exportieren und in die default Log-Datei loggen");
        System.out.println("         -config=config/sos_settings.ini -tables=t1 -log-level=9");
        System.out.println("");
        System.out.println("Beispiel 2 : wie Beispiel 1 + Schl�sselfeld ID f�r die Tabelle t1 definieren");
        System.out.println("         -config=config/sos_settings.ini -tables=t1 -keys=ID -log-level=9");
        System.out.println("");
        System.out.println("Beispiel 3 : alle Daten der Tabellen t1 und t2 ohne zu loggen exportieren");
        System.out.println("         -config=config/sos_settings.ini -tables=t1+t2");
        System.out.println("");
        System.out
                .println("Beispiel 4 : wie Beispiel 3 + Schl�sselfelder T1_ID und T1_NAME f�r die Tabelle t1 und Schl�sselfeld T2_ID f�r die Tabelle t2 definieren");
        System.out.println("         -config=config/sos_settings.ini -tables=t1+t2 -keys=T1_ID,T1_NAME+T2_ID");
        System.out.println("");
        System.out.println("Beispiel 5 : eigene SQL-Statement f�r die Tabelle t1 definieren");
        System.out.println("         -config=config/sos_settings.ini -execute=\"select * from t1 where ID=1\"");
    }

    public void doExport() throws Exception {
        try {
            if (this.isEnableTableParametr() && this.isEnableExecuteParametr() && (_tableNames == null || "".equals(_tableNames)) 
                    && (_executeQuery == null || "".equals(_executeQuery))) {
                throw new Exception("undefined operation for export. Check please input for your -tables or -execute arguments");
            }
            if (_logLevel == 0) {
                _sosLogger = new SOSStandardLogger(SOSStandardLogger.DEBUG);
            } else {
                _sosLogger = new SOSStandardLogger(_logFile.toString(), _logLevel);
            }
            _sosConnection = SOSConnection.createInstance(_configFile.toString(), _sosLogger);
            _sosConnection.connect();
            SOSExport export = new SOSExport(_sosConnection, _outputFile.toString(), "EXPORT", _sosLogger);
            prepareExport(export);
            export.doExport();
            System.out.println("");
            System.out.println("Export erfolgreich beendet.");
        } catch (Exception e) {
            throw new Exception("error in SOSExportProcessor: " + e.getMessage());
        } finally {
            try {
                if (_sosConnection != null) {
                    _sosConnection.disconnect();
                }
            } catch (Exception e) {
            }
        }

    }

    public void prepareExport(SOSExport export) throws Exception {
        String keys = "";
        String[] tablesKeys = {};
        if (_keys != null && !"".equals(_keys.trim())) {
            keys = _keys.toUpperCase();
            tablesKeys = keys.split("\\+");
        }
        if (!"".equals(_tableNames)) {
            StringTokenizer tables = new StringTokenizer(_tableNames, "+");
            int i = 0;
            while (tables.hasMoreTokens()) {
                String table = tables.nextToken().toUpperCase();
                String key = "";
                if (!"".equals(table)) {
                    if (tablesKeys != null && tablesKeys.length != 0) {
                        try {
                            key = tablesKeys[i];
                        } catch (Exception e) {
                        }
                    }
                    export.add(table, key, "select * from " + table, null, i);
                    i++;
                }
            }
        } else if (!"".equals(_executeQuery)) {
            StringTokenizer st = new StringTokenizer(_executeQuery, " ");
            String table = "";
            while (st.hasMoreTokens()) {
                String token = st.nextToken().toUpperCase();
                if ("FROM".equals(token)) {
                    table = st.nextToken().toUpperCase();
                    break;
                }
            }
            export.add(table, keys, _executeQuery, null, 0);
        }
    }

    /** TODO: translate javadoc to english only!!! 
     * Programm ausf�hren<br>
     * 
     * @param args Programmargumente<br>
     * <br>
     * 
     *            Mit dem Argument "?" bzw "help" kann mann sich Programm Usage
     *            anzeigen lassen.<br>
     * <br>
     * 
     *            Weitere m�gliche Argumente<br>
     *            -config Datei, in der die Zugangsdaten zur Datenbank enthalten
     *            sind<br>
     *            Default : sos_settings.ini<br>
     *            -log Name der Protokolldatei<br>
     *            Default : sos_export.log<br>
     *            -log-level Log Level f�r die Protokolldatei<br>
     *            Default : 0 keine Protokollierung<br>
     *            -output Name der XML-Datei f�r Export<br>
     *            Default : sos_export.xml<br>
     *            -tables Tabellennamen f�r den Export. <br>
     *            Es werden alle Daten der jeweiligen Tabelle exportiert <br>
     *            Mehrere Tabellen durch + Zeichen getrennt.<br>
     *            Default : Leerstring<br>
     *            -execute eigene SQL-Statement f�r eine Tabelle angeben. <br>
     *            Mu� in doppelten Hochkommas angegeben werden<br>
     *            Default : Leerstring<br>
     *            -keys Schl�sselfelder f�r eine bzw mehreren Tabellen. <br>
     *            Wird im Zusammenhang mit dem Argument -tables ber�cksichtigt <br>
     *            Schl�sselfelder sind wichtig, wenn eine Tabelle CLOB bzw BLOB
     *            enth�lt. <br>
     *            mehrere Schl�ssel f�r eine Tabelle - durch Komma getrennt <br>
     *            f�r mehreren Tabellen durch + Zeichen getrennt <br>
     *            f�r mehreren Tabellen : die Reihenfolge wie bei -tables<br>
     *            Default : Leerstring<br>
     * <br>
     *            -execute und -tables d�rfen nicht zusammen angegeben werden.<br>
     *            eine von beiden Optionen muss angegeben sein<br>
     * 
     * @throws Exception */
    public static void main(String[] args) throws Exception {
        boolean isExport = true;
        if (args.length == 1) {
            String argument = args[0].toLowerCase().trim();
            if ("?".equals(argument) || "help".equals(argument)) {
                isExport = false;
            }
        }
        if (isExport) {
            SOSArguments arguments = new SOSArguments(args);
            SOSExportProcessor processor = new SOSExportProcessor(new File(arguments.as_string("-config=", "sos_settings.ini")), new File(arguments.as_string(
                    "-log=", "sos_export.log")), arguments.as_int("-log-level=", 0), new File(arguments.as_string("-output=", "sos_export.xml")), new String(
                    arguments.as_string("-tables=", "")), new String(arguments.as_string("-execute=", "")), new String(arguments.as_string("-keys=", "")));
            arguments.check_all_used();
            processor.doExport();
        }
    }

    public boolean isEnableExecuteParametr() {
        return enableExecuteParametr;
    }

    public void setEnableExecuteParametr(boolean enableExecuteParametr) {
        this.enableExecuteParametr = enableExecuteParametr;
    }

    public boolean isEnableTableParametr() {
        return enableTableParametr;
    }

    public void setEnableTableParametr(boolean enableTableParametr) {
        this.enableTableParametr = enableTableParametr;
    }

}