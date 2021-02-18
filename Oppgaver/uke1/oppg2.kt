
Gitt følgende XML-dokument i Android Studio:

<?xml version="1.0" encoding="UTF-8"?>

<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <TextView
        android:text="@string/username"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
</LinearLayout>

a) Hva er xmlns:android og hvorfor er det viktig?
    - xmnls:android er et namespace man bruker for blant annet å unngå navnkonflikter, gjenbruke elementnavn osv.
    - xmnls er viktig

b) Hva skjer dersom man fjerner prefiksen android: fra android:layout_width?
    - 

c) Hvor på skjermen vil TextView plasseres i LinearLayout?