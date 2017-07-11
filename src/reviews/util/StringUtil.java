package reviews.util;

import javafx.collections.transformation.SortedList;

import java.util.*;

/**
 * Supporting functions for working with dates.
 */
public class StringUtil {

    /**
     * Simple string comparing.
     * @param s1 first string.
     * @param s2 second string.
     * @return true if string are equal(not case-sensitive).
     */
    public static boolean equalString(String s1, String s2) {
        return s1.trim().toLowerCase().equals(s2.trim().toLowerCase());
    }

    /**Author-string comparing.
     *
     * @param a1
     * @param a2
     * @return
     */
    public static boolean equalAuthors(String a1, String a2) {
        List<String> list1 = new ArrayList<>();
        for(String s : a1.trim().split(" ")) {
            list1.add(s);
        }
        List<String> list2 = new ArrayList<>();
        for(String s : a2.trim().split(" ")) {
            list2.add(s);
        }

        return list1.size() == list2.size() && list1.containsAll(list2);
    }

    /**Hash for simple string.
     *
     * @param s
     * @return
     */
    public static int hash(String s) {
        final int P = 31;
        int hash = 0, p_pow = 1;
        for (int i=0; i < s.length(); ++i)
        {
            hash += (s.charAt(i) - 'a' + 1) * p_pow;
            p_pow *= P;
        }
        return hash;
    }

    /** Hash for string-author(first & second name).
     *
     * @param s
     * @return hash.
     */
    public static int authorHash(String s) {
        int hash = 0;
        for (int i=0; i < s.length(); ++i)
        {
            hash += (s.charAt(i) - 'a' + 1);
        }
        return hash;
    }


    //TODO: smarter hash and equal for author
}
