
/Gitt følgende XML-dokument:


<?xml version="1.0" encoding="UTF-8"?>

<personRegister>

    <person>
        <Navn>Heidi</navn>
        <alder>56</alder>
    </person>

    <person alder=“32”>
        <navn>Harald</navn>
    </person>

    <person>
        <navn>Sivert</navn>
        <alder=“74”/>
    </person>

</PersonRegister>

a) Er XML-dokumentet riktig deklarert?

b) Hva er rot-elementet?
    - Rot-elementet her er <personRegister>

c) Hvor mange elementer inneholder rot-elementet?
    - Rot-elementet inneholder 3 elementer: 3 personer

d) Kan du finne noen feil som gjør at XML-dokumentet ikke er 'Well-Formed'?
    - Ja, Blant annet er taggene "navn" skrevet med både store og småe bokstaver i første person.
    man skal skrive <navn> og ikke <Navn>
