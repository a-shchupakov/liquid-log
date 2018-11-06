package ru.naumen.sd40.log.parser.parsers.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.storages.ActionDataStorage;
import ru.naumen.sd40.log.parser.storages.DataSet;
import ru.naumen.sd40.log.parser.storages.ErrorDataStorage;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("sdng")
public class SdngDataParser implements IDataParser {
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
    public void parseLine(String line, DataSet dataSet) {
        parseActionLine(line, dataSet.getActionsData());
        parseErrorLine(line, dataSet.getErrorData());
    }

    private void parseActionLine(String line, ActionDataStorage dataStorage) {
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

    private void parseErrorLine(String line, ErrorDataStorage dataStorage) {
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
