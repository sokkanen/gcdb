# Game Collector's Database

Game Collector's DB -sovellus on erityisesti videopelikeräilijöiden kokoelmahallintaan suunnattu sovellus.
Sovellukseen rekisteröityneet käyttäjät voivat lisätä sovelluksen tietokantaan kokoelmansa pelit järjestelmittäin, 
ja näin helpottaa kokoelmansa kirjanpitoa. Sovellus tukee useaa rekisteröitynyttä käyttäjää.

### Dokumentaatio  
[Vaatimusmäärittely](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/vaatimusmaarittely.md)

[Tuntikirjanpito](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/tuntikirjanpito.md)

[Arkkitehtuuri](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/arkkitehtuuri.md)

[Käyttöohje](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kayttoohje.md)

[Testausdokumentti](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/testausdokumentti.md)

### Releaset
[Viikko 5](https://github.com/sokkanen/ot-harjoitustyo/releases)

[Viikko 6](https://github.com/sokkanen/ot-harjoitustyo/releases)

[Loppupalautus](https://github.com/sokkanen/ot-harjoitustyo/releases)

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
#### Suoritettavan JAR:n generointi
target -hakemistoon luodaan GCDB-1.0-SNAPSHOT.jar komennolla
```
mvn package
```

#### Checkstyle
[checkstyle.xml](https://github.com/sokkanen/ot-harjoitustyo/blob/master/checkstyle.xml) mukaiset tarkastukset suoritetaan komennolla
```
mvn jxr:jxr checkstyle:checkstyle
```
Mahdolliset virheilmoitukset ovat tarkasteltavissa /target/site/checkstyle.html -tiedostosta.
