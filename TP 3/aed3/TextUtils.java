package aed3;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class TextUtils {

    private static final Set<String> STOPWORDS = new HashSet<>(Arrays.asList(
        "a","o","os","as","um","uma","uns","umas",
        "de","da","do","das","dos","d","e","é","em","no","na","nos","nas",
        "por","para","com","sem","ao","à","às","aos",
        "que","se","sua","seu","suas","seus",
        "para","pra","pro","à","ao","ou","onde","quando",
        "b","c","d","f","g","h","i","j","k","l","m","n","p","q","r","s","t","u","v","w","x","y","z"
    ));

    public static String normalize(String s) {
        if (s == null) return "";
        String lower = s.toLowerCase(Locale.ROOT);
        String norm = Normalizer.normalize(lower, Normalizer.Form.NFD);
        norm = norm.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        norm = norm.replaceAll("[^a-z0-9\\s]", " ");
        norm = norm.replaceAll("\\s+", " ").trim();
        return norm;
    }

    public static String[] tokenizeValidTerms(String text) {
        String norm = normalize(text);
        if (norm.isEmpty()) return new String[0];
        String[] parts = norm.split(" ");
        return Arrays.stream(parts)
                .filter(p -> !p.isEmpty())
                .filter(p -> !STOPWORDS.contains(p))
                .toArray(String[]::new);
    }
}


