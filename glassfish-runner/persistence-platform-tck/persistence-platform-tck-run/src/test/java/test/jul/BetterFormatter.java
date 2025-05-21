package test.jul;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class BetterFormatter extends Formatter {
    // date(1), source(2), logger(3), level(4), message(5), thrown(6)
    private static final String CUSTOM_FORMAT = "[%4$s] %1$tF %1$tT %3$s : %5$s%6$s%n";
    private static final int SOURCE_LENGTH = 20;

    public BetterFormatter() {
        System.out.println("BetterFormatter created");
    }
    @Override
    public String format(LogRecord record) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(
                record.getInstant(), ZoneId.systemDefault());
        String loggerName = record.getLoggerName();
        // Get the last 12 characters of the source
        if (loggerName != null && loggerName.length() > SOURCE_LENGTH) {
            loggerName = loggerName.substring(loggerName.length() - SOURCE_LENGTH);
        }
        String message = formatMessage(record);
        String throwable = "";
        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.println();
            record.getThrown().printStackTrace(pw);
            pw.close();
            throwable = sw.toString();
        }
        return String.format(CUSTOM_FORMAT,
                             zdt,
                             "",
                             loggerName,
                             record.getLevel().getName(),
                             message,
                             throwable);
    }
}