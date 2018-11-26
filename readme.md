# Game Collector's Database

Game Collector's DB -sovellus on erityisesti videopelikeräilijöiden kokoelmahallintaan suunnattu sovellus.
Sovellukseen rekisteröityneet käyttäjät voivat lisätä sovelluksen tietokantaan kokoelmansa pelit järjestelmittäin, 
ja näin helpottaa kokoelmansa kirjanpitoa. Sovellus tukee useaa rekisteröitynyttä käyttäjää.

### Dokumentaatio  
[Vaatimusmäärittely](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/vaatimusmaarittely.md)

[Tuntikirjanpito](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/tuntikirjanpito.md)

[Arkkitehtuuri](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/arkkitehtuuri.md)

### Komentorivitoiminnot
#### Testaus
Testit suoritetaan komennolla
```
mvn test
```
Testikattavuusraportti luodaan komennolla
```
mvn jacoco:report
```
#### Ohjelman suoritus
Ohjelma suoritetaan komennolla
```
mvn compile exec:java -Dexec.mainClass=app.gcdb.ui.GUI
```

#### Checkstyle
[checkstyle.xml](https://github.com/sokkanen/ot-harjoitustyo/blob/master/checkstyle.xml) mukaiset tarkastukset suoritetaan komennolla
```
mvn jxr:jxr checkstyle:checkstyle
```

