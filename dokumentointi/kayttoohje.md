# Käyttöohje

Lataa tiedostot GCDB.jar.

## Konfigurointi

Ohjelma olettaa, että ohjelman sisältävän kansion juuressa on gcdb.conf -tiedosto. Tiedostossa määritellään
käytettävän tietokannan osoite ja nimi.

Tiedoston muoto on seuraava

```
database=:resource:gcdb.db
```
Voit myös ladata varmiin ![gcdb.conf](https://github.com/sokkanen/ot-harjoitustyo/blob/master/gcdb.conf) -tiedoston.

Kaikki muut ohjelman tarvitsemat tiedostot (kuten esimerkiksi kuvat) sisältyvät .jar-pakkaukseen.

## Ohjelman käynnistäminen

Ohjelma käynnistetään komennolla

```
java -jar gcdb.jar
```

## Kirjautuminen

Sovellus käynnistyy kirjautumisnäkymään, jossa aikaisemmin rekisteröitynyt käyttäjä voi kirjautua
ohjelmaan käyttäjätunnuksellaan ja salasanallaan.

![loginwindow](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kuvat/loginwindow.jpg)

Kirjauduttuaan käyttäjä voi jatkaa ohjelmaan "login"-painikkeella tai kirjautua ulos "logout"-painikkeella

Kirjautumisnäkymästä voi sammuttaa ohjelman painamalla "Close program"-painiketta.

## Uuden käyttäjän luominen

Uuden käyttäjän luomisnäkymään siirrytään kirjautumisnäkymästä painamalla "Create new user"-painiketta.

Näkymässä käyttäjä voi valita itselleen käyttäjätunnuksen ja salasanan. Uusi käyttäjä luodaan painamalla
<ENTER> -näppäintä salasanan valintaikkunassa.

![newuserwindow](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kuvat/newuserwindow.jpg)

Näkymästä pääsee takaisin kirjautumiseen painamalla "Back"-painiketta.

## Ohjelman päänäkymä

Päänäkymässä käyttäjä voi hallita pelikokoelmansa kirjanpitoa. Käyttäjä voi lisätä pelialustoja ja näille 
pelejä sekä muokata jo lisättyjen pelien tietoja. Lisäksi käyttäjä voi poistaa pelialustoja ja pelejä. 

![mainwindow1](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kuvat/mainwindow1.jpg)

### Pelialustan lisääminen

Uusi alusta lisätään kirjoittamalla alustan nimi "new platform"-tekstikentään ja painamalla "Add a platform"-
painiketta. Alusta tulee näkyviin vasemmalle listalle.

### Pelin lisääminen ja tarkastelu

Uusi peli lisätään kirjoittamalla tekstikenttään "title" pelin nimi ja valitsemalla vähintään pelin kuntoluokka ja
sisältö. Kommentin ja aluekoodin lisääminen on valinnaista. Peli lisätään painamalla "Add a game"-painiketta.
Peli tulee näkyviin vasemmalle listalle.

Pelin voi valita listalta hiiren painalluksella, jolloin pelin tiedot tulevat näkyviin ikkunan alaosaan.

![mainwindow2](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kuvat/mainwindow2.jpg)

### Pelin muokkaaminen

Valitun pelin muokkaaminen tapahtuu "Modify game information"-painiketta painamalla ja muutokset tallennetaan
"Confirm"-painiketta painamalla. Muokkauksen saa peruttua valitsemalla hiiren painikkeella pelin uudestaan listalta.

### Tietojen poisto

Pelin ja alustan voi poistaa "Remove selected platform" ja "Remove selected game" -painikkeista. Ohjelma varmistaa,
että käyttäjä haluaa varmasti tehdä poiston. Valitsemalla "Yes" haluttu lisäys poistetaan.

### Uloskirjautuminen

Päänäkymästä pääsee kirjautumaan ulos painamalla "Logout"-painiketta.

## Ohjelman sammuttaminen

Painamalla kirjautumisnäkymässä painiketta "" avautuu ohjelman sulkemisnäkymä ja ohjelman voi sulkea yläosan
ruksista.

![exitwindow](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kuvat/exitwindow.jpg)

