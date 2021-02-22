package com.gmail.sbal.stels;

import java.util.*;
import java.util.stream.Collectors;

public abstract class DistanceWordProcessor implements WordProcessor{
    protected int countDist(String w1, String w2){
        int ans = Math.abs(w1.length() - w2.length());
        for (int i = 0; i < Math.min(w1.length(), w2.length()); i++) {
            if(w1.charAt(i) != w2.charAt(i)){
                ans ++;
            }
        }
        return ans;
    }

    protected List<StrPair> getMaxDistList(Collection<String> wordList){
        return counter(wordList).getFirst();
    }

    protected int getMaxDistFromList(Collection<String> wordList){
        return counter(wordList).getSecond();
    }

    private List<String> getListFromPairList(List<StrPair> list){
        List<String> ans = new ArrayList<>();
        for(var el: list){
            ans.add(el.getFirst());
            ans.add(el.getSecond());
        }
        return ans;
    }

    private boolean isMatch(Map<String, Set<String>> map, Set<String> candidates, Set<String> not)
    {
        if(!candidates.isEmpty() && !not.isEmpty()) {
            for (String s : not) {
                if (map.containsKey(s)) {
                    Set<String> newCandidates = new LinkedHashSet<>(candidates);
                    if (!map.get(s).isEmpty()) {
                        newCandidates.removeAll(map.get(s));
                        if(newCandidates.isEmpty()){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private Set<String> findClique(Map<String, Set<String>> map, Set<String> candidates, Set<String> not, Set<String> ans){
        while (!candidates.isEmpty() && !isMatch(map, candidates, ans)){

            String candidate = candidates.stream().findAny().get();

            Set<String> newCandidates = new LinkedHashSet<>(candidates);
            Set<String> newNot = new LinkedHashSet<>(not);
            newCandidates.retainAll(map.get(candidate));
            newNot.retainAll(map.get(candidate));

            if(candidate.isEmpty() && not.isEmpty())
                return ans;
            else
                findClique(map, newCandidates, newNot, ans);

            ans.remove(candidate);
            candidates.remove(candidate);
            not.add(candidate);
        }
        return ans;
    }

    protected Set<String> getSetCollectionOfMaxSubsetsFromList(Collection<String> wordList){
        var res = counter(wordList);
        List<String> maxDistList = getListFromPairList(res.getFirst());
        int maxDist = res.getSecond();

        Map<String, Set<String>> map = new LinkedHashMap<>();
        for (var el: maxDistList){
            map.put(el,
                    maxDistList.stream()
                                .filter(w -> countDist(w, el) == maxDist)
                                .collect(Collectors.toSet())
            );
        }
        Set<String> ans = findClique(
                map,
                map.keySet(),
                new LinkedHashSet<String>(),
                new LinkedHashSet<String>()
        );
        return ans;
    }

    private Pair<List<StrPair>, Integer> counter(Collection<String> wordList){
        List<StrPair> ans = new ArrayList<>();
        int max = 0;
        for(String w1: wordList)
            for(String w2: wordList){
                int dist = countDist(w1, w2);
                if (dist > max){
                    ans.clear();
                    max = dist;
                }
                if(dist == max){
                    if(!ans.contains(new StrPair(w1, w2))
                            && !ans.contains(new StrPair(w2, w1))){
                        ans.add(new StrPair(w2, w1));
                    }
                }
            }
        return new Pair(ans, max);
    }

    private int countMinDist(StrPair p1, StrPair p2){
        if(p1 != null) return p1.countMinDist(p2);
        else return 0;
    }

    protected class Pair<F,S>{
        private F first;
        private S second;

        protected Pair() {
        }

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }

        public F getFirst() {
            return first;
        }

        public S getSecond() {
            return second;
        }

        public void setFirst(F first) {
            this.first = first;
        }

        public void setSecond(S second) {
            this.second = second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return first.equals(pair.first) &&
                    second.equals(pair.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }

        @Override
        public String toString() {
            return first.toString() + "\t" + second.toString();
        }
    }

    protected class StrPair extends Pair<String, String>{

        public StrPair(String w2, String w1) {
            super(w1, w2);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StrPair pair = (StrPair) o;
            return (getFirst().equals(pair.getFirst()) &&
                    getSecond().equals(pair.getSecond())) ||
                    (getFirst().equals(pair.getSecond()) &&
                            getSecond().equals(pair.getFirst()));
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        private int countMinDist(StrPair other){
            if(this.equals(other) || other == null) return 0;
            return Math.min(
                    Math.min(
                            countDist(this.getFirst(), other.getFirst()),
                            countDist(this.getFirst(), other.getSecond())
                    ),
                    Math.min(
                            countDist(this.getSecond(), other.getFirst()),
                            countDist(this.getSecond(), other.getSecond())
                    )
            );
        }
    }


}
