package solutions.sulfura.gend.dsl.projections;

public class CharacterStream {
    int pos = -1;
    Character previousChar = null;
    Character currentChar = null;
    Character nextChar = null;
    char[] streamData;

    public CharacterStream(char[] streamData) {
        this.streamData = streamData;
        if (streamData.length > 0) {
            this.nextChar = streamData[0];
        }
        this.next();
    }

    public int getPos() {
        return pos;
    }

    public Character getPreviousChar() {
        return previousChar;
    }

    public Character getCurrentChar() {
        return currentChar;
    }

    public Character getNextChar() {
        return nextChar;
    }

    public Character next() {

        previousChar = currentChar;
        currentChar = nextChar;
        if (pos < streamData.length) {
            pos++;
            nextChar = pos < streamData.length - 1 ? streamData[pos + 1] : null;
        }

        return currentChar;
    }
}
