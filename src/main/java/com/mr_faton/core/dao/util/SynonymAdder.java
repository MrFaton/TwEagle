package com.mr_faton.core.dao.util;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.core.dao.SynonymizerDAO;
import com.mr_faton.core.pool.db_connection.TransactionManager;
import com.mr_faton.core.util.Command;
import com.mr_faton.core.util.SettingsHolder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.*;

public class SynonymAdder {
    private static final String FILE_PATH = "C:\\Synonym.txt";
    private static final int MAX_WORD_STR_LENGTH = 35;
    private static final int MAX_SYNONYM_STR_LENGTH = 90;
    private static final String DELIMITER_OLD = "|";
    private static final String DELIMITER_NEW = ",";
    private static final List<String> BAD_WORDS = Arrays.asList("Ant", "(", ")");
    private static int CURRENT_LINE = 0;
    private static int PROGRESS = 0;
    private static int LINE_NUM = 1_262_744;

    private final TransactionManager transactionManager;
    private final SynonymizerDAO synonymizerDAO;

    public SynonymAdder(TransactionManager transactionManager, SynonymizerDAO synonymizerDAO) {
        this.transactionManager = transactionManager;
        this.synonymizerDAO = synonymizerDAO;
    }


    public static void main(String[] args) throws Exception {
        SettingsHolder.loadSettings();
        TransactionManager transactionManager = (TransactionManager) AppContext.getBeanByName("transactionManager");
        SynonymizerDAO synonymizerDAO = (SynonymizerDAO) AppContext.getBeanByName("synonymizerDAO");

        final SynonymAdder synonymAdder = new SynonymAdder(transactionManager, synonymizerDAO);

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                synonymAdder.addWordsFromFile();
            }
        });
    }


    public void addWordsFromFile() {
        Map<String, String> wordsMap = new HashMap<>();
        String line = null;
        String word = null;
        String synonyms = null;
        try {
            try(Scanner scanner = new Scanner(new FileInputStream(FILE_PATH), "cp1251")) {
                while (scanner.hasNextLine()) {
                    CURRENT_LINE++;
                    line = scanner.nextLine();
                    int delimiterIndex = line.indexOf(DELIMITER_OLD);
                    word = line.substring(0, delimiterIndex);
                    if (word.contains(" ")) {
                        continue;
                    }
                    if (word.length() > MAX_WORD_STR_LENGTH) {
                        continue;
                    }
                    //Ignore words that have many synonyms
                    if (line.length() - word.length() > MAX_SYNONYM_STR_LENGTH) {
                        continue;
                    }
                    //ignore words that includes bad words
                    boolean containBadWord = false;
                    for (String badWord : BAD_WORDS) {
                        if (line.contains(badWord)) {
                            containBadWord = true;
                            break;
                        }
                    }
                    if (containBadWord) continue;

                    synonyms = line.substring(++delimiterIndex, line.length());

                    synonyms = synonyms.replace(DELIMITER_OLD, DELIMITER_NEW);

                    wordsMap.put(word, synonyms);

                    int currentProgress = (int) (((double)CURRENT_LINE / (double)LINE_NUM) * 100);
                    if (currentProgress != PROGRESS) {
                        PROGRESS = currentProgress;
                        System.out.println(PROGRESS + "%");
                    }
                }
                synonymizerDAO.addWords(wordsMap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println("line=" + line);
            System.err.println("word=" + word);
            System.err.println("synonyms=" + synonyms);
            e.printStackTrace();
        }
    }
}
