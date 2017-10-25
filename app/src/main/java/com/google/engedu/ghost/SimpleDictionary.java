package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if (prefix == null || prefix == "") {
            Random random = new Random();
            int randomIndex = random.nextInt(words.size());
            return words.get(randomIndex);
        }

        int low = 0;
        int upper = words.size()-1;
        int middle;
        String longerWord = null;
        while(low <= upper) {
            middle = (low + upper)/2;
            String middleWord = words.get(middle);
            if (middleWord.startsWith(prefix)) {
                longerWord = middleWord;
                break;
            } else if (middleWord.compareToIgnoreCase(prefix) < 0) {
                // middle word is greater than prefix
                low = middle + 1;
                continue;
            } else {
                upper = middle - 1;
                continue;
            }
        }
        return longerWord;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        return selected;
    }
}
