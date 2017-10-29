package com.google.engedu.ghost;

import android.test.FlakyTest;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
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

//      Binary search /  selection of word
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

        int lowerBound = findLowBound(prefix);
        int upperBound = findUpperBound(prefix);
        Log.d("Word_Bounds", "For " + prefix + ": " + words.get(lowerBound == -1 ? 0 : lowerBound)
                + ", " + words.get(upperBound == -1 ? 0 : upperBound));

        ArrayList<String> possibleWords = new ArrayList<>();
        for (int i = lowerBound; i <= upperBound ; i++) {
            String word = words.get(i);
            int wordLength = word.length();
            if (wordLength < MIN_WORD_LENGTH
                    && (wordLength - prefix.length())% 2 == 0
                    && wordLength - prefix.length() == 1) {
                continue;
            }
            possibleWords.add(word);
        }
        if (possibleWords.size() == 0) {
            return longerWord;
        }
        Random random = new Random();
        return possibleWords.get(random.nextInt(possibleWords.size()));
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        return selected;
    }

    public int findUpperBound(String prefix) {
        int low = 0;
        int upper = words.size()-1;
        int middle = (low + upper)/2;
        while(low < upper) {
            middle = (low + upper)/2;
            String middleWord = words.get(middle);
            if (middleWord.startsWith(prefix)) {
                low = middle;
                if (low == upper) {
                    return low;
                } else {
                    if (upper - low == 1) {
                        if (words.get(upper).startsWith(prefix)) {
                            return upper;
                        } else {
                            return low;
                        }
                    }
                    continue;
                }
            } else if (middleWord.compareToIgnoreCase(prefix) < 0) {
                // middle word is lesser than prefix
                low = middle + 1;
                continue;
            } else {
                upper = middle - 1;
                continue;
            }
        }
        return middle-1;
    }

    public int findLowBound(String prefix) {
        int low = 0;
        int upper = words.size()-1;
        int middle = (low + upper)/2;
        while(low < upper) {
            middle = (low + upper)/2;
            String middleWord = words.get(middle);
            if (middleWord.startsWith(prefix)) {
                upper = middle;
                if (low == upper) {
                    return low;
                } else {
                    if (upper - low == 1) {
                        if (words.get(low).startsWith(prefix)) {
                            return low;
                        } else {
                            return upper;
                        }
                    }
                }
            } else if (middleWord.compareToIgnoreCase(prefix) < 0) {
                // middle word is lesser than prefix
                low = middle + 1;
                continue;
            } else {
                upper = middle - 1;
                continue;
            }
        }
        return middle+1;
    }
}
