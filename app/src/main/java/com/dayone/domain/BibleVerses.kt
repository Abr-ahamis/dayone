package com.dayone.domain

object BibleVerses {
    val verses = listOf(
        "Psalm 118:24 — This is the day the Lord has made; let us rejoice and be glad in it.",
        "Philippians 4:13 — I can do all things through Christ who strengthens me.",
        "Proverbs 3:5 — Trust in the Lord with all your heart.",
        "Isaiah 41:10 — Do not fear, for I am with you.",
        "Jeremiah 29:11 — I know the plans I have for you, declares the Lord.",
        "Matthew 6:33 — Seek first the kingdom of God and his righteousness.",
        "Romans 8:28 — God works all things together for good.",
        "Joshua 1:9 — Be strong and courageous.",
        "Psalm 46:10 — Be still, and know that I am God.",
        "Lamentations 3:23 — Great is your faithfulness.",
        "2 Corinthians 5:7 — We walk by faith, not by sight.",
        "Galatians 6:9 — Do not grow weary of doing good.",
        "James 1:5 — If any of you lacks wisdom, ask God.",
        "1 Peter 5:7 — Cast all your anxiety on him because he cares for you.",
        "Psalm 23:1 — The Lord is my shepherd; I lack nothing.",
        "John 14:27 — Peace I leave with you; my peace I give you.",
        "Romans 12:12 — Be joyful in hope, patient in affliction, faithful in prayer.",
        "Colossians 3:23 — Work at it with all your heart.",
        "Hebrews 11:1 — Faith is confidence in what we hope for.",
        "Psalm 37:4 — Delight yourself in the Lord.",
        "Matthew 5:16 — Let your light shine before others.",
        "Ephesians 6:10 — Be strong in the Lord.",
        "Micah 6:8 — Act justly, love mercy, and walk humbly.",
        "Psalm 119:105 — Your word is a lamp to my feet.",
        "1 Thessalonians 5:16 — Rejoice always.",
        "2 Timothy 1:7 — God gave us a spirit of power, love, and self-control.",
        "Isaiah 40:31 — Those who hope in the Lord will renew their strength.",
        "Mark 10:27 — With God all things are possible.",
        "Psalm 34:8 — Taste and see that the Lord is good.",
        "John 15:5 — Apart from me you can do nothing.",
        "Numbers 6:24 — The Lord bless you and keep you."
    )

    fun forIndex(index: Int): String = verses[(index.coerceIn(1, 31) - 1) % verses.size]
    fun nextIndex(index: Int): Int = if (index >= 31) 1 else index + 1
}
