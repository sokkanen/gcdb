
Sovelluksen tarkoitus
---------------------

Game Collector's DB -sovellus on suunnattu erityisesti videopelikeräilijöiden kokoelmahallintaan. Sovellukseen
rekisteröityneet käyttäjät voivat lisätä sovelluksen tietokantaan kokoelmansa pelit alustoittain (Gaming platform), 
ja näin helpottaa kokoelmansa kirjanpitoa.

### Käyttäjät ###

Ohjelman ensimmäisessä versiossa on vain peruskäyttäjiä.

### Käyttöliittymä ###

Sovellus koostuu neljästä näkymästä. Nämä ovat kirjautumisnäkymä, uuden käyttäjän lisäämisnäkymä, päänäkymä sekä 
ohjelman sammutusnäkymä.

### Perusversion tarjoama toiminnallisuus ###

#### Ennen kirjautumista ###

* Käyttäjä voi luoda salasanalla suojatun käyttäjätunnuksen
	* Käyttäjätunnuksen tulee olla 5-20 merkkiä pitkä.
	* Salasanan tulee olla vähintään 5 merkkiä pitkä. 
	* Mikäli jompikumpi em. ehdoista ei toteudu, ilmoittaa järjestelmä virheestä.
	* Järjestelmä ilmoittaa mikäli käyttäjätunnus on varattu.
* Käyttäjä voi kirjautua järjestelmään käyttäjätunnuksella ja salasanalla
	* Jos käyttätunnusta ei ole luotu tai salasana on virheellinen, ilmoittaa järjestelmä virheestä.
* Käyttäjä voi jatkaa sovellukseen tai kirjautua ulos.
* Käyttäjä voi sulkea sovelluksen.

#### Kirjautumisen jälkeen ###

* Kunkin käyttäjän sisältö näkyy vain kyseiselle käyttäjälle

* Käyttäjä näkee lisäämänsä pelialustat
* Käyttäjä voi lisätä pelialustan
* Pelialustan valitsemalla, käyttäjä näkee alustalle lisätyt pelit

* Käyttäjä voi lisätä pelialustalle pelin
* Käyttäjä näkee kunkin pelialustan pelien määrän.
* Kokoelman pelit ovat yksilöitä, joten käyttäjä voi lisätä useamman samannimisen pelin,
olettaen että peleissä on jokin yksilöivä tekijä (nimi, kunto, sisältö, aluekoodi tai kommentti).
* Käyttäjä voi poistaa pelin
* Pelin valitsemalla käyttäjä näkee syöttämänsä pelin tiedot (nimi, kuntoluokka, sisältö, aluekoodi ja kommentti)
* Käyttäjä voi muokata lisätyn pelin sisäisiä tietoja (kunto, sisältö, aluekoodi ja kommentti).

* Käyttäjä voi poistaa pelin
* Käyttäjä voi poistaa pelialustan
* Poistamalla pelialustan myös alustalla mahdollisesti olevat pelit poistuvat
* Käyttäjä voi kirjautua ulos järjestelmästä

* Järjestelmä varmistaa ennen alustan tai pelin poistamista sekä uloskirjautumista, että käyttäjä on varma
haluamastaan toiminnosta.

### Jatkokehitysideoita ###

Perusversion julkaisun jälkeen ohjelmaa on tarkoitus täydentää seuraavilla ominaisuuksilla

* Peliin voi lisätä kuvia (Vaatii tiedon tallennuksen muutoksen, sillä SQLITE tukee vain bittikarttoja).
* Pelin ajantasaisen hinnan voi tarkastaa ohjelman kautta Ebaysta (esim. keskiarvohinta viimeisistä 10 myydystä.)
* Vapaa keräilykohteen ja kategorisoinnin valinta.
* Ohjelmasta voi tulostaa XML-muotoisen, jaettavan pelilistan
