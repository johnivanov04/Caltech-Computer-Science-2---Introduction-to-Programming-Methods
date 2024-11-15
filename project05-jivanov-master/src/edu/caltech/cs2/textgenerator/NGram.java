package edu.caltech.cs2.textgenerator;

import edu.caltech.cs2.datastructures.LinkedDeque;
import edu.caltech.cs2.interfaces.IDeque;

import java.util.Iterator;
public class NGram implements Iterable<String>, Comparable<NGram> {
    public static final String NO_SPACE_BEFORE = ",?!.-,:'";
    public static final String NO_SPACE_AFTER = "-'><=";
    public static final String REGEX_TO_FILTER = "”|\"|“|\\(|\\)|\\*";
    public static final String DELIMITER = "\\s+|\\s*\\b\\s*";
    private IDeque<String> data;

    public static String normalize(String s) {
        return s.replaceAll(REGEX_TO_FILTER, "").strip();
    }

    public NGram(IDeque<String> x) {
        this.data = new LinkedDeque<>();
        for (String word : x) {
            this.data.add(word);
        }
    }

    public NGram(String data) {
        this(normalize(data).split(DELIMITER));
    }

    public NGram(String[] data) {
        this.data = new LinkedDeque<>();
        for (String s : data) {
            s = normalize(s);
            if (!s.isEmpty()) {
                this.data.addBack(s);
            }
        }
    }

    public NGram next(String word) {
        String[] data = new String[this.data.size()];
        Iterator<String> dataIterator = this.data.iterator();
        dataIterator.next();
        for (int i = 0; i < data.length - 1; i++) {
            data[i] = dataIterator.next();
        }
        data[data.length - 1] = word;
        return new NGram(data);
    }

    public String toString() {
        String result = "";
        String prev = "";
        for (String s : this.data) {
            result += ((NO_SPACE_AFTER.contains(prev) || NO_SPACE_BEFORE.contains(s) || result.isEmpty()) ? "" : " ") + s;
            prev = s;
        }
        return result.strip();
    }

    @Override
    public Iterator<String> iterator() {
        return this.data.iterator();
    }

    @Override
    public int compareTo(NGram other) {
        Iterator<String> str1 = this.data.iterator();
        Iterator<String> str2 = other.data.iterator();
        for (int i = 0; i < this.data.size(); i++){
            if (!str2.hasNext()){
                return 1;
            }
            int checker  = str1.next().compareTo(str2.next());
            if (checker != 0){
                return checker;
            }
        }
        if (str2.hasNext()){
            return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NGram)) {
            return false;
        }
        NGram ng = (NGram) o;
        if (this.data.size() != ng.data.size())
        {
            return false;
        }
        Iterator<String> str1 = this.data.iterator();
        Iterator<String> str2 = ng.data.iterator();
        while (str1.hasNext()){
            if(!str1.next().equals(str2.next())){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int r  = 17;
        for (String w : this.data){
            int Hashcode = w.hashCode();
            r = 31*r + Hashcode;
        }
        return r;
    }
}