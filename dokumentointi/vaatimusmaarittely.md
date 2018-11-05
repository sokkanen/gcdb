##Sovelluksen tarkoitus

Game Collector's DB -sovellus on suunnattu erityisesti videopelikeräilijöiden kokoelmahallintaan. Sovellukseen
rekisteröityneet käyttäjät voivat lisätä sovelluksen tietokantaan kokoelmansa pelit alustoittain (Gaming platform), 
ja näin helpottaa kokoelmansa kirjanpitoa.

### Käyttäjät

Ohjelman ensimmäisessä versiossa on vain peruskäyttäjiä.

### Käyttöliittymäluonnos

Ensimmäisessä vaiheessa sovellus koostuu kolmesta näkymästä. Myöhempiin versioihin on tarkoitus lisätä pelinäkymä.

### Perusversion tarjoama toiminnallisuus

#### Ennen kirjautumista

* Käyttäjä voi luoda salasanalla suojatun käyttäjätunnuksen
	* Käyttäjätunnuksen ja salasanan tulee olla vähintään 5 merkkiä pitkiä
* Käyttäjä voi kirjautua järjestelmään käyttäjätunnuksella ja salasanalla
	* Jos käyttätunnusta ei ole luotu, järjestelmä ilmoittaa virheestä

#### Kirjautumisen jälkeen

* Kunkin käyttäjän sisältö näkyy vain kyseiselle käyttäjälle

* Käyttäjä näkee lisäämänsä pelialustat
	* Käyttäjä voi lisätä pelialustan
* Käyttäjä voi lisätä pelialustalle pelin
* Pelialustan valitsemalla, käyttäjä näkee alustalle lisätyt pelit

* Käyttäjä voi poistaa pelin
* Käyttäjä voi poistaa pelialustan

* Käyttäjä voi kirjautua ulos järjestelmästä

### Jatkokehitysideoita

Perusversion julkaisun jälkeen ohjelmaa on tarkoitus täydentää seuraavilla ominaisuuksilla

* Järjestelmään lisätään pelinäkymä, jossa yksittäisen pelin tietoja voidaan tarkastella ja muokata
* Peliin on mahdollista lisätä kommenttikenttään tarkentavia tietoja käyttäjän omistamasta pelistä (esim. kuntoluokitus)
* Peliin voi linkata pelistä kertovan nettisivun (esim. Wikipedia)
* Järjestelmä hakee pelin kansikuvan automaattisesti ja näyttää sen kulloinkin valitun pelin vieressä

