package Wordle.Shared.Data;


import VitoBarra.JavaUtil.ColoredOutput.Color;
import VitoBarra.JavaUtil.ColoredOutput.ColoredStringHelper;

import java.util.ArrayList;
import java.util.List;

public class HintedWord {
    private List<HintFrame> HintedFrames;

    public HintedWord(String word) {
        HintedFrames = new ArrayList<>(word.length());
        for (var e : word.split(""))
            HintedFrames.add(new HintFrame(e));
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (var frame : HintedFrames) {
            Color color;
            switch (frame.HintStatus) {
                case Miss -> color = Color.Gray;
                case Correct -> color = Color.Green;
                case DifferentPosition -> color = Color.Yellow;

                default -> throw new IllegalStateException("Unexpected value: " + frame.HintStatus);
            }
            s.append(ColoredStringHelper.TextInColor(frame.Char, color));
        }
        return s.toString();
    }

    public void UpdateStatusHint(int Position, String c, String c2) {
        if (Position >= HintedFrames.size()) return;
        HintedFrames.get(Position).HintStatus = c.equals(c2) ? HintState.Correct : HintState.DifferentPosition;
    }

    public class HintFrame {
        public String Char;
        public HintState HintStatus;

        public HintFrame(String e) {
            Char = e;
            HintStatus = HintState.Miss;
        }

    }

    public HintedWord CreateBlankHint() {
        var blackHintedWord = new HintedWord("*".repeat(HintedFrames.size()));
        for (int i = 0; i < HintedFrames.size(); i++)
            blackHintedWord.HintedFrames.get(i).HintStatus = HintedFrames.get(i).HintStatus;
        return blackHintedWord;
    }

    public enum HintState {
        Miss,
        Correct,
        DifferentPosition
    }

}
