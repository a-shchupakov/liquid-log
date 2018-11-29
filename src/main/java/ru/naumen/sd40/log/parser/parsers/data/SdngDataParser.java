package ru.naumen.sd40.log.parser.parsers.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.dataSets.ActionDataSet;
import ru.naumen.sd40.log.parser.dataSets.ErrorDataSet;
import ru.naumen.sd40.log.parser.dataSets.SdngDataSet;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("sdng" + "DataParser")
public class SdngDataParser implements IDataParser<SdngDataSet> {
    private static final Pattern doneRegEx = Pattern.compile("Done\\((\\d+)\\): ?(.*?Action)");
    private static final Set<String> EXCLUDED_ACTIONS = new HashSet<>();
    static
    {
        EXCLUDED_ACTIONS.add("EventAction".toLowerCase());
    }

    private static final Pattern warnRegEx = Pattern.compile("^\\d+ \\[.+?\\] \\(.+?\\) WARN");
    private static final Pattern errorRegEx = Pattern.compile("^\\d+ \\[.+?\\] \\(.+?\\) ERROR");
    private static final Pattern fatalRegEx = Pattern.compile("^\\d+ \\[.+?\\] \\(.+?\\) FATAL");

    @Override
    public void parseLine(String line, SdngDataSet dataSet) {
        parseActionLine(line, dataSet.getAction());
        parseErrorLine(line, dataSet.getError());
    }

    private void parseActionLine(String line, ActionDataSet dataStorage) {
        Matcher matcher = doneRegEx.matcher(line);

        if (matcher.find()) {
            String actionInLowerCase = matcher.group(2).toLowerCase();
            if (EXCLUDED_ACTIONS.contains(actionInLowerCase)) {
                return;
            }
            dataStorage.getTimes().add(Integer.parseInt(matcher.group(1)));
            if (actionInLowerCase.equals("addobjectaction")) {
                dataStorage.increaseAddObjectActions();
            } else if (actionInLowerCase.equals("editobjectaction")) {
                dataStorage.increaseEditObjectsActions();
            } else if (actionInLowerCase.matches("(?i)[a-zA-Z]+comment[a-zA-Z]+")) {
                dataStorage.increaseCommentActions();
            } else if (!actionInLowerCase.contains("advlist")
                    && actionInLowerCase.matches("(?i)^([a-zA-Z]+|Get)[a-zA-Z]+List[a-zA-Z]+"))
            {
                dataStorage.increaseListActions();
            } else if (actionInLowerCase.matches("(?i)^([a-zA-Z]+|Get)[a-zA-Z]+Form[a-zA-Z]+")) {
                dataStorage.increaseFormActions();
            } else if (actionInLowerCase.matches("(?i)^([a-zA-Z]+|Get)[a-zA-Z]+DtObject[a-zA-Z]+")) {
                dataStorage.increaseDtObjectActions();
            } else if (actionInLowerCase.matches("(?i)[a-zA-Z]+search[a-zA-Z]+")) {
                dataStorage.increaseSearchActions();
            } else if (actionInLowerCase.equals("getcatalogsaction")) {
                dataStorage.increaseCatalogsActions();
            }
        }
    }

    private void parseErrorLine(String line, ErrorDataSet dataStorage) {
        if (warnRegEx.matcher(line).find())
        {
            dataStorage.increaseWarnCount();
        }
        if (errorRegEx.matcher(line).find())
        {
            dataStorage.increaseErrorCount();
        }
        if (fatalRegEx.matcher(line).find())
        {
            dataStorage.increaseFatalCount();
        }
    }
}
