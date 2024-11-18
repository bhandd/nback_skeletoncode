package mobappdev.example.nback_cimpl.ui.viewmodels



enum class NumbersToLetters(val letter: Char) {
        ONE('A'),
        TWO('B'),
        THREE('C'),
        FOUR('D'),
        FIVE('E'),
        SIX('F'),
        SEVEN('G'),
        EIGHT('H'),
        NINE('I'),
        TEN('J'),
        ELEVEN('K'),
        TWELVE('L'),
        THIRTEEN('M'),
        FOURTEEN('N'),
        FIFTEEN('O'),
        SIXTEEN('P'),
        SEVENTEEN('Q'),
        EIGHTEEN('R'),
        NINETEEN('S'),
        TWENTY('T'),
        TWENTY_ONE('U'),
        TWENTY_TWO('V'),
        TWENTY_THREE('W'),
        TWENTY_FOUR('X'),
        TWENTY_FIVE('Y'),
        TWENTY_SIX('Z');

        companion object {
            fun fromInt(value: Int): Char? {
                return values().find { it.ordinal + 1 == value }?.letter
            }
    }
}

