
Sovelluksen tarkoitus
---------------------

Game Collector's DB -sovellus on suunnattu erityisesti videopelikeräilijöiden kokoelmahallintaan. Sovellukseen
rekisteröityneet käyttäjät voivat lisätä sovelluksen tietokantaan kokoelmansa pelit alustoittain (Gaming platform), 
ja näin helpottaa kokoelmansa kirjanpitoa.

### Käyttäjät ###

Ohjelman ensimmäisessä versiossa on vain peruskäyttäjiä.

### Käyttöliittymäluonnos ###

Sovellus koostuu kolmesta näkymästä. Nämä ovat kirjautumisnäkymä, uuden käyttäjän lisäämisnäkymä ja päänäkymä.

### Perusversion tarjoama toiminnallisuus ###

#### Ennen kirjautumista ###

* Käyttäjä voi luoda salasanalla suojatun käyttäjätunnuksen
	* Käyttäjätunnuksen tulee olla vähintään 4 ja salasanan vähintään 5 merkkiä pitkiä. Mikäli
	jompikumpi ehdoista ei toteudu, ilmoittaa järjestelmä virheestä.
* Käyttäjä voi kirjautua järjestelmään käyttäjätunnuksella ja salasanalla
	* Jos käyttätunnusta ei ole luotu tai salanasa on virheellinen, ilmoittaa järjestelmä virheestä.

#### Kirjautumisen jälkeen ###

* Kunkin käyttäjän sisältö näkyy vain kyseiselle käyttäjälle

* Käyttäjä näkee lisäämänsä pelialustat
* Käyttäjä voi lisätä pelialustan
* Pelialustan valitsemalla, käyttäjä näkee alustalle lisätyt pelit

* Käyttäjä voi lisätä pelialustalle pelin
* Kokoelman pelit ovat yksilöitä, joten käyttäjä voi lisätä useamman saman pelin.
* Käyttäjä voi poistaa pelin
* Pelin valitsemalla käyttäjä näkee syöttämänsä pelin tiedot (nimeke, kuntoluokka, sisältö ja kommentti)
* Käyttäjä voi poistaa pelialustan

* Käyttäjä voi kirjautua ulos järjestelmästä

### Jatkokehitysideoita ###

Perusversion julkaisun jälkeen ohjelmaa on tarkoitus täydentää seuraavilla ominaisuuksilla

* Peliin voi linkata pelistä kertovan nettisivun (esim. Wikipedia)
* Järjestelmä hakee pelin kansikuvan automaattisesti ja näyttää sen kulloinkin valitun pelin vieressä
* Vapaa keräilykohteen ja kategorisoinnin valinta.
